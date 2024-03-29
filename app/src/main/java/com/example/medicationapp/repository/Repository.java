package com.example.medicationapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.local.LocalDB;
import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;

import java.util.List;

public class Repository implements RepositoryInterface {

    private final LocalDB localDB;
    private static Repository repository = null;

    private Repository (Context context){
        this.localDB = LocalDB.getInstance(context);
    }

    public static synchronized Repository getInstance(Context context){
        if(repository == null){
            repository = new Repository(context);
        }
        return repository;
    }

    @Override
    public LiveData<String> getMedById(String id) {
        return localDB.getMedNameById(id);
    }

    @Override
    public int getRefillNo(String id) {
        return localDB.getRefillNo(id);
    }

    @Override
    public int getTotalPills(String id) {
        return localDB.getTotalPills(id);
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
    public void update(String id,String name, int refillNo, int isActive, List<MedDetails> medDetails, int img, int midStrength, String timeToFood,int current, long startDate, List<String> days, int allDays) {
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
    public LiveData<List<Medication>> getAllMedicationInAllDay(long currentDay) {
        return localDB.getMedicationInAllDays(currentDay);
    }

    @Override
    public void refill(int amount, String id) {
        localDB.refill(amount, id);
    }

    @Override
    public Medication getMedToPopup(String id) {
        return localDB.getMedicationToPopup(id);
    }


    @Override
    public void onSendRequest(Request request) {
        //connection.sendRequest(request);
    }

    @Override
    public List<Medication> onReceiveMedication() {
        return null;
    }

    @Override
    public void updateTaken(List<MedDetails> medDetails, String id) {
        localDB.updateTaken(medDetails, id);
    }
}
