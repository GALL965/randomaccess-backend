package com.gael.newbackend.controller;

import com.gael.newbackend.model.User;
import com.gael.newbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Asignar rol por defecto
    user.getRoles().add("USER");

    userRepository.save(user);
    return ResponseEntity.ok("Usuario registrado");
}

}
