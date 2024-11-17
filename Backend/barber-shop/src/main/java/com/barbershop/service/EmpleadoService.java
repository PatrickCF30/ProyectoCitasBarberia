package com.barbershop.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.barbershop.model.Empleado;

public interface EmpleadoService {

	public ResponseEntity<Map<String, Object>> listarEmpleados();
	
	public ResponseEntity<Map<String, Object>> listarEmpleadosPorId(Long id);

	public ResponseEntity<Map<String, Object>> agregarEmpleados(@Validated Empleado empleado);

	public ResponseEntity<Map<String, Object>> actualizarEmpleados(Empleado empleado, Long id);

	public ResponseEntity<Map<String, Object>> eliminarEmpleadosLogico(Long id);

	public ResponseEntity<Map<String, Object>> eliminarEmpleados(Long id);

	public ResponseEntity<Map<String, Object>> listarEmpleadosEnable();

	public ResponseEntity<Map<String, Object>> listarServiciosPorEmpleado(Long idEmpleado);
	
	public ResponseEntity<Map<String, Object>> listarEmpleadosPorServicio(Long idServicio);
}
