package com.example.medicationapp.repository;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;

import java.util.List;

public interface RepositoryInterface {

    LiveData<String> getMedById(String id);

    int getRefillNo(String id);

    int getTotalPills(String id);

    void insertMedication(Medication medication);

    void deleteMedication(Medication medication);

    void updateActive(int active,String id);

    void update(String id,String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,int current,
                long startDate,List<String>days,int allDays);

    LiveData<List<Medication>> getAllMedications();

    LiveData<Medication> getMedication(String name);

    LiveData<List<Medication>> getActiveMedicines();

    LiveData<List<Medication>> getInActiveMedicines();

    LiveData<List<Medication>> getAllMedicationInAllDay(long currentDay);

    void refill(int amount,String id);

    Medication getMedToPopup(String id);

    void onSendRequest(Request request);

    List<Medication> onReceiveMedication();

    void updateTaken(List<MedDetails> medDetails, String id);

}
