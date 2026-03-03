package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.BillPaymentRequest;
import com.bankapp.bank_backend.entity.BillPayment;
import com.bankapp.bank_backend.service.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3000")
public class BillPaymentController {

    @Autowired
    private BillPaymentService billPaymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> payBill(@RequestBody BillPaymentRequest request) {
        try {
            BillPayment payment = billPaymentService.processPayment(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Bill paid successfully!",
                    "payment", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getPaymentHistory(@PathVariable String accountNumber) {
        try {
            List<BillPayment> history = billPaymentService.getPaymentHistory(accountNumber);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
