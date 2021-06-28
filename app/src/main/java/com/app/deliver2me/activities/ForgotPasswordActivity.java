package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private MaterialButton resetButton;
    private TextInputEditText userEmail;
    private String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        userEmail.setText(intent.getStringExtra("resetEmail"));
    }

    private void setOnClickListeners() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();
                if(email.isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "E-mail-ul nu a fost introdus. Va rugam incercati din nou", Toast.LENGTH_SHORT).show();
                }
                else {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Linkul de resetare a fost trimis pe mail.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Nu s-a putut trimite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
            }
        });
    }

    private void initializeViews() {

        resetButton = findViewById(R.id.resetPassButton);
        userEmail = findViewById(R.id.userEmail);
    }
}