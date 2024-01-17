package com.devbyjose.portfolio.controller;

import com.devbyjose.portfolio.model.Usuario;
import com.devbyjose.portfolio.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @Autowired
    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar-correo")
    public ResponseEntity<Object> enviarCorreo(@RequestBody Usuario usuario) {
        try {
            // Llamar al método enviarCorreo del servicio EmailService
            emailService.enviarCorreos(usuario);

            // Envía una respuesta JSON con indicador de éxito y mensaje
            return ResponseEntity.ok().body("{\"exito\": true, \"mensaje\": \"Formulario enviado con éxito\"}");
        } catch (Exception e) {
            // Si hay un error, envía una respuesta JSON con indicador de error y mensaje específico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"exito\": false, \"mensaje\": \"Error al enviar el formulario: " + e.getMessage() + "\"}");
        }
    }

}
