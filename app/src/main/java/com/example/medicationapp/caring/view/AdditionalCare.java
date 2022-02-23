package com.example.medicationapp.caring.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.caring.presenter.CaringPresenter;
import com.example.medicationapp.databinding.InfoDialogBinding;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Helper;

public class AdditionalCare extends AppCompatActivity {

    EditText edtEmail;
    Button btnSend;
    CaringPresenter presenter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_care);
        edtEmail = findViewById(R.id.edtTxtEmail);
        btnSend = findViewById(R.id.btnSendInvitation);

        presenter = new CaringPresenter(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validationResult = Helper.validate(String.valueOf(edtEmail.getText()));
                boolean isNetworkConnected = Helper.isNetworkAvailable(getApplicationContext());
                if (validationResult) {
                    if (isNetworkConnected) {
                        Toast.makeText(AdditionalCare.this, "network succeed", Toast.LENGTH_SHORT).show();
//                      sendToEmail();
                        HomePresenter homePresenter = new HomePresenter(AdditionalCare.this);

                        homePresenter.getMedicationList().observe(AdditionalCare.this, medications -> {
                            Request request;
                            request = new Request("Mohamed", "Peter",
                                    "Sent you a Medical Request", "peter.samir299@gmail.com",
                                    "mohamedhosameldien07@gmail,com", false, medications);

                            Dialog dialog = new Dialog(AdditionalCare.this);
                            View view2 = LayoutInflater.from(AdditionalCare.this).inflate(R.layout.info_dialog, null);
                            dialog.setContentView(view2);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            InfoDialogBinding binding = InfoDialogBinding.bind(view2);
                            binding.btnSendEmail.setOnClickListener(view1 -> {
                                sendToEmail();
                                Toast.makeText(AdditionalCare.this, "clicked", Toast.LENGTH_SHORT).show();
                            });
                            dialog.show();
                            binding.btnSendInfoDialog.setOnClickListener(Vie -> {
                                request.setPatientName(binding.edtPatientName.getText().toString());
                                if (request.getPatientName().toString().trim().length() >= 2) {
                                    presenter.sendRequest(request);
                                    binding.edtPatientName.setText("");
                                    binding.txtSuccess.setVisibility(View.VISIBLE);
                                    binding.imgSuccess.setVisibility(View.VISIBLE);
                                } else {
                                    binding.edtPatientName.setError("please write the name ");
                                }
                            });
                        });

                    } else {
                        Toast.makeText(AdditionalCare.this, "network Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtEmail.setError("Email problem");
                }
            }
        });
    }

    public void sendToEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{edtEmail.getText().toString()});
        i.putExtra(Intent.EXTRA_SUBJECT, "Medication APP Health Care invitation ");
        i.putExtra(Intent.EXTRA_TEXT, "Hello, Iam sending you this Email to follow Medication Schedule");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AdditionalCare.this, "There is no Email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

  /*  @Override
    public void showDialogInfo(Request request) {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.info_dialog,null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        InfoDialogBinding binding = InfoDialogBinding.bind(view);
        request.setPatientName(binding.edtPatientName.getText().toString());
        dialog.show();
        binding.btnSendInfoDialog.setOnClickListener(View ->{

        });
    }*/
}