package com.barbershop.serviceImplement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.barbershop.model.Customer;
import com.barbershop.model.Role;
import com.barbershop.model.User;
import com.barbershop.repository.CustomerRepository;
import com.barbershop.repository.RoleRepository;
import com.barbershop.repository.UsuarioRepository;

@Service
public class UserServiceImplement implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioDao;
	@Autowired
	private RoleRepository roleDao;
	
    @Autowired
    private CustomerRepository customerDao;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User usuario = usuarioDao.findOneByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("El usuario con dicho email: " + email + "no existe."));

		return new UserDetailImplement(usuario);
	}

	public ResponseEntity<Map<String, Object>> agregarUsuario(User u, String roleName, Customer c) {
	    Map<String, Object> respuesta = new HashMap<>();

	    // Verificar si el usuario ya existe
	    if (usuarioDao.existsByUsername(u.getUsername())) {
	        respuesta.put("mensaje", "El nombre de usuario ya existe");
	        respuesta.put("status", HttpStatus.CONFLICT);
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
	    }

	    if (usuarioDao.existsByEmail(u.getEmail())) {
	        respuesta.put("mensaje", "El correo electrónico ya está registrado");
	        respuesta.put("status", HttpStatus.CONFLICT);
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
	    }

	    // Asignar el rol según el valor de roleName
	    Role role = roleDao.findByName(roleName)
	            .orElseThrow(() -> new RuntimeException("El rol no existe"));

	    // Crear y guardar el nuevo usuario
	    User usuario = new User();
	    usuario.setEmail(u.getEmail());
	    usuario.setUsername(u.getUsername());
	    usuario.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
	    usuario.setRole(role);
	    usuarioDao.save(usuario);

	    // Si el rol es "Customer", crear también el perfil de cliente
	    if (role.getName().equals("Customer")) {
	        Customer customer = new Customer();
	        customer.setFirstName(c.getFirstName());  
	        customer.setLastName(c.getLastName());  
	        customer.setPhone(c.getPhone());  
	        customer.setUser(usuario);  // Relacionar al usuario creado

	        customerDao.save(customer);

	        // Construir la respuesta personalizada
	        Map<String, Object> customerData = new HashMap<>();
	        customerData.put("first_name", customer.getFirstName());
	        customerData.put("last_name", customer.getLastName());
	        customerData.put("user", usuario.getUsername());
	        customerData.put("password", usuario.getPassword()); 
	        customerData.put("email", usuario.getEmail());
	        customerData.put("phone", customer.getPhone());
	        customerData.put("role", role.getName());

	        respuesta.put("customer", customerData);
	    }

	    respuesta.put("mensaje", "Se añadió correctamente el usuario");
	    respuesta.put("status", HttpStatus.CREATED);
	    respuesta.put("fecha", new Date());

	    return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}
	
	public ResponseEntity<Map<String, Object>> verPerfil(Authentication authentication) {
		// Obtener el email del usuario desde el token JWT
		String email = authentication.getName();

		// Buscar al usuario por su email
		User usuario = usuarioDao.findOneByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Verificar si el rol del usuario es Customer
		if (!usuario.getRole().getName().equals("Customer")) {
			return ResponseEntity.status(403)
					.body(Map.of("mensaje", "Solo los usuarios con rol de Customer pueden acceder a esta sección"));
		}

		// Buscar el perfil del cliente asociado
		Customer customer = customerDao.findByUserId(usuario.getId())
				.orElseThrow(() -> new RuntimeException("Perfil de cliente no encontrado"));

		// Crear la respuesta personalizada
		Map<String, Object> response = new HashMap<>();
		response.put("fecha", java.time.LocalDateTime.now().toString());
		response.put("mensaje", "Datos del Cliente");

		// Crear un mapa con los datos del cliente
		Map<String, Object> customerData = new HashMap<>();
		customerData.put("id", customer.getId());
		customerData.put("first_name", customer.getFirstName());
		customerData.put("last_name", customer.getLastName());
		customerData.put("email", usuario.getEmail());
		customerData.put("username", usuario.getUsername());
		customerData.put("password", usuario.getPassword());
		customerData.put("phone", customer.getPhone());

		// Añadir los datos del cliente al cuerpo de la respuesta
		response.put("customer", customerData);
		response.put("status", "OK");

		// Devolver la respuesta
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> editarPerfil(Authentication authentication,
			@RequestBody Map<String, String> request) {
		// Obtén el email del usuario desde el token JWT
		String email = authentication.getName();

		// Busca al usuario por su email
		User usuario = usuarioDao.findOneByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Verificar si el rol del usuario es Customer
		if (!usuario.getRole().getName().equals("Customer")) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("mensaje", "Solo los usuarios con rol de Customer pueden modificar su perfil.");
			return ResponseEntity.status(403).body(errorResponse);
		}

		// Busca el perfil del cliente asociado
		Customer customer = customerDao.findByUserId(usuario.getId())
				.orElseThrow(() -> new RuntimeException("Perfil de cliente no encontrado"));

		// Validar que los campos no sean nulos
		String username = request.get("username");
		String firstName = request.get("first_name");
		String lastName = request.get("last_name");
		String phone = request.get("phone");

		if (username == null || firstName == null || lastName == null || phone == null) {
			return ResponseEntity.badRequest().body(
					Map.of("mensaje", "Datos de perfil inválidos o incompletos. Todos los campos son requeridos."));
		}

		// Actualiza los campos permitidos
		usuario.setUsername(username);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setPhone(phone);

		// Guardar los cambios en la base de datos
		usuarioDao.save(usuario); // Guardar cambios en el usuario (username)
		customerDao.save(customer); // Guardar cambios en el perfil del cliente

		// Crear la respuesta personalizada
		Map<String, Object> response = new HashMap<>();
		response.put("fecha", java.time.LocalDateTime.now().toString());
		response.put("mensaje", "Perfil actualizado exitosamente");

		// Crear un mapa con los datos actualizados del cliente
		Map<String, Object> customerData = new HashMap<>();
		customerData.put("id", customer.getId());
		customerData.put("first_name", customer.getFirstName());
		customerData.put("last_name", customer.getLastName());
		customerData.put("email", usuario.getEmail()); 
		customerData.put("username", usuario.getUsername()); 
		customerData.put("password", usuario.getPassword()); 
		customerData.put("phone", customer.getPhone());

		// Añadir los datos del cliente al cuerpo de la respuesta
		response.put("customer", customerData);
		response.put("status", "OK");

		// Retornar la respuesta
		return ResponseEntity.ok(response);
	}
}
