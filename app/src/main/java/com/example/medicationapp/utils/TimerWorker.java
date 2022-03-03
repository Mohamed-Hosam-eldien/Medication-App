package com.example.medicationapp.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimerWorker extends Worker {

    Context context;

    long[] array;

    public TimerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        array = getInputData().getLongArray("times");
        String name = getInputData().getString("medName");
        String id = getInputData().getString("id");

        int dose = getInputData().getInt("dose", 0);
        String food = getInputData().getString("medFood");

        long current = Calendar.getInstance().getTimeInMillis();

        if(array != null) {
            for (long l : array) {

                Log.d("DDD", l +"");

                long too = l / 1000L - current / 1000L;

                OneTimeWorkRequest saveRequest = new OneTimeWorkRequest
                        .Builder(ReminderWorker.class)
                        .addTag(name)
                        .setInputData(
                                new Data.Builder()
                                        .putLong("alarm", l)
                                        .putString("id", id)
                                        .putString("medName", name)
                                        .putInt("dose", dose)
                                        .putString("medFood", food)
                                        .putLongArray("list",array)
                                        .build()
                        ).setInitialDelay(too, TimeUnit.SECONDS).build();

                WorkManager.getInstance(context).enqueue(saveRequest);
            }
            return Result.success();
        }
        return Result.failure();
    }

}



