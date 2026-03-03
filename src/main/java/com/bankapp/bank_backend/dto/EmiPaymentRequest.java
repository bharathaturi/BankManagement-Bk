package com.bankapp.bank_backend.dto;

import java.math.BigDecimal;

public class EmiPaymentRequest {
    private Long emiId;
    private String accountNumber;
    private BigDecimal amountPaid;

    // Getters and Setters
    public Long getEmiId() {
        return emiId;
    }

    public void setEmiId(Long emiId) {
        this.emiId = emiId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}
