package com.example.rewards.service;

import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Simulates an asynchronous remote data-source call, as requested in the
     * assignment tips. Replace with WebClient/RestTemplate or a DB call in production.
     */
    @Async("transactionExecutor")
    public CompletableFuture<List<Transaction>> fetchTransactions(String customerId,
                                                                   LocalDate startDate,
                                                                   LocalDate endDate) {
        List<Transaction> transactions = transactionRepository
                .findByCustomerAndDateRange(customerId, startDate, endDate);
        return CompletableFuture.completedFuture(transactions);
    }
}
