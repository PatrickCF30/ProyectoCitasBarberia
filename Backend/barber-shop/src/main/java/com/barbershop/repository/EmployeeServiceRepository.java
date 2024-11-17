package com.barbershop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbershop.model.EmployeeService;

@Repository
public interface EmployeeServiceRepository extends JpaRepository<EmployeeService, Long>{
	
	// Método para buscar relacions por el ID del empleado
	List<EmployeeService> findByEmpleado_Id(Long empleadoId);

	// Método para buscar relaciones por el ID del servicio
    List<EmployeeService> findByServicioId(Long servicioId);
}
