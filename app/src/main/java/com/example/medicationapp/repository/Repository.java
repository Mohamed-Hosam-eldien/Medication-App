package com.example.medicationapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.Medication;

import java.util.List;

public class Repository implements RepositoryInterface {

    LocalDB localDB;
    private static Repository repository = null;
    public Context context;

    private Repository (Context context,  LocalDB localDB){
        this.context = context;
        this.localDB = localDB;
    }

    public static synchronized Repository getInstance(Context context, LocalDB localDB){
        if(repository == null){
            repository = new Repository(context, localDB);
        }
        return repository;
    }

    @Override
    public void insertMedication(Medication medication) {
        localDB.insertMedicine(medication);
    }

    @Override
    public void deleteMedication(Medication medication) {
        localDB.deleteMedication(medication);
    }

    @Override
    public LiveData<List<Medication>> getAllMedications() {
        return localDB.getAllMedications();
    }

    @Override
    public LiveData<Medication> getMedication(String name) {
        return localDB.getMedication(name);
    }

    @Override
    public LiveData<List<Medication>> getActiveMedicines() {
        return localDB.getAllActiveMedicines();
    }

    @Override
    public LiveData<List<Medication>> getInActiveMedicines() {
        return localDB.getAllInActiveMedicines();
    }
}
