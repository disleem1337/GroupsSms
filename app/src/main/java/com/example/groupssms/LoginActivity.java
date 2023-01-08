package com.example.groupssms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    Button redirectToRegisterButton;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_emailEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);

        loginButton = findViewById(R.id.login_loginButton);
        redirectToRegisterButton = findViewById(R.id.login_redirectToRegisterButton);

        mAuth = FirebaseAuth.getInstance();

        redirectToRegisterButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Giriş başarılı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Giriş başarısız", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}