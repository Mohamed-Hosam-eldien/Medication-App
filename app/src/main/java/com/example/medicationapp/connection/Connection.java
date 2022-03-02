package com.example.medicationapp.connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.medicationapp.home.view.HomeMedFragment;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;
import com.example.medicationapp.utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


public class Connection {

    private static NetworkInterface networkInterface;
    private static FirebaseDatabase firebaseDatabase = null;
    private static Connection connection = null;
    GetAllMedication getAllMedication;
    Context context;

    private Connection(NetworkInterface networkInterface, Context context) {
        Connection.networkInterface = networkInterface;
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.context = context;
    }

    public static Connection getInstance(NetworkInterface networkInterface, Context context) {
        if(connection == null) {
            connection = new Connection(networkInterface, context);
        }
        return connection;
    }

    public void sendRequest(Request request) {
        firebaseDatabase.getReference(Common.Request)
                .child(request.getId()).setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Request has been sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILURE", "failed : "+ e.getMessage());
            }
        });
    }

    public void receiveMedication() {

        firebaseDatabase.getReference(Common.Request)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    final List<Request> requests = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot sanp : snapshot.getChildren()) {
                            Request request = sanp.getValue(Request.class);

                            if(request.isRequest() && request.getReceiverEmail().equals(Paper.book().read(Common.emailUserPaper))) {
                                requests.add(request);
                            }
                            networkInterface.onReceiveMedication(requests);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
    }

    public void saveUserToFirebase(User user) {
        Log.d("TAG", user.getName());
        Log.d("TAG", user.getEmail());
        Log.d("TAG", user.getUid());
        firebaseDatabase.getReference(Common.User)
                .child(user.getUid())
                .setValue(user)
                .addOnSuccessListener(unused -> {

                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show();
                });

        FirebaseDatabase.getInstance().getReference("HealthTacker")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
