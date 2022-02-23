package com.example.medicationapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Request {
    String senderName;
    String patientName;
    String description;
    String receiverEmail;
    String senderEmail;
    boolean request;
    List<Medication> medicationList;

    public Request() {
    }

    public Request(String senderName, String patientName, String description, String receiverEmail, String senderEmail, boolean request, List<Medication> medicationList) {
        this.senderName = senderName;
        this.patientName = patientName;
        this.description = description;
        this.receiverEmail = receiverEmail;
        this.senderEmail = senderEmail;
        this.request = request;
        this.medicationList = medicationList;
    }

    public List<Medication> getMedicationList() {
        return medicationList;
    }

    public void setMedicationList(List<Medication> medicationList) {
        this.medicationList = medicationList;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

}
