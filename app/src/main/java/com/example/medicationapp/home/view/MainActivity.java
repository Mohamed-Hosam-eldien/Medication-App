package com.example.medicationapp.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.caring.view.AdditionalCare;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.databinding.ActivityMainBinding;
import com.example.medicationapp.medications.view.addEditMed.AddEditActivity;
import com.example.medicationapp.model.Medication;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnDateSelect{
    int id;

    LocalDB localDB;
//    Patient patient;
    Medication medication;
//    List <Medication> medications;
//    List<Patient> getPatients;
    private ActivityMainBinding binding;
    private HomeMedFragment homeMedFragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        localDB = LocalDB.getInstance(this);

//        medication = new Medication();
//        medication.setName("zithrocan");
//        medications = new ArrayList<>();
//        medications.add(medication);
//        patient.setMedications(medications);
////        patient.setAge(20);
//        localDB.insertPatient(patient);

        initNavController();

        initAllMedicationFragment();

        initDependentLayout();

        initFabButton();

        binding.btnAddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("comeFrom",1);
                startActivity(intent);
            }
        });

    }

    private void initFabButton() {
        binding.btnAddMed.setOnClickListener(v -> {
            Toast.makeText(this, "add med", Toast.LENGTH_SHORT).show();
            binding.flaoting.collapse();
        });

        binding.btnAddTaker.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdditionalCare.class));
            binding.flaoting.collapse();
        });
    }

    private void initAllMedicationFragment() {
        FragmentManager manager;
        FragmentTransaction transaction;

        manager = getSupportFragmentManager();

        if(bundle == null) {
            homeMedFragment = new HomeMedFragment();

            transaction = manager.beginTransaction();

            transaction
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, homeMedFragment, "fragment_home")
                    .commit();
        } else {
            homeMedFragment = (HomeMedFragment) manager.findFragmentByTag("fragment_home");
        }
    }

    private void initNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navigationView, navController);

        NavigationUI.setupWithNavController(binding.bottomBar, navController);

        binding.layoutCurrentDep.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
    }


    private void initDependentLayout() {
        View navLayout = LayoutInflater.from(this).inflate(R.layout.layout_nav_include, null);
        binding.navigationView.addView(navLayout);
        navLayout.findViewById(R.id.layoutAddDependent)
                .setOnClickListener(v -> Toast.makeText(this, "not active", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDateSelected(String date) {
        //initAllMedicationFragment();
        homeMedFragment.getDate(date, this);
    }

}