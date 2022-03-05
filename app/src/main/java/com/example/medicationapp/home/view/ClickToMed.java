package com.example.medicationapp.home.view;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

public interface ClickToMed {
    void showMedDetails(MedDetails medDetail, Medication medication, int position);
}
