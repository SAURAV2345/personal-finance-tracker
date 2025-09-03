package com.saurav.finance_tracker.controller;

import com.saurav.finance_tracker.dto.LoginEvent;
import com.saurav.finance_tracker.dto.LoginRequest;
import com.saurav.finance_tracker.model.User;
import com.saurav.finance_tracker.repository.UserRepository;
import com.saurav.finance_tracker.service.ExpenseEventProducer;
import com.saurav.finance_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExpenseEventProducer expenseEventProducer;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already present");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session){

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);


        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }



        if (user.getEmail().equals(loginRequest.getEmail()) &&
                passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            String token = jwtUtil.generateToken(loginRequest.getEmail());
            // Store user in session
            session.setAttribute("user", user);

            // create login event and sent to Kafka

            LoginEvent loginEvent = new LoginEvent();
            loginEvent.setUsername(user.getEmail());
            loginEvent.setLoginDate(LocalDateTime.now());
            expenseEventProducer.publishLoginEvent(loginEvent);

            return ResponseEntity.ok(token);
        }

        return ResponseEntity.ok("Login Successful");

    }
}
