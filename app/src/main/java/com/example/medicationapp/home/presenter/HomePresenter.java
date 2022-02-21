package com.example.medicationapp.home.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

import java.util.List;

public class HomePresenter {

    private final Repository repository;

    public HomePresenter(Context context) {
        this.repository = Repository.getInstance(context);
    }

    public void addMedication(Medication medication) {
        repository.insertMedication(medication);
    }

    public LiveData<Medication> getMedication(String name) {
        return repository.getMedication(name);
    }

    public LiveData<List<Medication>> getMedicationList() {
        return repository.getAllMedications();
    }

    public LiveData<List<Medication>> getMedicationListByAllDay() {
        return repository.getAllMedicationInAllDay();
    }

}
