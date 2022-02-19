package com.example.medicationapp.model;

public class MedDetails {
    String time;
    int dose;
    String type;

    public MedDetails() {}

    public MedDetails(String time, int dose, String type) {
        this.time = time;
        this.dose = dose;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
