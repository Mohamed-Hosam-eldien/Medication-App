package com.example.medicationapp.ui.signIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.medicationapp.databinding.ActivityLoginBinding;
import com.example.medicationapp.ui.home.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(view -> loginUser());

        binding.tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, SignInActivity.class));
            finish();
        });
    }




    private void loginUser(){
        String email = binding.etLoginEmail.getText().toString();
        String password = binding.etLoginPass.getText().toString();

        if (TextUtils.isEmpty(email)){
            binding.etLoginEmail.setError("Email cannot be empty");
            binding.etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            binding.etLoginPass.setError("Password cannot be empty");
            binding.etLoginPass.requestFocus();
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Log.d("TAG", task.getException().toString());
                }
            });
        }
    }

}