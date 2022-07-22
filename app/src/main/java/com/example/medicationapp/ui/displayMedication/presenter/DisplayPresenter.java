package com.example.medicationapp.ui.displayMedication.presenter;

import android.content.Context;

import com.example.medicationapp.models.Medication;
import com.example.medicationapp.repository.Repository;

public class DisplayPresenter {
    private final Repository repo;

    public DisplayPresenter(Context context) {
        this.repo = Repository.getInstance(context);
    }

    public void deleteMedication(Medication med) {
        repo.deleteMedication(med);
    }

    public void updateActive(int active, String id) {
        repo.updateActive(active, id);
    }

    public void refill(int amount, String id) {
        repo.refill(amount, id);
    }

    public void updateFirebase(int refill, String medId) {}


}
