package com.saurav.finance_tracker.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaErrorHandlerConfig(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Send failed messages to a Dead Letter Topic
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        // Exponential backoff: starts at 1 second, doubles each time, max 10 seconds, retries 5 times
        ExponentialBackOff backOff = new ExponentialBackOff();
        backOff.setInitialInterval(1000L);   // 1 second
        backOff.setMultiplier(2.0);          // double each retry
        backOff.setMaxInterval(10000L);      // cap at 10 seconds
        backOff.setMaxElapsedTime(30000L);   // stop after 30s total

        return new DefaultErrorHandler(recoverer, backOff);
    }
}

