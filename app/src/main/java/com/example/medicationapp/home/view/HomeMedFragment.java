package com.example.medicationapp.home.view;

import android.app.Dialog;
import android.app.PendingIntent;
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
import com.example.medicationapp.R;
import com.example.medicationapp.databinding.FragmentHomeMedBinding;
import com.example.medicationapp.databinding.HomeBottomDialogBinding;
import com.example.medicationapp.medications.displayMedication.view.DisplayMedicationActivity;
import com.example.medicationapp.requests.view.RequestsFragment;
import com.example.medicationapp.snooze_refill.view.RefillActivity;
import com.example.medicationapp.snooze_refill.view.SnoozeActivity;
import com.example.medicationapp.home.presenter.HomePresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import io.paperdb.Paper;

public class HomeMedFragment extends Fragment implements ShowBottomDialog {

    private FragmentHomeMedBinding binding;
    private DatabaseReference ref;
    private String reqId;

    private HomePresenter presenter;
    private HomeAdapter adapter;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference(Common.Request);
        reqId = Paper.book().read(RequestsFragment.ACEPTED_REQUEST_ID, "null");

        binding.homeRecyclerMid.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.homeRecyclerMid.setHasFixedSize(true);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.lottieMain.setAnimation(R.raw.pick);

        getDate(Calendar.getInstance().getTimeInMillis(), getActivity());
    }

    public void getDate(long currentDate, LifecycleOwner lifecycleOwner) {
        Log.d("CURRENT DATE : ", currentDate + "");
        presenter.getMedicationListByAllDay(currentDate).observe(lifecycleOwner, medications -> {
            if (medications.size() > 0) {
                adapter = new HomeAdapter(medications, getActivity(), this, currentDate);
                binding.homeRecyclerMid.setAdapter(adapter);
                binding.lottieMain.setVisibility(View.GONE);
            } else {
                binding.lottieMain.setVisibility(View.VISIBLE);
                adapter = new HomeAdapter(new ArrayList<>(), getActivity(), this, currentDate);
                binding.homeRecyclerMid.setAdapter(adapter);
            }

        });

    }


    private String getRemainingTime(long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, time);
    }


    @Override
    public void showMedDialog(MedDetails detail, Medication medication, int position) {
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_bottom_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        HomeBottomDialogBinding dialogBinding = HomeBottomDialogBinding.bind(view);
        dialogBinding.txtName.setText(medication.getName());
        dialogBinding.txtTime.setText(new StringBuilder(getString(R.string.take_at)).append(" ").append(getRemainingTime(detail.getTime())));
        dialogBinding.txtDose.setText(new StringBuilder(getString(R.string.take))
                .append(" ")
                .append(detail.getDose())
                .append(" ")
                .append(getString(R.string.dose))
                .append(" ")
                .append(medication.getTimeToFood()));

        dialogBinding.txtRefile.setText(new StringBuilder().append(getString(R.string.refile_after)).append(medication.getRefillNo()).append(getString(R.string.pills)).toString());

        dialogBinding.imgInfo.setOnClickListener(view1 -> navigateToInfo(medication, dialog));

        dialogBinding.imgEdit.setOnClickListener(view1 -> navigateToInfo(medication, dialog));

        dialogBinding.imgDelete.setOnClickListener(view1 -> showBackDialog(medication, dialog));

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


                refillIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                refillIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    refillPendingIntent = PendingIntent.getActivity(
                            getContext(), 1, refillIntent, PendingIntent.FLAG_IMMUTABLE
                    );
                }


                Helper.showNotification(getContext(),
                        "Your " /*+ medication.getName()*/ + " " + getString(R.string.notificationBody),
                        pendingIntent, snoozePendingIntent, refillPendingIntent);

            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void navigateToInfo(Medication medication, Dialog dialog) {
        Intent in = new Intent(getActivity(), DisplayMedicationActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("med", medication);
        in.putExtra("bundle", b);
        dialog.dismiss();
        startActivity(in);
    }

    void showBackDialog(Medication medication, Dialog dia) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getString(R.string.delete_med)).setPositiveButton(R.string.delete, (dialogInterface, i) -> {
            presenter.deleteMedicine(medication);
            if (!(reqId.equals("null") || reqId.equals("") || reqId == null))
                showFirebaseDialog(medication);
                dia.dismiss();
        });
        dialog.setNegativeButton("cancel", (dialogInterface, i) -> { });
        dialog.show();
    }


    void showFirebaseDialog(Medication med) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
        String text, btnText;
        btnText = "delete";
        text = getString(R.string.do_you_want_to_delete);
        dialog.setTitle(text).setPositiveButton(R.string.save, (dialogInterface, i)
                -> ref.child(reqId).child("medicationList").child(med.getId()).removeValue());
        dialog.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
        });
        dialog.show();
    }

}

