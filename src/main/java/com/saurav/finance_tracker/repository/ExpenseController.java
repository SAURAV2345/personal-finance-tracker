package com.saurav.finance_tracker.repository;


import com.saurav.finance_tracker.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // get all expenses
    @GetMapping
    public List<Expense> getExpenses(){
        return expenseRepository.findAll();
    }

    // Create expense
    @PostMapping
    public Expense createExpense(@RequestBody Expense expense){
        return expenseRepository.save(expense);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        System.out.println("Fetching expense with ID: " + id);
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
