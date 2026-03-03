package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.EmiPaymentRequest;
import com.bankapp.bank_backend.entity.Emi;
import com.bankapp.bank_backend.service.EmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emis")
@CrossOrigin(origins = "http://localhost:3000")
public class EmiController {

    @Autowired
    private EmiService emiService;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getEmis(@PathVariable String accountNumber) {
        try {
            List<Emi> emis = emiService.getEmis(accountNumber);
            return ResponseEntity.ok(emis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payEmi(@RequestBody EmiPaymentRequest request) {
        try {
            Emi emi = emiService.payEmi(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "EMI for " + emi.getLoanName() + " paid successfully!",
                    "emi", emi));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
