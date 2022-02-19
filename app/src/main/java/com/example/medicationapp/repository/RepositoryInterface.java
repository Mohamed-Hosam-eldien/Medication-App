package com.example.medicationapp.repository;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;

import java.util.List;

public interface RepositoryInterface {

    public void insertMedication(Medication medication);

    public void deleteMedication(Medication medication);

    public LiveData<List<Medication>> getAllMedications();

    public LiveData<Medication> getMedication(String name);

    public LiveData<List<Medication>> getActiveMedicines();

    public LiveData<List<Medication>> getInActiveMedicines();


}
