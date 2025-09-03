package com.saurav.finance_tracker.dto;


import java.time.LocalDateTime;

public class LoginEvent {
    String username;
    LocalDateTime loginDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }
}
