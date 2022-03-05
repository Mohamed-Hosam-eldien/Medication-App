package com.example.medicationapp.home.view;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

public interface ShowBottomDialog {
    void showMedDialog(MedDetails detail, Medication medication, int position);
}
