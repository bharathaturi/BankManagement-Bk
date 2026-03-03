package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.LoanRequest;
import com.bankapp.bank_backend.entity.Loan;
import com.bankapp.bank_backend.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:3000")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(@RequestBody LoanRequest request) {
        try {
            Loan loan = loanService.applyForLoan(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Loan approved and activated successfully!",
                    "loan", loan));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getLoans(@PathVariable String accountNumber) {
        try {
            List<Loan> loans = loanService.getLoansByAccount(accountNumber);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
