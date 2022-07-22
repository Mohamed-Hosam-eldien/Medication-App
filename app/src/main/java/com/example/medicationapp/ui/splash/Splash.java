package com.example.medicationapp.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.ActivitySplashBinding;
import com.example.medicationapp.ui.home.view.MainActivity;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(() -> binding.lottieMain.setAnimation(R.raw.man)).start();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        },4500);

    }
}