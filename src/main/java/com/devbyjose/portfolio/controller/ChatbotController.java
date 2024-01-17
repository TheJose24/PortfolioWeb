package com.devbyjose.portfolio.controller;

import com.devbyjose.portfolio.model.PreguntaUsuario;
import com.devbyjose.portfolio.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chatbot")
    public ResponseEntity<String> obtenerRespuesta(@RequestBody PreguntaUsuario preguntaUsuario) {
        try {
            String respuesta = chatbotService.generarRespuestaOpenAI(preguntaUsuario.getPregunta());
            System.out.println("Pregunta: " + preguntaUsuario.getPregunta());
            System.out.println("Respuesta: " + respuesta);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la pregunta");
        }
    }
}
