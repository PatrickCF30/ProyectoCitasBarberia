package com.barbershop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbershop.model.EmployeeSchedule;

@Repository
public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Long> {
    
    // Método para obtener los horarios de un empleado por ID
    List<EmployeeSchedule> findByEmpleadoId(Long empleadoId);
}
