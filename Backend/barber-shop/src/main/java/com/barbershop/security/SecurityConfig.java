package com.barbershop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        
		JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
		jwtAuthenticationFilter.setAuthenticationManager(authManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/login");
		
    	return http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/usuario/register").permitAll() // Permite el acceso público al registro
                .antMatchers("/api/password/**").permitAll() // Permite el acceso público a restablecer contraseña
                .antMatchers("/api/servicios").permitAll() // Permite el acceso público a lista de servicios
                .antMatchers("/api/employees/enable").permitAll() // Permite el acceso público a lista de empleados activos
                .antMatchers("/admin/**").hasAuthority("Admin") // Solo accesible para admin
                .antMatchers("/customer/**").hasAuthority("Customer") // Solo accesible para clientes
                .antMatchers("/api/employees/{employeeId}/services").hasAuthority("Customer")  // Solo accesible para Customer
                .anyRequest().authenticated() // Requiere autenticación para otras rutas
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
			
	}
	
	/*
	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager memoryManager = new InMemoryUserDetailsManager();
		memoryManager.createUser(
				User
				.withUsername("javier")
				.password(passwordEncoder().encode("password"))
				.roles()
				.build());
		return memoryManager;
	}
	*/
	
//	METODO PARA ENCRYPTAR Y VER LA PASSWORD
//	public static void main(String[] args) {
//		System.out.println("Pasword: "+ new BCryptPasswordEncoder().encode("emp1Pass"));
//	}
}
