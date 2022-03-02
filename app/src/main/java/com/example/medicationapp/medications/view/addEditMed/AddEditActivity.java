package com.example.medicationapp.medications.view.addEditMed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.ActivityAddEditBinding;
import com.example.medicationapp.medications.view.addEditMed.model.ReminderTime;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.utils.Helper;
import com.example.medicationapp.utils.TimerWorker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddEditActivity extends AppCompatActivity {
    ActivityAddEditBinding binding;

    int comeFrom, medStrength, noDays = 0, totalAmount = 0;
    Medication medication, medFroEdit;
    MedDetails medDetails;
    List<ReminderTime> timesArrayAdapter;
    List<String> days;
    ReminderTimesRecycleAdapter adapter;
    String takingInstruction = null, otherInstruction;
    Calendar calendarFroDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        days = new ArrayList<>();
        timesArrayAdapter = new ArrayList<>();
        medDetails = new MedDetails();

        calendarFroDate=Calendar.getInstance();

        binding.addMedRBtnOngoing.setVisibility(View.GONE);
        binding.addMedRBtnNoDays.setVisibility(View.GONE);
        binding.addMedTextRg1.setVisibility(View.GONE);



        Intent intent = getIntent();

        comeFrom = intent.getIntExtra("comeFrom", 5);
        if(comeFrom==3)
        medFroEdit = intent.getBundleExtra("bundle").getParcelable("med");

        binding.addMedInstrRadGro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.addMedInstrRaBtnAfter:
                        takingInstruction = "After";
                        Toast.makeText(AddEditActivity.this, "" + takingInstruction, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.addMedInstrRaBtnBefore:
                        takingInstruction = "Before";
                        break;
                    case R.id.addMedInstrRaBtnWhile:
                        takingInstruction = "While";
                        break;
                    case R.id.addMedInstrRaBtnDoesnt:
                        takingInstruction = "Doesn't";
                        break;
                }
            }
        });

        binding.addMedScheduleStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        adapter = new ReminderTimesRecycleAdapter(this, timesArrayAdapter, new OnAdapterClickListener() {
            @Override
            public void onCLick(ReminderTime time, int index) {
                showTimeDialog(time, index);
            }
        });
        binding.addMedReminderRv.setLayoutManager(new LinearLayoutManager(this));
        binding.addMedReminderRv.setAdapter(adapter);
        binding.addMedReminderRv.setHasFixedSize(true);

        binding.addMedRBtnNoDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoDaysDialog();
            }
        });


        binding.addMedRBtnSpecificDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comeFrom == 3) {
                    showSpecificDaysDialog(medFroEdit.getDays());
                } else
                    showSpecificDaysDialog(null);
            }
        });

        binding.addMedReminderTimeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        addToArraylist(1);
                        break;
                    case 2:
                        addToArraylist(2);
                        break;
                    case 3:
                        addToArraylist(3);
                        break;
                    case 4:
                        addToArraylist(4);
                        break;
                    case 5:
                        addToArraylist(5);
                        break;
                    case 6:
                        addToArraylist(6);
                        break;
                    case 7:
                        timesArrayAdapter.clear();
                        timesArrayAdapter.add(new ReminderTime(8, 0, 1));
                        timesArrayAdapter.add(new ReminderTime(20, 0, 1));
                        break;
                    case 8:
                        timesArrayAdapter.clear();
                        timesArrayAdapter.add(new ReminderTime(8, 1, 1));
                        timesArrayAdapter.add(new ReminderTime(16, 1, 1));
                        timesArrayAdapter.add(new ReminderTime(24, 1, 1));
                        break;
                    case 9:
                        timesArrayAdapter.clear();
                        timesArrayAdapter.add(new ReminderTime(8, 1, 1));
                        timesArrayAdapter.add(new ReminderTime(14, 1, 1));
                        timesArrayAdapter.add(new ReminderTime(20, 1, 1));
                        timesArrayAdapter.add(new ReminderTime(24, 1, 1));
                        break;
                }
                adapter.setReminderTimes(timesArrayAdapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setSupportActionBar(binding.addToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        if (comeFrom == 1 || comeFrom == 2)
            binding.addToolbar.setTitle("Add Medication");
        else if (comeFrom == 3) {
            binding.addToolbar.setTitle("Edit Medication");
            setDataToInputField(medFroEdit);
            binding.addMedEtMedName.setEnabled(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); //yyyy-MM-dd
            noDays=medFroEdit.getAllDays();
            //try {
                //calendarFroDate.setTime(sdf.parse(medFroEdit.getStartDate()));
                calendarFroDate.setTimeInMillis(medFroEdit.getStartDate());
                //calendarFroDate.setTime(medFroEdit.getStartDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEditMenuSave: {
               collectDataFromUser();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        showBackDialog();
        return false;
    }

    private void collectDataFromUser() {
        medication = new Medication();

        String medName = null;
        int amount = -1;
        medName = binding.addMedEtMedName.getText().toString();
        List<ReminderTime> reminderTimes = adapter.getAdapterList();

        if (binding.addMedInstrEtOtherInstr.getText().toString() != null && !binding.addMedInstrEtOtherInstr.getText().equals(""))
            otherInstruction = binding.addMedInstrEtOtherInstr.getText().toString();
        if (binding.addMedEtStrength.getText().toString() != null && !binding.addMedEtStrength.getText().toString().equals(""))
            medStrength = Integer.parseInt(binding.addMedEtStrength.getText().toString());
        if (binding.addMedNoPillToRemind.getText().toString() != null && !binding.addMedNoPillToRemind.getText().toString().equals(""))
            amount = Integer.parseInt(binding.addMedNoPillToRemind.getText().toString());
        int everyDay = 0;
        if (binding.addMedRBtnEveryDay.isChecked())
            everyDay = 1;
        if (binding.addMedPresAmountOfMedEt.getText().toString() != null && !binding.addMedPresAmountOfMedEt.getText().toString().equals(""))
            totalAmount = Integer.parseInt(binding.addMedPresAmountOfMedEt.getText().toString());
        boolean allIsFull = true;
        if (medName == null || medName.equals("")) {
            allIsFull = false;
            binding.addMedEtMedName.setError("Enter name");
            Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG).show();
        }
        if (reminderTimes.size() == 0) {
            allIsFull = false;
            Toast.makeText(this, "Please add time and pill(s) to remind you ", Toast.LENGTH_SHORT).show();
        }
        if (binding.addMedScheduleStartDate.getText() == null||binding.addMedScheduleStartDate.getText().equals("")) {
            allIsFull = false;
            Toast.makeText(this, "Please choose start date ", Toast.LENGTH_SHORT).show();
        }
        if (everyDay == 0 && days.size() == 0) {
            allIsFull = false;
            Toast.makeText(this, "Please choose day to remind you ", Toast.LENGTH_SHORT).show();
        }
        if (takingInstruction == null) {
            allIsFull = false;
            Toast.makeText(this, "Please Choose when you want to take a dose ", Toast.LENGTH_SHORT).show();
        }
        if (amount < 0) {
            allIsFull = false;
            Toast.makeText(this, "Enter number of pills to remind you when to refill reached", Toast.LENGTH_SHORT).show();
            binding.addMedEtStrength.setError("Enter amount");
        }
        List<MedDetails> medDetails1 = new ArrayList<>();
        if (allIsFull) {
            medication.setName(medName);
            if (medStrength != 0)
                medication.setMidStrength(medStrength);
            medication.setTimeToFood(takingInstruction);
            medication.setRefillNo(amount);
            medication.setAllDays(everyDay);
            medication.setTotalPills(totalAmount);
            if (days.size() > 0)
                medication.setDays(days);
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //medication.setStartDate(sdf.format(calendarFroDate.getTime()));
            medication.setStartDate(removeAllHours(calendarFroDate));

            long[] listOfTimes = new long[reminderTimes.size()];

            int i=0;

            for (ReminderTime r : reminderTimes) {

                Calendar c = Calendar.getInstance();

                c.setTime(calendarFroDate.getTime());
                c.set(Calendar.HOUR_OF_DAY, r.getHour());
                c.set(Calendar.MINUTE, r.getMinute());

                Log.d("Calender Time2", c.getTimeInMillis() + "");

                medDetails1.add(new MedDetails(c.getTimeInMillis(), r.getPill(), "pill", 0));

                // work manager
                listOfTimes[i] = c.getTimeInMillis();
                i+=1;
            }
            Log.d("Before : ", listOfTimes[0]  + "");
            medication.setMedDetails(medDetails1);

            WorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(TimerWorker.class,
                            24, TimeUnit.HOURS)
                            .setInputData(
                                    new Data.Builder()
                                            .putLongArray("times",listOfTimes)
                                            .putString("medName", medName)
                                            .putInt("dose", medication.getMedDetails().get(0).getDose())
                                            .putString("medFood", medication.getTimeToFood())
                                            .build()
                            )
                            .addTag(medName)
                            .build();

            WorkManager.getInstance(this).enqueue(saveRequest);

            AddEditPresenter presenter=new AddEditPresenter(this);
            if (comeFrom == 1||comeFrom==2) {
                presenter.insertMedication(medication);
                Log.d("TAG", "inserted time : " + medication.getStartDate());
                Toast.makeText(this, "insert", Toast.LENGTH_SHORT).show();
            }
            else{
                presenter.updateMedication(medication);
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable("med",medication);
                intent.putExtra("bundle",bundle);
                setResult(6,intent);
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }

    private long removeAllHours(Calendar calendar) {
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        return calendar.getTimeInMillis();
    }

    void showTimeDialog(ReminderTime time, int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_time, null);
        TextView btnPlus = view.findViewById(R.id.dialogTimeBtnPlus);
        TextView btnMinus = view.findViewById(R.id.dialogTimeBtnMinus);
        EditText etAmout = view.findViewById(R.id.dialogTimeEtNumber);
        TimePicker timePicker = view.findViewById(R.id.dialogTimePicker);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(time.getHour());
            timePicker.setMinute(time.getMinute());
            etAmout.setText(time.getPill() + "");
        }
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmout.setText((Integer.parseInt(etAmout.getText().toString()) + 1) + "");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(etAmout.getText().toString()) > 1)
                    etAmout.setText((Integer.parseInt(etAmout.getText().toString()) - 1) + "");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle("When do you need to take this dose ?").
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ReminderTime reminderTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    reminderTime = new ReminderTime(timePicker.getHour(), timePicker.getMinute(), Integer.parseInt(etAmout.getText().toString()));
                }
                adapter.updateList(reminderTime, index);
            }
        }).show();
    }

    void addToArraylist(int num) {
        timesArrayAdapter.clear();
        for (int i = 0; i < num; i++) {
            timesArrayAdapter.add(new ReminderTime(1 + 3 * i, 1, 1));
        }
    }

    private void showDateDialog() {
        //Calendar calendar = Calendar.getInstance();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker_view, null);
        DatePicker datePicker = view.findViewById(R.id.addDatePicker);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle("Set start date").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                calendarFroDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()); //calender and return calender

                Log.d("Calender 4" , calendarFroDate.getTimeInMillis()+"");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = sdf.format(calendarFroDate.getTime());
                binding.addMedScheduleStartDate.setText(dateString);
            }
        }).show();
    }

    private void showNoDaysDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_no_days, null);
        TextView btnPlus = v.findViewById(R.id.dialogNoDaysBtnPlus);
        TextView btnMinus = v.findViewById(R.id.dialogNoDaysBtnMinus);
        EditText etNoDays = v.findViewById(R.id.dialogNoDaysEtNumber);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNoDays.setText((Integer.parseInt(etNoDays.getText().toString()) + 1) + "");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(etNoDays.getText().toString()) > 1)
                    etNoDays.setText((Integer.parseInt(etNoDays.getText().toString()) - 1) + "");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Set number of days (from start date)");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noDays = Integer.parseInt(etNoDays.getText().toString());
                binding.addMedRBtnNoDays.setText("number of day : " + noDays);
            }
        });
        builder.show();
    }

    private void showSpecificDaysDialog(List<String> daysFroEdit) {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_specific_days, null);
        CheckBox sat = v.findViewById(R.id.dialogChBoxSat);
        CheckBox sun = v.findViewById(R.id.dialogChBoxSun);
        CheckBox mon = v.findViewById(R.id.dialogChBoxMon);
        CheckBox tue = v.findViewById(R.id.dialogChBoxTue);
        CheckBox wen = v.findViewById(R.id.dialogChBoxWed);
        CheckBox thu = v.findViewById(R.id.dialogChBoxThu);
        CheckBox fri = v.findViewById(R.id.dialogChBoxFri);

        if (comeFrom == 3 && daysFroEdit != null)
            for (String day : daysFroEdit) {
                switch (day) {
                    case "sat":
                        sat.setChecked(true);
                        break;
                    case "sun":
                        sun.setChecked(true);
                        break;
                    case "mon":
                        mon.setChecked(true);
                        break;
                    case "tue":
                        tue.setChecked(true);
                        break;
                    case "wen":
                        wen.setChecked(true);
                        break;
                    case "thu":
                        thu.setChecked(true);
                        break;
                    case "fri":
                        fri.setChecked(true);
                        break;
                }


            }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Select days");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                days.clear();
                setSpecificDays(sat);
                setSpecificDays(sun);
                setSpecificDays(mon);
                setSpecificDays(tue);
                setSpecificDays(wen);
                setSpecificDays(thu);
                setSpecificDays(fri);
                String daysText = null;
                for (int k = 0; k < days.size(); k++) {
                    if (daysText == null)
                        daysText = days.get(k);
                    else
                        daysText = daysText + " , " + days.get(k);
                }
                if (daysText != null)
                    binding.addMedRBtnSpecificDays.setText("Specific days of the week : " + daysText);
            }
        });
        builder.show();
    }

    private void setSpecificDays(CheckBox ch) {
        if (ch.isChecked())
            days.add(ch.getText().toString());
    }

    void setDataToInputField(Medication medication) {
        binding.addMedEtMedName.setText(medication.getName());
        List<ReminderTime> times = new ArrayList<>();
        for (MedDetails m : medication.getMedDetails()) {
            timesArrayAdapter.add(new ReminderTime(Helper.convertLongToHours(m.getTime()),
                    Helper.convertLongToMinuets(m.getTime()), m.getDose()));
        }
        adapter.notifyDataSetChanged();
        binding.addMedEtStrength.setText(medication.getMidStrength() + "");
        binding.addMedNoPillToRemind.setText(medication.getRefillNo() + "");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binding.addMedScheduleStartDate.setText(format.format(medication.getStartDate()));

        if (medication.getAllDays() == 1)
            binding.addMedRBtnEveryDay.setChecked(true);
        else {
            binding.addMedRBtnSpecificDays.setChecked(true);
        }
        if(medication.getTotalPills()>0)
            binding.addMedPresAmountOfMedEt.setText(medication.getTotalPills()+"");

        switch (medication.getTimeToFood()) {
            case "After":
                binding.addMedInstrRaBtnAfter.setChecked(true);
                break;
            case "Before":
                binding.addMedInstrRaBtnBefore.setChecked(true);
                break;
            case "While":
                binding.addMedInstrRaBtnWhile.setChecked(true);
                break;
            case "Doesn't":
                binding.addMedInstrRaBtnDoesnt.setChecked(true);
                break;
        }
    }

    void showBackDialog()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure to exit ?").setPositiveButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();

    }

}