package com.example.rewards.repository;

import com.example.rewards.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomerRepository {
    private final Map<String, Customer> customers = new LinkedHashMap<String, Customer>();

    public CustomerRepository() {
        customers.put("C001", new Customer("C001", "Aarav Sharma", "aarav@example.com"));
        customers.put("C002", new Customer("C002", "Maya Patel", "maya@example.com"));
        customers.put("C003", new Customer("C003", "Rohan Desai", "rohan@example.com"));
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public java.util.List<Customer> findAll() {
        return Arrays.asList(customers.values().toArray(new Customer[0]));
    }
}
