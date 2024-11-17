package com.barbershop.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.barbershop.model.Customer;

public interface CustomerService {

	ResponseEntity<Map<String, Object>> listarClientes();

	ResponseEntity<Map<String, Object>> listarClientesPorId(Long id);

	ResponseEntity<Map<String, Object>> agregarCliente(Customer customer);

	ResponseEntity<Map<String, Object>> editarCliente(Customer customer, Long id);

	ResponseEntity<Map<String, Object>> eliminarCliente(Long id);

}
