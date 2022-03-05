package com.example.medicationapp.requests.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Helper;
import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Request> requestList;
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
        if (viewType != 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.requests_custom_row, null);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.accepted_layout, null);
            return new AcceptedRequestRow(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (requestList.get(position).isRequest()) {
            AcceptedRequestRow acceptHolder = (AcceptedRequestRow) holder;
        } else {
            ViewHolder myHolder = (ViewHolder) holder;
            myHolder.txtSenderName.setText(requestList.get(position).getSenderName());
            myHolder.txtRreceiverName.setText(requestList.get(position).getSenderEmail());
            myHolder.notificationBody.setText(requestList.get(position).getDescription());
            myHolder.btnAccept.setOnClickListener(view -> {
                if (Helper.isNetworkAvailable(context)) {
                    onRequestClick.onAcceptClick(requestList.get(position));
                } else {
                    Toast.makeText(context, "You are not connected", Toast.LENGTH_SHORT).show();
                }
            });
            myHolder.btnReject.setOnClickListener(view -> {
                if (Helper.isNetworkAvailable(context)) {
                    showBackDialog(position);
                } else {
                    Toast.makeText(context, "You are not connected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void showBackDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Are you sure to Reject ?").setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onRequestClick.onRejectClick(requestList.get(position));
            }
        });
        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });

        dialog.show();

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
        CardView btnAccept;
        CardView btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtRreceiverName = itemView.findViewById(R.id.txtPersonName);
            notificationBody = itemView.findViewById(R.id.txtnotifBody);
            btnAccept = itemView.findViewById(R.id.btnCardAccept);
            btnReject = itemView.findViewById(R.id.btnCardReject);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setArrayList(ArrayList<Request> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
    }
}
