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
        customers.put("C001", new Customer("C001", "Gaurav Shah", "gaurav@example.com"));
        customers.put("C002", new Customer("C002", "Meeta Patil", "meeta@example.com"));
        customers.put("C003", new Customer("C003", "Rohit Desai", "rohit@example.com"));
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public java.util.List<Customer> findAll() {
        return Arrays.asList(customers.values().toArray(new Customer[0]));
    }
}
