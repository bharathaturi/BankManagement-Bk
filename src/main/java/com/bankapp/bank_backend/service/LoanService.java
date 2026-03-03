package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.LoanRequest;
import com.bankapp.bank_backend.entity.Loan;
import com.bankapp.bank_backend.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public Loan applyForLoan(LoanRequest request) {
        Loan loan = new Loan();
        loan.setAccountNumber(request.getAccountNumber());
        loan.setLoanType(request.getLoanType());
        loan.setPrincipalAmount(request.getPrincipalAmount());
        loan.setTenureMonths(request.getTenureMonths());

        // Auto-approve the loan for now
        loan.setStatus("ACTIVE");

        // Set basic interest rates based on loan type
        BigDecimal interestRate;
        switch (request.getLoanType()) {
            case "Home Loan":
                interestRate = new BigDecimal("8.5");
                break;
            case "Car Loan":
                interestRate = new BigDecimal("10.5");
                break;
            case "Education Loan":
                interestRate = new BigDecimal("11.0");
                break;
            case "Personal Loan":
            default:
                interestRate = new BigDecimal("14.0");
                break;
        }
        loan.setInterestRate(interestRate);

        // Calculate EMI
        // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
        // where P = Principal, r = monthly interest rate (annual / 12 / 100), n =
        // tenure in months
        double p = request.getPrincipalAmount().doubleValue();
        double r = interestRate.doubleValue() / 12 / 100;
        int n = request.getTenureMonths();

        double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
        loan.setEmiAmount(new BigDecimal(emi).setScale(2, RoundingMode.HALF_UP));

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByAccount(String accountNumber) {
        return loanRepository.findByAccountNumber(accountNumber);
    }
}
