package com.barbershop.security;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.barbershop.model.Auth;
import com.barbershop.serviceImplement.UserDetailImplement;
import com.barbershop.util.Token;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		Auth authCredenciales = new Auth();
		
		try {
			authCredenciales = new ObjectMapper().readValue(request.getReader(), Auth.class);
		} catch (Exception e) {
		}

		System.out.println(authCredenciales);
		UsernamePasswordAuthenticationToken userPAT = new UsernamePasswordAuthenticationToken(
				authCredenciales.getEmail(),
				authCredenciales.getPassword(),
				Collections.emptyList()
				);
		
		return getAuthenticationManager().authenticate(userPAT);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
	        Authentication authResult) throws IOException, ServletException {

	    UserDetailImplement userDetails = (UserDetailImplement) authResult.getPrincipal();
	    String token = Token.crearToken(userDetails.getUser(), userDetails.getUsername(), userDetails.getUsuario().getRole().getName());

	    response.addHeader("Authorization", "Bearer " + token);
	    response.getWriter().flush();
		// Configurar la respuesta como JSON
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// Crear un objeto para la respuesta
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("username", userDetails.getUsername());
		responseBody.put("authorities", userDetails.getAuthorities());
		responseBody.put("userId", userDetails.getId()); // Retorna el id del usuario
		responseBody.put("token", token);

		// Convertir el Map a JSON y escribirlo en la respuesta
		ObjectMapper mapper = new ObjectMapper();
		response.getWriter().write(mapper.writeValueAsString(responseBody));

		super.successfulAuthentication(request, response, chain, authResult);
	}

	
	
	
}
