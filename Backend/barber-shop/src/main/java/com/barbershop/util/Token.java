package com.barbershop.util;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class Token {

	private final static String TOKEN_SECRETO = "A2Rx2bHaDJBqfGARpPV5pEqbCCmcQPvq";
	private final static Long TOKEN_DURACION = 60 * 60 * 1000L;
	
	public static String crearToken(String username, String email, String role) {
        Date expiracionFecha = new Date(System.currentTimeMillis() + TOKEN_DURACION);
		
		Map<String, Object> map = new HashMap<>();
		map.put("nombre", username);
		map.put("role", role);
		
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(expiracionFecha)
				.addClaims(map)
				.signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
				.compact();
		
	}
	
	public static UsernamePasswordAuthenticationToken getAuth(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(TOKEN_SECRETO.getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
			
			String email = claims.getSubject();
            String role = claims.get("role", String.class);
            
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            
            return new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));
		} catch (JwtException e) {
			return null;
		}
	}

	
}
