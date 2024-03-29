package com.example.medicationapp.ui.caring.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.ui.caring.presenter.CaringPresenter;
import com.example.medicationapp.databinding.InfoDialogBinding;
import com.example.medicationapp.databinding.RegisterDialogBinding;
import com.example.medicationapp.ui.home.presenter.HomePresenter;
import com.example.medicationapp.models.Request;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import io.paperdb.Paper;

public class AdditionalCare extends AppCompatActivity implements CaringViewInterface{

    private BottomSheetDialog bottomSheetDialog;
    private EditText edtEmail;
    private CaringPresenter presenter;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;
    private Dialog dialog;
    private InfoDialogBinding infoDialogBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_care);
        edtEmail = findViewById(R.id.edtTxtEmail);
        Button btnSend = findViewById(R.id.btnSendInvitation);
        Paper.init(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        presenter = new CaringPresenter(this);

        createDialog();
        btnSend.setOnClickListener(view -> sendMedicationRequest());
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.info_dialog, null, false);
        infoDialogBinding = InfoDialogBinding.bind(view);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        infoDialogBinding.btnCancel.setOnClickListener(view1 -> bottomSheetDialog.cancel());

        infoDialogBinding.btnSendInfoDialog.setOnClickListener(view1 -> {
            if (infoDialogBinding.edtPatientName.getText().toString().trim().length() < 2) {
                infoDialogBinding.edtPatientName.setError(getString(R.string.least_to_character));
            } else {
                if (Paper.book().read(edtEmail.getText().toString()) == null){
                    sendRequest();
                    Paper.book().write((edtEmail.getText().toString()), "1");
                    infoDialogBinding.txtSuccess.setVisibility(View.VISIBLE);
                    infoDialogBinding.imgSuccess.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, getString(R.string.already_sent_before), Toast.LENGTH_SHORT).show();
                }
            }
        });

        infoDialogBinding.btnSendEmail.setOnClickListener(view1 -> sendToEmail());
        bottomSheetDialog.setContentView(view);
    }

    private void sendMedicationRequest() {
        boolean validationResult = Helper.validate(String.valueOf(edtEmail.getText()));
        boolean isNetworkConnected = Helper.isNetworkAvailable(getApplicationContext());
        if (validationResult) {
            if (isNetworkConnected) {
                if (!edtEmail.getText().toString().equals(Paper.book().read(Common.emailUserPaper))) {
                    if (checkUserRegistration()) {
                        bottomSheetDialog.show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.cant_send_to_email), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdditionalCare.this, getString(R.string.network_failed), Toast.LENGTH_SHORT).show();
            }
        } else {
            edtEmail.setError(getString(R.string.email_problem));
        }
    }


    @Override
    public void sendRequest() {
        HomePresenter homePresenter = new HomePresenter(AdditionalCare.this);
        homePresenter.getMedicationList().observe(AdditionalCare.this, medications -> {
            if (medications.size() != 0) {
                Request request;
                request = new Request(Helper.generateKey(), Common.currentUser.getName(),
                        String.valueOf(infoDialogBinding.edtPatientName.getText()),
                        getString(R.string.sent_you_medical_request), edtEmail.getText().toString(),
                        Common.currentUser.getEmail(), false);

                presenter.onSendRequest(request);
                presenter.onSendMedicine(medications,request.getId());
                presenter.onSaveUserData(Common.currentUser);

            } else {
                Toast.makeText(this, getString(R.string.dont_have_medication_list), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void sendToEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{edtEmail.getText().toString()});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.please_open_your_app));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AdditionalCare.this, getString(R.string.no_email), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkUserRegistration() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.authID))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Common.currentUser.setEmail(account.getEmail());
            Common.currentUser.setName(account.getDisplayName());
            Common.currentUser.setUid(account.getId());

            Log.d("TAG", Common.currentUser.getName());
            Log.d("TAG", Common.currentUser.getEmail());
            Log.d("TAG", Common.currentUser.getUid());

            presenter.onSaveUserData(Common.currentUser);

            writeToPaper();
            return true;
        } else {
            // sign in first time
            showRegDialog();
            return false;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showRegDialog() {
        dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.register_dialog,null);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RegisterDialogBinding dialogBinding = RegisterDialogBinding.bind(view);
        dialogBinding.btnRegister.setOnClickListener(v -> signIn());

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("TAG", "handleSignInResult: success" + account);
            Common.currentUser.setEmail(account.getEmail());
            Common.currentUser.setName(account.getDisplayName());
            Common.currentUser.setUid(account.getId());

            writeToPaper();
            Log.d("USER DATA", Common.currentUser.getName());
            presenter.onSaveUserData(Common.currentUser);

            if (dialog != null)
                dialog.dismiss();

        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void writeToPaper() {
        Paper.book().write(Common.userNamePaper, Common.currentUser.getName());
        Paper.book().write(Common.emailUserPaper, Common.currentUser.getEmail());
    }


}