package com.example.medicationapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicationapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAO {

    @Insert
    public void insertPatient(Patient patient);

    @Query("SELECT * FROM Patient")
    public LiveData<List<Patient>> getAllPAtient();

    @Delete
    public void deletePatient(Patient patient);

    @Query("SELECT * FROM Patient WHERE ID=:id")
    public LiveData<Patient> getPatient(int id);
}
