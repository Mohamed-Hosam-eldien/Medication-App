package com.example.medicationapp.medications.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.medications.view.MedicationFragment;
import com.example.medicationapp.medications.view.MedicationViewInterface;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

import java.util.List;

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
