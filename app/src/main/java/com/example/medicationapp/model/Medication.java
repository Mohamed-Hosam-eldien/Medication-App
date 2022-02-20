package com.example.medicationapp.model;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Medication {

    String name;
    List<MedDetails> medDetails;
    MedScheduler medScheduler;
    int image;
    int midStrength;

    public Medication(){
    }

    public Medication(String name, ArrayList<MedDetails> medDetails,
                      MedScheduler medScheduler, int image, int midStrength,
                      MedInstructions medInstructions) {
        this.name = name;
        this.medDetails = medDetails;
        this.medScheduler = medScheduler;
        this.image = image;
        this.midStrength = midStrength;
        this.medInstructions = medInstructions;
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

    public MedInstructions getMedInstructions() {
        return medInstructions;
    }

    public void setMedInstructions(MedInstructions medInstructions) {
        this.medInstructions = medInstructions;
    }

    MedInstructions medInstructions;
}
