package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public interface LocalInterface {

    public LiveData<String> getMedNameById(String id);

    public int getRefillNo(String id);

    public int getTotalPills(String id);

    public void insertMedicine(Medication medication);

    public void deleteMedication(Medication medication);

    void updateActive(int active,String id);

    Medication getMedicationToPopup(String id);

    void update(String id,String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,int current,
                long startDate,List<String>days,int allDays);

    LiveData<List<Medication>> getAllMedications();

    LiveData<Medication> getMedication(String name);

    LiveData<List<Medication>> getAllActiveMedicines();

    LiveData<List<Medication>> getAllInActiveMedicines();

    LiveData<List<Medication>> getMedicationInAllDays(long currentDay);

    void refill(int amount,String id);

}
