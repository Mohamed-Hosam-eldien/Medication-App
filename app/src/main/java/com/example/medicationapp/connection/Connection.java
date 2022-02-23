package com.example.medicationapp.connection;

import androidx.annotation.NonNull;

import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Connection implements NetworkInterface{

    private static NetworkInterface networkInterface;
    private static FirebaseDatabase firebaseDatabase = null;
    private static Connection connection = null;
    GetAllMedication getAllMedication;

    private Connection(NetworkInterface networkInterface) {
        Connection.networkInterface = networkInterface;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static Connection getInstance(NetworkInterface networkInterface) {
        if(connection == null) {
            connection = new Connection(networkInterface);
        }
        return connection;
    }

    @Override
    public void onSendRequest(Request request) {
        firebaseDatabase.getReference(Common.Request)
                .push().setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Log.d("SUCCESS", "success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d("FAILURE", "failed");
            }
        });
    }

    @Override
    public void onReceiveMedication() {
        firebaseDatabase.getReference(Common.Request)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    final List<Request> requests = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap : snapshot.getChildren()) {
                            Request request = snap.getValue(Request.class);

                            if(request.isRequest() && request.getReceiverEmail().equals("mohamedhosameldien07@gmail.com")) {
                                requests.add(request);
                            }
                            networkInterface.onReceiveMedication();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
    }


}
