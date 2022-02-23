package com.example.medicationapp.medications.view.displayMedication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.database.LocalDB;
import com.example.medicationapp.databinding.ActivityDisplayMedicationBinding;
import com.example.medicationapp.databinding.DialogRefillBinding;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.medications.view.addEditMed.AddEditActivity;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.repository.Repository;
import com.example.medicationapp.utils.Helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DisplayMedicationActivity extends AppCompatActivity {

    TextView tvDays, tvRefill, tvLastTime, tvReasonOfTaking;
    Medication med;
    ActivityDisplayMedicationBinding binding;
    boolean isActive = true;
    DisplayPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDisplayMedicationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        tvDays = findViewById(R.id.showDrugTvRemindersDays);
        tvRefill = findViewById(R.id.showDrugTvPrescriptionRefill);
        tvLastTime = findViewById(R.id.showDrugTvLastTime);
        tvReasonOfTaking = findViewById(R.id.showDrugTvReasonOfTaking);

        presenter = new DisplayPresenter(DisplayMedicationActivity.this);

        binding.showDrugBtnRefill.setVisibility(View.GONE);


        Intent in = getIntent();
        Bundle b = in.getBundleExtra("bundle");
        med = (Medication) b.getParcelable("med");

        Toolbar toolbar = findViewById(R.id.displayToolbar);
        toolbar.setTitle(med.getName());
        toolbar.setLogo(R.drawable.gmail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        showData(med);

        if (med.getIsActive() == 1) {
            isActive = true;
            binding.showDrugBtnSuspend.setText("Suspend");
        }
        else
        {
            isActive=false;
            binding.showDrugBtnSuspend.setText("Active");
        }


        binding.showDrugBtnSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DisplayMedicationActivity.this, ""+med.getName()+" "+med.getIsActive(), Toast.LENGTH_SHORT).show();
                if (isActive) {
                    binding.showDrugBtnSuspend.setText("Active");
                    presenter.updateActive(0,med.getName());
                    isActive = false;
                } else {
                    binding.showDrugBtnSuspend.setText("Suspend");
                    presenter.updateActive(1,med.getName());
                    isActive = true;
                }
            }
        });

        binding.showDrugBtnRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRefillDialog();
            }
        });

    }

    private void setTimeToTextView(String txt, int pill) {
        binding.showDrugTvTime.setText(binding.showDrugTvTime.getText().toString() + txt + " take " + pill + " pill(s)\n");
    }

    private void setDayToTextView(int i, List<String> days) {
        if (i == 1)
            tvDays.setText("Every day");
        else {
            String txt = "";
            for (String d : days)
                txt = txt + d + " ,";
            tvDays.setText(txt);

        }

    }

    private void setTextToHowtoUseTv(String txt) {
        if (!txt.equals("Doesn't"))
            binding.showDrugTvHowToUse.setText(txt + " food");
        else binding.showDrugTvHowToUse.setText("Doesn't matter");
    }

    private void setTextToPrescriptionTV(int totalPills, int noPillToRemind) {
//        "Rx number : "+totalPills+
        tvRefill.setText(
                " When i have " + noPillToRemind + " pills");
    }

    private void setTextToLastTimeTv(String last) {
        tvLastTime.setText("Last time token : " + last);

    }

    private void setTextToReasonOfTaking(String reason) {
        tvReasonOfTaking.setText("Reason of Taking \n" + reason);
    }

    private void showData(Medication medication) {
        for (MedDetails med : medication.getMedDetails()) {
            String time = Helper.convertLongToHours(med.getTime()) + " : " +
                    Helper.convertLongToMinuets(med.getTime());
            setTimeToTextView(time, med.getDose());
        }
        setDayToTextView(medication.getAllDays(), medication.getDays());
        setTextToPrescriptionTV(0, medication.getRefillNo());
        setTextToHowtoUseTv(medication.getTimeToFood());
        binding.showDrugStartDate.setText(medication.getStartDate());
        binding.showDrugStrength.setText(medication.getMidStrength() + "");
        binding.displayCurrentPillsTv.setText(medication.getTotalPills() + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.displayMenuDelete:
                showDeleteDialog();
                break;
            case R.id.displayMenuEdit:
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra("comeFrom", 3);
                Bundle bundle = new Bundle();
                bundle.putParcelable("med", med);
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 3);

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    void showDeleteDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure to delete ?").setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                presenter.deleteMedication(med);
                Toast.makeText(DisplayMedicationActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3)
            if (resultCode == 6) {
                Bundle b = data.getBundleExtra("bundle");
                showData(b.getParcelable("med"));
            }
    }

    void showRefillDialog()
    {
        View v= LayoutInflater.from(this).inflate(R.layout.dialog_refill,null);
        DialogRefillBinding refillBinding=DialogRefillBinding.inflate(getLayoutInflater());
        TextView tvMinus=v.findViewById(R.id.dialogRefillBtnMinus);
        TextView tvPlus=v.findViewById(R.id.dialogRefillBtnPlus);
        EditText editText=v.findViewById(R.id.dialogRefillEt);
        editText.setText(med.getTotalPills()+"");
        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(editText.getText().toString())>=2)
                    editText.setText(Integer.parseInt(editText.getText().toString())-1+"");
            }
        });
        tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(Integer.parseInt(editText.getText().toString())+1+"");
            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Refill you medicine ");
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!(Integer.parseInt(editText.getText().toString())<0&&editText.getText().toString().equals("")))
                    presenter.refill(Integer.parseInt(editText.getText().toString()),med.getName());
            }
        });

        builder.show();


    }


}