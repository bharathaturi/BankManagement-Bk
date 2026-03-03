package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.EmiPaymentRequest;
import com.bankapp.bank_backend.entity.Emi;
import com.bankapp.bank_backend.repository.EmiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmiService {

    @Autowired
    private EmiRepository emiRepository;

    public List<Emi> getEmis(String accountNumber) {
        return emiRepository.findByAccountNumberOrderByNextDueDateAsc(accountNumber);
    }

    public Emi payEmi(EmiPaymentRequest request) throws Exception {
        Optional<Emi> optionalEmi = emiRepository.findById(request.getEmiId());
        if (optionalEmi.isEmpty()) {
            throw new Exception("EMI not found");
        }

        Emi emi = optionalEmi.get();
        if (!emi.getAccountNumber().equals(request.getAccountNumber())) {
            throw new Exception("Unauthorized EMI payment");
        }
        if ("COMPLETED".equals(emi.getStatus())) {
            throw new Exception("This EMI is already fully paid");
        }

        // Quick validation: Ensure paid amount matches EMI amount
        if (request.getAmountPaid().compareTo(emi.getEmiAmount()) != 0) {
            throw new Exception("Payment amount must exactly match the EMI amount: " + emi.getEmiAmount());
        }

        // Process payment
        emi.setMonthsPaid(emi.getMonthsPaid() + 1);

        // Update Due Date by 1 month securely
        if (emi.getNextDueDate() != null) {
            emi.setNextDueDate(emi.getNextDueDate().plusMonths(1));
        }

        if (emi.getMonthsPaid() >= emi.getTotalMonths()) {
            emi.setStatus("COMPLETED");
        }

        return emiRepository.save(emi);
    }
}
