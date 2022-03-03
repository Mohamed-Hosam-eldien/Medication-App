package com.example.medicationapp.snooze_refill.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.snooze_refill.presenter.SnoozeRefillPresenter;
import com.example.medicationapp.utils.RefillSnoozeWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SnoozeActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    SnoozeRefillPresenter snoozeRefillPresenter;
    public int time =0;
    Button btnSnooze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        radioGroup = findViewById(R.id.radioGroup);
        snoozeRefillPresenter = new SnoozeRefillPresenter(this);
        btnSnooze = findViewById(R.id.activityBtnSnooze);
//        Intent intent = getIntent();
//        Medication medication = intent.getParcelableExtra("medicine");
//        Toast.makeText(this, "" + medication.getName().toString(), Toast.LENGTH_SHORT).show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radiobtnMin:{
                        time = 30;
                        break;
                    }
                    case R.id.radiobtn1Hr:
                        time = 60;
                        break;
                    case R.id.radiobtn2Hr:
                        time = 120;
                        break;
                }
            }
        });

        btnSnooze.setOnClickListener(view -> {
            doSnooze();
            Toast.makeText(this, "alarm set", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void doSnooze(){
        long snoozeTime = TimeUnit.SECONDS.toMillis(time);
        long totalDelay = snoozeTime + Calendar.getInstance().getTimeInMillis();
        OneTimeWorkRequest refillSnooze = new OneTimeWorkRequest.Builder(
                RefillSnoozeWorker.class)
                .setInitialDelay(totalDelay, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(refillSnooze);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(SnoozeActivity.this, MainActivity.class);
        startActivity(back);
    }


}