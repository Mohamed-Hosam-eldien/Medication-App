package com.example.medicationapp.home.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.FragmentHomeMedBinding;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.MedInstructions;
import com.example.medicationapp.model.MedScheduler;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;
import com.example.medicationapp.utils.Helper;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeMedFragment extends Fragment {

    private FragmentHomeMedBinding binding;

    HomePresenter presenter;
    Calendar calendarr;

    public HomeMedFragment() {
        // Required empty public constructor
        calendarr = Helper.getCurrentCalender();
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

//        //calendarr.set(2022, 2, 19, 9, 20);
////        Date date = calendarr.getTime();
////        Log.d("TIME", date.getHours() + ":"  +date.getMinutes());
//
//        List<Medication> medicationList = new ArrayList<>();
//
//
//        List<String> list = new ArrayList<>();
//        list.add("sunday");
//        list.add("friday");
//
//        ArrayList<MedDetails> arrayList = new ArrayList<>();
//        MedScheduler scheduler = new MedScheduler("5/5/2022", (short) 123, list);
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 9, 20);
//        arrayList.add(new MedDetails(calendarr, (short) 1, ""));
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 9, 20);
//        arrayList.add(new MedDetails(calendarr, (short) 1, ""));
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 10, 20);
//        arrayList.add(new MedDetails(calendarr, (short) 1, ""));
//
//
//        medicationList.add(new Medication("Cold and flow", arrayList, scheduler,
//                0, 100, new MedInstructions("test 1", "test 2")));
//
//
//        List<String> list2 = new ArrayList<>();
//        list2.add("sunday");
//        list2.add("friday");
//
//        ArrayList<MedDetails> arrayList2 = new ArrayList<>();
//        MedScheduler scheduler2 = new MedScheduler("5/5/2022", (short) 123, list2);
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 2, 20);
//        arrayList2.add(new MedDetails(calendarr, (short) 1, ""));
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 4, 20);
//        arrayList2.add(new MedDetails(calendarr, (short) 1, ""));
//
//        //calendarr = Calendar.getInstance();
//        //calendarr.set(2022, 2, 19, 5, 20);
//        arrayList2.add(new MedDetails(calendarr, (short) 1, ""));
//
//
//        medicationList.add(new Medication("down town", arrayList2, scheduler2,
//                0, 100, new MedInstructions("test 1", "test 2")));

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //getDate(null);

    }

    public void getDate(String date, LifecycleOwner lifecycleOwner) {

        Log.d("DATE 1 : " , date);
        Log.d("DATE 2 : " , Helper.convertLongToDateFormat(calendarr.getTime().getTime()));

        presenter.getPatient(10).observe(lifecycleOwner, patient -> {
            if(patient.getMedications().size() > 0) {
                HomeAdapter adapter = new HomeAdapter(patient.getMedications(), getActivity());
                binding.homeRecyclerMid.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.homeRecyclerMid.setAdapter(adapter);
                binding.lottieMain.setVisibility(View.GONE);
            } else
                binding.lottieMain.setVisibility(View.VISIBLE);
        });

    }

}