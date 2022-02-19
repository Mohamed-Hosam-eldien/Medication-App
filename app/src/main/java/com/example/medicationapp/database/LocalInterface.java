package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;

import java.util.List;

public interface LocalInterface {

    public void insertMedicine(Medication medication);

    public void deleteMedication(Medication medication);

    LiveData<List<Medication>> getAllMedications();

    LiveData<Medication> getMedication(String name);

    LiveData<List<Medication>> getAllActiveMedicines();

    LiveData<List<Medication>> getAllInActiveMedicines();
}
