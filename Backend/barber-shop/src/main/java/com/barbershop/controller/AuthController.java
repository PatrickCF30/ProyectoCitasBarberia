package com.barbershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.barbershop.model.Auth;
import com.barbershop.serviceImplement.UserDetailImplement;
import com.barbershop.util.Token;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Auth authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailImplement userDetails = (UserDetailImplement) authentication.getPrincipal();

            // Generar el token
            String token = Token.crearToken(userDetails.getUser(), userDetails.getUsername(), userDetails.getUsuario().getRole().getName());

            // Crear la respuesta del body
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", userDetails.getUsuario().getRole().getName());

            // Retornar la respuesta con el token y el rol
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Autenticación fallida"));
        }
    }
}