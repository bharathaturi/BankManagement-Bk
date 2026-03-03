package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.BillPaymentRequest;
import com.bankapp.bank_backend.entity.BillPayment;
import com.bankapp.bank_backend.repository.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillPaymentService {

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    public BillPayment processPayment(BillPaymentRequest request) {
        BillPayment payment = new BillPayment();
        payment.setAccountNumber(request.getAccountNumber());
        payment.setBillerCategory(request.getBillerCategory());
        payment.setBillerName(request.getBillerName());
        payment.setConsumerNumber(request.getConsumerNumber());
        payment.setAmount(request.getAmount());

        // Simulating payment approval
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        return billPaymentRepository.save(payment);
    }

    public List<BillPayment> getPaymentHistory(String accountNumber) {
        return billPaymentRepository.findByAccountNumberOrderByPaymentDateDesc(accountNumber);
    }
}
