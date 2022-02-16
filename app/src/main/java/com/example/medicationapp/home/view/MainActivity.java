package com.example.medicationapp.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.databinding.ActivityMainBinding;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int id;

    LocalDB localDB;
    Patient patient;
    Medication medication;
    List <Medication> medications;
    List<Patient> getPatients;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localDB = LocalDB.getInstance(this);
        patient = new Patient();
        patient.setFirstName("mina");
        patient.setLastName("peter");
        medication = new Medication();
        medication.setName("zithrocan");
        medications = new ArrayList<>();
        medications.add(medication);
        patient.setMedications(medications);
//        patient.setAge(20);
        localDB.insertPatient(patient);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navigationView, navController);

        NavigationUI.setupWithNavController(binding.bottomBar, navController);

        binding.layoutCurrentDep.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        initDependentLayout();

        getPatients = new ArrayList<>();
        localDB.getAllPatients().observe(this, new Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                getPatients = patients;
                Log.i("TAG", "main activity: "+ getPatients.get(0).getMedications().get(0).getName());
                id=getPatients.get(0).getId();
                Toast.makeText(MainActivity.this, ""+ id, Toast.LENGTH_SHORT).show();
            }

        });

        localDB.getPatient(1).observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(Patient patient) {
                Toast.makeText(MainActivity.this, ""+patient.getFirstName() , Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void initDependentLayout() {
        View navLayout = LayoutInflater.from(this).inflate(R.layout.layout_nav_include, null);
        binding.navigationView.addView(navLayout);
        navLayout.findViewById(R.id.layoutAddDependent)
                .setOnClickListener(v -> Toast.makeText(this, "not active", Toast.LENGTH_SHORT).show());
    }

}