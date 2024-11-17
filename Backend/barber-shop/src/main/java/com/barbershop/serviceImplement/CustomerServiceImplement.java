package com.barbershop.serviceImplement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barbershop.model.Customer;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.service.CustomerService;

@Service
public class CustomerServiceImplement implements CustomerService {

    @Autowired
    private CustomerRepository customerDao;

    @Override
    public ResponseEntity<Map<String, Object>> listarClientes() {
        Map<String, Object> respuesta = new HashMap<>();
        List<Customer> customers = customerDao.findAll();

        if (!customers.isEmpty()) {
            List<Map<String, Object>> customersList = customers.stream()
                .map(c -> {
                    Map<String, Object> customerMap = new HashMap<>();
                    customerMap.put("id", c.getId());
                    customerMap.put("firstName", c.getFirstName());
                    customerMap.put("lastName", c.getLastName());
                    customerMap.put("phone", c.getPhone());
                    customerMap.put("username", c.getUser().getUsername());
                    return customerMap;
                })
                .collect(Collectors.toList());

            respuesta.put("mensaje", "Lista de Clientes");
            respuesta.put("clientes", customersList);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No existen registros");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> listarClientesPorId(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Customer> customer = customerDao.findById(id);

        if (customer.isPresent()) {
            Customer c = customer.get();
            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("id", c.getId());
            customerMap.put("firstName", c.getFirstName());
            customerMap.put("lastName", c.getLastName());
            customerMap.put("phone", c.getPhone());
            customerMap.put("username", c.getUser().getUsername()); 

            respuesta.put("cliente", customerMap);
            respuesta.put("mensaje", "Búsqueda correcta");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok().body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> agregarCliente(Customer customer) {
        Map<String, Object> respuesta = new HashMap<>();
        customerDao.save(customer);
        respuesta.put("cliente", customer);
        respuesta.put("mensaje", "Se añadió correctamente el cliente");
        respuesta.put("status", HttpStatus.CREATED);
        respuesta.put("fecha", new Date());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Override
    public ResponseEntity<Map<String, Object>> editarCliente(Customer customer, Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Customer> customerExistente = customerDao.findById(id);

        if (customerExistente.isPresent()) {
            Customer cliente = customerExistente.get();
            cliente.setFirstName(customer.getFirstName());
            cliente.setLastName(customer.getLastName());
            cliente.setPhone(customer.getPhone());
            customerDao.save(cliente);
            respuesta.put("cliente", cliente);
            respuesta.put("mensaje", "Datos del cliente modificados");
            respuesta.put("status", HttpStatus.CREATED);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> eliminarCliente(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Customer> customerExistente = customerDao.findById(id);

        if (customerExistente.isPresent()) {
            Customer cliente = customerExistente.get();
            customerDao.delete(cliente);
            respuesta.put("mensaje", "Eliminado correctamente");
            respuesta.put("status", HttpStatus.NO_CONTENT);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
}
