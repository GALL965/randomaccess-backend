package com.gael.randomaccess.controller;

import com.gael.randomaccess.model.User;
import com.gael.randomaccess.repository.UserRepository;
import com.gael.randomaccess.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {
userRepository.save(user);

    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully");
}


@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
    try {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.get("username"),
                        loginData.get("password")
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(token);
    } catch (Exception e) {
        e.printStackTrace(); // muestra error en consola
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
    }
}


}
