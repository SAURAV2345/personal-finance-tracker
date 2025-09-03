package com.saurav.finance_tracker.service;

import com.saurav.finance_tracker.dto.ExpenseEvent;
import com.saurav.finance_tracker.dto.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {


    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "userLogin-topic",groupId = "login-group")
    public void sendEmailNotification(LoginEvent event,@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                      @Header(KafkaHeaders.OFFSET) long offset) {

        System.out.println("New user login topic received in partition ="+ partition+" offset = "+offset);
        sendEmail("sauravchoudhary78@gmail.com", " New user login detected for user ","User datils : "+event.getUsername());
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);
    }
}
