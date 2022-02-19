package com.example.medicationapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Medication")
public class Medication {

    @PrimaryKey()
    @NonNull
    private String name;
    private int refillNo;
    private int isActive = 1;
    private List<MedDetails> medDetails;
    private MedScheduler medScheduler;
    private int image;
    private int midStrength;
    private String timeToFood;

    public Medication(String name, List<MedDetails> medDetails,
                      MedScheduler medScheduler,int midStrength,
                      int isActive, int refillNo, String timeToFood) {
        this.timeToFood = timeToFood;
        this.refillNo = refillNo;
        this.isActive = isActive;
        this.name = name;
        this.medDetails = medDetails;
        this.medScheduler = medScheduler;
//        this.image = image;
        this.midStrength = midStrength;
    }

    public String getTimeToFood() {
        return timeToFood;
    }

    public void setTimeToFood(String timeToFood) {
        this.timeToFood = timeToFood;
    }

    public int getRefillNo() {
        return refillNo;
    }

    public void setRefillNo(int refillNo) {
        this.refillNo = refillNo;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MedDetails> getMedDetails() {
        return medDetails;
    }

    public void setMedDetails(List<MedDetails> medDetails) {
        this.medDetails = medDetails;
    }

    public MedScheduler getMedScheduler() {
        return medScheduler;
    }

    public void setMedScheduler(MedScheduler medScheduler) {
        this.medScheduler = medScheduler;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getMidStrength() {
        return midStrength;
    }

    public void setMidStrength(int midStrength) {
        this.midStrength = midStrength;
    }

}
