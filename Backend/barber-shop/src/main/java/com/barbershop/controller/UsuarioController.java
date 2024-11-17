package com.barbershop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barbershop.model.Customer;
import com.barbershop.model.User;
import com.barbershop.serviceImplement.UserServiceImplement;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UserServiceImplement service;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> agregar(@RequestBody Map<String, Object> request,
			@RequestParam String role) {
		// Obtener los datos del usuario y cliente desde el request
		User usuario = new User();
		usuario.setUsername((String) request.get("username"));
		usuario.setEmail((String) request.get("email"));
		usuario.setPassword((String) request.get("password"));

		Customer customer = new Customer();
		if (role.equals("Customer")) {
			customer.setFirstName((String) request.get("first_name"));
			customer.setLastName((String) request.get("last_name"));
			customer.setPhone((String) request.get("phone"));
		}

		// Pasar los datos al servicio para realizar el registro
		return service.agregarUsuario(usuario, role, customer);
	}

    // Ver perfil del cliente
    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> perfil(Authentication authentication) {
        // Llamar al servicio para ver el perfil del usuario
        return service.verPerfil(authentication);
    }

    // Editar perfil del cliente
    @PutMapping("/perfil")
    public ResponseEntity<Map<String, Object>> editPerfil(Authentication authentication, 
    		@RequestBody Map<String, String> request) {
        // Llamar al servicio para editar el perfil del usuario
        return service.editarPerfil(authentication, request);
    }

}
