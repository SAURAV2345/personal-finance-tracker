package com.saurav.finance_tracker.controller;


import com.saurav.finance_tracker.dto.MonthySummaryDto;
import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.model.User;
import com.saurav.finance_tracker.repository.ExpenseRepository;
import com.saurav.finance_tracker.service.ExpenseService;
import com.saurav.finance_tracker.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SummaryService summaryService;

    // get all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(HttpSession session, Principal principal){
        User user = (User)session.getAttribute("user");
        String user1 = principal.getName();
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(expenseService.getAllExpense(user1));
        //return ResponseEntity.ok(expenseRepository.findByUser(user));
    }

    // Create expense
    @PostMapping
    public Expense createExpense(@RequestBody Expense expense,HttpSession session){
        User user = (User)session.getAttribute("user");
        expense.setUser(user);
        return expenseService.createExpense(expense);
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<Expense>> getExpenseById(@PathVariable Long id) {
        System.out.println("Fetching expense with ID: " + id);
        List<Expense> expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/filter")
    public List<Expense> getExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Long userId,Principal principal
    ) {
        return expenseService.getFilteredExpenses(userId, category, minAmount, maxAmount, startDate, endDate,principal.getName());
    }
    @GetMapping("/user/{userId}")
    public Map<String, Object> getExpenses(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "10") int size){

        List<Expense> expenses = expenseService.getExpensesCursor(userId,cursor,size);

        Long nextCursor = expenses.isEmpty()
                ? null
                : expenses.get(expenses.size() - 1).getId();

        Map<String, Object> response = new HashMap<>();
        response.put("data", expenses);
        response.put("nextCursor", nextCursor);
        return response;

    }
    @GetMapping("/summary/monthly/{userId}")
    public MonthySummaryDto getSummary(@PathVariable Long userId,
                                       @RequestParam int month,
                                       @RequestParam int year){
        return summaryService.monthlySummary(userId,month,year);
    }


}
