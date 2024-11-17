package com.barbershop.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.barbershop.model.Role;

public interface RoleService {
	
	ResponseEntity<Map<String, Object>> listarRoles();
	
	ResponseEntity<Map<String, Object>> listarRolesPorId(Long id);
	
	ResponseEntity<Map<String, Object>> agregarRoles(Role role);
	
	ResponseEntity<Map<String, Object>> editarRoles(Role role, Long id);
	
	ResponseEntity<Map<String, Object>> eliminarRoles(Long id);
	
	ResponseEntity<Map<String, Object>> listarRolesEnable();
	
	ResponseEntity<Map<String, Object>> eliminarRolesEnable(Long id);
}
