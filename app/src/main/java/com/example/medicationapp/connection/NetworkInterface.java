package com.example.medicationapp.connection;

import com.example.medicationapp.model.Request;

import java.util.List;

public interface NetworkInterface {

    void onSendRequest(Request request);

    void onReceiveMedication();

}
