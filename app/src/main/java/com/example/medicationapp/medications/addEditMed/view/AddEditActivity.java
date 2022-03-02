package com.example.medicationapp.medications.addEditMed.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.databinding.ActivityAddEditBinding;
import com.example.medicationapp.medications.addEditMed.presenter.AddEditPresenter;
import com.example.medicationapp.medications.addEditMed.model.ReminderTime;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {
    ActivityAddEditBinding binding;
    LocalDB localDB;
    int comeFrom, medStrength, noDays = 0, totalAmount = 0;
    Medication medication;/*, medFroEdit;*/
    MedDetails medDetails;
    List<ReminderTime> timesArrayAdapter;
    List<String> days;
    ReminderTimesRecycleAdapter adapter;
    String takingInstruction = null, otherInstruction;
    Calendar calendarFroDate = null;
    AddEditPresenter presenter;
    FirebaseDatabase database;
    DatabaseReference ref;
    private FirebaseDatabase firebaseDatabase = null;
    private String requestId;
    private boolean isDateSaved = false, isAllTimeOk = false;
    private boolean isTotalPillGreaterThanNoPillToRemind = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new AddEditPresenter(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        days = new ArrayList<>();
        timesArrayAdapter = new ArrayList<>();
        medDetails = new MedDetails();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendarFroDate = Calendar.getInstance();
        }
        binding.addMedRBtnOngoing.setVisibility(View.GONE);
        binding.addMedRBtnNoDays.setVisibility(View.GONE);
        binding.addMedTextRg1.setVisibility(View.GONE);
        Intent intent = getIntent();
        comeFrom = intent.getIntExtra("comeFrom", 15);
        if (comeFrom == 5 || comeFrom == 6)//5 update offline, 6 update firebase
        {
            medication = intent.getBundleExtra("bundle").getParcelable("med");
            isDateSaved = true;
        }
        if (comeFrom == 3)// 3 insert into firebase, 6 update firebase
            requestId = intent.getStringExtra("requestId");
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
                    manageSpecificDaysDialog(medication.getDays());
                } else
                    manageSpecificDaysDialog(null);
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
        if (comeFrom == 1 || comeFrom == 2 || comeFrom == 3)
            binding.addToolbar.setTitle("Add Medication");
        else if (comeFrom == 5 || comeFrom == 6) {
            binding.addToolbar.setTitle("Edit Medication");
            setDataToInputField(medication);
            binding.addMedEtMedName.setEnabled(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            noDays = medication.getAllDays();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    calendarFroDate.setTime(sdf.parse(medication.getStartDate()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        localDB = LocalDB.getInstance(this);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void collectDataFromUser() {
        List<ReminderTime> reminderTimes;
        List<MedDetails> medDetails1 = new ArrayList<>();
        if (comeFrom == 1 || comeFrom == 2 || comeFrom == 3)
            medication = new Medication();

        String medName = null;
        int amount = -1;
        int everyDay = 0;

        if (validate1()) {
            medName = binding.addMedEtMedName.getText().toString();
            otherInstruction = binding.addMedInstrEtOtherInstr.getText().toString();
            reminderTimes = adapter.getAdapterList();
            if (!binding.addMedEtStrength.getText().toString().trim().equals(""))
                medStrength = Integer.parseInt(binding.addMedEtStrength.getText().toString());
            amount = Integer.parseInt(binding.addMedNoPillToRemind.getText().toString());
            totalAmount = Integer.parseInt(binding.addMedCurrentPillsOfMedEt.getText().toString());
            if (amount >= totalAmount) {
                isTotalPillGreaterThanNoPillToRemind = false;
                Toast.makeText(this, "Number of to Remind can't be greater than Total pills", Toast.LENGTH_SHORT).show();
                binding.addMedNoPillToRemind.setError("greater than total pills");
                binding.addMedCurrentPillsOfMedEt.setError("Smaller than number of pills to remind");
            } else {
                isTotalPillGreaterThanNoPillToRemind = true;
                medication.setRefillNo(amount);
                medication.setTotalPills(totalAmount);
            }

            if (binding.addMedRBtnEveryDay.isChecked())
                everyDay = 1;

            medication.setName(medName);
            medication.setMidStrength(medStrength);
            medication.setTimeToFood(takingInstruction);
            medication.setAllDays(everyDay);
            medication.setDays(days);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            medication.setStartDate(sdf.format(calendarFroDate.getTime()));
            Calendar c = Calendar.getInstance();
            for (ReminderTime r : reminderTimes) {
//                Log.i("TAG", "collectDataFromUser: " + r.getHour() + " cal " + c.getTime().getHours());
//                if (r.getHour() >= c.getTime().getHours()) {
//                    if (r.getMinute() >= c.getTime().getMinutes()) {
                        isAllTimeOk = true;
                        c.set(calendarFroDate.getTime()
                                .getYear(), calendarFroDate.getTime()
                                .getMonth(), calendarFroDate.getTime().getDay(), r.getHour(), r.getMinute());
                        medDetails1.add(new MedDetails(c.getTimeInMillis(), r.getPill(), "pill", 0));
                        Log.i("TAG", "time: " + c.getTime().getHours() + " min " +
                                c.getTime().getMinutes() + "mill " + c.getTimeInMillis());
//                    }
//                } else {
//                    isAllTimeOk = false;
//                    break;
//                }
            }
//            if (isAllTimeOk)
                medication.setMedDetails(medDetails1);
            if ((comeFrom == 1 || comeFrom == 2) && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/)) {
                insertIntoDatabase(medication);
            } else if (comeFrom == 3 && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/))
                insertIntoFirebase(medication);
            else if (comeFrom == 5 && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/))
                updateDatabase(medication);
            else if (comeFrom == 6 && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/)) {
                updateFirebase(medication);
            }
//            if (isAllTimeOk)
                if (isTotalPillGreaterThanNoPillToRemind)
                    finish();
                else
                    Toast.makeText(this, "Number of to Remind can't be greater than Total pills", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(this, "There is a time in the past ", Toast.LENGTH_SHORT).show();

        }
    }

    boolean validate1() {
        if (!binding.addMedEtMedName.getText().toString().trim().equals(""))
            if (adapter.getAdapterList().size() > 0)
                if (isDateSaved) {
                    if (validate2())
                        return true;
                } else
                    Toast.makeText(this, "Please select date to remind you", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Please select time(s) to remind you", Toast.LENGTH_SHORT).show();
        else {
            binding.addMedEtMedName.setError("is empty");
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    boolean validate2() {
        if (binding.addMedRBtnEveryDay.isChecked() || binding.addMedRBtnSpecificDays.isChecked())
            if (binding.addMedInstrRadGro.getCheckedRadioButtonId() != -1)
                if (!binding.addMedCurrentPillsOfMedEt.getText().toString().trim().equals(""))
                    if (!binding.addMedNoPillToRemind.getText().toString().trim().equals(""))
                        return true;
                    else {
                        binding.addMedNoPillToRemind.setError("is empty");
                        Toast.makeText(this, "Number of pill to remind you can't be empty", Toast.LENGTH_SHORT).show();
                    }
                else {
                    binding.addMedCurrentPillsOfMedEt.setError("is empty");
                    Toast.makeText(this, "Current pill you have can't be empty", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(this, "Please select when you want to take your medicine", Toast.LENGTH_SHORT).show();
            }
        else {
            Toast.makeText(this, "Please select day(s) to remind you", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    void insertIntoDatabase(Medication medication) {
        medication.setId(Helper.generateKey());
        presenter.insertMedication(medication);
        Toast.makeText(this, "insert", Toast.LENGTH_SHORT).show();
    }

    void updateDatabase(Medication medication) {
        presenter.updateMedication(medication);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("med", medication);
        intent.putExtra("bundle", bundle);
        setResult(6, intent);
        Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
    }

    void insertIntoFirebase(Medication medication) {
        medication.setId(Helper.generateKey());
        ref.child(Common.Request).child(requestId).child("medicationList").child(medication.getId()).setValue(medication);
    }

    void updateFirebase(Medication medication) {
        ref.child(Common.Request).child(requestId).child("medicationList").child(medication.getId()).setValue(medication);
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
                if (Integer.parseInt(etAmout.getText().toString()) < 5)
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
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker_view, null);
        DatePicker datePicker = view.findViewById(R.id.addDatePicker);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        builder.setView(view).setTitle("Set start date").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Calendar calendar = Calendar.getInstance();
                if (datePicker.getYear() < calendar.getTime().getYear() || datePicker.getMonth() < calendar.getTime().getMonth() || datePicker.getDayOfMonth() < calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                    Toast.makeText(AddEditActivity.this, "You can't choose date in past", Toast.LENGTH_SHORT).show();
                    binding.addMedScheduleStartDate.setText("Press to add date ");
                    isDateSaved = false;
                } else {
                    calendarFroDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    Log.i("TAG", "onClick: dialog " + calendarFroDate.getTime().getDay() + "time" + calendarFroDate.getTimeInMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = sdf.format(calendarFroDate.getTime());
                    binding.addMedScheduleStartDate.setText(dateString);
                    isDateSaved = true;
                }
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

    private void manageSpecificDaysDialog(List<String> daysFroEdit) {
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
                setDaysNameToRadioBtnSpecificDays();
            }
        });
        builder.show();
    }

    private void setSpecificDays(CheckBox ch) {
        if (ch.isChecked())
            days.add(ch.getText().toString());
    }

    void setDaysNameToRadioBtnSpecificDays() {
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

    void setDataToInputField(Medication medication) {
        binding.addMedEtMedName.setText(medication.getName());
        for (MedDetails m : medication.getMedDetails()) {
            timesArrayAdapter.add(new ReminderTime(Helper.convertLongToHours(m.getTime()),
                    Helper.convertLongToMinuets(m.getTime()), m.getDose()));
        }
        binding.addMedEtStrength.setText(medication.getMidStrength() + "");
        binding.addMedNoPillToRemind.setText(medication.getRefillNo() + "");
        binding.addMedScheduleStartDate.setText(medication.getStartDate());
        if (medication.getAllDays() == 1)
            binding.addMedRBtnEveryDay.setChecked(true);
        else {
            binding.addMedRBtnSpecificDays.setChecked(true);
        }
        if (medication.getTotalPills() > 0)
            binding.addMedCurrentPillsOfMedEt.setText(medication.getTotalPills() + "");
        markInstructionRadioButtons(medication.getTimeToFood());
    }

    void markInstructionRadioButtons(String time) {
        switch (time) {
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

    void markSpecificDaysCheckBox(List<String> days) {

    }

    void showBackDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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