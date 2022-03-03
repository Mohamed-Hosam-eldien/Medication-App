package com.example.medicationapp.dependent.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.connection.GetAllMedication;
import com.example.medicationapp.databinding.FragmentDependentBinding;
import com.example.medicationapp.dependent.presenter.DependentPresenter;
import com.example.medicationapp.home.view.ShowBottomDialog;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;

import java.util.List;

public class DependentFragment extends Fragment implements ShowBottomDialog, GetAllMedication {

    FragmentDependentBinding binding;
    DependentPresenter presenter;
    DependentAdapter adapter;
    List<Request> requestList;

    public DependentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_dependent, container, false);

        binding = FragmentDependentBinding.bind(view);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler.setHasFixedSize(true);


        presenter = new DependentPresenter(this, getActivity());

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getPatientMed();

    }

    @Override
    public void showMedDialog(MedDetails detail, Medication medication, int position) {}

    @Override
    public void getMedicationList(List<Request> request) {
        adapter = new DependentAdapter(getContext(), request);
        requestList = request;
        binding.recycler.setAdapter(adapter);
    }

}