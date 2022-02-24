package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public interface LocalInterface {

    public void insertMedicine(Medication medication);

    public void deleteMedication(Medication medication);

    void updateActive(int active,String medName);

    void update(String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,int current,
                String startDate,List<String>days,int allDays);

    LiveData<List<Medication>> getAllMedications();

    LiveData<Medication> getMedication(String name);

    LiveData<List<Medication>> getAllActiveMedicines();

    LiveData<List<Medication>> getAllInActiveMedicines();

    LiveData<List<Medication>> getMedicationInAllDays();

    void refill(int amount,String medName);

}
