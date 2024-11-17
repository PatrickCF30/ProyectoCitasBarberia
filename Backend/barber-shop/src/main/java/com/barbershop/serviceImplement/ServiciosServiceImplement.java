package com.barbershop.serviceImplement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barbershop.model.Servicio;
import com.barbershop.repository.ServiceRepository;
import com.barbershop.service.ServiciosService;

@Service
public class ServiciosServiceImplement implements ServiciosService{

	@Autowired
	private ServiceRepository serviceDao;
	
	@Override
	public ResponseEntity<Map<String, Object>> listarServicios() {
		Map<String, Object> response = new HashMap<>();
		List<Servicio> servicios = serviceDao.findAll();
		
		if(!servicios.isEmpty()) {
			response.put("mensaje", "Lista de Servicios");
			response.put("services", servicios);
			response.put("status", HttpStatus.OK);
			response.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No existen servicios");
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarServiciosPorId(Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Servicio> servicios = serviceDao.findById(id);
		
		if(servicios.isPresent()) {
			response.put("services", servicios);
			response.put("mensaje", "Busqueda correcta");
			response.put("status", HttpStatus.OK);
			response.put("fecha", new Date());	
			
			return ResponseEntity.ok().body(response);	
		} else {
			response.put("mensaje", "Sin registros con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());	
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> agregarServicios(Servicio service) {
		Map<String, Object> response = new HashMap<>();
		serviceDao.save(service);
		response.put("services", service);
		response.put("mensaje", "Se añadio correctamente el servicio");
		response.put("status", HttpStatus.CREATED);
		response.put("fecha", new Date());	
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> editarServicios(Servicio service, Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Servicio> servicioExiste = serviceDao.findById(id);
		if(servicioExiste.isPresent()) {
			Servicio servicio = servicioExiste.get();
			servicio.setName(service.getName());
			servicio.setDescription(service.getDescription());
			servicio.setPrice(service.getPrice());
			servicio.setDurationMinutes(service.getDurationMinutes());
			serviceDao.save(servicio);
			
			response.put("servicios", servicio);			
			response.put("mensaje", "Datos del servicio modificados");
			response.put("status", HttpStatus.CREATED);
			response.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			response.put("mensaje", "Sin registros con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());	
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarServicios(Long id) {
		Map<String,Object> response = new HashMap<>();	
		Optional<Servicio> servicioExiste = serviceDao.findById(id);	
		if (servicioExiste.isPresent()) {
			Servicio servicio = servicioExiste.get();
			serviceDao.delete(servicio);
			response.put("mensaje", "Eliminado correctamente");
			response.put("status", HttpStatus.NO_CONTENT);
			response.put("fecha", new Date());	

			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}else {
			response.put("mensaje", "Sin registros con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());	
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
	}
}
