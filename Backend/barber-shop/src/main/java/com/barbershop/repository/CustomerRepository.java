package com.barbershop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.barbershop.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
	Optional<Customer> findByUserId(Long userId);
    
    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.email = ?1")
    Optional<Customer> findByEmail(String email);
    
}
