package com.example.medicationapp.ui.requests.view;

import com.example.medicationapp.models.Request;

public interface OnRequestClick {
    void onAcceptClick(Request request);
    void onRejectClick(Request request);

}
