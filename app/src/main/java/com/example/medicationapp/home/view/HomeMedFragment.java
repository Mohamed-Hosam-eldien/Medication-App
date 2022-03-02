package com.example.medicationapp.home.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.FragmentHomeMedBinding;
import com.example.medicationapp.databinding.HomeBottomDialogBinding;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import java.util.ArrayList;
import java.util.Calendar;


public class HomeMedFragment extends Fragment implements ShowBottomDialog {

    private FragmentHomeMedBinding binding;
    private HomeAdapter adapter;

    private HomePresenter presenter;

    public HomeMedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_med, container, false);
        binding = FragmentHomeMedBinding.bind(view);

        presenter = new HomePresenter(getActivity());

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.lottieMain.setAnimation(R.raw.pick);

        getDate(Calendar.getInstance().getTimeInMillis(), getActivity());
    }

    public void getDate(long currentDate, LifecycleOwner lifecycleOwner) {
        Log.d("CURRENT DATE : " , currentDate+"");

        presenter.getMedicationListByAllDay(currentDate).observe(lifecycleOwner, medications -> {
                if (medications.size() > 0) {
                    adapter = new HomeAdapter(medications, getActivity(), this);
                    binding.homeRecyclerMid.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.homeRecyclerMid.setAdapter(adapter);
                    binding.lottieMain.setVisibility(View.GONE);
                } else {
                    binding.lottieMain.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    adapter.setList(new ArrayList<>());
                }
            });

    }


    private String getRemainingTime(long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, time);
    }


    @Override
    public void showMedDialog(MedDetails detail, Medication medication) {
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_bottom_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        HomeBottomDialogBinding dialogBinding = HomeBottomDialogBinding.bind(view);
        dialogBinding.txtName.setText(medication.getName());
        dialogBinding.txtTime.setText(new StringBuilder(getString(R.string.take_at)).append(getRemainingTime(detail.getTime())));
        dialogBinding.txtDose.setText(new StringBuilder(getString(R.string.take))
                .append(detail.getDose())
                .append(" ")
                .append(medication.getTimeToFood()));

        dialog.show();
    }
}