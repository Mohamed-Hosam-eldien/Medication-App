package com.example.medicationapp.medications.addEditMed.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicationapp.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {

    ArrayList<Integer> images;
    Context context;
    OnImageAdapterListener listener;

    public ImageAdapter(ArrayList<Integer> images, Context context,OnImageAdapterListener listener) {
        this.images = images;
        this.context = context;
        this.listener=listener;

    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_image_row,null);

        return new Holder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageButton.setImageResource(images.get(position));
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                listener.onImageClick(images.get(holder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageButton imageButton;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageButton=itemView.findViewById(R.id.customImageButton);


        }
    }
}
