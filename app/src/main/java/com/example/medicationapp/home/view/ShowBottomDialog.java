package com.example.medicationapp.home.view;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public interface ShowBottomDialog {
    void showMedDialog(MedDetails detail, Medication medication);
}