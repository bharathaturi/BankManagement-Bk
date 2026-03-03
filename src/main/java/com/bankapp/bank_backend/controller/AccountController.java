package com.bankapp.bank_backend.controller;

// Account Controller (AccountController.java)
import com.bankapp.bank_backend.entity.Account;
import com.bankapp.bank_backend.entity.Transaction;
import com.bankapp.bank_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register-and-create")
    public ResponseEntity<?> registerAndCreateAccount(
            @RequestBody com.bankapp.bank_backend.dto.RegisterRequest request) {
        try {
            java.util.Map<String, Object> response = accountService.registerAndCreateAccount(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            return ResponseEntity.ok(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        Optional<Account> accountOpt = accountService.getAccountByNumber(accountNumber);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("account", account);

            Optional<com.bankapp.bank_backend.entity.Customer> customerOpt = accountService
                    .getCustomerById(account.getCustomerId());
            if (customerOpt.isPresent()) {
                response.put("customer", customerOpt.get());
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * @GetMapping("/users")
     * public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
     * Optional<Account> account = accountService.getAccountByNumber(accountNumber);
     * if (account.isPresent()) {
     * return ResponseEntity.ok(account.get());
     * } else {
     * return ResponseEntity.notFound().build();
     * }
     * }
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAccount(@RequestBody Map<String, String> credentials) {
        String accountNumber = credentials.get("accountNumber");
        String pin = credentials.get("pin");

        boolean isAuthenticated = accountService.authenticateAccount(accountNumber, pin);
        if (isAuthenticated) {
            Optional<Account> account = accountService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(Map.of("authenticated", true, "account", account.get()));
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber,
            @RequestBody Map<String, Object> request) {
        try {
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            String pin = (String) request.get("pin");

            Account updatedAccount = accountService.deposit(accountNumber, amount, description, pin);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accountNumber,
            @RequestBody Map<String, Object> request) {
        try {
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            String pin = (String) request.get("pin");

            Account updatedAccount = accountService.withdraw(accountNumber, amount, description, pin);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<?> transfer(@PathVariable String accountNumber, @RequestBody Map<String, Object> request) {
        try {
            String fromAccount = accountNumber;
            String toAccount = (String) request.get("toAccount");
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            String pin = (String) request.get("pin");

            accountService.transfer(fromAccount, toAccount, amount, description, pin);
            return ResponseEntity.ok(Map.of("success", true, "message", "Transfer completed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable String customerId) {
        List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountNumber}/card-limit")
    public ResponseEntity<?> updateCardLimit(@PathVariable String accountNumber,
            @RequestBody Map<String, Object> request) {
        try {
            BigDecimal newLimit = new BigDecimal(request.get("dailyCardLimit").toString());
            Account updatedAccount = accountService.updateCardLimit(accountNumber, newLimit);
            return ResponseEntity.ok(Map.of("success", true, "message", "Card limit updated successfully",
                    "account", updatedAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/customer/{customerId}")
    public ResponseEntity<?> updateCustomerDetails(@PathVariable String customerId,
            @RequestBody Map<String, String> updates) {
        try {
            com.bankapp.bank_backend.entity.Customer updatedCustomer = accountService.updateCustomerDetails(customerId,
                    updates);
            return ResponseEntity.ok(Map.of("success", true, "message", "Customer details updated successfully",
                    "customer", updatedCustomer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}