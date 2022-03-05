package com.example.medicationapp.signIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private ActivitySignInBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.authID))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();

            Log.d("USER",""+account.getDisplayName());
            Log.d("USER",""+account.getEmail());
            Log.d("USER",""+account.getId());
        }

        binding.tvLoginHere.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));


        binding.btnRegister.setOnClickListener(v-> signIn());
    }


//    private void createUser(){
//        String email = binding.etRegEmail.getText().toString();
//        String password = binding.etRegPass.getText().toString();
//
//        if (TextUtils.isEmpty(email)){
//            binding.etRegEmail.setError("Email cannot be empty");
//            binding.etRegEmail.requestFocus();
//        }else if (TextUtils.isEmpty(password)){
//            binding.etRegPass.setError("Password cannot be empty");
//            binding.etRegPass.requestFocus();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("TAG", "handleSignInResult: A" + account);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
        }
    }
}