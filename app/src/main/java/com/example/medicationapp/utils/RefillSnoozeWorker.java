package com.example.medicationapp.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.medicationapp.R;
import com.example.medicationapp.database.DAO;
import com.example.medicationapp.database.DatabaseBuilder;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.medications.addEditMed.presenter.AddEditPresenter;
import com.example.medicationapp.snooze_refill.view.RefillActivity;
import com.example.medicationapp.snooze_refill.view.SnoozeActivity;

public class RefillSnoozeWorker extends Worker {
    Context context;
    String medId = null;
    int refillNo = 0;
    int totalPills = 0;
    String medName = null;
    AddEditPresenter addEditPresenter;
    public RefillSnoozeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
//        refillNo = addEditPresenter.getRefillNo(medId);
//        totalPills = addEditPresenter.getTotalPills(medId);
    }

    @NonNull
    @Override
    public Result doWork() {
//        medId = getInputData().getString("medId");
//        refillNo = getInputData().getInt("refillAmount", 0);
//        totalPills = getInputData().getInt("totalPills", 0);
//        medName = getInputData().getString("medNameSnooze");
//        addEditPresenter = new AddEditPresenter(context);


//        boolean check = addEditPresenter.checkRefill(medId);

        Log.i("TAG", "doWork: " + refillNo);
        Log.i("TAG", "doWork: " + totalPills);

            myNotificationIntents();

        return Result.success();
    }


    public void myNotificationIntents(){
        if (totalPills <= refillNo) {

            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(
                        context, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE
                );
            }
            PendingIntent snoozePendingIntent = null;
            Intent snoozeIntent = new Intent(context, SnoozeActivity.class);
                snoozeIntent.putExtra("medicine",medName);
            snoozeIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                snoozePendingIntent = PendingIntent.getActivity(
                        context, 1, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
                );
            }
            PendingIntent refillPendingIntent = null;
            Intent refillIntent = new Intent(context, RefillActivity.class);
            refillIntent.putExtra("medicine", medId);


            refillIntent.putExtra("med",medName);

            refillIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            refillIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                refillPendingIntent = PendingIntent.getActivity(
                        context, 1, refillIntent, PendingIntent.FLAG_IMMUTABLE
                );
            }
            Helper.showNotification(context,
                    "Your " /*+ medName*/ +" "+ context.getString(R.string.notificationBody),
                    pendingIntent, snoozePendingIntent, refillPendingIntent);

        }
    }
}
