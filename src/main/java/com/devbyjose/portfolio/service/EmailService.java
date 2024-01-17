package com.devbyjose.portfolio.service;

import com.devbyjose.portfolio.configuration.EmailConfiguration;
import com.devbyjose.portfolio.model.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailConfiguration emailConfiguration;
    private final SimpleMailMessage simpleMailMessage;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine, EmailConfiguration emailConfiguration, SimpleMailMessage simpleMailMessage) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailConfiguration = emailConfiguration;
        this.simpleMailMessage = simpleMailMessage;
    }

    public void enviarCorreos(Usuario usuario) throws MessagingException {
        // Enviar correo al usuario
        enviarCorreoUsuario(usuario);

        // Enviar correo a ti mismo
        enviarCorreoAdministrador(usuario);
    }

    private void enviarCorreoAdministrador(Usuario usuario) throws MessagingException {
        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(emailConfiguration.getEmailRecipient());
        helper.setSubject("Has recibido un nuevo mensaje de " + usuario.getNombre());

        // Cargar y procesar la plantilla HTML
        String cuerpoHTML = cargarPlantillaHTMLAdministrador(usuario);

        // true indica que el contenido es HTML
        helper.setText(cuerpoHTML, true);

        // Establecer el remitente desde la configuración
        helper.setFrom(simpleMailMessage.getFrom());

        javaMailSender.send(mensaje);
    }


    private void enviarCorreoUsuario(Usuario usuario) throws MessagingException {
        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(usuario.getEmail());
        helper.setSubject("Gracias por contactar conmigo");

        // Cargar y procesar la plantilla HTML
        String cuerpoHTML = cargarPlantillaHTMLUsuario(usuario);

        // true indica que el contenido es HTML
        helper.setText(cuerpoHTML, true);

        // Establecer el remitente desde la configuración
        helper.setFrom(simpleMailMessage.getFrom());

        javaMailSender.send(mensaje);
    }

    private String cargarPlantillaHTMLUsuario(Usuario usuario) {
        Context contexto = new Context();
        contexto.setVariable("usuario", usuario);

        // Procesar la plantilla HTML con Thymeleaf
        return templateEngine.process("correo-template-usuario", contexto);
    }

    private String cargarPlantillaHTMLAdministrador(Usuario usuario) {
        Context contexto = new Context();
        contexto.setVariable("usuario", usuario);

        // Procesar la plantilla HTML con Thymeleaf
        return templateEngine.process("correo-template-admin", contexto);
    }
}
