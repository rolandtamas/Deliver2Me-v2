package com.app.deliver2me.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class CourierLoginActivity extends AppCompatActivity {

    private TextInputEditText emailField;
    private TextInputEditText passField;
    private CheckBox rememberMeCheck;
    private Button loginButton;
    private Button registerButton;
    private Button courierRegisterButton;
    private FirebaseAuth mAuth;
    private TextView switchAccount;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_login);
        initializeViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourierLoginActivity.this, "Heading to CourierFrontPage", Toast.LENGTH_SHORT).show();
                //
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourierLoginActivity.this, LoginActivity.class));
            }
        });
    }

    private void initializeViews() {
        emailField = findViewById(R.id.login_emailEdit);
        passField = findViewById(R.id.login_passwordEdit);
        rememberMeCheck = findViewById(R.id.rememberMeCheckBox);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        courierRegisterButton = findViewById(R.id.courierRegisterButton);
        switchAccount = findViewById(R.id.switch_account);

    }
}