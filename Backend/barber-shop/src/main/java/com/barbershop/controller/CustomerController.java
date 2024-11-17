package com.barbershop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbershop.model.Customer;
import com.barbershop.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService Customerservice;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> listar() {
		return Customerservice.listarClientes();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> listarPorID (@PathVariable Long id) {
		return Customerservice.listarClientesPorId(id);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> agregar(@Validated @RequestBody Customer customer){
		return Customerservice.agregarCliente(customer);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> editar(@RequestBody Customer customer,@PathVariable Long id){
		return Customerservice.editarCliente(customer,id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
		return Customerservice.eliminarCliente(id);
	}
	
}
