package com.barbershop.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.barbershop.model.Servicio;

public interface ServiciosService {
	
	ResponseEntity<Map<String, Object>> listarServicios();
	
	ResponseEntity<Map<String, Object>> listarServiciosPorId(Long id);
	
	ResponseEntity<Map<String, Object>> agregarServicios(Servicio service);
	
	ResponseEntity<Map<String, Object>> editarServicios(Servicio service, Long id);
	
	ResponseEntity<Map<String, Object>> eliminarServicios(Long id);

}
