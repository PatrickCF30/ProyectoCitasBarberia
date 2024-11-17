package com.barbershop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barbershop.model.Empleado;
import com.barbershop.model.EmployeeService;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
	
	List<Empleado> findAllByActive(Boolean active);
	
	@Query("SELECT DISTINCT es FROM EmployeeService es WHERE es.empleado.id = :empleadoId")
	List<EmployeeService> findByEmpleado_Id(@Param("empleadoId") Long empleadoId);
	
}
