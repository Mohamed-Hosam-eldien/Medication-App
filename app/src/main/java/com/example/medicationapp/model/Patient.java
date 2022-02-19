package com.example.medicationapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;


public class Patient {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    private short age;
    private int image;
    private String disease;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @NonNull
    private List<Medication> medications;

    public Patient(int id, String firstName, String lastName,
                   short age, int image, String disease, List<Medication> medications) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.image = image;
        this.disease = disease;
        this.medications = medications;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }


}
