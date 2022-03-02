package com.example.medicationapp.snooze_refill.view;


import androidx.lifecycle.LiveData;

import com.example.medicationapp.model.Medication;

public interface SnoozeRefill_Interface {

    public void Refill(int amount, String name);

    public LiveData<String> getMedNameById(String id);

}