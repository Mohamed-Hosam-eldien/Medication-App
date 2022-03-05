package com.example.medicationapp.requests.view;

import com.example.medicationapp.model.Request;

public interface OnRequestClick {
    void onAcceptClick(Request request);
    void onRejectClick(Request request);

}
