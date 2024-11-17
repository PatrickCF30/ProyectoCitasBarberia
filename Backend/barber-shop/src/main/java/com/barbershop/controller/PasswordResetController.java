package com.barbershop.controller;

import com.barbershop.model.User;
import com.barbershop.repository.UsuarioRepository;
import com.barbershop.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Genera un token y lo envía al correo
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Busca el usuario por su email
        User user = userRepository.findOneByEmail(email)
                .orElseThrow(() -> new RuntimeException("El correo no está registrado."));

        // Genera un token de restablecimiento (número de 6 dígitos)
        int token = (int)(Math.random() * 900000) + 100000; // Genera un número aleatorio de 6 dígitos
        user.setResetPasswordToken(String.valueOf(token));
        
        // Establece la fecha de expiración del token (5 minutos)
        user.setTokenExpirationDate(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        // Envía el correo con el token
        emailService.sendResetPasswordEmail(user.getEmail(), String.valueOf(token));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Se ha enviado un correo con instrucciones para restablecer la contraseña.");
        return ResponseEntity.ok(response);
    }

    // Restablecer la contraseña usando el token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        // Busca al usuario por el token
        User user = userRepository.findByResetPasswordToken(token)
                .orElse(null); // Si no encuentra el token, devuelve null

        // Validar si el token es inválido o ha sido usado
        if (user == null || user.getResetPasswordToken() == null) {
            return ResponseEntity.status(400).body("El token es inválido o ya ha sido usado.");
        }

        // Verifica si el token ha expirado
        if (user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body("El token ha expirado.");
        }

        // Verifica si la nueva contraseña es igual a la anterior
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.status(400).body("La nueva contraseña no puede ser igual a la anterior.");
        }

        // Cambia la contraseña a la nueva si es válida
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null); // Elimina el token después de usarlo
        user.setTokenExpirationDate(null); // Elimina la fecha de expiración
        userRepository.save(user);

        return ResponseEntity.ok("Contraseña restablecida correctamente.");
    }
}

