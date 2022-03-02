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
    public void updateActive(int active, String id) {
        localDB.updateActive(active,id);
    }

    @Override
    public void update(String id,String name, int refillNo, int isActive, List<MedDetails> medDetails, int img, int midStrength, String timeToFood,int current, String startDate, List<String> days, int allDays) {
        localDB.update(id,name, refillNo, isActive, medDetails, img, midStrength, timeToFood,current, startDate, days, allDays);
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
    public LiveData<List<Medication>> getAllMedicationInAllDay() {
        return localDB.getMedicationInAllDays();
    }

    @Override
    public void refill(int amount, String id) {
        localDB.refill(amount, id);
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
