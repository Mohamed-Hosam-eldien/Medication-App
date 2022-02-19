package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertMedication(Medication medication);

    @Query("SELECT * FROM Medication")
    public LiveData<List<Medication>> getAllMedicines();

    @Delete
    public void deleteMedication(Medication medication);

    @Query("SELECT * FROM Medication WHERE name=:name")
    public LiveData<Medication> getMedication(String name);

    @Query("Select * FROM Medication WHERE isActive = 1")
    public LiveData<List<Medication>> getActiveMedicines();

    @Query("Select * FROM Medication WHERE isActive = 0")
    public LiveData<List<Medication>> getInActiveMedicines();
}
