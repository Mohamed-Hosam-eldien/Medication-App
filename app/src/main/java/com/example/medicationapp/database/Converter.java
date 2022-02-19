package com.example.medicationapp.database;

import androidx.room.TypeConverter;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedScheduler;
import com.example.medicationapp.model.Medication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {


    @TypeConverter
    public String fromSchedulerToString(MedScheduler medScheduler){
        return new Gson().toJson(medScheduler);
    }

    @TypeConverter
    public MedScheduler fromStringToScheduler(String string){
        return new Gson().fromJson(string, MedScheduler.class);
    }

    @TypeConverter
    public String fromMedDetailsToString(List<MedDetails>medDetails){
        return new Gson().toJson(medDetails);
    }

    @TypeConverter
    public List<MedDetails> fromStringToMedDetails(String medSchedule){
        Type listType = new TypeToken<List<MedScheduler>>() {}.getType();
        return new Gson().fromJson(medSchedule, listType);
    }
}
