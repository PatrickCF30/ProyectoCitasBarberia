package com.barbershop.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.barbershop.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

	// Buscar citas por empleado y rango de fechas sin incluir canceladas
	@Query("SELECT c FROM Cita c WHERE c.employee.id = :employeeId AND c.datetime BETWEEN :startDatetime AND :endDatetime AND c.cancelled = false")
	List<Cita> findByEmployeeIdAndDatetimeBetween(@Param("employeeId") Long employeeId,
			@Param("startDatetime") LocalTime startDatetime, @Param("endDatetime") LocalTime endDatetime);

	// Obtener citas por cliente (Customer)
	@Query("SELECT c FROM Cita c WHERE c.customer.id = :customerId")
	List<Cita> findByCustomerId(@Param("customerId") Long customerId);

	// Cancelar cita específica por empleado y fecha/hora
	@Modifying
	@Transactional
	@Query("UPDATE Cita c SET c.cancelled = true WHERE c.employee.id = :empleadoId AND c.datetime = :fechaHora")
	void cancelCita(@Param("empleadoId") Long empleadoId, @Param("fechaHora") LocalDateTime fechaHora);

	// Liberar horario del empleado en EmployeeSchedule
	@Modifying
	@Transactional
	@Query("UPDATE EmployeeSchedule es SET es.available = true WHERE es.empleado.id = :empleadoId AND es.fromHour <= :hora AND es.toHour >= :hora")
	void releaseEmployeeSchedule(@Param("empleadoId") Long empleadoId, @Param("hora") LocalTime hora);

	@EntityGraph(attributePaths = { "customer", "employee", "service" })
	Optional<Cita> findById(Long id);

	@EntityGraph(attributePaths = { "customer", "employee", "service" })
	Optional<Cita> findWithRelationsById(Long id);

	@Query("SELECT c FROM Cita c WHERE c.employee.id = :employeeId AND c.datetime = :datetime AND c.cancelled = false")
	List<Cita> findByEmployeeIdAndDatetimeAndCancelledFalse(@Param("employeeId") Long employeeId,
			@Param("datetime") LocalDateTime datetime);

	@Query("SELECT c FROM Cita c WHERE c.employee.id = :empleadoId AND c.datetime BETWEEN :startTime AND :endTime AND c.cancelled = false")
	List<Cita> findAvailableCitas(@Param("empleadoId") Long empleadoId, @Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime);

}
