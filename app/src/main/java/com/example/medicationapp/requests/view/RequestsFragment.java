package com.example.medicationapp.requests.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medicationapp.R;
import com.example.medicationapp.model.Request;
import com.example.medicationapp.requests.RequestsAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<Request> myList;
    Request request;
    Request request2;
    Request request3;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        request = new Request("peter", "abdo", " alf salama");
        request2 = new Request("ahmed", "abdullah", " alf salama");
        request3 = new Request("hoba", "abdo", " alf salama");
        myList = new ArrayList<>();
        myList.add(request);
        myList.add(request2);
        myList.add(request3);
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView = view.findViewById(R.id.requestRecycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionsMenu floatingActionsMenu= getActivity().findViewById(R.id.flaoting);
        floatingActionsMenu.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());

//        recyclerView = view.findViewById(R.id.requestRecycler);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RequestsAdapter(getContext(), myList);
        recyclerView.setAdapter(adapter);

    }
}