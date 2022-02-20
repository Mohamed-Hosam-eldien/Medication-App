package com.example.medicationapp.model;

public class MedInstructions {
    String timeToFood;
    String otherInstructions;

    public MedInstructions(String timeToFood, String otherInstructions) {
        this.timeToFood = timeToFood;
        this.otherInstructions = otherInstructions;
    }

    public String getTimeToFood() {
        return timeToFood;
    }

    public void setTimeToFood(String timeToFood) {
        this.timeToFood = timeToFood;
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }
}
