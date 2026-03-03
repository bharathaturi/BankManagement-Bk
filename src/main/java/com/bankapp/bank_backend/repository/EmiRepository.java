package com.bankapp.bank_backend.repository;

import com.bankapp.bank_backend.entity.Emi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiRepository extends JpaRepository<Emi, Long> {
    List<Emi> findByAccountNumberOrderByNextDueDateAsc(String accountNumber);
}
