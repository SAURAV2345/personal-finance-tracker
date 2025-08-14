package com.saurav.finance_tracker.controller;

import com.saurav.finance_tracker.dto.LoginRequest;
import com.saurav.finance_tracker.model.User;
import com.saurav.finance_tracker.repository.UserRepository;
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

    //@Autowired
   // private AuthenticationManager authenticationManager;

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
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
//        Authentication auth = authenticationManager.authenticate(token);
//        SecurityContextHolder.getContext().setAuthentication(auth);
        // Storing session info



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
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.ok("Login Successful");

    }
}
