package com.example.medicationapp.medications.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Medication;

import java.util.List;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder> {

    List<Medication> activeMedicines;
//    List<Medication> suspendedMedicines;
    Context context;

    public void setActiveMedicines(List<Medication> activeMedicines) {
        this.activeMedicines = activeMedicines;
    }

    public ActiveAdapter(Context context, List<Medication>activeMedicines){
        this.activeMedicines = activeMedicines;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView medImage;
        TextView medicineName;
        TextView medDose;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View generatedRow) {
            super(generatedRow);
            medImage = generatedRow.findViewById(R.id.imgMed);
            medicineName = generatedRow.findViewById(R.id.txtViewMedName);
            medDose = generatedRow.findViewById(R.id.txtViewDose);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View generatedRow = layoutInflater.inflate(R.layout.custom_row, parent, false);
        ViewHolder vh= new ViewHolder(generatedRow);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.medicineName.setText(activeMedicines.get(position).getName());
        holder.medDose.setText(""+activeMedicines.get(position).getMidStrength());
        holder.medImage.setImageResource(activeMedicines.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return activeMedicines.size();
    }


}
