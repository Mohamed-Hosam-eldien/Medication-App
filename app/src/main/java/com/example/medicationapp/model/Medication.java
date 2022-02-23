package com.example.medicationapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Medication")
public class Medication implements Parcelable {

    @PrimaryKey()
    @NonNull
    private String name;
    private int refillNo;
    private int isActive = 1;
    private List<MedDetails> medDetails;
    //private MedScheduler medScheduler;
    private int image;
    private int midStrength;
    private String timeToFood;
    private String startDate;
    private List <String> days;
    private int allDays;
    int totalPills;

    public int getTotalPills() {
        return totalPills;
    }

    public void setTotalPills(int totalPills) {
        this.totalPills = totalPills;
    }


    public Medication() {
    }

    public Medication(@NonNull String name, int refillNo, int isActive, List<MedDetails> medDetails, int image
            , int midStrength, String timeToFood, String startDate, List<String> days, int allDays, int totalPills) {
        this.name = name;
        this.refillNo = refillNo;
        this.isActive = isActive;
        this.medDetails = medDetails;
        this.image = image;
        this.midStrength = midStrength;
        this.timeToFood = timeToFood;
        this.startDate = startDate;
        this.days = days;
        this.allDays = allDays;
        this.totalPills = totalPills;
    }
//    public Medication(@NonNull String name, int refillNo, int isActive,
//                      List<MedDetails> medDetails, int midStrength,
//                      String timeToFood, String startDate, List<String> days, int allDays) {
//        this.name = name;
//        this.refillNo = refillNo;
//        this.isActive = isActive;
//        this.medDetails = medDetails;
//        this.midStrength = midStrength;
//        this.timeToFood = timeToFood;
//        this.startDate = startDate;
//        this.days = days;
//        this.allDays = allDays;
//    }


    public int getAllDays() {
        return allDays;
    }

    public void setAllDays(int allDays) {
        this.allDays = allDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public String getTimeToFood() {
        return timeToFood;
    }

    public void setTimeToFood(String timeToFood) {
        this.timeToFood = timeToFood;
    }

    public int getRefillNo() {
        return refillNo;
    }

    public void setRefillNo(int refillNo) {
        this.refillNo = refillNo;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MedDetails> getMedDetails() {
        return medDetails;
    }

    public void setMedDetails(List<MedDetails> medDetails) {
        this.medDetails = medDetails;
    }
//
//    public MedScheduler getMedScheduler() {
//        return medScheduler;
//    }
//
//    public void setMedScheduler(MedScheduler medScheduler) {
//        this.medScheduler = medScheduler;
//    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getMidStrength() {
        return midStrength;
    }

    public void setMidStrength(int midStrength) {
        this.midStrength = midStrength;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.refillNo);
        dest.writeInt(this.isActive);
        dest.writeList(this.medDetails);
        dest.writeInt(this.image);
        dest.writeInt(this.midStrength);
        dest.writeString(this.timeToFood);
        dest.writeString(this.startDate);
        dest.writeStringList(this.days);
        dest.writeInt(this.allDays);
        dest.writeInt(this.totalPills);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.refillNo = source.readInt();
        this.isActive = source.readInt();
        this.medDetails = new ArrayList<MedDetails>();
        source.readList(this.medDetails, MedDetails.class.getClassLoader());
        this.image = source.readInt();
        this.midStrength = source.readInt();
        this.timeToFood = source.readString();
        this.startDate = source.readString();
        this.days = source.createStringArrayList();
        this.allDays = source.readInt();
        this.totalPills = source.readInt();
    }

    protected Medication(Parcel in) {
        this.name = in.readString();
        this.refillNo = in.readInt();
        this.isActive = in.readInt();
        this.medDetails = new ArrayList<MedDetails>();
        in.readList(this.medDetails, MedDetails.class.getClassLoader());
        this.image = in.readInt();
        this.midStrength = in.readInt();
        this.timeToFood = in.readString();
        this.startDate = in.readString();
        this.days = in.createStringArrayList();
        this.allDays = in.readInt();
        this.totalPills = in.readInt();
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel source) {
            return new Medication(source);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };
}
