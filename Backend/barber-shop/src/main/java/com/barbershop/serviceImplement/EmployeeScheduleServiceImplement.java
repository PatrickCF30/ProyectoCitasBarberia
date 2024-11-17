package com.barbershop.serviceImplement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.barbershop.model.Empleado;
import com.barbershop.model.EmployeeSchedule;
import com.barbershop.repository.EmpleadoRepository;
import com.barbershop.repository.EmployeeScheduleRepository;
import com.barbershop.service.EmployeeScheduleService;

import javax.transaction.Transactional;

@Service
@Validated
public class EmployeeScheduleServiceImplement implements EmployeeScheduleService {

    @Autowired
    private EmployeeScheduleRepository employeeScheduleDao;
    
	@Autowired
	private EmpleadoRepository empleadoDao;


    @Override
    public List<Map<String, Object>> listarHorarios() {
        return employeeScheduleDao.findAll().stream()
            .map(schedule -> {
                Map<String, Object> scheduleMap = new HashMap<>();
                scheduleMap.put("id", schedule.getId());
                scheduleMap.put("empleado", schedule.getEmpleado().getFirstName() + " " + schedule.getEmpleado().getLastName());
                scheduleMap.put("fromHour", schedule.getFromHour());
                scheduleMap.put("toHour", schedule.getToHour());
                scheduleMap.put("available", schedule.isAvailable());
                return scheduleMap;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Map<String, Object>> listarHorarioPorId(Long id) {
        Optional<EmployeeSchedule> horario = employeeScheduleDao.findById(id);
        return horario.map(schedule -> {
            Map<String, Object> scheduleMap = new HashMap<>();
            scheduleMap.put("id", schedule.getId());
            scheduleMap.put("empleado", schedule.getEmpleado().getFirstName() + " " + schedule.getEmpleado().getLastName());
            scheduleMap.put("fromHour", schedule.getFromHour());
            scheduleMap.put("toHour", schedule.getToHour());
            scheduleMap.put("available", schedule.isAvailable());
            return scheduleMap;
        });
    }

    @Override
    public List<Map<String, Object>> listarHorariosPorEmpleado(Long empleadoId) {
        return employeeScheduleDao.findByEmpleadoId(empleadoId).stream()
            .map(schedule -> {
                Map<String, Object> scheduleMap = new HashMap<>();
                scheduleMap.put("id", schedule.getId());
                scheduleMap.put("empleado", schedule.getEmpleado().getFirstName() + " " + schedule.getEmpleado().getLastName());
                scheduleMap.put("fromHour", schedule.getFromHour());
                scheduleMap.put("toHour", schedule.getToHour());
                scheduleMap.put("available", schedule.isAvailable());
                return scheduleMap;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> agregarHorarioEmpleado(EmployeeSchedule schedule) {
        Map<String, Object> respuesta = new HashMap<>();

        // Buscar el empleado
        Empleado empleado = empleadoDao.findById(schedule.getEmpleado().getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Guardar el horario
        EmployeeSchedule nuevoHorario = employeeScheduleDao.save(schedule);

        // Crear la respuesta
        Map<String, Object> horarioDetalles = new HashMap<>();
        horarioDetalles.put("id", nuevoHorario.getId());
        
        // Concatenar nombre y apellido
        String nombreCompleto = empleado.getFirstName() + " " + empleado.getLastName();
        horarioDetalles.put("empleado", nombreCompleto);
        
        horarioDetalles.put("fromHour", nuevoHorario.getFromHour());
        horarioDetalles.put("toHour", nuevoHorario.getToHour());
        horarioDetalles.put("available", nuevoHorario.isAvailable());

        respuesta.put("mensaje", "Horario de empleado agregado con éxito.");
        respuesta.put("horario", horarioDetalles);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> editarHorarioEmpleado(Long horarioId, EmployeeSchedule scheduleActualizado) {
        Map<String, Object> respuesta = new HashMap<>();

        // Buscar el horario existente
        EmployeeSchedule horarioExistente = employeeScheduleDao.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Verificar el empleado
        Empleado empleado = empleadoDao.findById(scheduleActualizado.getEmpleado().getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Actualizar los campos permitidos
        horarioExistente.setFromHour(scheduleActualizado.getFromHour());
        horarioExistente.setToHour(scheduleActualizado.getToHour());

        // Guardar el horario actualizado
        employeeScheduleDao.save(horarioExistente);

        // Crear la respuesta
        Map<String, Object> horarioDetalles = new HashMap<>();
        horarioDetalles.put("id", horarioExistente.getId());

        // Concatenar nombre y apellido
        String nombreCompleto = empleado.getFirstName() + " " + empleado.getLastName();
        horarioDetalles.put("empleado", nombreCompleto);

        horarioDetalles.put("fromHour", horarioExistente.getFromHour());
        horarioDetalles.put("toHour", horarioExistente.getToHour());
        horarioDetalles.put("available", horarioExistente.isAvailable());

        respuesta.put("mensaje", "Horario de empleado actualizado con éxito.");
        respuesta.put("horario", horarioDetalles);

        return ResponseEntity.ok(respuesta);
    }


    @Override
    public void eliminarHorario(Long id) {
    	employeeScheduleDao.deleteById(id);
    }

}

