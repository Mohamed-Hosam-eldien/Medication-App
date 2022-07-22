package com.example.medicationapp.ui.home.view;

import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;

public interface ShowBottomDialog {
    void showMedDialog(MedDetails detail, Medication medication, int position);
}
