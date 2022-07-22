package com.example.medicationapp.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;

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


    @Query("SELECT * FROM Medication WHERE id=:id")
    Medication getMedicationToPopupWin(String id);

    @Query("Select * FROM Medication WHERE isActive = 1")
    public LiveData<List<Medication>> getActiveMedicines();

    @Query("select refillNo from medication where id=:senderId ")
    public int getRefillNo(String senderId);

    @Query("select totalPills from medication where id=:senderId ")
    public int getTotalPills(String senderId);

    //get medicine name by id
    @Query("select name from medication where id=:id")
    public LiveData<String> getMedicineByID(String id);

    @Query("Select * FROM Medication WHERE isActive = 0")
    public LiveData<List<Medication>> getInActiveMedicines();

    @Query("Select * FROM Medication where allDays = 1 and isActive = 1 and startDate <=:currentDate")
    LiveData<List<Medication>> getAllMedicationWithAllDays(long currentDate);

    @Query("update Medication set isActive =:active where id =:id")
    void updateActive(int active,String id);

    @Query("update Medication set totalPills =:amount where id =:id")
    void refill(int amount,String id);

    @Query("update Medication set medDetails =:details where id =:id")
    void updateTaken(List<MedDetails> details,String id);

    @Query("update Medication set name=:name,refillNo=:refillNo,isActive=:isActive" +
            ",medDetails=:medDetails,image=:img,midStrength=:midStrength," +
            "timeToFood=:timeToFood,startDate=:startDate,days=:days,totalPills=:current," +
            "allDays= :allDays where id=:id")
    void update(String id,String name, int refillNo, int isActive, List<MedDetails>medDetails
            ,int img,int midStrength,String timeToFood,int current,
                long startDate,List<String>days,int allDays);

}
