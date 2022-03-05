package com.example.medicationapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.medicationapp.R;
import com.example.medicationapp.model.TimeLong;

public class ReminderWorker extends Worker {

    private static NotificationChannel notificationChannel;
    public static final String NOTIFICATION_CHANNEL_ID = "App.Location.Notification.ID";

    Context context;
    long time;
    String name;
    int dose;
    String food, id;
    long[] list;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        time = getInputData().getLong("alarm",0);
        name = getInputData().getString("medName");
        dose = getInputData().getInt("dose", 0);
        food = getInputData().getString("medFood");
        list = getInputData().getLongArray("list");
        id = getInputData().getString("id");


        startService();
        showNotification(context, 123, name, "you have " + dose + " dose should be taken " + food, null);

        Log.d("set time alarm", "Done : " + time);

        return Result.success();
    }

//
//    public static void showNotification(Context context, String body, PendingIntent intent,
//                                        PendingIntent snoozePIntent, PendingIntent refillP) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "reminder", NotificationManager.IMPORTANCE_HIGH);
//            channel.setDescription(" description");
//            NotificationManager nm = context.getSystemService(NotificationManager.class);
//            nm.createNotificationChannel(channel);
//            // Create an Intent for the activity you want to start
//
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
//            builder.setContentTitle("").setSmallIcon(R.drawable.launch)
//                    .setContentText(body)
//                    .setPriority(Notification.PRIORITY_DEFAULT).setContentIntent(intent)
//                    .setAutoCancel(true)
//                    .setColor(ContextCompat.getColor(context, R.color.darkBlue))
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//            NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
//            nmc.notify(2, builder.build());
//
//        }
//    }


    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(context)) {
                // start the service based on the android version

                Intent intent = new Intent(context, PopupService.class);
                intent.putExtra("Name", name);
                intent.putExtra("Dose", dose);
                intent.putExtra("Time", time);
                intent.putExtra("Food", food);
                intent.putExtra("id", id);

                Log.d("reminder_Time", time+"");

                // for all times in work manager
                TimeLong timeLong = new TimeLong();
                timeLong.setTimes(list);
                intent.putExtra("list", timeLong.getTimes());

                Log.d("SERVICE_ARRAY_Worker", timeLong.getTimes().length+"");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(new Intent(intent));
                }
            }
        }else{
            context.startService(new Intent(context, PopupService.class));
        }
    }


    private static void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Follow_Me", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Follow_Me");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
        }
    }


    public static void showNotification(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if(intent != null)
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel();
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setVibrate(new long[]{0,1000,500,1000})
                    .setSound(defaultSoundUri)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.launch);

            if(pendingIntent != null)
                builder.setContentIntent(pendingIntent);

            Notification notification = builder.build();
            notificationManager.notify(id,notification);


        } else {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "App_Location");

            notificationBuilder.setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                    .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                    .setContentTitle(title)
                    .setContentText(content)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setVibrate(new long[]{0,1000,500,1000})
                    .setSound(defaultSoundUri)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManagerOldVersion = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManagerOldVersion.notify(id, notificationBuilder.build());

        }

    }

}
