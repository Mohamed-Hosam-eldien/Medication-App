package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

@Dao
public interface DAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertMedication(Medication medication);

    @Query("SELECT * FROM Medication")
    public LiveData<List<Medication>> getAllMedicines();

    @Delete
    public void deleteMedication(Medication medication);

    @Query("SELECT * FROM Medication WHERE id=:id")
    public LiveData<Medication> getMedication(String id);

    @Query("Select * FROM Medication WHERE isActive = 1")
    public LiveData<List<Medication>> getActiveMedicines();

    @Query("Select * FROM Medication WHERE isActive = 0")
    public LiveData<List<Medication>> getInActiveMedicines();

    @Query("Select * FROM Medication where allDays = 1 and isActive = 1")
    LiveData<List<Medication>> getAllMedicationWithAllDays();

    @Query("update Medication set isActive =:active where id =:id")
    void updateActive(int active,String id);
    @Query("update Medication set totalPills =:amount where id =:id")
    void refill(int amount,String id);

    @Query("update Medication set name=:name,refillNo=:refillNo,isActive=:isActive" +
            ",medDetails=:medDetails,image=:img,midStrength=:midStrength," +
            "timeToFood=:timeToFood,startDate=:startDate,days=:days,totalPills=:current," +
            "allDays= :allDays where id=:id")
    void update(String id,String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,int current,
                String startDate,List<String>days,int allDays);

}
