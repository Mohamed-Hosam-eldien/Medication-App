package com.example.medicationapp.snooze_refill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.snooze_refill.presenter.SnoozeRefillPresenter;

public class RefillActivity extends AppCompatActivity {

    Button btnRefill;
    EditText edtRefill;
    TextView txtPlus;
    TextView txtMinus;
    Medication med;
    TextView medicineName;
    SnoozeRefillPresenter snoozeRefillPresenter;
    String extras;
    static int counter = 1;
    Intent backIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        extras = getIntent().getStringExtra("medicine");
        edtRefill = findViewById(R.id.edtRefillNum);
        txtMinus = findViewById(R.id.txtMinus);
        txtPlus = findViewById(R.id.txtPlus);
        medicineName = findViewById(R.id.medicineName);
        medicineName.setText(extras);
        txtPlus.setOnClickListener(view -> {
            counter += 1;
            edtRefill.setText(String.valueOf(counter));
        });
        txtMinus.setOnClickListener(view -> {
            if (counter > 1) {
                counter -= 1;
                edtRefill.setText(String.valueOf(counter));
            }
        });

        snoozeRefillPresenter = new SnoozeRefillPresenter(this);
        btnRefill = findViewById(R.id.btnRefill);
        btnRefill.setOnClickListener(view -> {
            if (!edtRefill.getText().toString().isEmpty() && !edtRefill.getText().toString().equals("0")) {
                snoozeRefillPresenter.Refill(Integer.parseInt
                        (String.valueOf(edtRefill.getText())), extras);
                Toast.makeText(this, "Refill succeeded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RefillActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "please insert a number", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backIntent = new Intent(RefillActivity.this, MainActivity.class);
        startActivity(backIntent);
    }
}