package com.barbershop.model;

import java.time.LocalTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table(name = "employee_schedules")
@EntityListeners(AuditingEntityListener.class)
public class EmployeeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El empleado no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "employees_id")
    private Empleado empleado;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "from_hour")
    private LocalTime fromHour;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "to_hour")
    private LocalTime toHour;
    
    @Column(name = "available")
    private boolean available = true;
}
