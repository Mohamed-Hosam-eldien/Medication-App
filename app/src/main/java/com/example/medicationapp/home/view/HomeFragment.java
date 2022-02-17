package com.example.medicationapp.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.medicationapp.R;
import com.example.medicationapp.caring.view.AdditionalCare;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Button btnTst;
    LocalDB localDB;
    Patient patient;
    Medication medication;
    List <Medication> medications;
    List<Patient> getPatients;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        btnTst = view.findViewById(R.id.btnTest);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdditionalCare.class);
                startActivity(intent);
            }
        });


    }
}