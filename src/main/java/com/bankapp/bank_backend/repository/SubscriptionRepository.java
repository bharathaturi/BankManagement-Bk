package com.bankapp.bank_backend.repository;

import com.bankapp.bank_backend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByAccountNumberOrderByStartDateDesc(String accountNumber);
}
