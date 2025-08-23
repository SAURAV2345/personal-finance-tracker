package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.dto.ExpenseEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventProducer {
    private final KafkaTemplate<String, ExpenseEvent> kafkaTemplate;
    private static final String TOPIC = "expenses-topic";

    public ExpenseEventProducer(KafkaTemplate<String, ExpenseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishExpenseEvent(ExpenseEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}