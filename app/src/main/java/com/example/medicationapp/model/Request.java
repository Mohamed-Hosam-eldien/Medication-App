package com.example.medicationapp.model;

public class Request {
    String senderName;
    String dependentName;
    String requestText;

    public Request(String senderName, String dependentName, String requestText) {
        this.senderName = senderName;
        this.dependentName = dependentName;
        this.requestText = requestText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }
}
