package com.bankapp.bank_backend.repository;


// Account Repository (AccountRepository.java)
import com.bankapp.bank_backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(String customerId);

    List<Account> findByIsActiveTrue();

    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.isActive = true")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") String customerId);

    boolean existsByAccountNumber(String accountNumber);
}
