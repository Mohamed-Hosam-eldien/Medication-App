package com.example.medicationapp.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public class LocalDB implements LocalInterface {
    DAO medicationDao;
    Context context;
    DatabaseBuilder databaseBuilder;
    private LiveData<List<Medication>> allMedications;
    private static LocalDB localDB = null;

    private LocalDB(Context context) {
        databaseBuilder = DatabaseBuilder.getInstance(context);
        Log.i("TAG", "LocalDB: created");
        this.context = context;
        medicationDao = databaseBuilder.getDao();
        allMedications = medicationDao.getAllMedicines();
    }

    public static LocalDB getInstance(Context context) {
        if (localDB == null) {
            localDB = new LocalDB(context);
        }
        return localDB;
    }

    @Override
    public LiveData<String> getMedNameById(String id) {
        return medicationDao.getMedicineByID(id);
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
                medicationDao.deleteMedication(medication);
            }
        }).start();
    }

    @Override
    public void updateActive(int active, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicationDao.updateActive(active, id);
            }
        }).start();

    }

    @Override
    public void update(String id, String name, int refillNo, int isActive, List<MedDetails> medDetails, int img, int midStrength, String timeToFood, int current, String startDate, List<String> days, int allDays) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicationDao.update(id, name, refillNo, isActive, medDetails, img, midStrength, timeToFood, current, startDate, days, allDays);
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

    @Override
    public LiveData<List<Medication>> getMedicationInAllDays() {
        return medicationDao.getAllMedicationWithAllDays();
    }

    public void refill(int amount, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicationDao.refill(amount, id);
            }
        }).start();

    }
}
