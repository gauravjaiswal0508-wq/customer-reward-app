package com.example.rewards.repository;

import com.example.rewards.model.Transaction;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory sample data source. Replace this class with a database repository
 * when connecting the API to persistent storage.
 */
@Repository
public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<Transaction>();

    public TransactionRepository() {
        LocalDate today = LocalDate.now();

        add("T1001", "C001", "120.00", today.minusDays(2), "Electronics");
        add("T1002", "C001", "75.00", today.minusDays(10), "Groceries");
        add("T1003", "C001", "49.99", today.minusMonths(1).withDayOfMonth(10), "Books");
        add("T1004", "C001", "210.00", today.minusMonths(1).withDayOfMonth(15), "Home appliances");
        add("T1005", "C001", "100.00", today.minusMonths(2).withDayOfMonth(5), "Furniture");
        add("T2001", "C002", "55.00", today.minusDays(4), "Clothing");
        add("T2002", "C002", "350.00", today.minusMonths(1).withDayOfMonth(7), "Laptop");
        add("T2003", "C002", "40.00", today.minusMonths(2).withDayOfMonth(12), "Accessories");
        add("T3001", "C003", "500.00", today.minusDays(7), "Travel");
        add("T3002", "C003", "90.00", today.minusMonths(1).withDayOfMonth(20), "Dining");
    }

    private void add(String id, String customerId, String amount, LocalDate date, String description) {
        transactions.add(new Transaction(id, customerId, new BigDecimal(amount), date, description));
    }

    public List<Transaction> findByCustomerAndDateRange(String customerId,
                                                        LocalDate startInclusive,
                                                        LocalDate endInclusive) {
        return transactions.stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .filter(t -> !t.getTransactionDate().isBefore(startInclusive))
                .filter(t -> !t.getTransactionDate().isAfter(endInclusive))
                .sorted((a, b) -> a.getTransactionDate().compareTo(b.getTransactionDate()))
                .collect(Collectors.toList());
    }
}
