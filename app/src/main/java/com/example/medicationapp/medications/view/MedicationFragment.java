package com.example.medicationapp.medications.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedScheduler;
import com.example.medicationapp.model.Medication;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MedicationFragment extends Fragment {

    LocalDB localDB;
    List<Medication> activeList;
    List<Medication> suspendedList;
    List<MedDetails> medDetailsList;
    Medication medication;
    MedScheduler medScheduler;
    RecyclerView suspendedRecyclerView;
    RecyclerView activeRecyclerView;
    RecyclerView.LayoutManager activeLayoutManager;
    RecyclerView.LayoutManager suspendedLayoutManager;
    ActiveAdapter activeAdapter;
    ActiveAdapter suspendedAdapter;

    public MedicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        medScheduler = new MedScheduler();
        localDB = LocalDB.getInstance(getContext());
        activeList = new ArrayList<>();
        medDetailsList = new ArrayList<>();
        suspendedList = new ArrayList<>();
        medication = new Medication("zithrocassnsa",medDetailsList, medScheduler, 100, 1,1, "after breakfast");
        localDB.insertMedicine(medication);

        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionsMenu floatingActionsMenu= getActivity().findViewById(R.id.flaoting);
        floatingActionsMenu.setVisibility(View.GONE);
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

        localDB.getAllActiveMedicines().observe(getActivity(), new Observer<List<Medication>>() {
            @Override
            public void onChanged(List<Medication> medications) {
                activeList = medications;
                activeAdapter.setActiveMedicines(activeList);
                activeAdapter.notifyDataSetChanged();
            }
        });
    }
}