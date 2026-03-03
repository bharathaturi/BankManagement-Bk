package com.bankapp.bank_backend.repository;

// Customer Repository (CustomerRepository.java)

import com.bankapp.bank_backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(String customerId);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCustomerId(String customerId);
}
