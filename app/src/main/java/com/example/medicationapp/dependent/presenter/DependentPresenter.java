package com.example.medicationapp.dependent.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.medicationapp.connection.Connection;
import com.example.medicationapp.connection.GetAllMedication;
import com.example.medicationapp.connection.NetworkInterface;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;
import com.example.medicationapp.repository.Repository;
import com.example.medicationapp.requests.view.RequestsFragment;
import com.example.medicationapp.utils.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DependentPresenter implements NetworkInterface {

    Connection connection;
    GetAllMedication getAllMedication;
    Context context;
    FirebaseDatabase database;
    DatabaseReference reference;



    public DependentPresenter(GetAllMedication getAllMedication, Context context) {
        this.connection = Connection.getInstance(this, context);
        this.getAllMedication = getAllMedication;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference(Common.Request);
    }

    @Override
    public void onSendRequest(Request request) {

    }

    public void getPatientMed() {
        connection.receiveMedication();
    }

    @Override
    public void onReceiveMedication(List<Request> list) {
        getAllMedication.getMedicationList(list);
    }

    @Override
    public void onSaveUserData(User user) {

    }

    @Override
    public void onSendMedicine(List<Medication> medications, String requestId) {
       String id= Paper.book().read(RequestsFragment.ACEPTED_REQUEST_ID,"null");
       List<Medication>medicationList=new ArrayList<>();
        if(!(id.equals("null")||id.equals("")||id==null))
            reference.child(id).child("medicationList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    medicationList.clear();
                    for(DataSnapshot s:snapshot.getChildren())
                        medicationList.add(s.getValue(Medication.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

}
