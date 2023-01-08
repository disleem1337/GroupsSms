package com.example.groupssms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.splash_register).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
        });

        findViewById(R.id.splash_login).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        });

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            Toast.makeText(SplashActivity.this, "Yönlendiriliyorsunuz", Toast.LENGTH_SHORT).show();
        }
    }
}