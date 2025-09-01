package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.dto.ExpenseEvent;
import com.saurav.finance_tracker.model.Expense;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventListner {

    private final SummaryService summaryService;

    public ExpenseEventListner(SummaryService summaryService){
        this.summaryService=summaryService;
    }

    @KafkaListener(topics = "expenses-topic",groupId = "expense-group")
    public void handleExpenseEvent(ExpenseEvent event) {
        // Get the Expenseid from the event published
        System.out.println("Expense even received updating summary");
        int month = event.getExpenseDate().getMonthValue();
        int year = event.getExpenseDate().getYear();

        summaryService.refreshMonthlySummary(event.getUserId(), month, year);
    }
}
