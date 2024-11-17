package com.barbershop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barbershop.model.Servicio;

@Repository
public interface ServiceRepository extends JpaRepository<Servicio, Long>{
	
	Optional<Servicio> findByName(String name);

}
