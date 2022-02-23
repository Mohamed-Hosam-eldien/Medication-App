package com.example.medicationapp.medications.view.displayMedication;

import android.content.Context;

import com.example.medicationapp.medications.view.MedicationViewInterface;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

public class DisplayPresenter {
    Context context;
    Repository repo;

    public DisplayPresenter(Context context) {
        this.context = context;
        this.repo = Repository.getInstance(context);
    }
    void  deleteMedication(Medication med)
    {
        repo.deleteMedication(med);
    }

    void updateActive(int active,String medName)
    {
        repo.updateActive(active, medName);
    }
    void refill(int amount,String medName)
    {
        repo.refill(amount, medName);
    }


}
