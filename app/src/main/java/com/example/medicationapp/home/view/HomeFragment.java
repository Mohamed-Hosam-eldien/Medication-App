package com.example.medicationapp.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.caring.view.AdditionalCare;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Patient;
import com.example.medicationapp.databinding.FragmentHomeBinding;
import com.example.medicationapp.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(view);

        setCalenderDetails();

        return binding.getRoot();
    }

    private void setCalenderDetails() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);


        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(binding.getRoot(), R.id.calendarView)
                .range(startDate, endDate)
                .build();

        binding.txtToday.setOnClickListener(v -> {
            Log.d("Current Date : " , Calendar.getInstance()+"");
            Log.d("Selected Date : " , horizontalCalendar.getSelectedDate()+"");
            if(!horizontalCalendar.getSelectedDate().equals(Calendar.getInstance())) {
                horizontalCalendar.selectDate(Calendar.getInstance(), true);
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                OnDateSelect select = (OnDateSelect) getActivity();
                select.onDateSelected(Helper.convertLongToDateFormat(date.getTime().getTime()));
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {}
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}