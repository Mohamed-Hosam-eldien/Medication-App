package com.example.medicationapp.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;

import java.util.List;

public class LocalDB implements LocalInterface{
    DAO medicationDao;
    Context context;
    DatabaseBuilder databaseBuilder;
    private LiveData<List<Medication>> allMedications;
    private static LocalDB localDB = null;

    private LocalDB(Context context){
        databaseBuilder = DatabaseBuilder .getInstance(context);
        Log.i("TAG", "LocalDB: created");
        this.context = context;
        medicationDao = databaseBuilder.getDao();
        allMedications = medicationDao.getAllMedicines();
    }

    public static LocalDB getInstance(Context context){
        if(localDB == null){
            localDB = new LocalDB(context);
        }
        return localDB;
    }

    @Override
    public void insertMedicine(Medication medication) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicationDao.insertMedication(medication);
            }
        }).start();
    }

    @Override
    public void deleteMedication(Medication medication) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteMedication(medication);
            }
        }).start();
    }

    @Override
    public LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    @Override
    public LiveData<Medication> getMedication(String name) {
        return medicationDao.getMedication(name);
    }

    @Override
    public LiveData<List<Medication>> getAllActiveMedicines() {
        return medicationDao.getActiveMedicines();
    }

    @Override
    public LiveData<List<Medication>> getAllInActiveMedicines() {
        return medicationDao.getInActiveMedicines();
    }
}
