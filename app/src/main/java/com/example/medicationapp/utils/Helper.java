package com.example.medicationapp.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.model.Medication;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static final int NOTIFICATION_ID = 10;
    public static final String CHANNEL_ID = "MEDICATION";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static Calendar getCurrentCalender() {
        return Calendar.getInstance();
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public long convertDateToLong(int year, int month, int date) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }

    public Calendar convertDateToCalender(Date source) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        return calendar;
    }

    public static Calendar convertTimeStringToCalender(String timeAsString) throws ParseException {
        //String myDateString = "13:24:40";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = sdf.parse(timeAsString);

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return calendar;
        //int hour = calendar.get(Calendar.HOUR);
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertLongToDateFormat(long time) {
        return new SimpleDateFormat("MM/dd/yyyy").format(new Date(time));
    }

    public static int convertLongToHours(long milliseconds) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);
        return time.get(Calendar.HOUR_OF_DAY);
    }

    public static int convertLongToMinuets(long milliseconds) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);
        return time.get(Calendar.MINUTE);
    }

    //send notification
    public static void showNotification(Context context, String body, PendingIntent intent,
                                        PendingIntent snoozePIntent, PendingIntent refillP) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "mina Wael", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(" description");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
            // Create an Intent for the activity you want to start

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setContentTitle("Missed Notification").setSmallIcon(R.drawable.launch)
                    .setContentText(body)
                    .setPriority(Notification.PRIORITY_DEFAULT).setContentIntent(intent)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.darkBlue))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .addAction(R.drawable.custom_accept, "snooze", snoozePIntent)
                    .addAction(R.drawable.launch, "Refill", refillP);


            NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
            nmc.notify(NOTIFICATION_ID, builder.build());

        }
    }

    public static String generateKey(){
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

}
