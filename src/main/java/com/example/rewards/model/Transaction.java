package com.example.rewards.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private final String id;
    private final String customerId;
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final String description;

    public Transaction(String id, String customerId, BigDecimal amount,
                       LocalDate transactionDate, String description) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getDescription() { return description; }
}
