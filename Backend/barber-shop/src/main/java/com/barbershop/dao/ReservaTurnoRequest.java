package com.barbershop.dao;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservaTurnoRequest {
    private Long empleadoId;
    private Long servicioId;
    private LocalDate fecha;
    private String horario;
    private Long customerId;
}
