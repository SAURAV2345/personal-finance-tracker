package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.dto.MonthySummaryDto;
import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SummaryService {

    private ExpenseRepository expenseRepository;

    public SummaryService(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }

    public MonthySummaryDto monthlySummary(Long userId, int month, int year){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findExpensesForMonth(userId, startDate, endDate);
        return new MonthySummaryDto(month,year,expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum());


    }
}

