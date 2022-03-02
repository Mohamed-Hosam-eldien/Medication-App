package com.example.medicationapp.medications.displayMedication.presenter;

import android.content.Context;

import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;

public class DisplayPresenter {
    Context context;
    Repository repo;

    public DisplayPresenter(Context context) {
        this.context = context;
        this.repo = Repository.getInstance(context);
    }
  public  void  deleteMedication(Medication med)
    {
        repo.deleteMedication(med);
    }

   public void updateActive(int active,String id)
    {
        repo.updateActive(active, id);
    }
  public  void refill(int amount,String id)
    {
        repo.refill(amount, id);
    }
    public void updateFirebase(int refill,String medId){}


}
