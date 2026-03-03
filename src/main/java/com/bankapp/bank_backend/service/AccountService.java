package com.bankapp.bank_backend.service;

// Account Service (AccountService.java)
import com.bankapp.bank_backend.entity.Account;
import com.bankapp.bank_backend.entity.Customer;
import com.bankapp.bank_backend.entity.Transaction;
import com.bankapp.bank_backend.repository.AccountRepository;
import com.bankapp.bank_backend.repository.CustomerRepository;
import com.bankapp.bank_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(Account account) {
        // Generate unique account number
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

        // Encrypt PIN
        account.setPin(passwordEncoder.encode(account.getPin()));

        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public java.util.Map<String, Object> registerAndCreateAccount(
            com.bankapp.bank_backend.dto.RegisterRequest request) {
        // 1. Create or Find Customer (For simplicity, creating a new one)
        Customer customer = new Customer();
        customer.setCustomerId(generateCustomerId());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        // Map new Personal & Contact details
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setNationality(request.getNationality());
        customer.setParentName(request.getParentName());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setOccupation(request.getOccupation());
        customer.setAnnualIncome(request.getAnnualIncome());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPinCode(request.getPinCode());

        // Map KYC
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setPassportNumber(request.getPassportNumber());
        customer.setVoterId(request.getVoterId());

        // Map Security & Declarations
        customer.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        customer.setSecurityQuestion(request.getSecurityQuestion());
        customer.setSecurityAnswer(request.getSecurityAnswer());
        customer.setAcceptedTerms(request.getAcceptedTerms());
        customer.setFatcaDeclaration(request.getFatcaDeclaration());
        customer.setDigitalSignatureConsent(request.getDigitalSignatureConsent());

        customer = customerRepository.save(customer);

        // 2. Create Account linked to Customer
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setCustomerId(customer.getCustomerId());
        account.setAccountType(request.getAccountType());
        account.setBalance(
                request.getInitialDeposit() != null ? request.getInitialDeposit() : java.math.BigDecimal.ZERO);
        account.setPin(passwordEncoder.encode(request.getPin()));
        account.setIsActive(true);

        // Map new Account details
        account.setBranchName(request.getBranchName());
        account.setInitialDeposit(
                request.getInitialDeposit() != null ? request.getInitialDeposit() : java.math.BigDecimal.ZERO);
        account.setNomineeName(request.getNomineeName());
        account.setNomineeRelationship(request.getNomineeRelationship());

        account = accountRepository.save(account);

        // 3. Construct response matching frontend expectation
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("customer", customer);
        response.put("account", account);

        return response;
    }

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public boolean authenticateAccount(String accountNumber, String pin) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isPresent() && account.get().getIsActive()) {
            return passwordEncoder.matches(pin, account.get().getPin());
        }
        return false;
    }

    public void changePin(String accountNumber, String oldPin, String newPin) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();
        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (!passwordEncoder.matches(oldPin, account.getPin())) {
            throw new RuntimeException("Current PIN is incorrect");
        }

        account.setPin(passwordEncoder.encode(newPin));
        accountRepository.save(account);
    }

    public Account deposit(String accountNumber, BigDecimal amount, String description, String pin) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();
        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new RuntimeException("pin incorrect");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        // Update balance
        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(accountNumber);
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "Deposit");
        transactionRepository.save(transaction);

        return savedAccount;
    }

    public Account withdraw(String accountNumber, BigDecimal amount, String description, String pin) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();
        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new RuntimeException("pin incorrect");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update balance
        account.setBalance(account.getBalance().subtract(amount));
        Account savedAccount = accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(accountNumber);
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAW);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "Withdrawal");
        transactionRepository.save(transaction);

        return savedAccount;
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description,
            String pin) {
        // Validate both accounts exist and are active
        Optional<Account> fromAccountOpt = accountRepository.findByAccountNumber(fromAccountNumber);
        Optional<Account> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber);

        if (fromAccountOpt.isEmpty()) {
            throw new RuntimeException("Source account not found");
        }
        if (toAccountOpt.isEmpty()) {
            throw new RuntimeException("Destination account not found");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (!fromAccount.getIsActive()) {
            throw new RuntimeException("Source account is not active");
        }
        if (!toAccount.getIsActive()) {
            throw new RuntimeException("Destination account is not active");
        }

        if (!passwordEncoder.matches(pin, fromAccount.getPin())) {
            throw new RuntimeException("pin incorrect");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(fromAccountNumber);
        transaction.setToAccount(toAccountNumber);
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "Transfer");
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    public List<Transaction> getTransactionHistory(String accountNumber, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepository.findByAccountNumberAndDateRange(accountNumber, startDate, endDate);
    }

    private String generateAccountNumber() {
        String prefix = "ACC";
        String uniqueId = String.valueOf(System.currentTimeMillis());
        return prefix + uniqueId;
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    public Account updateCardLimit(String accountNumber, BigDecimal newLimit) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOpt.get();
        if (!account.getIsActive()) {
            throw new RuntimeException("Account is not active");
        }

        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Card limit cannot be negative");
        }

        account.setDailyCardLimit(newLimit);
        return accountRepository.save(account);
    }

    public Customer updateCustomerDetails(String customerId, java.util.Map<String, String> updates) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerId(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found.");
        }

        Customer customer = customerOpt.get();
        if (updates.containsKey("email")) {
            customer.setEmail(updates.get("email"));
        }
        if (updates.containsKey("phone")) {
            customer.setPhone(updates.get("phone"));
        }
        if (updates.containsKey("address")) {
            customer.setAddress(updates.get("address"));
        }
        if (updates.containsKey("dateOfBirth")) {
            customer.setDateOfBirth(updates.get("dateOfBirth"));
        }
        if (updates.containsKey("aadhaarNumber")) {
            customer.setAadhaarNumber(updates.get("aadhaarNumber"));
        }
        if (updates.containsKey("panNumber")) {
            customer.setPanNumber(updates.get("panNumber"));
        }

        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(String customerId) {
        return customerRepository.findByCustomerId(customerId);
    }
}