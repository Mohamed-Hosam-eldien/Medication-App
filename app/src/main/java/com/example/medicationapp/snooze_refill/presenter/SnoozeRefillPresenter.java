package com.example.medicationapp.snooze_refill.presenter;


import android.content.Context;

import androidx.lifecycle.LiveData;
import com.example.medicationapp.repository.Repository;
import com.example.medicationapp.snooze_refill.view.SnoozeRefill_Interface;

public class SnoozeRefillPresenter implements SnoozeRefill_Interface {
    Repository repository;
    Context context;

    public SnoozeRefillPresenter(Context context){
        this.context = context;
        repository = Repository.getInstance(context);
    }



    @Override
    public void Refill(int num, String name) {
        repository.refill(num, name);
    }

    @Override
    public LiveData<String> getMedNameById(String id) {
        return repository.getMedById(id);
    }
}
