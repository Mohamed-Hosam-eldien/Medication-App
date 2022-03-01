package com.example.medicationapp.requests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.requests.view.AcceptedRequestRow;
import com.example.medicationapp.requests.view.OnRequestClick;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList <Request> requestList;
    Context context;
    OnRequestClick onRequestClick;

    public RequestAdapter(Context context, OnRequestClick onRequestClick, ArrayList<Request> requestList) {
        this.context = context;
        this.onRequestClick = onRequestClick;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType !=1) {
            View view = LayoutInflater.from(context).inflate(R.layout.requests_custom_row, null);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.accepted_layout, null);
            return new AcceptedRequestRow(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(requestList.get(position).isRequest()){
                AcceptedRequestRow acceptHolder = (AcceptedRequestRow) holder;
        }else{
            ViewHolder myHolder = (ViewHolder) holder;
            myHolder.txtSenderName.setText(requestList.get(position).getSenderName());
            myHolder.txtRreceiverName.setText(requestList.get(position).getReceiverEmail());
            myHolder.notificationBody.setText(requestList.get(position).getDescription());
            myHolder.btnAccept.setOnClickListener(view -> {
                onRequestClick.onAcceptClick(requestList.get(position));
            });
            myHolder.btnReject.setOnClickListener(view -> {
                onRequestClick.onRejectClick(requestList.get(position));
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position).isRequest() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSenderName;
        TextView notificationBody;
        TextView txtRreceiverName;
        Button btnAccept;
        Button btnReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtRreceiverName = itemView.findViewById(R.id.txtPersonName);
            notificationBody = itemView.findViewById(R.id.txtnotifBody);
            btnAccept =  itemView.findViewById(R.id.btnAcceptRequestActivity);
            btnReject =  itemView.findViewById(R.id.btnReject);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<Request> requestList){
        this.requestList = requestList;
        notifyDataSetChanged();
    }
}
