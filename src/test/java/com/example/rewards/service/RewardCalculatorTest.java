package com.example.rewards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RewardCalculatorTest {
    private RewardCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RewardCalculator();
    }

    @Test
    void earnsNoPointsAtOrBelowFiftyDollars() {
        assertEquals(0, calculator.calculate(new BigDecimal("0")));
        assertEquals(0, calculator.calculate(new BigDecimal("49.99")));
        assertEquals(0, calculator.calculate(new BigDecimal("50.00")));
    }

    @Test
    void earnsOnePointPerWholeDollarBetweenFiftyAndOneHundred() {
        assertEquals(1, calculator.calculate(new BigDecimal("51")));
        assertEquals(50, calculator.calculate(new BigDecimal("100")));
        assertEquals(25, calculator.calculate(new BigDecimal("75.99")));
    }

    @Test
    void earnsTwoPointsPerWholeDollarAboveOneHundredAndOnePointInMiddleTier() {
        assertEquals(90, calculator.calculate(new BigDecimal("120")));
        assertEquals(150, calculator.calculate(new BigDecimal("150")));
        assertEquals(250, calculator.calculate(new BigDecimal("200.99")));
    }

    @Test
    void handlesTierBoundariesAndFractionalAmounts() {
        assertEquals(0, calculator.calculate(new BigDecimal("50.99")));
        assertEquals(0, calculator.calculate(new BigDecimal("49.999")));
        assertEquals(49, calculator.calculate(new BigDecimal("99.99")));
        assertEquals(50, calculator.calculate(new BigDecimal("100.99")));
        assertEquals(52, calculator.calculate(new BigDecimal("101.99")));
    }

    @Test
    void rejectsNegativeOrNullAmounts() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }
}
