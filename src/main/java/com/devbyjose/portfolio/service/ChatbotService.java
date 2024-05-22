package com.devbyjose.portfolio.service;

import com.devbyjose.portfolio.configuration.OpenAiConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class ChatbotService {

    @Autowired
    private OpenAiConfiguration configuracionOpenAi;

    public String construirDatosSolicitud(String pregunta) {
        String contexto = "Eres José, un estudiante de Ingeniería de Sistemas e Informática en el quinto ciclo de tu carrera. Te has graduado del programa Oracle Next Education (ONE) y cuentas con un certificado que respalda tu formación."
                ;


        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Construir el mapa para representar la estructura de la solicitud
            java.util.Map<String, Object> solicitudMapa = new java.util.HashMap<>();
            solicitudMapa.put("model", configuracionOpenAi.getModel_name());

            java.util.List<java.util.Map<String, String>> mensajes = new java.util.ArrayList<>();

            java.util.Map<String, String> mensajeSistema  = new java.util.HashMap<>();
            mensajeSistema .put("role", "system");
            mensajeSistema .put("content", contexto);

            java.util.Map<String, String> mensajeUsuario  = new java.util.HashMap<>();
            mensajeUsuario .put("role", "user");
            mensajeUsuario .put("content", pregunta);

            mensajes.add(mensajeSistema );
            mensajes.add(mensajeUsuario );

            solicitudMapa.put("messages", mensajes);


            // Convertir el mapa a JSON
            return objectMapper.writeValueAsString(solicitudMapa);
        } catch (Exception e) {
            // Manejar cualquier excepción
            e.printStackTrace();
            return null;
        }
    }

    public String generarRespuestaOpenAI(String pregunta) {
        try {
            // Construir la solicitud JSON
            String requestData = construirDatosSolicitud(pregunta);

            // Configurar los encabezados de la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + configuracionOpenAi.getApiKey());

            // Configurar la entidad de la solicitud
            HttpEntity<String> requestEntity = new HttpEntity<>(requestData, headers);

            // Realizar la solicitud HTTP POST a la API de OpenAI
            ResponseEntity<String> responseEntity = configuracionOpenAi.restTemplate().exchange(
                    configuracionOpenAi.getApiUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Verificar si la solicitud fue exitosa (código de respuesta 2xx)
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Procesar la respuesta JSON
                String responseJson = responseEntity.getBody();
                return procesarRespuestaOpenAI(responseJson);
            } else {
                return "Error al generar la respuesta: Código de respuesta " + responseEntity.getStatusCodeValue();
            }
        } catch (RestClientException e) {
            // Manejar excepciones relacionadas con la red u otros problemas de comunicación
            e.printStackTrace();
            return "Error al comunicarse con la API de OpenAI";
        } catch (Exception e) {
            // Manejar otras excepciones inesperadas
            e.printStackTrace();
            return "Error inesperado al generar la respuesta";
        }
    }

    public String procesarRespuestaOpenAI(String responseJson) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(responseJson);

            if (jsonResponse.containsKey("choices")) {
                JSONArray choices = (JSONArray) jsonResponse.get("choices");

                if (choices != null && !choices.isEmpty()) {
                    JSONObject choice = (JSONObject) choices.get(0);
                    JSONObject message = (JSONObject) choice.get("message");

                    if (message != null) {
                        String content = (String) message.get("content");

                        return content;
                    } else {
                        return "Mensaje vacío en la respuesta";
                    }
                } else {
                    return "Respuesta vacía o no válida";
                }
            } else {
                return "Estructura de respuesta no válida: falta 'choices'";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error al procesar la respuesta generada: " + e.getMessage();
        }
    }



}
