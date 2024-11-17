package com.barbershop.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.barbershop.model.EmployeeSchedule;
import com.barbershop.service.EmployeeScheduleService;


@RestController
@RequestMapping("/api/employee-schedules")
@Validated
public class EmployeeScheduleController {

    @Autowired
    private EmployeeScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarHorarios() {
        return new ResponseEntity<>(scheduleService.listarHorarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarHorarioPorId(@PathVariable Long id) {
        Optional<Map<String, Object>> horario = scheduleService.listarHorarioPorId(id);
        return horario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<Map<String, Object>>> listarHorariosPorEmpleado(@PathVariable Long empleadoId) {
        return new ResponseEntity<>(scheduleService.listarHorariosPorEmpleado(empleadoId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarHorarioEmpleado(@Validated @RequestBody EmployeeSchedule employeeSchedule) {
        return scheduleService.agregarHorarioEmpleado(employeeSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editarHorarioEmpleado(@PathVariable Long id, @Validated @RequestBody EmployeeSchedule scheduleActualizado) {
        return scheduleService.editarHorarioEmpleado(id, scheduleActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        scheduleService.eliminarHorario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
