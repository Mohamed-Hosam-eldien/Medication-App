package com.example.medicationapp.requests.view;

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
import com.example.medicationapp.model.Request;
import com.example.medicationapp.requests.RequestsAdapter;
import com.example.medicationapp.utils.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    LinearLayout linearLayout;

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
        FloatingActionsMenu floatingActionsMenu = getActivity().findViewById(R.id.flaoting);
        floatingActionsMenu.setVisibility(View.GONE);
        init();
        loadRequest();
        FirebaseDatabase.getInstance().getReference("uder").setValue("sdfsdf");
    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.Request);
        Query query = databaseReference.orderByChild("receiverEmail").equalTo("peter.samir299@gmail.com");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Request>().
                setQuery(query, Request.class).build();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void loadRequest() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, RecyclerView.ViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder custom, int position, @NonNull Request model) {
                if(!model.isRequest()) {
                    CustomRequestRow holder = (CustomRequestRow) custom;
                    holder.senderName.setText(model.getSenderName());
                    holder.patientName.setText(model.getPatientName());
                    holder.Description.setText(model.getDescription());
                    holder.btnReject.setOnClickListener(View -> {
                        firebaseRecyclerAdapter.getRef(position).removeValue();
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                    });
                    holder.btnAccept.setOnClickListener(view -> {
                        firebaseRecyclerAdapter.getRef(position).child("request").setValue(true);
                    });

                } else {
                    AcceptedRequestRow acceptedRequestRow = (AcceptedRequestRow) custom;
                }

            }

            @Override
            public int getItemViewType(int position) {
                return firebaseRecyclerAdapter.getItem(position).isRequest() ? 1 : 0;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_custom_row, null);
                    return new CustomRequestRow(view);
                } else {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_layout, null);
                    return new AcceptedRequestRow(view);
                }
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
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