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
import com.example.medicationapp.connection.GetAllMedication;
import com.example.medicationapp.connection.NetworkInterface;
import com.example.medicationapp.databinding.FragmentHomeMedBinding;
import com.example.medicationapp.databinding.HomeBottomDialogBinding;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedScheduler;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeMedFragment extends Fragment implements ShowBottomDialog {

    private FragmentHomeMedBinding binding;

    HomePresenter presenter;
    Calendar calendar;

    public HomeMedFragment() {
        // Required empty public constructor
        calendar = Helper.getCurrentCalender();
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


        List<Medication> medicationList = new ArrayList<>();

        ArrayList<MedDetails> arrayList = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 5, 20);
        arrayList.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 7, 20);
        arrayList.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 10, 20);
        arrayList.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));


        medicationList.add(new Medication("Cold and flow", 4, 1,
                arrayList, 100,"after food","12/5/2022", null, 1));


        ArrayList<MedDetails> arrayList2 = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 8, 0);
        arrayList2.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));
        Log.d("TAG-1", "onViewCreated: "+calendar.getTimeInMillis());

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 16, 0);
        Log.d("TAG-1", "onViewCreated: "+calendar.getTimeInMillis());
        arrayList2.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 24, 0);
        Log.d("TAG-1", "onViewCreated: "+calendar.getTimeInMillis());
        arrayList2.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        medicationList.add(new Medication("H.M.A", 3, 1,
                arrayList2, 100,"before food","11/3/2022", null, 1));


        ///////////////////////////////////////////////////////////


        ArrayList<MedDetails> arrayList3 = new ArrayList<>();

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 8, 0);
        arrayList3.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 16, 0);
        arrayList3.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        calendar = Calendar.getInstance();
        calendar.set(2022, 2, 19, 24, 0);
        arrayList3.add(new MedDetails(calendar.getTimeInMillis(), 1, "",0));

        medicationList.add(new Medication("Go to hell", 3, 1,
                arrayList3, 100,"before food","11/3/2022", null, 0));

        presenter.addMedication(medicationList.get(0));
        presenter.addMedication(medicationList.get(1));


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        getDate(null,getActivity());

    }

    public void getDate(String date, LifecycleOwner lifecycleOwner) {

        presenter.getMedicationListByAllDay().observe(lifecycleOwner, medications -> {
            if(medications.size() > 0) {
                HomeAdapter adapter = new HomeAdapter(medications, getActivity(), this);
                binding.homeRecyclerMid.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.homeRecyclerMid.setAdapter(adapter);
                binding.lottieMain.setVisibility(View.GONE);
            } else {
                binding.lottieMain.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void showMedDialog(MedDetails detail, Medication medication) {
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_bottom_dialog,null);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        HomeBottomDialogBinding dialogBinding = HomeBottomDialogBinding.bind(view);
        dialogBinding.txtName.setText(medication.getName());
        dialogBinding.txtTime.setText("take at " + getRemainingTime(detail.getTime()));
        dialogBinding.txtDose.setText("Take " + detail.getDose() + " " + medication.getTimeToFood());

        dialog.show();
    }

    private String getRemainingTime(long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, time);
    }

}