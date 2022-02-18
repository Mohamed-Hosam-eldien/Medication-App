package com.example.medicationapp.medications.view.displayMedication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medicationapp.R;

public class DisplayMedicationActivity extends AppCompatActivity {

    TextView tvDays,tvRefill,tvTime,tvHowToUse,tvLastTime,tvReasonOfTaking;
    Button btnSuspend,btnRefill,btnAddDose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medication);
        tvDays=findViewById(R.id.showDrugTvRemindersDays);
        tvRefill=findViewById(R.id.showDrugTvPrescriptionRefill);
        tvTime=findViewById(R.id.showDrugTvTime);
        tvHowToUse=findViewById(R.id.showDrugTvHowToUse);
        tvLastTime=findViewById(R.id.showDrugTvLastTime);
        tvReasonOfTaking=findViewById(R.id.showDrugTvReasonOfTaking);


        btnSuspend=findViewById(R.id.showDrugBtnSuspend);
        btnRefill=findViewById(R.id.showDrugBtnRefill);
        btnAddDose=findViewById(R.id.showDrugBtnAddDose);

        btnAddDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeToTextView("8.00 Am",1);
                setDayToTextView("Every day",10);
                setTextToHowtoUseTv("Before eating");
                setTextToPrescriptionTV(5,50,3);
                setTextToLastTimeTv("today 5.00 Am");
                setTextToReasonOfTaking("hav sdlkks kmk kdlaksd");

            }
        });
    }
    private void setTimeToTextView(String txt,int pill)
    {
        tvTime.setText(tvTime.getText().toString()+txt+" take "+pill+" pill(s)\n");

    }
    private void setDayToTextView(String txt,int daysNum)
    {
        tvDays.setText(txt+", take for "+daysNum+" days");

    }
    private void setTextToHowtoUseTv(String txt)
    {
        tvHowToUse.setText("How to use \n"+txt);
    }
    private void setTextToPrescriptionTV(int currentPill,int totalPills,int noPillToRemind)
    {
        tvRefill.setText("Pills left : "+currentPill+"\nRx number : "+totalPills+
                "\nRefill Reminder : When i have "+noPillToRemind+" pills");
    }
    private void setTextToLastTimeTv(String last)
    {
        tvLastTime.setText("Last time token : "+last);

    }
    private void setTextToReasonOfTaking(String reason)
    {
        tvReasonOfTaking.setText("Reason of Taking \n"+reason);
    }
}