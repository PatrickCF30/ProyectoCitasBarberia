package com.barbershop.serviceImplement;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barbershop.model.Role;
import com.barbershop.service.RoleService;

@Service
public class RoleServiceImplement implements RoleService {

	@Override
	public ResponseEntity<Map<String, Object>> listarRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarRolesPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> agregarRoles(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> editarRoles(Role role, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarRoles(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarRolesEnable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarRolesEnable(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
