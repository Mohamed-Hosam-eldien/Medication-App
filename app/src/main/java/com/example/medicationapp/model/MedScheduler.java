package com.example.medicationapp.model;

import java.util.ArrayList;
import java.util.List;

public class MedScheduler {
    String startDate;
//    short treatDaysNo;
    List <String> days;

    public MedScheduler() {
    }

    public MedScheduler(String startDate, List<String> days) {
        this.startDate = startDate;
//        this.treatDaysNo = treatDaysNo;
        this.days = days;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

//    public short getTreatDaysNo() {
//        return treatDaysNo;
//    }

//    public void setTreatDaysNo(short treatDaysNo) {
//        this.treatDaysNo = treatDaysNo;
//    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
