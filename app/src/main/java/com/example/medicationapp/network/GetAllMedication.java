package com.example.medicationapp.network;

import com.example.medicationapp.models.Request;

import java.util.List;

public interface GetAllMedication {
    void getMedicationList(List<Request> medication);
}
