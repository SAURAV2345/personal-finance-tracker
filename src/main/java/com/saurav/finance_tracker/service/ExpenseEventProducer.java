package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.dto.ExpenseEvent;
import com.saurav.finance_tracker.dto.LoginEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    //private static final String TOPIC = "expenses-topic";

    public ExpenseEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishExpenseEvent(ExpenseEvent event) {
        kafkaTemplate.send("expenses-topic",String.valueOf(event.getUserId()), event);

    }
    public void publishLoginEvent(LoginEvent event) {
        kafkaTemplate.send("userLogin-topic",String.valueOf(event.getUsername()), event);
    }

}