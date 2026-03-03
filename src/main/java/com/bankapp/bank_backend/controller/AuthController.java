package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/verify-pin")
    public ResponseEntity<?> verifyPin(@RequestBody Map<String, String> request) {
        try {
            String accountNumber = request.get("accountNumber");
            String pin = request.get("pin");

            boolean isValid = accountService.authenticateAccount(accountNumber, pin);

            if (isValid) {
                // Return validPin and the balance from the account
                var accountOpt = accountService.getAccountByNumber(accountNumber);
                if (accountOpt.isPresent()) {
                    return ResponseEntity.ok(Map.of(
                            "validPin", true,
                            "balance", accountOpt.get().getBalance()));
                }
            }

            return ResponseEntity.ok(Map.of("validPin", false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/change-pin")
    public ResponseEntity<?> changePin(@RequestBody Map<String, String> request) {
        try {
            String accountNumber = request.get("accountNumber");
            String oldPin = request.get("oldPin");
            String newPin = request.get("newPin");

            accountService.changePin(accountNumber, oldPin, newPin);

            return ResponseEntity.ok(Map.of("success", true, "message", "PIN changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
