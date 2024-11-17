package com.barbershop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Método para enviar el correo electrónico con solo el token
    public void sendResetPasswordEmail(String toEmail, String token) {
        String subject = "Restablecer contraseña";
        String body = "Tu código de verificación para restablecer tu contraseña es: " + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}