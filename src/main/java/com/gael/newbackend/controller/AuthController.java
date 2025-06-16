package com.gael.newbackend.controller;

import java.util.Optional;
import com.gael.newbackend.model.User;
import com.gael.newbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // CORS local aquí también
public class AuthController {

    @Autowired
    private UserRepository userRepository;
@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body("Email ya registrado");
    }

// Evita error por roles nulos
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
        user.getRoles().add("USER");
    }

    userRepository.save(user);
return ResponseEntity.ok(Map.of("message", "Usuario registrado"));

}


@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail()).orElse(null);

    if (user == null || !user.getPassword().equals(request.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    String token = Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .claim("username", user.getUsername())
            .claim("email", user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
            .signWith(SignatureAlgorithm.HS256, "secreto123".getBytes())
            .compact();

    return ResponseEntity.ok(Map.of("token", token));
}


}
