package com.example.medicationapp.medications.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.medications.presenter.MedicationPresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedScheduler;
import com.example.medicationapp.model.Medication;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

public class MedicationFragment extends Fragment implements MedicationViewInterface{

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
    MedicationPresenter medicationPresenter;
    public MedicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        medScheduler = new MedScheduler();
        activeList = new ArrayList<>();
        medDetailsList = new ArrayList<>();
        suspendedList = new ArrayList<>();


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

        medicationPresenter = new MedicationPresenter(getActivity() ,this);
        medicationPresenter.getActiveMedications();
        medicationPresenter.getInActiveMedications();
    }

    @Override
    public void getAllActiveMedicines(LiveData<List<Medication>> active) {
        active.observe(getActivity(), new Observer<List<Medication>>() {
            @Override
            public void onChanged(List<Medication> medications) {
                activeList = medications;
                activeAdapter.setActiveMedicines(activeList);
                activeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getAllInActiveMedicines(LiveData<List<Medication>> inActive) {
        inActive.observe(getActivity(), new Observer<List<Medication>>() {
            @Override
            public void onChanged(List<Medication> medications) {
                suspendedList = medications;
                suspendedAdapter.setActiveMedicines(suspendedList);
                suspendedAdapter.notifyDataSetChanged();
            }
        });
    }
}