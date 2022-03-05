package com.example.medicationapp.connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Medication;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.model.User;
import com.example.medicationapp.requests.view.RequestsFragment;
import com.example.medicationapp.utils.Common;
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
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, context.getString(R.string.request_has_been_sent), Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Log.d("FAILURE", "failed : "+ e.getMessage()));

        firebaseDatabase.getReference(Common.Request).child(request.getId()).child("request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Boolean.class)==true)
                {
                    Paper.book().write(RequestsFragment.ACEPTED_REQUEST_ID,request.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMedicine(List<Medication>medications,String requestId) {
        for(Medication m:medications)
            firebaseDatabase.getReference(Common.Request)
                .child(requestId).child("medicationList").child(m.getId()).setValue(m);
    }

    public void receiveMedication() {
        firebaseDatabase.getReference(Common.Request)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    final List<Request> requests = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot sanp : snapshot.getChildren()) {

                            Request request = sanp.getValue(Request.class);
                            Log.d("TAG", "onDataChange: request = "+request.getReceiverEmail() + request.getPatientName());

                            if(request.isRequest() && request.getReceiverEmail().equals(Paper.book().read(Common.emailUserPaper))) {
                                requests.add(request);
                            }
                            Log.d("TAG", "onDataChange: list = "+requests.size());
                            Log.d("TAG", "onDataChange: email = "+ Paper.book().read(Common.emailUserPaper));
                        }
                        networkInterface.onReceiveMedication(requests);

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
