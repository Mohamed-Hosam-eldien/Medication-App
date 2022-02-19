package com.example.medicationapp.medications.view.displayMedication;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.medications.view.MedicationFragment;
import com.example.medicationapp.medications.view.MedicationViewInterface;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

import java.util.List;

public class Presenter {
    Context context;
    MedicationViewInterface medicationViewInterface;
    Repository repo;

    public Presenter(Context context, Repository repo, MedicationViewInterface medicationViewInterface) {
        this.context = context;
        this.repo = repo;
        this.medicationViewInterface=medicationViewInterface ;

    }

    public void getActiveMedications(){
        medicationViewInterface.getAllActiveMedicines(repo.getActiveMedicines());
    }

    public void getInActiveMedications(){
        medicationViewInterface.getAllInActiveMedicines(repo.getInActiveMedicines());
    }
}
