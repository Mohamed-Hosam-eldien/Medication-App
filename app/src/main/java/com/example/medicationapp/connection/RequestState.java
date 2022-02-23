package com.example.medicationapp.connection;

public interface RequestState {

    void onRequestSuccess();

    String onRequestFailure(String e);

}
