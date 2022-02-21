package com.example.medicationapp.model;

import java.util.Calendar;

public class MedDetails {
    Calendar time;
    short dose;
    String type;
    int taken;

    public MedDetails() {}

    public MedDetails(Calendar time, short dose, String type, int taken) {
        this.time = time;
        this.dose = dose;
        this.type = type;
        this.taken = taken;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public short getDose() {
        return dose;
    }

    public void setDose(short dose) {
        this.dose = dose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}