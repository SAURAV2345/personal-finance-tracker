package com.saurav.finance_tracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userLoginTopic() {
        return TopicBuilder.name("userLogin-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic expensesTopic() {
        return TopicBuilder.name("expenses-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
