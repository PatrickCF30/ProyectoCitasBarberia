package com.barbershop.serviceImplement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.barbershop.dao.ReservaTurnoRequest;
import com.barbershop.model.Cita;
import com.barbershop.model.Customer;
import com.barbershop.model.Empleado;
import com.barbershop.model.EmployeeSchedule;
import com.barbershop.model.Servicio;
import com.barbershop.repository.CitaRepository;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.repository.EmpleadoRepository;
import com.barbershop.repository.EmployeeScheduleRepository;
import com.barbershop.repository.ServiceRepository;
import com.barbershop.service.CitaService;

@Service
public class CitaServiceImplement implements CitaService {

	@Autowired
	private CitaRepository citaDao;

	@Autowired
	private EmployeeScheduleRepository employeeScheduleDao;

	@Autowired
	private EmpleadoRepository empleadoDao;

	@Autowired
	private ServiceRepository serviceDao;

	@Autowired
	private CustomerRepository customerDao;

	@Override
	public ResponseEntity<Map<String, Object>> listarCitas() {
		Map<String, Object> response = new HashMap<>();
		List<Map<String, Object>> citasList = citaDao.findAll().stream().map(cita -> {
			Map<String, Object> citaMap = new HashMap<>();
			citaMap.put("id", cita.getId());
			citaMap.put("customer", cita.getCustomer().getFirstName() + " " + cita.getCustomer().getLastName());
			citaMap.put("employee", cita.getEmployee().getFirstName() + " " + cita.getEmployee().getLastName());
			citaMap.put("service", cita.getService().getName());
			citaMap.put("datetime", cita.getDatetime());
			citaMap.put("end_time", cita.getEndTime());
			citaMap.put("cancelled", cita.getCancelled());
			return citaMap;
		}).collect(Collectors.toList());

		if (!citasList.isEmpty()) {
			response.put("mensaje", "Lista de citas");
			response.put("citas", citasList);
			response.put("status", HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No existen citas registradas");
			response.put("status", HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> obtenerCitaPorId(Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Cita> citaOpt = citaDao.findById(id);

		if (citaOpt.isPresent()) {
			Cita cita = citaOpt.get();
			Map<String, Object> citaMap = new HashMap<>();
			citaMap.put("id", cita.getId());
			citaMap.put("customer", cita.getCustomer().getFirstName() + " " + cita.getCustomer().getLastName());
			citaMap.put("employee", cita.getEmployee().getFirstName() + " " + cita.getEmployee().getLastName());
			citaMap.put("service", cita.getService().getName());
			citaMap.put("datetime", cita.getDatetime());
			citaMap.put("end_time", cita.getEndTime());
			citaMap.put("cancelled", cita.getCancelled());

			response.put("cita", citaMap);
			response.put("mensaje", "Cita encontrada");
			response.put("status", HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No se encontró la cita con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> crearCita(Cita cita) {
		Map<String, Object> response = new HashMap<>();

		// Cargar el servicio desde la base de datos
		Servicio servicio = serviceDao.findById(cita.getService().getId())
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

		// Verificar la duración del servicio
		if (servicio.getDurationMinutes() == null) {
			response.put("mensaje", "Error: La duración del servicio no está configurada.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		// Cargar el cliente y el empleado
		Customer customer = customerDao.findById(cita.getCustomer().getId())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		Empleado empleado = empleadoDao.findById(cita.getEmployee().getId())
				.orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

		// Verificación: Validar que el empleado no tenga otra cita en la misma fecha y
		// hora
		List<Cita> citasExistentes = citaDao.findByEmployeeIdAndDatetimeAndCancelledFalse(empleado.getId(),
				cita.getDatetime());
		if (!citasExistentes.isEmpty()) {
			response.put("mensaje", "Horario no disponible, el empleado ya tiene una cita en este horario.");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

		// Obtener los detalles de fecha y duración
		LocalDateTime startTime = cita.getDatetime();
		LocalDateTime endTime = startTime.plusMinutes(servicio.getDurationMinutes());
		cita.setEndTime(endTime);
		cita.setService(servicio);
		cita.setCustomer(customer);
		cita.setEmployee(empleado);

		// Guardar la cita
		cita = citaDao.save(cita);

		// Preparar la respuesta con los detalles de la cita
		Map<String, Object> detallesCita = new HashMap<>();
		detallesCita.put("datetime", cita.getDatetime());
		detallesCita.put("end_time", cita.getEndTime());
		detallesCita.put("cliente", customer.getFirstName() + " " + customer.getLastName());
		detallesCita.put("empleado", empleado.getFirstName() + " " + empleado.getLastName());
		detallesCita.put("servicio", servicio.getName());
		detallesCita.put("cancelled", cita.getCancelled());
		detallesCita.put("id", cita.getId());

		response.put("mensaje", "Cita creada con éxito");
		response.put("cita", detallesCita);
		response.put("status", "CREATED");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> actualizarCita(Long citaId, Cita citaActualizada) {
	    Map<String, Object> response = new HashMap<>();

	    // Buscar la cita existente
	    Cita citaExistente = citaDao.findById(citaId)
	            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

	    // Verificar si el servicio está presente y su duración
	    if (citaActualizada.getService() == null || 
	        citaActualizada.getService().getId() == null) {
	        response.put("mensaje", "Error: El servicio no está configurado.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Cargar el servicio desde la base de datos
	    Servicio servicio = serviceDao.findById(citaActualizada.getService().getId())
	            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

	    // Verificar si la duración del servicio está configurada
	    if (servicio.getDurationMinutes() == null) {
	        response.put("mensaje", "Error: La duración del servicio no está configurada.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Cargar el cliente y el empleado
	    Customer customer = customerDao.findById(citaActualizada.getCustomer().getId())
	            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

	    Empleado empleado = empleadoDao.findById(citaActualizada.getEmployee().getId())
	            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

	    // Verificar si se intenta cambiar el datetime
	    LocalDateTime newDatetime = citaActualizada.getDatetime();
	    boolean datetimeChanged = (newDatetime != null && !newDatetime.equals(citaExistente.getDatetime()));

	    // Si se cambia el datetime, verificar la disponibilidad
	    if (datetimeChanged) {
	        List<Cita> citasReservadas = citaDao.findAvailableCitas(
	                empleado.getId(),
	                newDatetime,
	                newDatetime.plusMinutes(servicio.getDurationMinutes())
	        );

	        // Si hay citas reservadas en el nuevo horario
	        if (!citasReservadas.isEmpty()) {
	            response.put("mensaje", "Horario no disponible, el empleado ya tiene una cita en este horario.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        // Actualizar el datetime y calcular el nuevo end_time
	        citaExistente.setDatetime(newDatetime);
	        LocalDateTime newEndTime = newDatetime.plusMinutes(servicio.getDurationMinutes());
	        citaExistente.setEndTime(newEndTime);
	    }

	    // Si el datetime no ha cambiado, verificar si el cliente también ha cambiado
	    if (!datetimeChanged) {
	        if (!citaExistente.getCustomer().getId().equals(customer.getId())) {
	            response.put("mensaje", "Horario no disponible, el empleado ya tiene una cita en este horario.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	    }

	    // Actualizar los demás campos permitidos
	    citaExistente.setCustomer(customer); // Actualiza el cliente
	    citaExistente.setEmployee(empleado); // Actualiza el empleado
	    citaExistente.setService(servicio); // Actualiza el servicio
	    citaExistente.setCancelled(citaActualizada.getCancelled()); // Actualiza el estado de cancelación

	    // Guardar la cita actualizada
	    citaDao.save(citaExistente);

	    // Preparar la respuesta
	    Map<String, Object> citaDetalles = new HashMap<>();
	    citaDetalles.put("id", citaExistente.getId());
	    citaDetalles.put("customer", customer.getFirstName() + " " + customer.getLastName());
	    citaDetalles.put("employee", empleado.getFirstName() + " " + empleado.getLastName());
	    citaDetalles.put("service", servicio.getName());
	    citaDetalles.put("datetime", citaExistente.getDatetime());
	    citaDetalles.put("end_time", citaExistente.getEndTime());
	    citaDetalles.put("cancelled", citaExistente.getCancelled());

	    response.put("mensaje", "Cita actualizada con éxito");
	    response.put("cita", citaDetalles);
	    response.put("status", HttpStatus.OK);

	    return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarCita(Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<Cita> citaOpt = citaDao.findById(id);

		if (citaOpt.isPresent()) {
			citaDao.delete(citaOpt.get());
			response.put("mensaje", "Cita eliminada correctamente");
			response.put("status", HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			response.put("mensaje", "No se encontró la cita con ID: " + id);
			response.put("status", HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarCitasPorCustomer(Authentication authentication) {
		String email = authentication.getName();
		Customer customer = customerDao.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Customer no encontrado"));

		List<Cita> citas = citaDao.findByCustomerId(customer.getId());
		List<Map<String, Object>> citasResponse = new ArrayList<>();

		for (Cita cita : citas) {
			Empleado empleado = empleadoDao.findById(cita.getEmployee().getId())
					.orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

			Map<String, Object> citaDetails = new HashMap<>();

			// Formatear la hora y fecha para la respuesta
			DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("hh:mm a");
			DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			String horarioFormateado = cita.getDatetime().format(horaFormatter).toUpperCase();
			String fechaFormateada = cita.getDatetime().format(fechaFormatter);

			citaDetails.put("horario", horarioFormateado);
			citaDetails.put("fecha", fechaFormateada);
			citaDetails.put("empleado", empleado.getFirstName() + " " + empleado.getLastName());
			citaDetails.put("id", cita.getId());

			Servicio servicio = cita.getService();
			citaDetails.put("servicio", servicio != null ? servicio.getName() : "Servicio no disponible");
			citaDetails.put("duracion",
					servicio != null ? servicio.getDurationMinutes() + " minutos" : "Duración no disponible");

			// Estado de la cita
			citaDetails.put("estado", cita.getCancelled() ? "CANCELADA" : "ACTIVA");

			citasResponse.add(citaDetails);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Resumen de Citas");
		response.put("citas", citasResponse);
		response.put("status", "OK");

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarHorariosDisponibles(Long empleadoId, LocalDate fecha) {
	    List<EmployeeSchedule> horarios = employeeScheduleDao.findByEmpleadoId(empleadoId);

	    // Filtrar horarios disponibles (solo aquellos donde available es true)
	    List<EmployeeSchedule> horariosDisponiblesFiltrados = horarios.stream()
	            .filter(EmployeeSchedule::isAvailable)
	            .collect(Collectors.toList());

	    // Verificar si hay horarios disponibles
	    if (horariosDisponiblesFiltrados.isEmpty()) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("mensaje", "No hay horarios disponibles para este trabajador.");
	        response.put("status", "NO_AVAILABLE");
	        return ResponseEntity.ok(response);
	    }

	    LocalDateTime startOfDay = fecha.atStartOfDay();
	    LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX);

	    // Obtener citas no canceladas para el día y empleado específicos
	    List<Cita> citasReservadas = citaDao.findAvailableCitas(empleadoId, startOfDay, endOfDay)
	            .stream()
	            .filter(cita -> !cita.getCancelled())
	            .collect(Collectors.toList());

	    // Crear set para horarios ocupados por citas de una hora completa
	    Set<LocalDateTime> horariosOcupados = new HashSet<>();
	    for (Cita cita : citasReservadas) {
	        LocalDateTime inicioCita = cita.getDatetime();
	        LocalDateTime finCita = inicioCita.plusMinutes(
	                cita.getService() != null ? cita.getService().getDurationMinutes() : 60).withMinute(0); // Considera la hora completa

	        // Marcar cada hora completa ocupada
	        LocalDateTime tiempo = inicioCita.withMinute(0); // Ajustar a la hora completa
	        while (!tiempo.isAfter(finCita)) {
	            horariosOcupados.add(tiempo); // Solo horas completas
	            tiempo = tiempo.plusHours(1); // Avanzar en bloques de una hora
	        }
	    }

	    // Filtrar los horarios disponibles solo con horas completas
	    List<String> horariosFinalesDisponibles = new ArrayList<>();
	    for (EmployeeSchedule horario : horariosDisponiblesFiltrados) {
	        LocalDateTime current = fecha.atTime(horario.getFromHour());
	        LocalDateTime endHour = fecha.atTime(horario.getToHour());
	        while (current.isBefore(endHour)) {
	            if (!horariosOcupados.contains(current)) {
	                horariosFinalesDisponibles.add(current.toLocalTime().toString());
	            }
	            current = current.plusHours(1); // Solo avanzar por horas completas
	        }
	    }

	    // Obtener el nombre completo del empleado
	    Empleado empleado = empleadoDao.findById(empleadoId)
	            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
	    String empleadoNombreCompleto = empleado.getFirstName() + " " + empleado.getLastName();

	    // Crear la respuesta
	    Map<String, Object> response = new HashMap<>();
	    response.put("horariosDisponibles", horariosFinalesDisponibles);
	    response.put("mensaje", "Horarios disponibles para el empleado " + empleadoNombreCompleto);
	    response.put("status", "OK");

	    return ResponseEntity.ok(response);
	}


	@Override
	public ResponseEntity<Map<String, Object>> reservarTurno(ReservaTurnoRequest request,
			Authentication authentication) {
		String email = authentication.getName();
		Customer customer = customerDao.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Customer no encontrado"));

		Empleado empleado = empleadoDao.findById(request.getEmpleadoId())
				.orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

		Servicio servicio = serviceDao.findById(request.getServicioId())
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

		LocalDateTime fechaHora = LocalDateTime.of(request.getFecha(), LocalTime.parse(request.getHorario()));

		// Buscar citas reservadas en la misma fecha y hora
		List<Cita> citasReservadas = citaDao.findByEmployeeIdAndDatetimeAndCancelledFalse(empleado.getId(), fechaHora);

		if (!citasReservadas.isEmpty()) {
			throw new RuntimeException("Horario no disponible");
		}

		// Crear nueva cita con los datos del cliente, empleado y servicio
		Cita nuevaCita = new Cita();
		nuevaCita.setCustomer(customer);
		nuevaCita.setEmployee(empleado);
		nuevaCita.setService(servicio);
		nuevaCita.setDatetime(fechaHora);

		// Calcular `endTime`
		LocalDateTime endTime = fechaHora.plusMinutes(servicio.getDurationMinutes());
		nuevaCita.setEndTime(endTime);

		citaDao.save(nuevaCita);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "RESUMEN DE CITA");

		Map<String, Object> detallesCita = new HashMap<>();
		detallesCita.put("fecha", formatFecha(fechaHora)); // Formateo de la fecha
		detallesCita.put("precio", "S/ " + servicio.getPrice());
		detallesCita.put("servicio", servicio.getName());

		DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		String horarioFormateado = fechaHora.format(horaFormatter).toUpperCase();
		detallesCita.put("horario", horarioFormateado);
		detallesCita.put("duracion", servicio.getDurationMinutes() + " minutos");

		response.put("cita", detallesCita);
		response.put("status", "OK");

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> cancelarCita(Long citaId) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Busca la cita por ID y lanza excepción si no se encuentra
	        Cita cita = citaDao.findById(citaId)
	                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

	        // Verifica si la cita ya fue cancelada
	        if (cita.getCancelled()) {
	            throw new RuntimeException("La cita ya fue cancelada previamente");
	        }

	        // Llama al método para liberar el horario del empleado usando LocalDateTime
	        citaDao.releaseEmployeeSchedule(cita.getEmployee().getId(), cita.getDatetime().toLocalTime());

	        // Marca la cita como cancelada y la guarda
	        cita.setCancelled(true);
	        citaDao.save(cita);

	        Servicio servicio = cita.getService();

	        // Construye los detalles de la cita cancelada para la respuesta
	        Map<String, Object> citaDetalles = new HashMap<>();
	        citaDetalles.put("Fecha", cita.getDatetime().toLocalDate().toString());
	        citaDetalles.put("Duración", servicio.getDurationMinutes() + " minutos");
	        citaDetalles.put("Servicio", servicio.getName());

	        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
	        citaDetalles.put("Horario", cita.getDatetime().format(horaFormatter));
	        citaDetalles.put("estado", "CANCELADA");

	        response.put("citas", List.of(citaDetalles));
	        response.put("mensaje", "Cita cancelada con éxito");
	        response.put("status", "OK");

	        return ResponseEntity.ok(response);

	    } catch (RuntimeException e) {
	        response.put("mensaje", e.getMessage());
	        response.put("status", "ERROR");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    } catch (Exception e) {
	        response.put("mensaje", "Error inesperado al cancelar la cita");
	        response.put("status", "ERROR");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	// Método para formatear la fecha y hora
	private String formatFecha(LocalDateTime fechaHora) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'del' yyyy");
		return fechaHora.format(formatter);
	}

}
