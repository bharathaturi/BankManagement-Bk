package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.FixedDepositRequest;
import com.bankapp.bank_backend.entity.FixedDeposit;
import com.bankapp.bank_backend.repository.FixedDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FixedDepositService {

    @Autowired
    private FixedDepositRepository fdRepository;

    public FixedDeposit openFixedDeposit(FixedDepositRequest request) {
        FixedDeposit fd = new FixedDeposit();
        fd.setAccountNumber(request.getAccountNumber());
        fd.setPrincipalAmount(request.getPrincipalAmount());
        fd.setTenureMonths(request.getTenureMonths());

        fd.setStatus("ACTIVE");
        fd.setStartDate(LocalDateTime.now());
        fd.setMaturityDate(LocalDateTime.now().plusMonths(request.getTenureMonths()));

        // Tiered Interest Rates based on tenure
        BigDecimal interestRate;
        int m = request.getTenureMonths();
        if (m < 6) {
            interestRate = new BigDecimal("4.5");
        } else if (m < 12) {
            interestRate = new BigDecimal("5.5");
        } else if (m < 36) {
            interestRate = new BigDecimal("6.5");
        } else {
            interestRate = new BigDecimal("7.5");
        }
        fd.setInterestRate(interestRate);

        // Calculate Maturity Amount (Simple Annual Compounding for Demonstration)
        // A = P(1 + r/n)^(nt). Let's use Quarterly compounding (n=4).
        // t = tenure in years (m / 12)
        double p = request.getPrincipalAmount().doubleValue();
        double r = interestRate.doubleValue() / 100;
        double t = m / 12.0;
        int n = 4; // quarterly

        double amount = p * Math.pow(1 + (r / n), n * t);
        fd.setMaturityAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));

        return fdRepository.save(fd);
    }

    public List<FixedDeposit> getFixedDepositsByAccount(String accountNumber) {
        return fdRepository.findByAccountNumber(accountNumber);
    }
}
