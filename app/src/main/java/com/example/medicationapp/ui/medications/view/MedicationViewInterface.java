package com.example.medicationapp.ui.medications.view;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.models.Medication;

import java.util.List;

public interface MedicationViewInterface {
    void getAllActiveMedicines(LiveData<List<Medication>> active);
    void getAllInActiveMedicines(LiveData<List<Medication>> inActive);
}
