package com.example.medicationapp.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.Date;
import java.util.List;

public class MedDetailsAdapter extends RecyclerView.Adapter<MedDetailsAdapter.ViewHolder>{

    private final List<MedDetails> medDetails;

    private final ClickToMed clickToMed;
    private final Medication medication;
    private final Context context;
    private final long currentDate;

    public MedDetailsAdapter(List<MedDetails> medDetails, Medication medication, ClickToMed clickToMed, Context context, long currentDate) {
        this.medDetails = medDetails;
        this.clickToMed = clickToMed;
        this.medication = medication;
        this.context = context;
        this.currentDate = currentDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_med_details_layout, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedDetails details = medDetails.get(position);

        holder.txtDrugDate.setText(getRemainingTime(details.getTime()));

        holder.txtDrugDetails.setText(context.getString(R.string.take) + " " + details.getDose()
                + " " + context.getString(R.string.dose) + " (" + medication.getTimeToFood() + ")");


        if(currentDate != -1) {
            if (details.getTaken() == 1 && getRemainingDate(details.getTime()).equals(getRemainingDate(currentDate))) {
                holder.img.setImageResource(R.drawable.ic_baseline_check_circle_24);
                holder.img.setVisibility(View.VISIBLE);
            }
        }
        Log.d("TAG", "onBindViewHolder: " +getRemainingDate(details.getTime()));
        Log.d("TAG", "onBindViewHolder: "+ details.getTaken());

        holder.txtDetails.setVisibility(medication.getInstruction()==null? View.GONE:View.VISIBLE);
        holder.txtDetails.setText(medication.getInstruction());

        holder.layout.setOnClickListener(v -> clickToMed.showMedDetails(details, medication, position));

    }

    private String getRemainingTime(long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, time);
    }

    private String getRemainingDate(long time) {
        String delegate = "yyyy-MM-dd";
        return (String) DateFormat.format(delegate, time);
    }

    @Override
    public int getItemCount() {
        return medDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtDrugDate, txtDrugDetails, txtDetails;
        private AdapterClickListener clickListener;
        RelativeLayout layout;
        ImageView img;

        public void setClickListener(AdapterClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            txtDrugDate = itemView.findViewById(R.id.txtDrugDate);
            txtDrugDetails = itemView.findViewById(R.id.txtDragDetails);
            img = itemView.findViewById(R.id.imgTaken);
            txtDetails = itemView.findViewById(R.id.txtDescription);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClickListener(v, getAdapterPosition());
        }
    }

}
