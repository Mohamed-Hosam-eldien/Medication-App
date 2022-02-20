package com.example.medicationapp.requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    Context context;
    List<Request> list;
    public RequestsAdapter(Context context, List list){
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSenderName;
        TextView txtDependentName;
        TextView txtDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtDescription = itemView.findViewById(R.id.txtnotifBody);
            txtDependentName = itemView.findViewById(R.id.txtPersonName);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recycler, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recycler.getContext());
        View generatedRow = layoutInflater.inflate(R.layout.requests_custom_row, recycler, false);
        ViewHolder viewHolder = new ViewHolder(generatedRow);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSenderName.setText(list.get(position).getSenderName());
        holder.txtDescription.setText(list.get(position).getRequestText());
        holder.txtDependentName.setText(list.get(position).getDependentName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
