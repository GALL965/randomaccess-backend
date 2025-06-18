package com.gael.newbackend.controller;

import com.gael.newbackend.model.User;
import com.gael.newbackend.repository.UserRepository;
import java.util.Map;
import com.gael.newbackend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

@Autowired
private UserRepository userRepository;


@PostMapping
public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
        String url = cloudinaryService.upload(file);
        return ResponseEntity.ok().body(Map.of("url", url));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", "Error al subir imagen"));
    }
}





@PostMapping("/upload/profile/{userId}")
public ResponseEntity<Map<String, String>> subirFotoPerfil(
        @PathVariable Long userId,
        @RequestParam("image") MultipartFile file) {

    try {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String imageUrl = cloudinaryService.upload(file);
        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", "Error al subir imagen"));
    }
}



}
