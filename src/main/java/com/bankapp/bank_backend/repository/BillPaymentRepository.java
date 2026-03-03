package com.bankapp.bank_backend.repository;

import com.bankapp.bank_backend.entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    List<BillPayment> findByAccountNumberOrderByPaymentDateDesc(String accountNumber);
}
