package com.saurav.finance_tracker.dto;

public class MonthySummaryDto {
    private int month;
    private int year;
    private double monthSummary;

    public MonthySummaryDto(int month, int year, double monthSummary) {
        this.month = month;
        this.year = year;
        this.monthSummary = monthSummary;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getMonthSummary() {
        return monthSummary;
    }

    public void setMonthSummary(double monthSummary) {
        this.monthSummary = monthSummary;
    }
}
