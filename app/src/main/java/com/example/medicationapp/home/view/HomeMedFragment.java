package com.example.medicationapp.home.view;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.FragmentHomeMedBinding;
import com.example.medicationapp.databinding.HomeBottomDialogBinding;
import com.example.medicationapp.medications.displayMedication.view.DisplayMedicationActivity;
import com.example.medicationapp.snooze_refill.view.RefillActivity;
import com.example.medicationapp.snooze_refill.view.SnoozeActivity;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.utils.Helper;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeMedFragment extends Fragment implements ShowBottomDialog{

    private FragmentHomeMedBinding binding;

    HomePresenter presenter;
    Calendar calendar;
    private HomeAdapter adapter;

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

        getDate(Calendar.getInstance().getTimeInMillis(), getActivity());
    }

    public void getDate(long currentDate, LifecycleOwner lifecycleOwner) {
        Log.d("CURRENT DATE : " , currentDate+"");
        HomeAdapter adapter = new HomeAdapter(new ArrayList<>(), getActivity(), this);
        presenter.getMedicationListByAllDay(currentDate).observe(lifecycleOwner, medications -> {
                if (medications.size() > 0) {
                    adapter.setList(medications);
                    binding.homeRecyclerMid.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.homeRecyclerMid.setAdapter(adapter);
                    binding.lottieMain.setVisibility(View.GONE);
                } else {
                    binding.lottieMain.setVisibility(View.VISIBLE);
                    adapter.setList(new ArrayList<>());
                    adapter.notifyDataSetChanged();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_bottom_dialog,null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        HomeBottomDialogBinding dialogBinding = HomeBottomDialogBinding.bind(view);
        dialogBinding.txtName.setText(medication.getName());
        dialogBinding.txtTime.setText(new StringBuilder(getString(R.string.take_at)).append(getRemainingTime(detail.getTime())));
        dialogBinding.txtDose.setText(new StringBuilder(getString(R.string.take))
                .append(detail.getDose())
                .append(" ")
                .append(medication.getTimeToFood()));

        dialogBinding.imgInfo.setOnClickListener(view1 -> navigateToInfo(medication));

        dialogBinding.imgEdit.setOnClickListener(view1 -> navigateToInfo(medication));

        dialogBinding.imgDelete.setOnClickListener(view1 -> showBackDialog(medication));

        dialogBinding.btnTakeDialog.setOnClickListener(view1 -> {
            presenter.updateRefill(medication.getTotalPills() - 1, medication.getId());
            if (medication.getTotalPills() <= medication.getRefillNo()) {

                Intent resultIntent = new Intent(getContext(), MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    pendingIntent = PendingIntent.getActivity(
                            getContext(), 1, resultIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                }
                PendingIntent snoozePendingIntent = null;
                Intent snoozeIntent = new Intent(getContext(), SnoozeActivity.class);


//                snoozeIntent.putExtra("snoozeName",medication.getName());
                snoozeIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    snoozePendingIntent = PendingIntent.getActivity(
                            getContext(), 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                }
                PendingIntent refillPendingIntent = null;
                Intent refillIntent = new Intent(getActivity(), RefillActivity.class);
                refillIntent.putExtra("medicine", medication.getId());
                Toast.makeText(getActivity(), ""+ medication.getId(), Toast.LENGTH_SHORT).show();


//                refillIntent.putExtra("med", medication.getName());

                refillIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                refillIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                b = new Bundle();
//                b.putString("medicine", medication.getName());
//                b.putParcelable("m", medication);
//                refillIntent.putExtra("b", b);

//                refillIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    refillPendingIntent = PendingIntent.getActivity(
                            getContext(), 1, refillIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                }
                Helper.showNotification(getContext(),
                        "Your " /*+ medication.getName()*/ +" "+ getString(R.string.notificationBody),
                        pendingIntent, snoozePendingIntent, refillPendingIntent);

            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void navigateToInfo(Medication medication){
        Intent in = new Intent(getActivity(), DisplayMedicationActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("med", medication);
        in.putExtra("bundle", b);
        startActivity(in);
    }

    void showBackDialog(Medication medication) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        dialog.setTitle("Are you sure Delete Medicine ?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.deleteMedicine(medication);
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
 /*   private void snoozeIntent(PendingIntent s) {
        Intent snoozeIntent = new Intent(getContext(), SnoozeActivity.class);
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PendingIntent snoozePendingIntent = PendingIntent.getActivity(
                    getContext(), 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
            );
        }
    }*/
}

    /*private void intentTake(Medication medication) {
        Intent snoozeIntent = new Intent(getContext(), SnoozeActivity.class);
        snoozeIntent.putExtra("medicine", medication);
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PendingIntent snoozePendingIntent = PendingIntent.getActivity(
                    getContext(), 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
            );
        }
    }*/
