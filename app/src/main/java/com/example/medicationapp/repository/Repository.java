package com.example.medicationapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;

import java.util.List;

public class Repository implements RepositoryInterface {

    LocalDB localDB;
    private static Repository repository = null;
    public Context context;

    private Repository (Context context){
        this.context = context;
        this.localDB = LocalDB.getInstance(context);
    }

    public static synchronized Repository getInstance(Context context){
        if(repository == null){
            repository = new Repository(context);
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
    public void updateActive(int active, String medName) {
        localDB.updateActive(active,medName);
    }

    @Override
    public void update(String name, int refillNo, int isActive, List<MedDetails> medDetails, int img, int midStrength, String timeToFood, long startDate, List<String> days, int allDays) {
        localDB.update(name, refillNo, isActive, medDetails, img, midStrength, timeToFood, startDate, days, allDays);
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

    @Override
    public LiveData<List<Medication>> getAllMedicationInAllDay(long currentDay) {
        return localDB.getMedicationInAllDays(currentDay);
    }

    @Override
    public void refill(int amount, String medName) {
        localDB.refill(amount, medName);
    }


    @Override
    public void onSendRequest(Request request) {
        //connection.sendRequest(request);
    }

    @Override
    public List<Medication> onReceiveMedication() {
        return null;
    }
}
