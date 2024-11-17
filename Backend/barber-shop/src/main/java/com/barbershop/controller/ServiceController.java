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

import com.barbershop.model.Servicio;
import com.barbershop.service.ServiciosService;

@RestController
@RequestMapping("/api/servicios")
public class ServiceController {
	
	@Autowired
	private ServiciosService servicios;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		return servicios.listarServicios();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> listarPorID (@PathVariable Long id) {
		return servicios.listarServiciosPorId(id);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> agregar(@Validated @RequestBody Servicio service){
		return servicios.agregarServicios(service);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> editar(@RequestBody Servicio service,@PathVariable Long id){
		return servicios.editarServicios(service,id);
	}
	
	@DeleteMapping
	("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
		return servicios.eliminarServicios(id);
	}
}
