package com.barbershop.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.barbershop.model.Cita;
import com.barbershop.dao.ReservaTurnoRequest;

public interface CitaService {

    ResponseEntity<Map<String, Object>> listarCitas();
    
    ResponseEntity<Map<String, Object>> obtenerCitaPorId(Long id);
    
    ResponseEntity<Map<String, Object>> crearCita(Cita cita);
    
    ResponseEntity<Map<String, Object>> actualizarCita(Long id, Cita cita);
    
    ResponseEntity<Map<String, Object>> eliminarCita(Long id);
    
    ResponseEntity<Map<String, Object>> listarCitasPorCustomer(Authentication authentication);
    
    ResponseEntity<Map<String, Object>> listarHorariosDisponibles(Long empleadoId, LocalDate fecha);
    
    ResponseEntity<Map<String, Object>> reservarTurno(ReservaTurnoRequest request, Authentication authentication);
    
    ResponseEntity<Map<String, Object>> cancelarCita(Long citaId);

}
