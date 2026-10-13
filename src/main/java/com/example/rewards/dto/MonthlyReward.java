package com.example.rewards.dto;

public class MonthlyReward {
    private final String month;
    private final int transactionCount;
    private final long rewardPoints;
    private final String totalSpend;

    public MonthlyReward(String month, int transactionCount, long rewardPoints, String totalSpend) {
        this.month = month;
        this.transactionCount = transactionCount;
        this.rewardPoints = rewardPoints;
        this.totalSpend = totalSpend;
    }

    public String getMonth() { return month; }
    public int getTransactionCount() { return transactionCount; }
    public long getRewardPoints() { return rewardPoints; }
    public String getTotalSpend() { return totalSpend; }
}
