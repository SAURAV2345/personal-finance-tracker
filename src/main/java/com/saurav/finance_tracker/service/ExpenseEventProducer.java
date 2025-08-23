package com.saurav.finance_tracker.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "expenses-topic";

    public ExpenseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishExpenseEvent(String expenseJson) {
        kafkaTemplate.send(TOPIC, expenseJson);
    }
}