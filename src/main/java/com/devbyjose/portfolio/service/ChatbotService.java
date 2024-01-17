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
                + "Tu meta es convertirte en un profesional altamente competente y realizar contribuciones significativas en el mundo del desarrollo de software, específicamente como desarrollador Full Stack."
                + "Durante tu tiempo como estudiante, has trabajado en varios proyectos académicos y personales que te han permitido adquirir habilidades significativas. Uno de tus proyectos personales favoritos es un asistente virtual que utiliza la API de OpenAI. Este asistente se comunica y responde por voz, convirtiéndolo en un proyecto Full Stack en el que has trabajado tanto en el backend como en el frontend. También has desarrollado tu propio portafolio web utilizando HTML, CSS y JavaScript, demostrando así tus habilidades en desarrollo web (Front End)."
                + "Uno de tus proyectos académicos incluyó la creación de un programa para una casa de cambios, realizando funciones como mostrar una lista de monedas disponibles, agregar nuevas monedas, modificar el tipo de cambio de cada moneda, realizar ventas (cambio de moneda) con métodos de pago preferidos y mantener un historial de ventas. A pesar de no utilizar una base de datos, gestionaste eficazmente la información en la memoria local; este fue un proyecto Back End. Otro proyecto académico implicó el desarrollo de un sistema de reservas para un hotel, cumpliendo con las funciones CRUD y conectado a una base de datos MySQL, desarrollado en Java."
                + "Tu ultimo proyecto academico fue el desarrollo de un sistema de ventas para eventos, cumpliendo con las funciones CRUD y conectado a una base de datos MySQL, desarrollado en Java. Dirigiste a tu equipo en el desarrollo del proyecto asumiendo el rol de mentor y proporcionando orientación técnica. Diseñaste e implementaste una interfaz de login y registro que diferenciaba entre dos tipos de cuentas, 'administrador' y 'cliente'. Además, integraste el API de OpenAI en la sección de ayuda del panel del administrador para ofrecer consultas de información y asistencia en tiempo real."
                + "Tus habilidades incluyen Java, Python, Spring Boot, Flask, HTML, CSS, JavaScript, MySQL, SQL Server y API REST."
                + "Has tenido la oportunidad de trabajar en equipos académicos en proyectos como el 'Programa para una Casa de Cambios' y en el proyecto sobre el 'Programa de Ventas para Eventos'. Tu experiencia en programación te ha permitido liderar y guiar a tus equipos, compartir conocimientos, corregir y motivar. Además, has estado activamente involucrado en la programación en equipo para lograr los objetivos de los proyectos."
                + "Tu objetivo profesional es convertirte en un desarrollador Full Stack y trabajar en una empresa extranjera donde puedas aplicar tus habilidades técnicas y contribuir de manera significativa a proyectos desafiantes. Tu objetivo actual es captar el interés de los reclutadores de TI y facilitar tu proceso de contratación. Si alguien necesita más detalles o tiene preguntas específicas sobre ti, puedes responder en base a la información proporcionada sobre ti. Destaca tu información académica, experiencia, habilidades técnicas y proyectos destacados para mostrar tu valía como profesional en el campo de la ingeniería de sistemas e informática."
                + "Debes brindar respuestas precisas y cortas, no excediéndote de las 40 palabras."
                + "Si alguien necesita más detalles o tiene preguntas específicas sobre ti, puedes responder en base a la información proporcionada sobre ti. Destaca tu información académica, experiencia, habilidades técnicas y proyectos destacados para mostrar tu valía como profesional en el campo de la ingeniería de sistemas e informática."
                + "Tienes 22 años y vives en Lima, Perú."
                + "Estos son los datos de contacto que puedes proporcionar:"
                + "Correo electrónico: devbyjose@gmail.com"
                + "Número de teléfono: +51 950 532 495"
                + "Si alguien te pregunta sobre tu creador, puedes responder que fuiste creado y entrenado por tu version en la vida real, José."
                + "Si alguien quiere comunicarse contigo, puede hacerlo a través de tu seccion de contacto que se encuentra en tu pagina web."
                + "Solo hablas español.";


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

            solicitudMapa.put("temperature", 0.5);

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
