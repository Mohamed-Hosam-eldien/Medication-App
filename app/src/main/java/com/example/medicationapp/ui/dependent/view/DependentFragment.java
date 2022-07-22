package com.example.medicationapp.ui.dependent.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.medicationapp.R;
import com.example.medicationapp.network.GetAllMedication;
import com.example.medicationapp.databinding.FragmentDependentBinding;
import com.example.medicationapp.ui.home.view.ShowBottomDialog;
import com.example.medicationapp.models.MedDetails;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;
import com.example.medicationapp.ui.requests.view.RequestsFragment;
import com.example.medicationapp.utils.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DependentFragment extends Fragment implements ShowBottomDialog, GetAllMedication {

    private FragmentDependentBinding binding;
    private DependentAdapter adapter;
    private List<Request> requestList;
    private DatabaseReference reference;
    private List<Medication>medicationList;

    public DependentFragment() {
        // Required empty public constructor

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dependent, container, false);

        binding = FragmentDependentBinding.bind(view);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler.setHasFixedSize(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference= database.getReference(Common.Request);
        medicationList=new ArrayList<>();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase.getInstance().getReference(Common.Request)
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
                        adapter = new DependentAdapter(getContext(), requests,medicationList);
                        requestList = requests;
                        binding.recycler.setAdapter(adapter);
                        //networkInterface.onReceiveMedication(requests);
                        String id= Paper.book().read(RequestsFragment.ACEPTED_REQUEST_ID,"null");
                        if(!(id.equals("null")||id.equals("")||id==null))
                            Log.i("TAG", "getMedicationList: "+id);
                        reference.child(id).child("medicationList").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                medicationList.clear();
                                for(DataSnapshot s:snapshot.getChildren())
                                    medicationList.add(s.getValue(Medication.class));
                                adapter.setMedications(medicationList);
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});

    }

    @Override
    public void showMedDialog(MedDetails detail, Medication medication, int position) {}

    @Override
    public void getMedicationList(List<Request> requests) {
        Log.d("TAG", "getMedicationList: REQUEST " + requests.size());
        adapter = new DependentAdapter(getContext(), requests);
        requestList = requests;
        binding.recycler.setAdapter(adapter);
    }

}