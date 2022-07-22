package com.example.medicationapp.ui.snooze_refill.view;


import androidx.lifecycle.LiveData;

public interface SnoozeRefill_Interface {

    void Refill(int amount, String name);

    LiveData<String> getMedNameById(String id);

}
