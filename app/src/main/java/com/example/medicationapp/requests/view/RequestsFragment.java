package com.example.medicationapp.requests.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.home.view.MainActivity;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.requests.RequestAdapter;
import com.example.medicationapp.utils.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class RequestsFragment extends Fragment implements OnRequestClick {

    LinearLayout linearLayout;
    ArrayList<Request> requestArrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request, RecyclerView.ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Request> firebaseRecyclerOptions;
    RecyclerView.LayoutManager layoutManager;
    //    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<Request> myList;
    Request request;
    Request request2;
    Request request3;
    Query query;
    RequestAdapter requestAdapter;
    public static final String ACCEPTED_REQUEST = "ACCEPTED_REQUEST";

    int i = 0;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView = view.findViewById(R.id.requestRecycler);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Paper.init(getActivity());
        FloatingActionsMenu floatingActionsMenu = getActivity().findViewById(R.id.flaoting);
        floatingActionsMenu.setVisibility(View.GONE);
        requestArrayList = new ArrayList<>();
        requestAdapter = new RequestAdapter(getContext(), this, requestArrayList);
        init();
        getFirebaseRequest();
    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.Request);
         query = databaseReference.orderByChild("receiverEmail").equalTo(Paper.book().read(Common.emailUserPaper));
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Request>().
                setQuery(query, Request.class).build();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(requestAdapter);
    }

    public void getFirebaseRequest() {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestArrayList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    requestArrayList.add(d.getValue(Request.class));
                }
                requestAdapter.setArrayList(requestArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAcceptClick(Request request) {
        if (Paper.book().read(ACCEPTED_REQUEST) != null) {
            databaseReference.child(request.getId()).child("request").setValue(true);
            Paper.book().write(ACCEPTED_REQUEST, "1");
        } else {
            Toast.makeText(getActivity(), "you cant accept more than one user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRejectClick(Request request) {
        databaseReference.child(request.getId()).removeValue();
    }

    private void loadRequest() {
        Log.i("TAG", "loadRequest: " + i);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(firebaseRecyclerAdapter!=null){
//            firebaseRecyclerAdapter.startListening();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if(firebaseRecyclerAdapter!=null){
//            firebaseRecyclerAdapter.stopListening();
//        }
//    }
}