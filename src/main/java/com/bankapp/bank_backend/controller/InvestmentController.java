package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.InvestmentRequest;
import com.bankapp.bank_backend.entity.Investment;
import com.bankapp.bank_backend.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "http://localhost:3000")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseInvestment(@RequestBody InvestmentRequest request) {
        try {
            Investment investment = investmentService.purchaseInvestment(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Investment recorded successfully!",
                    "investment", investment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getPortfolio(@PathVariable String accountNumber) {
        try {
            List<Investment> portfolio = investmentService.getInvestmentsByAccount(accountNumber);
            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
