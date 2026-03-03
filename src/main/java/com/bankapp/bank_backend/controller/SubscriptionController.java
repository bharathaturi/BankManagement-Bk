package com.bankapp.bank_backend.controller;

import com.bankapp.bank_backend.dto.SubscriptionRequest;
import com.bankapp.bank_backend.entity.Subscription;
import com.bankapp.bank_backend.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = "http://localhost:3000")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionRequest request) {
        try {
            Subscription sub = subscriptionService.subscribe(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Subscribed successfully to " + sub.getPlatform() + "!",
                    "subscription", sub));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getSubscriptionsHistory(@PathVariable String accountNumber) {
        try {
            List<Subscription> history = subscriptionService.getSubscriptionsHistory(accountNumber);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
