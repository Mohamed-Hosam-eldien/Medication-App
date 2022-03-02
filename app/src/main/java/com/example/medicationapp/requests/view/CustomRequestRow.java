package com.example.medicationapp.requests.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;

public class CustomRequestRow extends RecyclerView.ViewHolder {
    Button btnAccept;
    Button btnReject;
    TextView senderName;
    TextView Description;
    TextView patientName;

    public CustomRequestRow(@NonNull View itemView) {
        super(itemView);
        btnAccept = itemView.findViewById(R.id.btnCardAccept);
        btnReject = itemView.findViewById(R.id.btnCardReject);
        senderName = itemView.findViewById(R.id.txtSenderName);
        Description = itemView.findViewById(R.id.txtnotifBody);
        patientName = itemView.findViewById(R.id.txtPersonName);
    }
}