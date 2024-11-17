package com.barbershop.model;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointments")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customers_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employees_id", nullable = false)
    private Empleado employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "services_id", nullable = false)
    private Servicio service;

    @JsonProperty("datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @JsonProperty("cancelled")
    @Column(nullable = false)
    private Boolean cancelled = false;

}