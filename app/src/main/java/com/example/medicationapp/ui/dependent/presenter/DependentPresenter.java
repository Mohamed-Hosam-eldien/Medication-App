package com.example.medicationapp.ui.dependent.presenter;

import android.content.Context;
import android.util.Log;
import com.example.medicationapp.network.Connection;
import com.example.medicationapp.network.GetAllMedication;
import com.example.medicationapp.network.NetworkInterface;
import com.example.medicationapp.models.Medication;
import com.example.medicationapp.models.Request;
import com.example.medicationapp.models.User;
import java.util.List;


public class DependentPresenter implements NetworkInterface {

    Connection connection;
    GetAllMedication getAllMedication;

    public DependentPresenter(GetAllMedication getAllMedication, Context context) {
        this.connection = Connection.getInstance(this, context);
        this.getAllMedication = getAllMedication;
    }

    @Override
    public void onSendRequest(Request request) {}

    public void getPatientMed() {
        connection.receiveMedication();
    }

    @Override
    public void onReceiveMedication(List<Request> list) {
        Log.d("TAG", "onReceiveMedication: "+ list.size());
        getAllMedication.getMedicationList(list);
    }

    @Override
    public void onSaveUserData(User user) {}

    @Override
    public void onSendMedicine(List<Medication> medications, String requestId) {}

}
