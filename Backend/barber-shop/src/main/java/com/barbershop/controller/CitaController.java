package com.barbershop.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.barbershop.dao.ReservaTurnoRequest;
import com.barbershop.model.Cita;
import com.barbershop.service.CitaService;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    
    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Validated @RequestBody Cita cita) {
        return citaService.crearCita(cita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @Validated @RequestBody Cita cita) {
        return citaService.actualizarCita(id, cita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        return citaService.eliminarCita(id);
    }
    
    @GetMapping("/listarPorCustomer")
    public ResponseEntity<Map<String, Object>> listarCitasPorCustomer(Authentication authentication) {
        return citaService.listarCitasPorCustomer(authentication);
    }
    
    @GetMapping("/{empleadoId}/disponibilidad")
    public ResponseEntity<Map<String, Object>> obtenerDisponibilidadEmpleado(
            @PathVariable Long empleadoId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return citaService.listarHorariosDisponibles(empleadoId, fecha);
    }
    
    @PostMapping("/reservar")
    public ResponseEntity<Map<String, Object>> reservarTurno(@RequestBody ReservaTurnoRequest request, Authentication authentication) {
        return citaService.reservarTurno(request, authentication);
    }
    
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Map<String, Object>> cancelarCita(@PathVariable Long id) {
        return citaService.cancelarCita(id);
    }
}
