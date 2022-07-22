package com.example.medicationapp.ui.caring.presenter;

import android.content.Context;

import com.example.medicationapp.network.Connection;
import com.example.medicationapp.network.NetworkInterface;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;
import com.example.medicationapp.models.User;
import java.util.List;

public class CaringPresenter implements NetworkInterface {

    private final Connection connection;

    public CaringPresenter(Context context) {
        connection = Connection.getInstance(this, context);
    }

    @Override
    public void onSendRequest(Request request) {
        connection.sendRequest(request);
    }

    @Override
    public void onReceiveMedication(List<Request> list) {}

    @Override
    public void onSaveUserData(User user) {}

    @Override
    public void onSendMedicine(List<Medication> medications,String requestId) {
       connection.sendMedicine(medications,requestId);
    }
}
