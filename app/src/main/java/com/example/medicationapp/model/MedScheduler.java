package com.example.medicationapp.model;

import java.util.ArrayList;
import java.util.List;

public class MedScheduler {
    String startDate;
    List <String> days;

    public MedScheduler() {
    }

    public MedScheduler(String startDate, List<String> days) {
        this.startDate = startDate;
        this.days = days;
    }

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
