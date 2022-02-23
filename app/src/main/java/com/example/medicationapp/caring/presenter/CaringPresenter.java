package com.example.medicationapp.caring.presenter;

import android.content.Context;

import com.example.medicationapp.connection.CaringViewInterface;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.repository.Repository;

public class CaringPresenter {

    Repository repository;

    public CaringPresenter(Context context) {
        this.repository = Repository.getInstance(context);
    }

    public void sendRequest(Request request) {
        repository.onSendRequest(request);
    }

}