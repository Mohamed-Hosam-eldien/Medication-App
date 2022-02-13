package com.example.medicationapp.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navigationView, navController);

        NavigationUI.setupWithNavController(binding.bottomBar, navController);

        binding.layoutCurrentDep.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        initDependentLayout();

    }

    private void initDependentLayout() {
        View navLayout = LayoutInflater.from(this).inflate(R.layout.layout_nav_include, null);
        binding.navigationView.addView(navLayout);
        navLayout.findViewById(R.id.layoutAddDependent)
                .setOnClickListener(v -> Toast.makeText(this, "not active", Toast.LENGTH_SHORT).show());
    }

}