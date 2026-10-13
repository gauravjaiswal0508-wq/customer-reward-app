package com.example.rewards.service;

import com.example.rewards.dto.MonthlyReward;
import com.example.rewards.dto.RewardResponse;
import com.example.rewards.dto.TransactionReward;
import com.example.rewards.exception.CustomerNotFoundException;
import com.example.rewards.model.Customer;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RewardService {
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;
    private final RewardCalculator rewardCalculator;

    public RewardService(CustomerRepository customerRepository,
                         TransactionService transactionService,
                         RewardCalculator rewardCalculator) {
        this.customerRepository = customerRepository;
        this.transactionService = transactionService;
        this.rewardCalculator = rewardCalculator;
    }

    public RewardResponse getRewards(String customerId, int months) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("customerId must not be blank.");
        }
        if (months < 1 || months > 36) {
            throw new IllegalArgumentException("months must be between 1 and 36.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = YearMonth.from(endDate).minusMonths(months - 1).atDay(1);

        List<Transaction> transactions;
        try {
            transactions = transactionService.fetchTransactions(customerId, startDate, endDate)
                    .get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while retrieving transactions.", ex);
        } catch (ExecutionException | CompletionException ex) {
            throw new IllegalStateException("Unable to retrieve transactions.", ex);
        }

        Map<YearMonth, List<Transaction>> byMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate()),
                        TreeMap::new, Collectors.toList()));

        List<MonthlyReward> monthlyRewards = new ArrayList<MonthlyReward>();
        YearMonth firstMonth = YearMonth.from(startDate);
        YearMonth lastMonth = YearMonth.from(endDate);

        // Include months with no transactions so the response is predictable.
        for (YearMonth month = firstMonth; !month.isAfter(lastMonth); month = month.plusMonths(1)) {
            List<Transaction> monthTransactions = byMonth.getOrDefault(month, new ArrayList<Transaction>());
            long points = monthTransactions.stream()
                    .mapToLong(t -> rewardCalculator.calculate(t.getAmount())).sum();
            BigDecimal spend = monthTransactions.stream()
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            monthlyRewards.add(new MonthlyReward(month.format(MONTH_FORMAT),
                    monthTransactions.size(), points, spend.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
        }

        List<TransactionReward> transactionRewards = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate))
                .map(t -> new TransactionReward(t.getId(), t.getTransactionDate(), t.getDescription(),
                        t.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                        rewardCalculator.calculate(t.getAmount())))
                .collect(Collectors.toList());

        long totalPoints = transactionRewards.stream().mapToLong(TransactionReward::getRewardPoints).sum();
        BigDecimal totalSpend = transactions.stream().map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RewardResponse(customer, months, startDate, endDate, transactions.size(),
                totalSpend.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                totalPoints, monthlyRewards, transactionRewards);
    }
}
