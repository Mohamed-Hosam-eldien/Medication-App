package com.example.medicationapp.ui.home.view;

import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;

public interface ClickToMed {
    void showMedDetails(MedDetails medDetail, Medication medication, int position);
}
