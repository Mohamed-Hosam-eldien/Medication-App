package com.example.medicationapp.ui.medications.presenter;

import android.content.Context;

import com.example.medicationapp.ui.medications.view.MedicationViewInterface;
import com.example.medicationapp.repository.Repository;

public class MedicationPresenter {
    Context context;
    MedicationViewInterface medicationViewInterface;
    Repository repo;

    public MedicationPresenter(Context context, MedicationViewInterface medicationViewInterface) {
        this.context = context;
        this.repo = Repository.getInstance(context);
        this.medicationViewInterface=medicationViewInterface ;
    }

    public void getActiveMedications(){
        medicationViewInterface.getAllActiveMedicines(repo.getActiveMedicines());
    }

    public void getInActiveMedications(){
        medicationViewInterface.getAllInActiveMedicines(repo.getInActiveMedicines());
    }

}
