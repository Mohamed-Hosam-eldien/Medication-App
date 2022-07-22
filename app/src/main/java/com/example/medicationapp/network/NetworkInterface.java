package com.example.medicationapp.network;

import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;
import com.example.medicationapp.models.User;

import java.util.List;

public interface NetworkInterface {

    void onSendRequest(Request request);

    void onReceiveMedication(List<Request> list);

    void onSaveUserData(User user);

    void onSendMedicine(List<Medication>medications,String requestId);
}
