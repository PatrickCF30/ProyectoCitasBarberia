package com.barbershop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.barbershop.model.EmployeeSchedule;

public interface EmployeeScheduleService {

	List<Map<String, Object>>  listarHorarios();

	Optional<Map<String, Object>> listarHorarioPorId(Long id);

	List<Map<String, Object>> listarHorariosPorEmpleado(Long empleadoId);

	ResponseEntity<Map<String, Object>> agregarHorarioEmpleado(EmployeeSchedule schedule);

	ResponseEntity<Map<String, Object>> editarHorarioEmpleado(Long horarioId, EmployeeSchedule scheduleActualizado);

	void eliminarHorario(Long id);
}