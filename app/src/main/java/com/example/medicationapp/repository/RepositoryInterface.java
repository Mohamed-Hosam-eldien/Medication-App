package com.example.medicationapp.repository;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public interface RepositoryInterface {

    public void insertMedication(Medication medication);

    public void deleteMedication(Medication medication);

    void updateActive(int active,String medName);

    void update(String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,
                String startDate,List<String>days,int allDays);

    public LiveData<List<Medication>> getAllMedications();

    public LiveData<Medication> getMedication(String name);

    public LiveData<List<Medication>> getActiveMedicines();

    public LiveData<List<Medication>> getInActiveMedicines();

    LiveData<List<Medication>> getAllMedicationInAllDay();

    void refill(int amount,String medName);

}
