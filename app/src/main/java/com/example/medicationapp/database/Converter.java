package com.example.medicationapp.database;

import androidx.room.TypeConverter;

import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedScheduler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
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
    public String fromListToString(List<String> days){
        return new Gson().toJson(days);
    }

    @TypeConverter
    public List<String> fromStringToList(String days){
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(days, listType);
    }

    @TypeConverter
    public List<MedDetails> fromStringToMedDetails(String medDetails){
        Type listType = new TypeToken<List<MedDetails>>() {}.getType();
        return new Gson().fromJson(medDetails, listType);
    }

    @TypeConverter
    public Calendar fromStringToCalender(String string){
        return new Gson().fromJson(string, Calendar.class);
    }

    @TypeConverter
    public String fromCalenderToString(Calendar calendar){
        return new Gson().toJson(calendar);
    }


}
