package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.FixedDepositRequest;
import com.bankapp.bank_backend.entity.FixedDeposit;
import com.bankapp.bank_backend.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fds")
@CrossOrigin(origins = "http://localhost:3000")
public class FixedDepositController {

    @Autowired
    private FixedDepositService fdService;

    @PostMapping("/create")
    public ResponseEntity<?> createFixedDeposit(@RequestBody FixedDepositRequest request) {
        try {
            FixedDeposit fd = fdService.openFixedDeposit(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Fixed Deposit opened successfully!",
                    "fd", fd));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getFixedDeposits(@PathVariable String accountNumber) {
        try {
            List<FixedDeposit> fds = fdService.getFixedDepositsByAccount(accountNumber);
            return ResponseEntity.ok(fds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
