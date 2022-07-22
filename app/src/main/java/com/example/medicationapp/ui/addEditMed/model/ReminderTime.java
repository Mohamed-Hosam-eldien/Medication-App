package com.example.medicationapp.ui.addEditMed.model;

public class ReminderTime {
    private int hour;
    private int minute;
    private int pill;

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
