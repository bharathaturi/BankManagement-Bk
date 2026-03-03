package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.InvestmentRequest;
import com.bankapp.bank_backend.entity.Investment;
import com.bankapp.bank_backend.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    public Investment purchaseInvestment(InvestmentRequest request) {
        Investment investment = new Investment();
        investment.setAccountNumber(request.getAccountNumber());
        investment.setInvestmentType(request.getInvestmentType());
        investment.setSymbol(request.getSymbol().toUpperCase());
        investment.setAmountInvested(request.getAmountInvested());
        investment.setQuantity(request.getQuantity() != null ? request.getQuantity() : 1);

        investment.setStatus("ACTIVE");
        investment.setPurchaseDate(LocalDateTime.now());

        // For demonstration purposes, we will initialize the "current return" equal to
        // the invested amount.
        // In reality, this would be updated dynamically via Market APIs.
        investment.setCurrentReturn(request.getAmountInvested());

        return investmentRepository.save(investment);
    }

    public List<Investment> getInvestmentsByAccount(String accountNumber) {
        return investmentRepository.findByAccountNumber(accountNumber);
    }
}
