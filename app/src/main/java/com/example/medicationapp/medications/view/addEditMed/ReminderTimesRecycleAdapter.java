package com.example.medicationapp.medications.view.addEditMed;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;
import com.example.medicationapp.medications.view.addEditMed.model.ReminderTime;


import java.util.List;

public class ReminderTimesRecycleAdapter extends RecyclerView.Adapter<ReminderTimesRecycleAdapter.Holder> {

    List<ReminderTime> reminderTimes;
    Context context;
    OnAdapterClickListener onAdapterClickListener;

    public ReminderTimesRecycleAdapter(Context context,List<ReminderTime> reminderTimes,OnAdapterClickListener onAdapterClickListener) {
        this.context=context;
        this.onAdapterClickListener=onAdapterClickListener;
        this.reminderTimes=reminderTimes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.reminder_time_custom_item,null);
        return new Holder(v);
    }

    public void setReminderTimes(List<ReminderTime> reminderTimes) {
        this.reminderTimes = reminderTimes;
        notifyDataSetChanged();
    }
    public void updateList(ReminderTime reminderTime,int index)
    {
        reminderTimes.set(index,reminderTime);
        notifyDataSetChanged();

    }

    public ReminderTimesRecycleAdapter(List<ReminderTime> reminderTimes) {
        this.reminderTimes = reminderTimes;
    }
    public List<ReminderTime> getAdapterList()
    {
        return reminderTimes;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
       holder.tvTime.setText(parseTime(reminderTimes.get(position).getHour(),reminderTimes.get(position).getMinute()));
       holder.tvPill.setText("Take "+reminderTimes.get(position).getPill()+" pill(s)");
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onAdapterClickListener.onCLick(reminderTimes.get(holder.getAdapterPosition()), holder.getAdapterPosition());
           }
       });

    }

    private String parseTime(int hour,int min)
    {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return hour+" : "+min+" "+format;

    }


    @Override
    public int getItemCount() {
        return reminderTimes.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvTime,tvPill;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTime=itemView.findViewById(R.id.customReminderTvTime);
            tvPill=itemView.findViewById(R.id.customReminderTvPill);

        }
    }


}
