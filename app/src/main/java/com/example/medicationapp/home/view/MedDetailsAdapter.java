package com.example.medicationapp.home.view;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final Medication medication;
    ClickToMed clickToMed;

    public MedDetailsAdapter(List<MedDetails> medDetails, Medication medication, ClickToMed clickToMed) {
        this.medDetails = medDetails;
        this.medication = medication;
        this.clickToMed = clickToMed;
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

        holder.txtDrugDetails.setText("تناول " + details.getDose()
                +"جرعة");

        //clickToMed.showMedDetails(medication);

        holder.layout.setOnClickListener(v -> clickToMed.showMedDetails(details));

    }

    private String getRemainingTime(long time) {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, time);
    }

    @Override
    public int getItemCount() {
        return medDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtDrugDate, txtDrugDetails;
        private AdapterClickListener clickListener;
        RelativeLayout layout;

        public void setClickListener(AdapterClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            txtDrugDate = itemView.findViewById(R.id.txtDrugDate);
            txtDrugDetails = itemView.findViewById(R.id.txtDragDetails);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClickListener(v, getAdapterPosition());
        }
    }

}
