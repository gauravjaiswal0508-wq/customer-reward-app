package com.example.rewards.dto;

import java.time.LocalDate;

public class TransactionReward {
    private final String transactionId;
    private final LocalDate transactionDate;
    private final String description;
    private final String amount;
    private final long rewardPoints;

    public TransactionReward(String transactionId, LocalDate transactionDate, String description,
                             String amount, long rewardPoints) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.rewardPoints = rewardPoints;
    }

    public String getTransactionId() { return transactionId; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getDescription() { return description; }
    public String getAmount() { return amount; }
    public long getRewardPoints() { return rewardPoints; }
}
