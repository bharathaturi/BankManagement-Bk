package com.bankapp.bank_backend.service;

import com.bankapp.bank_backend.dto.SubscriptionRequest;
import com.bankapp.bank_backend.entity.Subscription;
import com.bankapp.bank_backend.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription subscribe(SubscriptionRequest request) {
        Subscription sub = new Subscription();
        sub.setAccountNumber(request.getAccountNumber());
        sub.setPlatform(request.getPlatform());
        sub.setPlanName(request.getPlanName());
        sub.setAmount(request.getAmount());

        // Simulating activation
        sub.setStatus("ACTIVE");

        LocalDateTime now = LocalDateTime.now();
        sub.setStartDate(now);
        sub.setEndDate(now.plusMonths(request.getDurationMonths()));

        return subscriptionRepository.save(sub);
    }

    public List<Subscription> getSubscriptionsHistory(String accountNumber) {
        return subscriptionRepository.findByAccountNumberOrderByStartDateDesc(accountNumber);
    }
}
