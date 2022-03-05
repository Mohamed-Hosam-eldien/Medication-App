package com.example.medicationapp.medications.addEditMed.model;

public class ReminderTime {
    int hour;
    int minute;
    int pill;

    public ReminderTime(int hour, int minute, int pill) {
        this.hour = hour;
        this.minute = minute;
        this.pill = pill;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getPill() {
        return pill;
    }

    public void setPill(int pill) {
        this.pill = pill;
    }
}
