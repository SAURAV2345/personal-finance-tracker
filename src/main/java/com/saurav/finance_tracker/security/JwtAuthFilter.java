package com.saurav.finance_tracker.security;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saurav.finance_tracker.service.CustomUserDetailsService;
import com.saurav.finance_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        String username="";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
                // Optionally, set authentication in SecurityContext
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                return;
            }
            if (username != null && (SecurityContextHolder.getContext().getAuthentication() == null ||
                    SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(token,userDetails.getUsername())){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }



            } else if (!httpRequest.getRequestURI().equals("/api/auth/login") && !httpRequest.getRequestURI().equals("/api/auth/signup") &&
        !httpRequest.getRequestURI().equals("/actuator/prometheus")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Token");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
