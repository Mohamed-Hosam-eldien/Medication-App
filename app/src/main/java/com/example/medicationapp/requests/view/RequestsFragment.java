package com.example.medicationapp.requests.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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

    public static final String ACEPTED_REQUEST_ID = "acceptedRedId";
    LinearLayout linearLayout;
    ArrayList<Request> requestArrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request, RecyclerView.ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Request> firebaseRecyclerOptions;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    List<Request> myList;
    Request request;
    Request request2;
    Request request3;
    Query query;
    RequestAdapter requestAdapter;
    ImageView imageDisc;
    CardView cardArraySize;
    ProgressBar progressBar;
    public static final String ACCEPTED_REQUEST = "ACCEPTED_REQUEST";

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView = view.findViewById(R.id.requestRecycler);
        cardArraySize = view.findViewById(R.id.cardArraySize);
        progressBar = view.findViewById(R.id.progressRequest);
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
        cardArraySize.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if(!Helper.isNetworkAvailable(getActivity())){
            Toast.makeText(getActivity(), "You are not connected", Toast.LENGTH_SHORT).show();
        }
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
                if (requestArrayList.size() == 0){
                    progressBar.setVisibility(View.GONE);
                    cardArraySize.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    cardArraySize.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                requestAdapter.setArrayList(requestArrayList);
                requestAdapter.notifyDataSetChanged();
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
            Paper.book().write(ACEPTED_REQUEST_ID,request.getId());
        } else {
            Toast.makeText(getActivity(), "you cant accept more than one user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRejectClick(Request request) {
        databaseReference.child(request.getId()).removeValue();
    }

}