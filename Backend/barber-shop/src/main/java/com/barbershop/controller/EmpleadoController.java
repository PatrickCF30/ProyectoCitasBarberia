package com.barbershop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbershop.model.Empleado;
import com.barbershop.service.EmpleadoService;


@RestController
@RequestMapping("/api/employees")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		return empleadoService.listarEmpleados();
	}
	
	@GetMapping("/enable")
	public ResponseEntity<Map<String, Object>> listarPorEnable(){
		return empleadoService.listarEmpleadosEnable();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> listaPorID(@PathVariable Long id){
		return empleadoService.listarEmpleadosPorId(id);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> agregar(@Validated @RequestBody Empleado empleado){
		return empleadoService.agregarEmpleados(empleado);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> actualizar(@RequestBody Empleado empleado,@PathVariable Long id) {
		return empleadoService.actualizarEmpleados(empleado,id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
		return empleadoService.eliminarEmpleados(id);
	}
	
	@PutMapping("/eliminar/{id}")
	public ResponseEntity<Map<String, Object>> eliminarLogico(@PathVariable Long id) {
		return empleadoService.eliminarEmpleadosLogico(id);
	}
	
    @PreAuthorize("hasAuthority('Customer')")
	@GetMapping("/{employeeId}/services")
	public ResponseEntity<Map<String, Object>> getEmployeeServices(@PathVariable Long employeeId) {
		return empleadoService.listarServiciosPorEmpleado(employeeId);
	}
    
    @GetMapping("/{serviceId}/employees")
    public ResponseEntity<Map<String, Object>> getEmployeesByService(@PathVariable Long serviceId) {
        return empleadoService.listarEmpleadosPorServicio(serviceId);
    }
	
}