package com.example.medicationapp.repository;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Patient;

import java.util.List;

public interface RepositoryInterface {

    public void insertPatient(Patient patient);
    public void deletePatient(Patient patient);
    public LiveData<List<Patient>> getAllPatients();
    public LiveData<Patient> getPatient(int id);
}
