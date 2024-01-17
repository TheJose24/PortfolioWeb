package com.devbyjose.portfolio.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = new ClassPathResource("static/images/email-image-template/" + imageName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)  // o MediaType.IMAGE_PNG según el tipo de imagen
                    .body(resource);
        } catch (Exception e) {
            // Manejar el error, por ejemplo, devolver una imagen de error genérica
            return ResponseEntity.notFound().build();
        }
    }
}
