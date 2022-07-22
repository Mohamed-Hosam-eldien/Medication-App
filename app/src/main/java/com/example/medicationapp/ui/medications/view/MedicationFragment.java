package com.example.medicationapp.ui.medications.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.medicationapp.R;
import com.example.medicationapp.ui.addEditMed.view.AddEditActivity;
import com.example.medicationapp.ui.displayMedication.view.DisplayMedicationActivity;
import com.example.medicationapp.ui.medications.presenter.MedicationPresenter;

import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.MedScheduler;
import com.example.medicationapp.models.Medication;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

public class MedicationFragment extends Fragment implements MedicationViewInterface {

    List<Medication> activeList;
    List<Medication> suspendedList;
    List<MedDetails> medDetailsList;
    MedScheduler medScheduler;
    RecyclerView suspendedRecyclerView;
    RecyclerView activeRecyclerView;
    RecyclerView.LayoutManager activeLayoutManager;
    RecyclerView.LayoutManager suspendedLayoutManager;
    ActiveAdapter activeAdapter;
    ActiveAdapter suspendedAdapter;
    MedicationPresenter medicationPresenter;
    Button btnAdd;

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
        FloatingActionsMenu floatingActionsMenu = getActivity().findViewById(R.id.flaoting);
        floatingActionsMenu.setVisibility(View.GONE);
        activeRecyclerView = view.findViewById(R.id.recyclerView);
        suspendedRecyclerView = view.findViewById(R.id.suspendedRecyclerView);

        btnAdd=view.findViewById(R.id.btnAddMedActiveFragment);

        suspendedLayoutManager = new LinearLayoutManager(getContext());
        activeLayoutManager = new LinearLayoutManager(getContext());

        suspendedRecyclerView.setLayoutManager(suspendedLayoutManager);
        activeRecyclerView.setLayoutManager(activeLayoutManager);

        suspendedRecyclerView.setHasFixedSize(true);
        activeRecyclerView.setHasFixedSize(true);

        suspendedAdapter = new ActiveAdapter(getContext(), suspendedList, medication -> {
            Intent in = new Intent(getActivity(), DisplayMedicationActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("med", medication);
            in.putExtra("bundle", b);
            startActivity(in);

        });

        activeAdapter = new ActiveAdapter(getContext(), activeList, medication -> {
            Intent in = new Intent(getActivity(), DisplayMedicationActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("med", medication);
            in.putExtra("bundle", b);
            startActivity(in);
        });
        activeRecyclerView.setAdapter(activeAdapter);
        suspendedRecyclerView.setAdapter(suspendedAdapter);

        medicationPresenter = new MedicationPresenter(getActivity(), this);
        medicationPresenter.getActiveMedications();
        medicationPresenter.getInActiveMedications();

        btnAdd.setOnClickListener(view1 -> {
            Intent in=new Intent(getActivity(), AddEditActivity.class);
            in.putExtra("comeFrom",1);
            startActivity(in);
        });
    }

    @Override
    public void getAllActiveMedicines(LiveData<List<Medication>> active) {
        active.observe(getActivity(), medications -> {
            activeList = medications;
            activeAdapter.setActiveMedicines(activeList);
            activeAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void getAllInActiveMedicines(LiveData<List<Medication>> inActive) {
        inActive.observe(getActivity(), medications -> {
            suspendedList = medications;
            suspendedAdapter.setActiveMedicines(suspendedList);
            suspendedAdapter.notifyDataSetChanged();
        });
    }
}