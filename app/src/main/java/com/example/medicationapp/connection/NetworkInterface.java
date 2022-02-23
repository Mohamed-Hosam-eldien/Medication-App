package com.example.medicationapp.connection;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;

import java.util.List;

public interface NetworkInterface {

    void onSendRequest(Request request);

    void onReceiveMedication(List<Request> list);

    void onSaveUserData(User user);
}
