package com.example.medicationapp.home.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements  ClickToMed {

    private final List<Medication> medicationList;
    private final Context context;
    ShowBottomDialog bottomDialog;
    Medication medication;

    public HomeAdapter(List<Medication> medication , Context context, ShowBottomDialog bottomDialog) {
        this.medicationList = medication;
        this.context = context;
        this.bottomDialog = bottomDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_mid_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        medication = medicationList.get(position);
        holder.txtTime.setText(medication.getName());
        holder.txtTimeFood.setText(medication.getTimeToFood());

        Log.d("MMMM", medication.getMedDetails()+"");

        setDataToDose(medication.getMedDetails(),holder.recyclerView, medication);
    }

    private void setDataToDose(List<MedDetails> medDetails, RecyclerView recyclerView, Medication medication) {
        MedDetailsAdapter adapter = new MedDetailsAdapter(medDetails, medication, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    @Override
    public void showMedDetails(MedDetails details) {
        bottomDialog.showBottomDialog(details, medication); // handle it
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTimeFood;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTimeFood = itemView.findViewById(R.id.txtFoodTime);
            txtTime = itemView.findViewById(R.id.txtDrugName);
            recyclerView = itemView.findViewById(R.id.recyclerMedicine);
        }
    }
}
