package com.example.medicationapp.ui.home.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;
import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;
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

    public LiveData<List<Medication>> getMedicationListByAllDay(long currentDay) {
        return repository.getAllMedicationInAllDay(currentDay);
    }

    public Medication getMedicationToPopup(String id) {
        return repository.getMedToPopup(id);
    }

    public void updateRefill(int amount, String name){
        repository.refill(amount, name);
    }

    public void updateTaken(List<MedDetails> details, String id){
        repository.updateTaken(details, id);
    }

    public void deleteMedicine(Medication medication){
        repository.deleteMedication(medication);
    }

}
