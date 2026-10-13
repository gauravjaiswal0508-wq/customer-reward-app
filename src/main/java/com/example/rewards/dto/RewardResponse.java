package com.example.rewards.dto;

import com.example.rewards.model.Customer;

import java.time.LocalDate;
import java.util.List;

public class RewardResponse {
    private final String customerId;
    private final String customerName;
    private final String email;
    private final int timeframeMonths;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int transactionCount;
    private final String totalSpend;
    private final long totalRewardPoints;
    private final List<MonthlyReward> monthlyRewards;
    private final List<TransactionReward> transactions;

    public RewardResponse(Customer customer, int timeframeMonths, LocalDate startDate,
                          LocalDate endDate, int transactionCount, String totalSpend,
                          long totalRewardPoints, List<MonthlyReward> monthlyRewards,
                          List<TransactionReward> transactions) {
        this.customerId = customer.getId();
        this.customerName = customer.getName();
        this.email = customer.getEmail();
        this.timeframeMonths = timeframeMonths;
        this.startDate = startDate;
        this.endDate = endDate;
        this.transactionCount = transactionCount;
        this.totalSpend = totalSpend;
        this.totalRewardPoints = totalRewardPoints;
        this.monthlyRewards = monthlyRewards;
        this.transactions = transactions;
    }

    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getEmail() { return email; }
    public int getTimeframeMonths() { return timeframeMonths; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getTransactionCount() { return transactionCount; }
    public String getTotalSpend() { return totalSpend; }
    public long getTotalRewardPoints() { return totalRewardPoints; }
    public List<MonthlyReward> getMonthlyRewards() { return monthlyRewards; }
    public List<TransactionReward> getTransactions() { return transactions; }
}
