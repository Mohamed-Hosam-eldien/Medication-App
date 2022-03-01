package com.example.medicationapp.home.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.medicationapp.connection.Connection;
import com.example.medicationapp.connection.GetAllMedication;
import com.example.medicationapp.connection.NetworkInterface;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.repository.Repository;

import java.util.List;

public class HomePresenter {

    private final Repository repository;
    Connection connection;
    GetAllMedication getAllMedication;


    public HomePresenter(Context context) {
        this.repository = Repository.getInstance(context);
        this.getAllMedication = getAllMedication;
    }

    public void addMedication(Medication medication) {
        repository.insertMedication(medication);
    }

    public LiveData<Medication> getMedication(String name) {
        return repository.getMedication(name);
    }

    public LiveData<List<Medication>> getMedicationList() {
        return repository.getAllMedications();
    }

    public LiveData<List<Medication>> getMedicationListByAllDay() {
        return repository.getAllMedicationInAllDay();
    }

    public void updateRefill(int amount, String name){
        repository.refill(amount, name);
    }

    public void deleteMedicine(Medication medication){
        repository.deleteMedication(medication);
    }

//
//    @Override
//    public void onSendRequest(Request request) {
//
//    }
//
//    @Override
//    public void onReceiveMedication(Request list) {
//        getAllMedication.getMedicationList(list);
//    }
//
//    public void getPatientMed() {
//        connection.receiveMedication();
//    }
}
