package com.example.medicationapp.dependent.presenter;

import android.content.Context;

import com.example.medicationapp.connection.Connection;
import com.example.medicationapp.connection.GetAllMedication;
import com.example.medicationapp.connection.NetworkInterface;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;
import com.example.medicationapp.repository.Repository;

import java.util.List;

public class DependentPresenter implements NetworkInterface {

    Connection connection;
    GetAllMedication getAllMedication;
    Context context;


    public DependentPresenter(GetAllMedication getAllMedication, Context context) {
        this.connection = Connection.getInstance(this, context);
        this.getAllMedication = getAllMedication;
    }

    @Override
    public void onSendRequest(Request request) {

    }

    public void getPatientMed() {
        connection.receiveMedication();
    }

    @Override
    public void onReceiveMedication(List<Request> list) {
        getAllMedication.getMedicationList(list);
    }

    @Override
    public void onSaveUserData(User user) {

    }
}
