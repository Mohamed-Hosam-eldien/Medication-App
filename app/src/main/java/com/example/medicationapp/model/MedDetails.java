package com.example.medicationapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class MedDetails implements Parcelable {
    Calendar time;
    int dose;
    String type;
    int taken;

    public MedDetails() {}

    public MedDetails(Calendar time, int dose, String type, int taken) {
        this.time = time;
        this.dose = dose;
        this.type = type;
        this.taken = taken;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.time);
        dest.writeInt(this.dose);
        dest.writeString(this.type);
        dest.writeInt(this.taken);
    }

    public void readFromParcel(Parcel source) {
        this.time = (Calendar) source.readSerializable();
        this.dose = source.readInt();
        this.type = source.readString();
        this.taken = source.readInt();
    }

    protected MedDetails(Parcel in) {
        this.time = (Calendar) in.readSerializable();
        this.dose = in.readInt();
        this.type = in.readString();
        this.taken = in.readInt();
    }

    public static final Creator<MedDetails> CREATOR = new Creator<MedDetails>() {
        @Override
        public MedDetails createFromParcel(Parcel source) {
            return new MedDetails(source);
        }

        @Override
        public MedDetails[] newArray(int size) {
            return new MedDetails[size];
        }
    };
}
