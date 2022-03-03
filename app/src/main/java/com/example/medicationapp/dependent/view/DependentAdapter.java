package com.example.medicationapp.dependent.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.HomeAdapter;
import com.example.medicationapp.home.view.ShowBottomDialog;
import com.example.medicationapp.model.MedDetails;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class DependentAdapter extends RecyclerView.Adapter<DependentAdapter.ViewHolder> implements ShowBottomDialog {

    Context context;
    List<Request> list;
    ShowBottomDialog bottomDialog;
    Request request;
    List<Medication>medications;


    public DependentAdapter(Context context, List<Request> list){
        this.list = list;
        this.context = context;

    }

    public DependentAdapter(Context context, List<Request> list, List<Medication> medications) {
        this.context = context;
        this.list = list;
        this.medications = medications;
    }

    @Override
    public void showMedDialog(MedDetails detail, Medication medication) {
        Toast.makeText(context, "asd", Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPateintName,txtTimeFood,txtTime;
        RecyclerView recyclerView;
        ImageView img;
        ExpandableLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPateintName = itemView.findViewById(R.id.textPatientName);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            img = itemView.findViewById(R.id.dropDown);
            //txtTimeFood = itemView.findViewById(R.id.txtFoodTimeDependent);
            //txtTime = itemView.findViewById(R.id.txtDrugNameDependent);
            recyclerView = itemView.findViewById(R.id.recyclerMedicineDependent);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recycler, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recycler.getContext());
        View generatedRow = layoutInflater.inflate(R.layout.dependent_layout, recycler, false);
        return new ViewHolder(generatedRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        request = list.get(position);
        holder.txtPateintName.setText(request.getPatientName());

        holder.img.setOnClickListener(v -> {
            if(holder.expandableLayout.isExpanded()) {
                holder.expandableLayout.collapse();
                holder.img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            } else {
                holder.expandableLayout.expand();
                holder.img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            }
        });

        HomeAdapter adapter = new HomeAdapter(medications, context, this);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter);
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
