package com.example.medicationapp.models;

import java.util.List;

public class MedScheduler {
    private String startDate;
    private List <String> days;

    public MedScheduler() {}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
