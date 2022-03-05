package com.example.medicationapp.medications.addEditMed.presenter;

import android.content.Context;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

public class AddEditPresenter {

    private final Repository repo;

    public AddEditPresenter(Context context) {
        this.repo = Repository.getInstance(context);
    }

    public int getRefillNo(String id) {
        return repo.getRefillNo(id);
    }

    public int getTotalPills(String id){
        return repo.getTotalPills(id);
    }

   public  void insertMedication(Medication med)
    {
        repo.insertMedication(med);
    }

    public void updateMedication(Medication medication)
    {
        repo.update(medication.getId(),medication.getName(),medication.getRefillNo(),medication.getIsActive(),
                medication.getMedDetails(),medication.getImage(),medication.getMidStrength(),
                medication.getTimeToFood(),medication.getTotalPills(),medication.getStartDate(),medication.getDays(), medication.getAllDays());
    }

}
