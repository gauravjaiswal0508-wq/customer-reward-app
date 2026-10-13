package com.example.rewards.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RewardCalculator {
    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWO = new BigDecimal("2");

    /**
     * Points are awarded on completed whole-dollar amounts in each tier.
     * $0-$50: 0 points; $50-$100: 1 point per dollar; above $100:
     * 1 point per dollar in the $50-$100 tier plus 2 per dollar above $100.
     */
    public long calculate(BigDecimal amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Transaction amount must be zero or greater.");
        }

        BigDecimal points = BigDecimal.ZERO;
        if (amount.compareTo(FIFTY) > 0) {
            BigDecimal eligibleBelow100 = amount.min(ONE_HUNDRED).subtract(FIFTY);
            points = points.add(eligibleBelow100.setScale(0, RoundingMode.FLOOR));

            if (amount.compareTo(ONE_HUNDRED) > 0) {
                BigDecimal above100 = amount.subtract(ONE_HUNDRED);
                points = points.add(above100.setScale(0, RoundingMode.FLOOR).multiply(TWO));
            }
        }
        return points.longValueExact();
    }
}
