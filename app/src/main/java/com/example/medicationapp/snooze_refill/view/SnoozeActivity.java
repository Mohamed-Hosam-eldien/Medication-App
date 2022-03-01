package com.example.medicationapp.snooze_refill.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.snooze_refill.presenter.SnoozeRefillPresenter;

public class SnoozeActivity extends AppCompatActivity {

    SnoozeRefillPresenter snoozeRefillPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);

        snoozeRefillPresenter = new SnoozeRefillPresenter(this);
//        Intent intent = getIntent();
//        Medication medication = intent.getParcelableExtra("medicine");
//        Toast.makeText(this, "" + medication.getName().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(SnoozeActivity.this, MainActivity.class);
        startActivity(back);
    }


}