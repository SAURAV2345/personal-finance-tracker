package com.saurav.finance_tracker.controller;


import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.model.User;
import com.saurav.finance_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // get all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(HttpSession session){
        User user = (User)session.getAttribute("user");
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(expenseRepository.findByUser(user));
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
