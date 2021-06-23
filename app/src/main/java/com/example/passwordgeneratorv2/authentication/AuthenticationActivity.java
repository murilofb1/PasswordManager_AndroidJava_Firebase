package com.example.passwordgeneratorv2.authentication;


import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.home.HomeView;

public class AuthenticationView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportActionBar().hide();
        if (FirebaseHelper.getFirebaseAuth().getCurrentUser() != null) {
            startActivity(new Intent(this, HomeView.class));
            finish();
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameAuthActivity, LoginFragment.class, null);
        transaction.commit();
    }
}
