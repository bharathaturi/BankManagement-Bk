package com.bankapp.bank_backend.repository;
// Transaction Repository (TransactionRepository.java)

import com.bankapp.bank_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountOrderByTransactionDateDesc(String accountNumber);

    List<Transaction> findByToAccountOrderByTransactionDateDesc(String accountNumber);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :accountNumber OR t.toAccount = :accountNumber) ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(@Param("accountNumber") String accountNumber);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :accountNumber OR t.toAccount = :accountNumber) AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountNumberAndDateRange(@Param("accountNumber") String accountNumber,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);
}
