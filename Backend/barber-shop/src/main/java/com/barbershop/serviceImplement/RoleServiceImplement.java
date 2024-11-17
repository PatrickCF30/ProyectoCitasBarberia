package com.barbershop.serviceImplement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barbershop.model.Role;
import com.barbershop.repository.RoleRepository;
import com.barbershop.service.RoleService;

@Service
public class RoleServiceImplement implements RoleService {

	@Autowired
	private RoleRepository roleDao;

	@Override
	public ResponseEntity<Map<String, Object>> listarRoles() {
		Map<String, Object> respuesta = new HashMap<>();
		List<Role> roles = roleDao.findAll();

		if (!roles.isEmpty()) {
			List<Map<String, Object>> rolesList = roles.stream()
					.map(r -> {
						Map<String, Object> roleMap = new HashMap<>();
						roleMap.put("id", r.getId());
						roleMap.put("name", r.getName());
						return roleMap;
					})
					.collect(Collectors.toList());

			respuesta.put("mensaje", "Lista de Roles");
			respuesta.put("roles", rolesList);
			respuesta.put("status", HttpStatus.OK);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} else {
			respuesta.put("mensaje", "No existen registros");
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarRolesPorId(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Role> role = roleDao.findById(id);

		if (role.isPresent()) {
			Map<String, Object> roleMap = new HashMap<>();
			roleMap.put("id", role.get().getId());
			roleMap.put("name", role.get().getName());

			respuesta.put("role", roleMap);
			respuesta.put("mensaje", "Búsqueda correcta");
			respuesta.put("status", HttpStatus.OK);
			respuesta.put("fecha", new Date());
			return ResponseEntity.ok().body(respuesta);
		} else {
			respuesta.put("mensaje", "Sin registros con ID: " + id);
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> agregarRoles(Role role) {
		Map<String, Object> respuesta = new HashMap<>();
		roleDao.save(role);
		respuesta.put("role", role);
		respuesta.put("mensaje", "Se añadió correctamente el rol");
		respuesta.put("status", HttpStatus.CREATED);
		respuesta.put("fecha", new Date());
		return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

	@Override
	public ResponseEntity<Map<String, Object>> editarRoles(Role role, Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Role> roleExistente = roleDao.findById(id);

		if (roleExistente.isPresent()) {
			Role rol = roleExistente.get();
			rol.setName(role.getName());
			roleDao.save(rol);
			respuesta.put("role", rol);
			respuesta.put("mensaje", "Datos del rol modificados");
			respuesta.put("status", HttpStatus.CREATED);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
		} else {
			respuesta.put("mensaje", "Sin registros con ID: " + id);
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarRoles(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Role> roleExistente = roleDao.findById(id);

		if (roleExistente.isPresent()) {
			roleDao.delete(roleExistente.get());
			respuesta.put("mensaje", "Eliminado correctamente");
			respuesta.put("status", HttpStatus.NO_CONTENT);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuesta);
		} else {
			respuesta.put("mensaje", "Sin registros con ID: " + id);
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}
}
