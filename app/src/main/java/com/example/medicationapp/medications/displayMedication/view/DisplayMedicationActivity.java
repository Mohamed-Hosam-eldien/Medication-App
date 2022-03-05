package com.example.medicationapp.medications.displayMedication.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.ActivityDisplayMedicationBinding;
import com.example.medicationapp.databinding.DialogRefillBinding;
import com.example.medicationapp.medications.addEditMed.view.AddEditActivity;
import com.example.medicationapp.medications.displayMedication.presenter.DisplayPresenter;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.requests.view.RequestsFragment;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import io.paperdb.Paper;

public class DisplayMedicationActivity extends AppCompatActivity {

    TextView tvDays, tvRefill, tvLastTime, tvReasonOfTaking;
    Medication med;
    ActivityDisplayMedicationBinding binding;
    boolean isActive = true;
    DisplayPresenter presenter;
    FirebaseDatabase database;
    DatabaseReference ref;
    String reqId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDisplayMedicationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        tvDays = findViewById(R.id.showDrugTvRemindersDays);
        tvRefill = findViewById(R.id.showDrugTvPrescriptionRefill);
        tvLastTime = findViewById(R.id.showDrugTvLastTime);
        tvReasonOfTaking = findViewById(R.id.showDrugTvReasonOfTaking);

        binding.showDrugTvReasonOfTaking.setVisibility(View.GONE);
        binding.showDrugTvOtherInstruction.setVisibility(View.GONE);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference(Common.Request);
        reqId = Paper.book().read(RequestsFragment.ACEPTED_REQUEST_ID, "null");

        presenter = new DisplayPresenter(DisplayMedicationActivity.this);

        Intent in = getIntent();
        Bundle b = in.getBundleExtra("bundle");
        med = b.getParcelable("med");

        toolbar = findViewById(R.id.displayToolbar);
        toolbar.setTitle("");

        binding.toolbarImage.setImageResource(med.getImage());
        binding.toolbarTextView.setText(med.getName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        showData(med);

        if (med.getIsActive() == 1) {
            isActive = true;
            binding.showDrugBtnSuspend.setText("Suspend");
        } else {
            isActive = false;
            binding.showDrugBtnSuspend.setText("Active");
        }


        binding.showDrugBtnSuspend.setOnClickListener(view -> {
            if (isActive) {
                binding.showDrugBtnSuspend.setText("Active");
                med.setIsActive(0);
                presenter.updateActive(0, med.getId());
                isActive = false;
                if (!(reqId.equals("null") || reqId.equals("") || reqId == null))
                    showFirebaseDialog(med, 1);
            } else {
                binding.showDrugBtnSuspend.setText("Suspend");
                presenter.updateActive(1, med.getId());
                med.setIsActive(1);
                isActive = true;
                if (!(reqId.equals("null") || reqId.equals("") || reqId == null))
                    showFirebaseDialog(med, 1);
            }
        });

        binding.showDrugBtnRefill.setOnClickListener(view -> showRefillDialog());

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
        tvRefill.setText(" When i have " + noPillToRemind + " pills");
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binding.showDrugStartDate.setText(format.format(medication.getStartDate()));

        binding.showDrugStrength.setText(medication.getMidStrength() + "");
        binding.displayCurrentPillsTv.setText(medication.getTotalPills() + "");
        toolbar.setTitle("");
        binding.toolbarImage.setImageResource(medication.getImage());
        binding.toolbarTextView.setText(medication.getName());

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
                intent.putExtra("comeFrom", 5);
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
        dialog.setTitle("Are you sure to delete ?").setPositiveButton("delete", (dialogInterface, i) -> {

            presenter.deleteMedication(med);
            if (!(reqId.equals("null") || reqId.equals("") || reqId == null))
                showFirebaseDialog(med, 3);

            Toast.makeText(DisplayMedicationActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
        dialog.setNegativeButton("cancel", (dialogInterface, i) -> {});

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

    void showRefillDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_refill, null);
        DialogRefillBinding refillBinding = DialogRefillBinding.inflate(getLayoutInflater());
        TextView tvMinus = v.findViewById(R.id.dialogRefillBtnMinus);
        TextView tvPlus = v.findViewById(R.id.dialogRefillBtnPlus);
        EditText editText = v.findViewById(R.id.dialogRefillEt);
        editText.setText(med.getTotalPills() + "");
        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(editText.getText().toString()) >= 2)
                    editText.setText(Integer.parseInt(editText.getText().toString()) - 1 + "");
            }
        });
        tvPlus.setOnClickListener(view -> editText.setText(Integer.parseInt(editText.getText().toString()) + 1 + ""));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Refill you medicine ");
        builder.setPositiveButton("save", (dialogInterface, i) -> {
            if (!(Integer.parseInt(editText.getText().toString()) < 0 && editText.getText().toString().trim().equals(""))) {
                med.setTotalPills(Integer.parseInt(editText.getText().toString()));
                presenter.refill(Integer.parseInt(editText.getText().toString()), med.getId());
                showFirebaseDialog(med, 2);
                binding.displayCurrentPillsTv.setText(Integer.parseInt(editText.getText().toString())+"");
            }
        });

        builder.show();
    }

    void showFirebaseDialog(Medication med1, int k) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String text,btnText;
        if (k == 1 || k == 2)
        {
            text = "Do you want to save to health taker";
            btnText="save";
        }
        else{
            btnText="delete";
            text = "Do you want to delete from health taker";}
        dialog.setTitle(text).setPositiveButton("save", (dialogInterface, i) -> {
            if (k == 1)
                ref.child(reqId).child("medicationList").child(med1.getId()).child("isActive").setValue(med1.getIsActive());
            else if (k == 2)
                ref.child(reqId).child("medicationList").child(med1.getId()).child("totalPills").setValue(med1.getTotalPills());
            else {
                ref.child(reqId).child("medicationList").child(med.getId()).removeValue();
                finish();
            }
        });
        dialog.setNegativeButton("cancel", (dialogInterface, i) -> {
            if(k==3)
                finish();
        });
        dialog.show();
    }


}