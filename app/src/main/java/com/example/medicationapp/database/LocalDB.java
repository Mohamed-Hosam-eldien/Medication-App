package com.example.medicationapp.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Patient;

import java.util.List;

public class LocalDB implements LocalInterface{
    DAO patientDao;
    Context context;
    DatabaseBuilder databaseBuilder;
    private final LiveData<List<Patient>> AllPatients;
    private static LocalDB localDB = null;

    private LocalDB(Context context){
        databaseBuilder = DatabaseBuilder .getInstance(context);
        Log.i("TAG", "LocalDB: created");
        this.context = context;
        patientDao = databaseBuilder.getDao();
        AllPatients = patientDao.getAllPAtient();
    }

    public static LocalDB getInstance(Context context){
        if(localDB == null){
            localDB = new LocalDB(context);
        }
        return localDB;
    }

    @Override
    public void insertPatient(Patient patient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                patientDao.insertPatient(patient);
            }
        }).start();
    }

    @Override
    public void deleterPatient(Patient patient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleterPatient(patient);
            }
        }).start();
    }

    @Override
    public LiveData<List<Patient>> getAllPatients() {
        return AllPatients;
    }

    @Override
    public LiveData<Patient> getPatient(int id) {
        return patientDao.getPatient(id);
    }
}
