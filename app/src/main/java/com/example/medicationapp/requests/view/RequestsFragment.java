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

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class RequestsFragment extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request, CustomRequestRow> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Request> firebaseRecyclerOptions;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

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

    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.Request);
        Query query = databaseReference.orderByChild("receiverEmail").equalTo("mohamedhosameldien07@gmail,com");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, CustomRequestRow>(firebaseRecyclerOptions) {

            @Override
            public int getItemViewType(int position) {
                return firebaseRecyclerAdapter.getItem(position).isRequest() ? 1:0;
            }

            @Override
            protected void onBindViewHolder(@NonNull CustomRequestRow holder, int position, @NonNull Request model) {

                holder.senderName.setText(model.getSenderName());
                holder.patientName.setText(model.getPatientName());
                holder.Description.setText(model.getDescription());

                holder.btnReject.setOnClickListener(View -> {
                    firebaseRecyclerAdapter.getRef(position).removeValue();
                });

                holder.btnAccept.setOnClickListener(view -> {
                    firebaseRecyclerAdapter.getRef(position)
                            .child("request").setValue(true);
                });

            }

            @NonNull
            @Override
            public CustomRequestRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                if(viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_custom_row, null);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, null);
                }
                return new CustomRequestRow(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    private void loadRequest() {
        Log.i("TAG", "loadRequest: " + i);
    }


}