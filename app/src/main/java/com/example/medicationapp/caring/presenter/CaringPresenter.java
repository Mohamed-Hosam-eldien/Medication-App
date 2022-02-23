package com.example.medicationapp.caring.presenter;

import android.content.Context;

import com.example.medicationapp.connection.CaringViewInterface;
import com.example.medicationapp.connection.Connection;
import com.example.medicationapp.connection.NetworkInterface;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;
import com.example.medicationapp.repository.Repository;

import java.util.List;

public class CaringPresenter implements NetworkInterface {

    Repository repository;
    Connection connection;

    public CaringPresenter(Context context) {
        //this.repository = Repository.getInstance(context);
        connection = Connection.getInstance(this, context);
    }
//
//    public void sendRequest(Request request) {
//        repository.onSendRequest(request);
//    }
//
//    public void saveUserData(User user) {
//        repository.onSaveUserData(user);
//    }

    @Override
    public void onSendRequest(Request request) {
        connection.sendRequest(request);
    }

    @Override
    public void onReceiveMedication(List<Request> list) {

    }

    @Override
    public void onSaveUserData(User user) {
        connection.saveUserToFirebase(user);
    }
}
