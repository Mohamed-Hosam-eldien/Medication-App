package com.example.medicationapp.medications.view;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;

import java.util.List;

public interface MedicationViewInterface {
    void getAllActiveMedicines(LiveData<List<Medication>> active);
    void getAllInActiveMedicines(LiveData<List<Medication>> inActive);
}
