package com.example.medicationapp.medications.view.addEditMed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.medications.view.addEditMed.model.ReminderTime;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditActivity extends AppCompatActivity {

    Toolbar toolbar;
    LocalDB localDB;
    EditText etMedName, etMedStrength, etTotalAmount, etAmountOfPillToRemind;
    RadioGroup instrRadioGroup, scheduleDurationGroup, scheduleDaysGroup;
    RadioButton raBtnNoDays, raBtnSpecificDays, raBtnEveryDay;
    int comeFrom, medStrength, noDays = 0, totalAmount = 0;
    Medication medication;
    MedDetails medDetails;
    Spinner spinner;
    RecyclerView rvReminderTimes;
    List<ReminderTime> timesArrayAdapter;
    List<String> days;
    ReminderTimesRecycleAdapter adapter;
    TextView tvStartDate;
    EditText edOtherInstruction;
    String takingInstruction = null, otherInstruction;
    Calendar calendarFroDate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);


        toolbar = findViewById(R.id.addToolbar);
        toolbar.setTitle("Add Medication");
        etMedName = findViewById(R.id.addMedEtMedName);
        etMedStrength = findViewById(R.id.addMedEtStrength);
        etTotalAmount = findViewById(R.id.addMedPresAmountOfMedEt);
        etAmountOfPillToRemind = findViewById(R.id.addMedNoPillToRemind);
        instrRadioGroup = findViewById(R.id.addMedInstrRadGro);
        spinner = findViewById(R.id.addMedReminderTimeSp);
        rvReminderTimes = findViewById(R.id.addMedReminderRv);
        tvStartDate = findViewById(R.id.addMedScheduleStartDate);
        edOtherInstruction = findViewById(R.id.addMedInstrEtOtherInstr);
        scheduleDurationGroup = findViewById(R.id.addMedDurationGroup);
        scheduleDaysGroup = findViewById(R.id.addMedDaysGroup);
        raBtnNoDays = findViewById(R.id.addMedRBtnNoDays);
        raBtnSpecificDays = findViewById(R.id.addMedRBtnSpecificDays);
        raBtnEveryDay = findViewById(R.id.addMedRBtnEveryDay);
        days = new ArrayList<>();


        instrRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarFroDate = showDateDialog();
            }
        });

        timesArrayAdapter = new ArrayList<>();
        adapter = new ReminderTimesRecycleAdapter(this, timesArrayAdapter, new OnAdapterClickListener() {
            @Override
            public void onCLick(ReminderTime time, int index) {
                showTimeDialog(time, index);
            }
        });


        raBtnNoDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoDaysDialog();
            }
        });


        raBtnSpecificDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpecificDaysDialog();
            }
        });

        rvReminderTimes.setLayoutManager(new LinearLayoutManager(this));
        rvReminderTimes.setAdapter(adapter);
        rvReminderTimes.setHasFixedSize(true);
        medDetails = new MedDetails();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Intent intent = getIntent();
        comeFrom = intent.getIntExtra("comFrom", 3);
        if (comeFrom == 1)
            Toast.makeText(this, "Come from add", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Come from edit", Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        localDB = LocalDB.getInstance(this);
        switch (item.getItemId()) {
            case R.id.addEditMenuSave: {
                if (comeFrom == 1) {
                    // insertData(collectDataFromUser());

//                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                }
                collectDataFromUser();
            }
//            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void insertDataToDatabase(Medication medication) {
        localDB = LocalDB.getInstance(this);
        List<Medication> medications = new ArrayList<>();
        medications.add(medication);

    }

    private void collectDataFromUser() {
        medication = new Medication();

        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        String medName = null;
        int amount = -1;
        medName = etMedName.getText().toString();
        List<ReminderTime> reminderTimes = adapter.getAdapterList();
        if (calendarFroDate == null)
            Toast.makeText(this, "date null  ", Toast.LENGTH_SHORT).show();
        if (edOtherInstruction.getText().toString() != null)
            otherInstruction = edOtherInstruction.getText().toString();
        if (etMedStrength.getText().toString() != null && !etMedStrength.getText().toString().equals(""))
            medStrength = Integer.parseInt(etMedStrength.getText().toString());
        if (etAmountOfPillToRemind.getText().toString() != null && !etAmountOfPillToRemind.getText().toString().equals(""))
            amount = Integer.parseInt(etAmountOfPillToRemind.getText().toString());
        int everyDay = 0;
        if (raBtnEveryDay.isChecked())
            everyDay = 1;
        if (etTotalAmount.getText().toString() != null && !etTotalAmount.getText().toString().equals(""))
            totalAmount = Integer.parseInt(etTotalAmount.getText().toString());
        boolean allIsFull = true;
        if (medName == null || medName.equals("")) {
            allIsFull = false;
            etMedName.setError("Enter name");
            Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG).show();

        }
        if (reminderTimes.size() == 0) {
            allIsFull = false;
            Toast.makeText(this, "Please add time and pill(s) to remind you ", Toast.LENGTH_SHORT).show();

        }

        if (calendarFroDate == null) {
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
            etAmountOfPillToRemind.setError("Enter amount");
        }
        List<MedDetails> medDetails1=new ArrayList<>();

        if (allIsFull) {
            finish();
            medication.setName(medName);
            if (medStrength != 0)
                medication.setMidStrength(medStrength);
            medication.setTimeToFood(takingInstruction);
            medication.setRefillNo(amount);
            medication.setAllDays(everyDay);
            if(days.size()>0)
                medication.setDays(days);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            medication.setStartDate(sdf.format(calendarFroDate.getTime()));
            for (ReminderTime r:reminderTimes)
            {
                Calendar c=Calendar.getInstance();
                c.set(calendarFroDate.getTime()
                        .getYear(),calendarFroDate.getTime()
                        .getMonth(),calendarFroDate.getTime().getDay(),r.getHour(),r.getMinute());
                medDetails1.add(new MedDetails(c.getTimeInMillis(),r.getPill(),"pill",0));
            }
            medication.setMedDetails(medDetails1);
            Repository repo=Repository.getInstance(this);
            repo.insertMedication(medication);
            Toast.makeText(this, "size "+medication.getMedDetails().size(), Toast.LENGTH_SHORT).show();

        }
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

    private Calendar showDateDialog() {
        Calendar calendar = Calendar.getInstance();
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
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = sdf.format(calendar.getTime());
                tvStartDate.setText(dateString);
            }
        }).show();
        return calendar;
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
                raBtnNoDays.setText("number of day : " + noDays);
            }
        });
        builder.show();
    }

    private void showSpecificDaysDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_specific_days, null);
        CheckBox sat = v.findViewById(R.id.dialogChBoxSat);
        CheckBox sun = v.findViewById(R.id.dialogChBoxSun);
        CheckBox mon = v.findViewById(R.id.dialogChBoxMon);
        CheckBox tue = v.findViewById(R.id.dialogChBoxTue);
        CheckBox wen = v.findViewById(R.id.dialogChBoxWed);
        CheckBox thu = v.findViewById(R.id.dialogChBoxThu);
        CheckBox fri = v.findViewById(R.id.dialogChBoxFri);
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
                    raBtnSpecificDays.setText("Specific days of the week : " + daysText);
            }
        });
        builder.show();
    }

    private void setSpecificDays(CheckBox ch) {
        if (ch.isChecked())
            days.add(ch.getText().toString());
    }


}