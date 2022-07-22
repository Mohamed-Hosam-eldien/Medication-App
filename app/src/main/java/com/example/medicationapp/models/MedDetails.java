package com.example.medicationapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class MedDetails implements Parcelable {
    private long time;
    private int dose;
    private String type;
    private int taken;

    public MedDetails() {}

    public MedDetails(long time, int dose, String type, int taken) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
        dest.writeLong(this.time);
        dest.writeInt(this.dose);
        dest.writeString(this.type);
        dest.writeInt(this.taken);
    }

    public void readFromParcel(Parcel source) {
        this.time = source.readLong();
        this.dose = source.readInt();
        this.type = source.readString();
        this.taken = source.readInt();
    }

    protected MedDetails(Parcel in) {
        this.time = in.readLong();
        this.dose = in.readInt();
        this.type = in.readString();
        this.taken = in.readInt();
    }

    public static final Parcelable.Creator<MedDetails> CREATOR = new Parcelable.Creator<MedDetails>() {
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
