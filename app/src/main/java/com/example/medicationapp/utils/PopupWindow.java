package com.example.medicationapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.medicationapp.R;
import com.example.medicationapp.ui.home.presenter.HomePresenter;
import com.example.medicationapp.models.Medication;

import static android.content.Context.WINDOW_SERVICE;

import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;


public class PopupWindow {

    private final Context context;
    private final View mView;
    private WindowManager.LayoutParams mParams;
    private final WindowManager mWindowManager;
    private int snooze;
    private final int dose;
    private final long time;
    private final long[] array;
    private final String name;
    private final String food;
    HomePresenter presenter;

    @SuppressLint("DefaultLocale")
    public PopupWindow(Context context, String name, long time, int dose, String food, long[] array, String id) {
        this.context = context;

        this.name = name;
        this.time = time;
        this.dose = dose;
        this.array = array;
        this.food = food;
        presenter = new HomePresenter(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = new WindowManager.LayoutParams(
                    // Shrink the window to wrap the content rather
                    // than filling the screen
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                    // Display it on top of other application windows
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // FLAG_TURN_SCREEN_ON
                    // Don't let it grab the input focus
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,//FLAG_NOT_FOCUSABLE,
                    // Make the underlying application window visible
                    // through any transparent parts
                    PixelFormat.TRANSPARENT); //TRANSLUCENT

        }
        // getting a LayoutInflater
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.popup_med_layout, null);
        RadioGroup radioGroup = mView.findViewById(R.id.radioAddSnooze);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.snooze1Minute:
                    snooze = 1;
                    break;
                case R.id.snooze5Minutes:
                    snooze = 5;
                    break;
                case R.id.snooze15Minutes:
                    snooze = 15;
                    break;
            }
        });


        // snooze button
        mView.findViewById(R.id.btnSnooze).setOnClickListener(view -> {
            ExpandableLayout expandableLayout = mView.findViewById(R.id.expandLayoutSnooze);
            expandableLayout.expand();
        });

        mView.findViewById(R.id.btnSnoozeNow).setOnClickListener(v -> {
            if(snooze != 0) {
                submitSnooze();
                close();
            } else {
                Toast.makeText(context, "please select snooze time!", Toast.LENGTH_SHORT).show();
            }
        });

        mView.findViewById(R.id.btnTake).setOnClickListener(view -> {
            updateTaken(getPosition());
            close();
        });

        mView.findViewById(R.id.btnRefuse).setOnClickListener(view -> close());


        TextView txtName = mView.findViewById(R.id.txtName);
        TextView txtDose = mView.findViewById(R.id.txtDose);
        TextView txtTime = mView.findViewById(R.id.txtTime);
        TextView txtFood = mView.findViewById(R.id.txtFood);


        txtFood.setText(food);
        txtTime.setText((new SimpleDateFormat("HH:mm aaa")).format(new Date(time)));

        StringBuilder builder = new StringBuilder();
        txtDose.setText(builder.append("you must take ").append(dose).append(" dose"));
        txtName.setText(name);


        // Define the position of the
        // window within the screen
        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
    }

    public void open() {

        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {
                    mWindowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.d("Error1", e.toString());
        }

    }

    public void close() {

        try {
            // remove the view from the window
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();

            PopupService service = new PopupService();
            service.stopSelf();

            // remove all views
            ((ViewGroup) mView.getParent()).removeAllViews();

            Log.d("Close is Called : ", "Called");

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("Error2", e.toString());
        }
    }

    private void updateTaken(int position) {
        new Thread(() -> {
            Medication medication =  presenter.getMedicationToPopup(Paper.book().read("IDMED"));
            medication.getMedDetails().get(position).setTaken(1);
            presenter.updateTaken(medication.getMedDetails(), medication.getId());
            Log.d("MED", "" + position);
            close();
        }).start();
        Toast.makeText(context, "mid is taken", Toast.LENGTH_SHORT).show();
    }

    private void submitSnooze() {
        for (int i = 0; i < array.length; i++) {
            if(array[i] == time) {
                long snoozeTime = TimeUnit.MINUTES.toMillis(snooze);
                Log.d("SnoozeTime", "onClick: " + snoozeTime);
                array[i] = array[i] + snoozeTime;
            }
        }

        WorkManager.getInstance(context).cancelAllWorkByTag(name);

        WorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(TimerWorker.class,
                        24, TimeUnit.HOURS)
                        .setInputData(
                                new Data.Builder()
                                        .putLongArray("times", array)
                                        .putString("medName", name)
                                        .putInt("dose", dose)
                                        .putString("medFood", food)
                                        .build()
                        )
                        .addTag(name)
                        .build();

        WorkManager.getInstance(context).enqueue(saveRequest);

        close();
    }

    private int getPosition() {
        int pos =0;
        for(int i=0 ; i<array.length ; i++) {
            if(array[i] == time) {
                pos = i;
            }
        }
        return pos;
    }

}