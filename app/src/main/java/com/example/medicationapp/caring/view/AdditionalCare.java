package com.example.medicationapp.caring.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.utils.Helper;

public class AdditionalCare extends AppCompatActivity {

    EditText edtEmail;
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_care);
        edtEmail = findViewById(R.id.edtTxtEmail);
        btnSend = findViewById(R.id.btnSendInvitation);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean validationResult =  Helper.validate(String.valueOf(edtEmail.getText()));
               boolean isNetworkConnected = Helper.isNetworkAvailable(getApplicationContext());
               if(validationResult){
                  if (isNetworkConnected){
                      Toast.makeText(AdditionalCare.this, "network succeed", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(Intent.ACTION_SEND);
                      i.setType("message/rfc822");
                      i.putExtra(Intent.EXTRA_EMAIL  , new String[]{edtEmail.getText().toString()});
                      i.putExtra(Intent.EXTRA_SUBJECT, "Medication APP Health Care invitation ");
                      i.putExtra(Intent.EXTRA_TEXT   , "hello hossam");
                      try {
                          startActivity(Intent.createChooser(i, "Send mail..."));
                      } catch (android.content.ActivityNotFoundException ex) {
                          Toast.makeText(AdditionalCare.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                      }
                  }else {
                      Toast.makeText(AdditionalCare.this, "network Failed", Toast.LENGTH_SHORT).show();
                  }

               }
               else {
                   edtEmail.setError("Email problem");

               }
            }
        });
    }
}