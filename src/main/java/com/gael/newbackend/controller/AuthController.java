package com.gael.newbackend.controller;


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
public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
    String email = loginData.get("email");
    String password = loginData.get("password");

Optional<User> optionalUser = userRepository.findByEmail(email);
if (optionalUser.isEmpty()) {
    return ResponseEntity.status(401).body("Usuario no encontrado");
}
User user = optionalUser.get();

    if (!user.getPassword().equals(password)) {
        return ResponseEntity.status(401).body("Contraseña incorrecta");
    }

    return ResponseEntity.ok(Map.of(
        "message", "Login exitoso",
        "username", user.getUsername(),
        "email", user.getEmail()
    ));
}


}
