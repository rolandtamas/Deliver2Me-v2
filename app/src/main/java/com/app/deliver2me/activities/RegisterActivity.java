package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText firstNameEdit;
    private TextInputEditText lastNameEdit;

    private TextInputLayout emailEditLayout;
    private TextInputEditText emailEdit;

    private TextInputEditText passEdit;
    private TextInputLayout passEditLayout;

    private TextInputEditText confirmPassEdit;
    private TextInputLayout confirmPassEditLayout;

    private SwitchMaterial isCourierSwitch;

    private Button registerButton;
    private FirebaseAuth mAuth;

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
        setOnClickListeners();
        setPassFieldListener();
        setEmailFieldListener();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setPassFieldListener() {
        passEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(passEdit.getText().toString().length()<6)
                {
                    passEditLayout.setError("Parola prea scurta");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passEdit.getText().toString().length()<6)
                {
                    passEditLayout.setError("Parola prea scurta");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(passEdit.getText().toString().length()>=6)
                {
                    passEditLayout.setError(null);
                }
            }
        });

        confirmPassEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!(passEdit.getText().toString().equals(confirmPassEdit.getText().toString())))
                {
                    confirmPassEditLayout.setError("Parolele nu se potrivesc");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(passEdit.getText().toString().equals(confirmPassEdit.getText().toString())))
                {
                    confirmPassEditLayout.setError("Parolele nu se potrivesc");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(passEdit.getText().toString().equals(confirmPassEdit.getText().toString()))
                {
                    confirmPassEditLayout.setError(null);
                }
            }
        });
    }

    private void setEmailFieldListener()
    {
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!(emailEdit.getText().toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS))))
                {
                    emailEditLayout.setError("Introduceti o adresa valida");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(emailEdit.getText().toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS))))
                {
                    emailEditLayout.setError("Introduceti o adresa valida");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(emailEdit.getText().toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
                {
                    emailEditLayout.setError(null);
                }
            }
        });
    }

    private void setOnClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = firstNameEdit.getText().toString();
                final String lastName = lastNameEdit.getText().toString();
                final String email = emailEdit.getText().toString();
                final String password = passEdit.getText().toString();
                final String confirmPassword = confirmPassEdit.getText().toString();
                final boolean isCourier = isCourierSwitch.isChecked();
                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"Nu ați completat toate datele, încercați din nou",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(!password.equals(confirmPassword))
                    {
                        Toast.makeText(RegisterActivity.this, "Parolele nu se potrivesc", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                            createUser(firstName,lastName,email,password, isCourier);
                    }
                }
            }
        });
    }


    private void createUser(String firstName, String lastName, String email, String password, boolean isCourier) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user == null)
                            {
                                return ;
                            }
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                            User courierModel = new User(firstName,lastName,email,password,isCourier);
                                            usersDatabase.child(user.getUid()).setValue(courierModel);
                                            Toast.makeText(RegisterActivity.this, "Înregistrat cu succes. Va rugam sa va verificati adresa de e-mail", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.putExtra("email",email);
                                            intent.putExtra("password",password);
                                            startActivity(intent);
                                    }
                                }
                            });
                        }
                        else {
                            if(mAuth.getCurrentUser().getEmail().equals(email))
                            {
                                Toast.makeText(RegisterActivity.this, "Acest email este deja folosit", Toast.LENGTH_SHORT).show();
                            }
                            else{
                            Toast.makeText(RegisterActivity.this, "Hopa! Înregistrarea a eșuat. Verifică conexiunea la internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void initializeViews() {
        firstNameEdit = findViewById(R.id.register_firstNameEdit);
        lastNameEdit = findViewById(R.id.register_lastNameEdit);

        emailEditLayout = findViewById(R.id.emailEditLayout);
        emailEdit = findViewById(R.id.register_emailEdit);

        passEdit = findViewById(R.id.register_passwordEdit);
        passEditLayout = findViewById(R.id.passEditLayout);
        confirmPassEditLayout = findViewById(R.id.confirmPassEditLayout);

        confirmPassEdit = findViewById(R.id.register_confirmPasswordEdit);

        isCourierSwitch = findViewById(R.id.isCourierSwitch);

        registerButton = findViewById(R.id.registerButton);
    }


}