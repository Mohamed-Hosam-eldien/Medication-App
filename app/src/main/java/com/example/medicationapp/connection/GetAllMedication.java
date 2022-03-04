package com.example.medicationapp.connection;

import com.example.medicationapp.model.Request;

import java.util.List;

public interface GetAllMedication {
    void getMedicationList(List<Request> medication);
}
