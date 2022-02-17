package com.example.medicationapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.Patient;

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
    public void insertPatient(Patient patient) {
        localDB.insertPatient(patient);
    }

    @Override
    public void deletePatient(Patient patient) {
        localDB.deleterPatient(patient);
    }

    @Override
    public LiveData<List<Patient>> getAllPatients() {
        return localDB.getAllPatients();
    }

    @Override
    public LiveData<Patient> getPatient(int id) {
        return localDB.getPatient(id);
    }
}
