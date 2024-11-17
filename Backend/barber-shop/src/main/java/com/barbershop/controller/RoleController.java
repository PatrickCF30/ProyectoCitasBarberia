package com.barbershop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbershop.model.Role;
import com.barbershop.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	private RoleService roleDao;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		return roleDao.listarRoles();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> listarPorID (@PathVariable Long id) {
		return roleDao.listarRolesPorId(id);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> agregar(@Validated @RequestBody Role role){
		return roleDao.agregarRoles(role);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> editar(@RequestBody Role role,@PathVariable Long id){
		return roleDao.editarRoles(role,id);
	}
	
	@DeleteMapping
	("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
		return roleDao.eliminarRoles(id);
	}
	
}
