package com.bankapp.bank_backend.repository;

import com.bankapp.bank_backend.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByAccountNumber(String accountNumber);
}
