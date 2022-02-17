package com.example.medicationapp.medications.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Medication;

import java.util.ArrayList;
import java.util.List;

public class MedicationFragment extends Fragment {

    List<Medication> activeList;
    List<Medication> suspendedList;
    Medication medication;
    Medication medication2;
    Medication medication3;
    Medication medication4;
    RecyclerView suspendedRecyclerView;
    RecyclerView activeRecyclerView;
    RecyclerView.LayoutManager activeLayoutManager;
    RecyclerView.LayoutManager suspendedLayoutManager;
    RecyclerView.Adapter activeAdapter;
    RecyclerView.Adapter suspendedAdapter;

    public MedicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activeList = new ArrayList<>();
        suspendedList = new ArrayList<>();
        medication = new Medication();
        medication2 = new Medication();
        medication3 = new Medication();
        medication4 = new Medication();
        medication.setName("zithrocan");
        medication.setMidStrength(100);
        medication.setImage(R.drawable.ic_baseline_person_24);
        medication2.setName("sfd");
        medication2.setImage(R.drawable.guest);
        medication2.setMidStrength(12);
        medication3.setMidStrength(12);
        medication3.setName("afdlsfkdsj");
        medication4.setImage(R.drawable.ic_baseline_person_24);medication3.setMidStrength(12);
        medication4.setName("afdlsfkdsdsddddddddddddddddddddddddddddddddddddddddddddsj");
        medication4.setImage(R.drawable.ic_baseline_person_24);
        activeList.add(medication);
        activeList.add(medication);
        activeList.add(medication2);
        activeList.add(medication2);
        activeList.add(medication2);
        activeList.add(medication3);
        activeList.add(medication3);
        activeList.add(medication3);
        activeList.add(medication3);
        activeList.add(medication3);
        activeList.add(medication4);
        activeList.add(medication4);
        activeList.add(medication4);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        suspendedList.add(medication);
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activeRecyclerView = view.findViewById(R.id.recyclerView);
        suspendedRecyclerView = view.findViewById(R.id.suspendedRecyclerView);

        suspendedLayoutManager = new LinearLayoutManager(getContext());
        activeLayoutManager = new LinearLayoutManager(getContext());

        suspendedRecyclerView.setLayoutManager(suspendedLayoutManager);
        activeRecyclerView.setLayoutManager(activeLayoutManager);

        suspendedAdapter = new ActiveAdapter(getContext(), activeList);
        activeAdapter = new ActiveAdapter(getContext(), suspendedList);
        activeRecyclerView.setAdapter(activeAdapter);
        suspendedRecyclerView.setAdapter(suspendedAdapter);

    }
}