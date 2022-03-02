package com.example.medicationapp.home.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicationapp.R;
import com.example.medicationapp.caring.presenter.CaringPresenter;
import com.example.medicationapp.caring.view.AdditionalCare;
import com.example.medicationapp.databinding.ActivityMainBinding;
import com.example.medicationapp.medications.view.addEditMed.AddEditActivity;
import com.example.medicationapp.utils.Common;
import com.example.medicationapp.utils.Helper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity implements OnDateSelect{

    private ActivityMainBinding binding;
    private HomeMedFragment homeMedFragment;
    private Bundle bundle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;
    private CaringPresenter presenter;
    private View navLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Paper.init(this);
        presenter = new CaringPresenter(this);

        checkOverlayPermission();

        addData();

        initNavController();

        initAllMedicationFragment();

        initDependentLayout();

        initFabButton();

        setUserDetails();
    }

    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

    private void addData() {

    }

    private void initFabButton() {
        binding.btnAddMed.setOnClickListener(view1 -> {
            Intent intent=new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("comeFrom",1);
            startActivity(intent);
        });

        binding.btnAddTaker.setOnClickListener(v -> {
            startActivity(new Intent(this, AdditionalCare.class));
            binding.flaoting.collapse();
        });
    }

    private void initAllMedicationFragment() {
        FragmentManager manager;
        FragmentTransaction transaction;

        manager = getSupportFragmentManager();

        if(bundle == null) {
            homeMedFragment = new HomeMedFragment();

            transaction = manager.beginTransaction();

            transaction
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, homeMedFragment, "fragment_home")
                    .commit();
        } else {
            homeMedFragment = (HomeMedFragment) manager.findFragmentByTag("fragment_home");
        }
    }

    private void initNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navigationView, navController);

        NavigationUI.setupWithNavController(binding.bottomBar, navController);

        binding.layoutCurrentDep.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
    }


    private void initDependentLayout() {
        navLayout = LayoutInflater.from(this).inflate(R.layout.layout_nav_include, null);
        binding.navigationView.addView(navLayout);
        navLayout.findViewById(R.id.layoutRegister).setOnClickListener(v -> {
            if(Helper.isNetworkAvailable(getApplicationContext())) {
                checkUserRegistration();
            } else {
                Toast.makeText(this, "please check your connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSelected(long date) {
        homeMedFragment.getDate(date, this);
    }

    private void checkUserRegistration() {
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

            Log.d("TAG Main", Common.currentUser.getName());
            Log.d("TAG Main", Common.currentUser.getEmail());
            Log.d("TAG Main", Common.currentUser.getUid());

            //presenter.onSaveUserData(Common.currentUser);
            Toast.makeText(this, "you are already exist", Toast.LENGTH_SHORT).show();

            writeToPaper();
        } else {
            // sign in first time
            signInFirstTime();
        }
    }

    private void signInFirstTime() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            setUserDetails();
            Log.d("USER DATA", Common.currentUser.getName());
            presenter.onSaveUserData(Common.currentUser);
            
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void setUserDetails() {
        if(Paper.book().contains(Common.userNamePaper)) {
            TextView txtName = navLayout.findViewById(R.id.txtHeaderName);
            TextView txtEmail = navLayout.findViewById(R.id.txtHeaderEmail);
            txtEmail.setText(Paper.book().read(Common.emailUserPaper));
            txtName.setText(Paper.book().read(Common.userNamePaper));
            binding.txtCurrentDep.setText(getFirsName());
        }
    }

    private String getFirsName() {
        String[] arr = Paper.book().read(Common.userNamePaper).toString().split(" ", 2);

        return arr[0];
    }

    private void writeToPaper() {
        Paper.book().write(Common.userNamePaper, Common.currentUser.getName());
        Paper.book().write(Common.emailUserPaper, Common.currentUser.getEmail());
    }

}