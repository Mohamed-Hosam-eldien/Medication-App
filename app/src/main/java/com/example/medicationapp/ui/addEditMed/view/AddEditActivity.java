package com.example.medicationapp.ui.addEditMed.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.local.LocalDB;
import com.example.medicationapp.databinding.ActivityAddEditBinding;
import com.example.medicationapp.ui.addEditMed.model.ReminderTime;
import com.example.medicationapp.ui.addEditMed.presenter.AddEditPresenter;
import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.ui.requests.view.RequestsFragment;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.example.medicationapp.utils.TimerWorker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class AddEditActivity extends AppCompatActivity {

    private ActivityAddEditBinding binding;
    private LocalDB localDB;
    private int comeFrom, medStrength, noDays = 0, totalAmount = 0;
    private Medication medication;
    private MedDetails medDetails;
    private List<ReminderTime> timesArrayAdapter;
    private List<String> days;
    private long[] listOfTimes;
    private ReminderTimesRecycleAdapter adapter;
    private String takingInstruction = null, otherInstruction;
    private Calendar calendarFroDate = null;
    private AddEditPresenter presenter;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String requestId;
    private boolean isDateSaved = false;
    private boolean isTotalPillGreaterThanNoPillToRemind = false;
    private String reqId;
    private ArrayList<Integer> images;
    private int image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Paper.init(this);

        images = new ArrayList<>();

        images.add(R.drawable.p5);
        images.add(R.drawable.p9);
        images.add(R.drawable.p7);
        images.add(R.drawable.p3);
        images.add(R.drawable.p4);
        images.add(R.drawable.p2);
        images.add(R.drawable.p11);

        ImageAdapter imageAdapter = new ImageAdapter(images, this, index -> image = index);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.addMedRvMedIcon.setLayoutManager(layoutManager);
        binding.addMedRvMedIcon.setHasFixedSize(true);
        binding.addMedRvMedIcon.setAdapter(imageAdapter);

        presenter = new AddEditPresenter(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(Common.Request);
        reqId = Paper.book().read(RequestsFragment.ACEPTED_REQUEST_ID, "null");

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
        if (comeFrom == 5)//5 update offline, 6 update firebase
        {
            medication = intent.getBundleExtra("bundle").getParcelable("med");
            isDateSaved = true;
        }

        binding.addMedInstrRadGro.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.addMedInstrRaBtnAfter:
                    takingInstruction = "after eating";
                    break;
                case R.id.addMedInstrRaBtnBefore:
                    takingInstruction = "before eating";
                    break;
                case R.id.addMedInstrRaBtnWhile:
                    takingInstruction = "while eating";
                    break;
                case R.id.addMedInstrRaBtnDoesnt:
                    takingInstruction = "Doesn't Matter";
                    break;
            }
        });

        binding.addMedScheduleStartDate.setOnClickListener(view -> showDateDialog());

        adapter = new ReminderTimesRecycleAdapter(this, timesArrayAdapter, this::showTimeDialog);

        binding.addMedReminderRv.setLayoutManager(new LinearLayoutManager(this));
        binding.addMedReminderRv.setAdapter(adapter);
        binding.addMedReminderRv.setHasFixedSize(true);

        binding.addMedRBtnNoDays.setOnClickListener(view -> showNoDaysDialog());

        binding.addMedRBtnSpecificDays.setOnClickListener(view -> {
            if (comeFrom == 1) {
                manageSpecificDaysDialog(medication.getDays());
            } else
                manageSpecificDaysDialog(null);
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
            binding.addToolbar.setTitle(R.string.add_medication);
        else if (comeFrom == 5 || comeFrom == 6) {
            binding.addToolbar.setTitle(R.string.edit_medication);
            setDataToInputField(medication);
            binding.addMedEtMedName.setEnabled(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            noDays = medication.getAllDays();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                calendarFroDate.setTimeInMillis(medication.getStartDate());
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
        int totalPills = 0;

        if (validate1()) {
            medName = binding.addMedEtMedName.getText().toString();
            otherInstruction = binding.addMedInstrEtOtherInstr.getText().toString();

            totalPills = Integer.parseInt(binding.addMedCurrentPillsOfMedEt.getText().toString());

            reminderTimes = adapter.getAdapterList();
            if (!binding.addMedEtStrength.getText().toString().trim().equals(""))
                medStrength = Integer.parseInt(binding.addMedEtStrength.getText().toString());
            amount = Integer.parseInt(binding.addMedNoPillToRemind.getText().toString());
            totalAmount = Integer.parseInt(binding.addMedCurrentPillsOfMedEt.getText().toString());
            if (amount >= totalAmount) {
                isTotalPillGreaterThanNoPillToRemind = false;
                Toast.makeText(this, getString(R.string.number_of_remind), Toast.LENGTH_SHORT).show();
                binding.addMedNoPillToRemind.setError(getString(R.string.greater_than_pills));
                binding.addMedCurrentPillsOfMedEt.setError(getString(R.string.smaller_than_number));
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
            medication.setStartDate(removeAllHours(calendarFroDate));

            int i = 0;

            listOfTimes = new long[reminderTimes.size()];

            for (ReminderTime r : reminderTimes) {
                Calendar c = Calendar.getInstance();

                c.setTime(calendarFroDate.getTime());
                c.set(Calendar.HOUR_OF_DAY, r.getHour());
                c.set(Calendar.MINUTE, r.getMinute());

                Log.d("Calender Time2", c.getTimeInMillis() + "");

                medDetails1.add(new MedDetails(c.getTimeInMillis(), r.getPill(), "pill", 0));

                // work manager
                listOfTimes[i] = c.getTimeInMillis();
                i += 1;
            }

            medication.setMedDetails(medDetails1);
            if (image == 0)
                image = R.drawable.p5;
            medication.setImage(image);

            if ((comeFrom == 1 || comeFrom == 2) && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/)) {
                medication.setId(Helper.generateKey());
                insertIntoDatabase(medication);
                if (!(reqId.equals("null") || reqId.equals("") || reqId == null))
                    showFirebaseDialog(medication);
            } else if (comeFrom == 5 && (isTotalPillGreaterThanNoPillToRemind /*&& isAllTimeOk*/)) {

                updateDatabase(medication);
                if (!(reqId.equals("null") || reqId.equals("") || reqId == null)) {
                    showFirebaseDialog(medication);
                }
            }

            if (isTotalPillGreaterThanNoPillToRemind) {
                if ((reqId.equals("null") || reqId.equals("") || reqId == null)) {
                    finish();

                    WorkManager.getInstance(this).cancelAllWorkByTag(medName);

                    WorkRequest saveRequest =
                            new PeriodicWorkRequest.Builder(TimerWorker.class,
                                    24, TimeUnit.HOURS)
                                    .setInputData(
                                            new Data.Builder()
                                                    .putLongArray("times", listOfTimes)
                                                    .putString("medName", medName)
                                                    .putString("id", medication.getId())
                                                    .putInt("dose", medication.getMedDetails().get(0).getDose())
                                                    .putString("medFood", medication.getTimeToFood())
                                                    .build())
                                    .addTag(medName)
                                    .build();

                    Paper.book().write("IDMED", medication.getId());

                    WorkManager.getInstance(this).enqueue(saveRequest);
                }

            } else
                Toast.makeText(this, getString(R.string.number_of_reminder_cant), Toast.LENGTH_SHORT).show();

        }
    }

    boolean validate1() {
        if (!binding.addMedEtMedName.getText().toString().trim().equals(""))
            if (adapter.getAdapterList().size() > 0)
                if (isDateSaved) {
                    if (validate2())
                        return true;
                } else
                    Toast.makeText(this, getString(R.string.select_date_to_remind), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.select_times), Toast.LENGTH_SHORT).show();
        else {
            binding.addMedEtMedName.setError("is empty");
            Toast.makeText(this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
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
        presenter.insertMedication(medication);
        Toast.makeText(this, "insert successfully", Toast.LENGTH_SHORT).show();
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
        ref.child(reqId).child("medicationList").child(medication.getId()).setValue(medication);
    }

    void updateFirebase(Medication medication) {
        ref.child(requestId).child("medicationList").child(medication.getId()).setValue(medication);
    }

    private long removeAllHours(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
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
        btnPlus.setOnClickListener(view12 -> {
            if (Integer.parseInt(etAmout.getText().toString()) < 5)
                etAmout.setText((Integer.parseInt(etAmout.getText().toString()) + 1) + "");
        });

        btnMinus.setOnClickListener(view1 -> {
            if (Integer.parseInt(etAmout.getText().toString()) > 1)
                etAmout.setText((Integer.parseInt(etAmout.getText().toString()) - 1) + "");
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(R.string.when_take_dose).
                setNegativeButton(R.string.cancel, (dialogInterface, i) -> {

                }).setPositiveButton(R.string.save, (dialogInterface, i) -> {
            ReminderTime reminderTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                reminderTime = new ReminderTime(timePicker.getHour(), timePicker.getMinute(), Integer.parseInt(etAmout.getText().toString()));
            }
            adapter.updateList(reminderTime, index);
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
        builder.setView(view).setTitle(R.string.set_start_date).setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})

                .setPositiveButton(R.string.save, (dialogInterface, i) -> {
                    Calendar calendar = Calendar.getInstance();
                    if (datePicker.getYear() < calendar.getTime().getYear() || datePicker.getMonth() < calendar.getTime().getMonth() || datePicker.getDayOfMonth() < calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                        Toast.makeText(AddEditActivity.this, "You can't choose date in past", Toast.LENGTH_SHORT).show();
                        binding.addMedScheduleStartDate.setText(getString(R.string.press_to_add_date));
                        isDateSaved = false;
                    } else {
                        calendarFroDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        Log.i("TAG", "onClick: dialog " + calendarFroDate.getTime().getDay() + "time" + calendarFroDate.getTimeInMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = sdf.format(calendarFroDate.getTime());
                        binding.addMedScheduleStartDate.setText(dateString);
                        isDateSaved = true;
                    }
                    calendarFroDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()); //calender and return calender

                    Log.d("Calender 4", calendarFroDate.getTimeInMillis() + "");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = sdf.format(calendarFroDate.getTime());
                    binding.addMedScheduleStartDate.setText(dateString);
                }).show();
    }

    private void showNoDaysDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_no_days, null);
        TextView btnPlus = v.findViewById(R.id.dialogNoDaysBtnPlus);
        TextView btnMinus = v.findViewById(R.id.dialogNoDaysBtnMinus);
        EditText etNoDays = v.findViewById(R.id.dialogNoDaysEtNumber);

        btnPlus.setOnClickListener(view -> etNoDays.setText((Integer.parseInt(etNoDays.getText().toString()) + 1) + ""));

        btnMinus.setOnClickListener(view -> {
            if (Integer.parseInt(etNoDays.getText().toString()) > 1)
                etNoDays.setText((Integer.parseInt(etNoDays.getText().toString()) - 1) + "");
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Set number of days (from start date)");
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });
        builder.setPositiveButton("Set", (dialogInterface, i) -> {
            noDays = Integer.parseInt(etNoDays.getText().toString());
            binding.addMedRBtnNoDays.setText("number of day : " + noDays);
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
        builder.setNegativeButton("cancel", (dialogInterface, i) -> {
        });

        builder.setPositiveButton("Set", (dialogInterface, i) -> {
            days.clear();
            setSpecificDays(sat);
            setSpecificDays(sun);
            setSpecificDays(mon);
            setSpecificDays(tue);
            setSpecificDays(wen);
            setSpecificDays(thu);
            setSpecificDays(fri);
            setDaysNameToRadioBtnSpecificDays();
        });
        builder.show();
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

    private void setSpecificDays(CheckBox ch) {
        if (ch.isChecked())
            days.add(ch.getText().toString());
    }

    void setDataToInputField(Medication medication) {
        binding.addMedEtMedName.setText(medication.getName());
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
        if (medication.getTotalPills() > 0)
            binding.addMedCurrentPillsOfMedEt.setText(medication.getTotalPills() + "");
        markInstructionRadioButtons(medication.getTimeToFood());
    }

    void markInstructionRadioButtons(String time) {
        switch (time) {
            case "after eating":
                binding.addMedInstrRaBtnAfter.setChecked(true);
                break;
            case "before eating":
                binding.addMedInstrRaBtnBefore.setChecked(true);
                break;
            case "while eating":
                binding.addMedInstrRaBtnWhile.setChecked(true);
                break;
            case "Doesn't Matter":
                binding.addMedInstrRaBtnDoesnt.setChecked(true);
                break;
        }
    }

    void showBackDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.sure_to_exist).setPositiveButton(getString(R.string.exist), (dialogInterface, i) -> finish());
        dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {});
        dialog.show();
    }

    void showFirebaseDialog(Medication med1) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.want_to_save_health_taker)).setPositiveButton(R.string.save, (dialogInterface, i) -> {
            insertIntoFirebase(med1);
            Toast.makeText(AddEditActivity.this, "saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        dialog.setNegativeButton(R.string.no, (dialogInterface, i) -> insertIntoDatabase(med1));
        dialog.show();
    }

}