package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.model.User;
import com.saurav.finance_tracker.repository.ExpenseRepository;
import com.saurav.finance_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseEventProducer expenseEventProducer;

    public List<Expense> getAllExpense(String username){
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseRepository.findByUser(user);
    }

    public Expense createExpense(Expense expense){
        Expense saved = expenseRepository.save(expense);
        expenseEventProducer.publishExpenseEvent("ExpenseCreated-"+expense.getId());
        return saved;


    }

    public List<Expense> getExpenseById(Long id){
        List<Expense> expense = expenseRepository.findByUserId(id);
        return expense;

    }

    public List<Expense> getFilteredExpenses(Long userId, String category, Double minAmount, Double maxAmount, LocalDate startDate, LocalDate endDate, String username)
    {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .filter(e -> category == null || e.getDescription().equalsIgnoreCase(category))
                .filter(e -> minAmount == null || e.getAmount() >= minAmount)
                .filter(e -> maxAmount == null || e.getAmount() <= maxAmount)
                .filter(e -> startDate == null || !e.getDate().isBefore(startDate))
                .filter(e -> endDate == null || !e.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }


    public List<Expense> getExpensesCursor(Long userId,Long cursor, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").ascending());
        if (cursor == null || cursor==0) {
            return expenseRepository.findByUserIdOrderByIdAsc(userId,pageable);
        } else {
            return expenseRepository.findByUserIdAndIdGreaterThanOrderByIdAsc(userId,cursor,pageable);
        }
    }
}
