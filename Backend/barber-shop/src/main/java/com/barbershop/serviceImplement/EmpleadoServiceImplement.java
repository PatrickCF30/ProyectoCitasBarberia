package com.barbershop.serviceImplement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.barbershop.model.Empleado;
import com.barbershop.model.EmployeeService;
import com.barbershop.model.Servicio;
import com.barbershop.repository.EmpleadoRepository;
import com.barbershop.repository.EmployeeServiceRepository;
import com.barbershop.repository.ServiceRepository;
import com.barbershop.service.EmpleadoService;
import java.util.Base64;
import java.util.Collections;

@Service
public class EmpleadoServiceImplement implements EmpleadoService {

	@Autowired
	private EmpleadoRepository empleadoDao;

	@Autowired
	private EmployeeServiceRepository empleadoServiceDao;

	@Autowired
	private ServiceRepository serviceDao;

	@Override
	public ResponseEntity<Map<String, Object>> listarEmpleados() {
		Map<String, Object> response = new HashMap<>();
		List<Empleado> empleados = empleadoDao.findAll();

		if (!empleados.isEmpty()) {
			response.put("mensaje", "Lista de Empleados");
			response.put("empleados", empleados);
			response.put("status", HttpStatus.OK);
			response.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No existen registros");
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarEmpleadosPorId(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Empleado> empleados = empleadoDao.findById(id);

		if (empleados.isPresent()) {
			respuesta.put("productos", empleados);
			respuesta.put("mensaje", "Busqueda correcta");
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
	public ResponseEntity<Map<String, Object>> actualizarEmpleados(Empleado emp, Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Empleado> empleadoOpt = empleadoDao.findById(id);

		if (empleadoOpt.isPresent()) {
			Empleado empleado = empleadoOpt.get();

			if (emp.getFirstName() != null)
				empleado.setFirstName(emp.getFirstName());
			if (emp.getLastName() != null)
				empleado.setLastName(emp.getLastName());
			if (emp.getProfession() != null)
				empleado.setProfession(emp.getProfession());
			if (emp.getPicture() != null)
				empleado.setPicture(emp.getPicture());
			if (emp.getWorking_days() != null)
				empleado.setWorking_days(emp.getWorking_days());
			if (emp.getActive() != null)
				empleado.setActive(emp.getActive());

			empleadoDao.save(empleado);
			response.put("empleados", empleado);
			response.put("mensaje", "Los datos del empleado han sido modificados");
			response.put("status", HttpStatus.CREATED);
			response.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		response.put("mensaje", "No existe el empleado con ID: " + id);
		response.put("status", HttpStatus.NOT_FOUND);
		response.put("fecha", new Date());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarEmpleadosLogico(Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Empleado> empleadoX = empleadoDao.findById(id);

		if (empleadoX.isPresent()) {
			Empleado empleado = empleadoX.get();
			empleado.setActive(false);
			empleadoDao.save(empleado);

			response.put("mensaje", "Se ha eliminado correctamente");
			response.put("status", HttpStatus.OK);
			response.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No existe el empleado con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			response.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> agregarEmpleados(Empleado empleado) {
		Map<String, Object> respuesta = new HashMap<>();

		if (empleado.getPictureBase64() != null && empleado.getPictureBase64().startsWith("data:image/")) {
			String base64Image = empleado.getPictureBase64().split(",")[1];
			empleado.setPicture(Base64.getDecoder().decode(base64Image));
		}

		if (empleado.getId() != null && empleadoDao.existsById(empleado.getId())) {
			respuesta.put("mensaje", "Error: Ya existe un empleado con el ID proporcionado.");
			respuesta.put("status", HttpStatus.BAD_REQUEST);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}

		empleadoDao.save(empleado);
		respuesta.put("empleados", empleado);
		respuesta.put("mensaje", "Se añadió correctamente el empleado.");
		respuesta.put("status", HttpStatus.CREATED);
		respuesta.put("fecha", new Date());

		return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarEmpleados(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		Optional<Empleado> empleadoExiste = empleadoDao.findById(id);
		if (empleadoExiste.isPresent()) {
			Empleado empleado = empleadoExiste.get();
			empleadoDao.delete(empleado);
			respuesta.put("mensaje", "Eliminado correctamente");
			respuesta.put("status", HttpStatus.OK);
			respuesta.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} else {
			respuesta.put("mensaje", "Sin registros con ID: " + id);
			respuesta.put("status", HttpStatus.NOT_FOUND);
			respuesta.put("fecha", new Date());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarEmpleadosEnable() {
		Map<String, Object> respuesta = new HashMap<>();
		List<Empleado> empleados = empleadoDao.findAllByActive(true);

		if (!empleados.isEmpty()) {
			respuesta.put("mensaje", "Lista de productos");
			respuesta.put("empleados", empleados);
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errores = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errores.put(error.getField(), error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errores);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarServiciosPorEmpleado(Long idEmpleado) {

	    Empleado employee = empleadoDao.findById(idEmpleado)
	            .orElse(null); 
	    
	    // Verificar si el empleado no existe
	    if (employee == null) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("mensaje", "El empleado con ID " + idEmpleado + " no fue encontrado.");
	        errorResponse.put("status", "NOT_FOUND");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	    }

	    // Obtener la lista de servicios por el ID del empleado
	    List<EmployeeService> employeeServices = empleadoServiceDao.findByEmpleado_Id(idEmpleado);

	    // Verificar si el empleado no tiene servicios asociados
	    if (employeeServices.isEmpty()) {
	        Map<String, Object> noServicesResponse = new HashMap<>();
	        noServicesResponse.put("employee", employee.getFirstName().concat(" " + employee.getLastName()));
	        noServicesResponse.put("services", Collections.singletonList(
	                Collections.singletonMap("mensaje", "No se encontraron servicios para este barbero")
	        ));
	        noServicesResponse.put("status", "Not Found");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noServicesResponse);
	    }

	    // Usar un conjunto para eliminar duplicados
	    Set<Servicio> uniqueServices = employeeServices.stream()
	            .map(EmployeeService::getServicio)
	            .collect(Collectors.toSet());
	    
	    // Obtener el nombre completo del empleado
	    String employeeName = employee.getFirstName().concat(" " + employee.getLastName());

	    // Crear la lista de servicios basada en el servicio
	    List<Map<String, Object>> services = uniqueServices.stream().map(service -> {
	        Map<String, Object> serviceData = new HashMap<>();
	        serviceData.put("id", service.getId()); 
	        serviceData.put("service", service.getName());
	        serviceData.put("description", service.getDescription());
	        serviceData.put("price", service.getPrice());
	        return serviceData;
	    }).collect(Collectors.toList());

	    // Crear la respuesta
	    Map<String, Object> response = new HashMap<>();
	    response.put("employee", employeeName);
	    response.put("services", services);
	    response.put("mensaje", "Servicios listados exitosamente");
	    response.put("status", "OK");

	    return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarEmpleadosPorServicio(Long idServicio) {

		Servicio servicio = serviceDao.findById(idServicio).orElse(null);

		if (servicio == null) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("mensaje", "El servicio " + idServicio + " no fue encontrado.");
			errorResponse.put("status", "NOT_FOUND");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		// Obtener la lista de empleados por el ID de servicio
		List<EmployeeService> employeeServices = empleadoServiceDao.findByServicioId(idServicio);

		// Obtener los detalles de cada empleado relacionado
		List<Map<String, Object>> employees = employeeServices.stream().map(es -> {
			// Obtener los detalles del empleado por employeeId
			Empleado employee = es.getEmpleado();

			Map<String, Object> employeeData = new HashMap<>();
			employeeData.put("employee", employee.getFirstName() + " " + employee.getLastName());

			return employeeData;
		}).collect(Collectors.toList());

		// Crear la respuesta
		Map<String, Object> response = new HashMap<>();
		response.put("service", servicio.getName());
		response.put("employees", employees);
		response.put("mensaje", "Empleados listados exitosamente");
		response.put("status", "OK");

		return ResponseEntity.ok(response);
	}
}
