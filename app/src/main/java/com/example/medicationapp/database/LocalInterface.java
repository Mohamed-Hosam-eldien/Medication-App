package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Patient;

import java.util.List;

public interface LocalInterface {
    public void insertPatient(Patient patient);
    public void deleterPatient(Patient patient);
    LiveData<List<Patient>> getAllPatients();
    LiveData<Patient> getPatient(int id);
}
