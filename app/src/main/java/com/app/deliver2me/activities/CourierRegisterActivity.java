package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CourierRegisterActivity extends AppCompatActivity {

    private TextInputEditText firstNameEdit;
    private TextInputEditText lastNameEdit;
    private TextInputEditText emailEdit;
    private TextInputEditText passEdit;
    private TextInputEditText confirmPassEdit;

    private Button registerButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_register);
        mAuth = FirebaseAuth.getInstance();
        initializeViews();
        setOnClickListeners();
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
                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                {
                    Toast.makeText(CourierRegisterActivity.this,"Nu ați completat toate datele, încercați din nou",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(!password.equals(confirmPassword))
                    {
                        Toast.makeText(CourierRegisterActivity.this, "Parolele nu se potrivesc", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        createCourier(firstName,lastName,email,password);
                    }
                }
            }
        });
    }

    private void createCourier(String firstName, String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user == null)
                            {
                                return;
                            }
                            User userModel = new User(firstName,lastName,email,password);
                            FirebaseHelper.couriersDatabase.child(user.getUid()).setValue(userModel);
                            Toast.makeText(CourierRegisterActivity.this, "Înregistrat cu succes", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CourierRegisterActivity.this, CourierLoginActivity.class);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            startActivity(intent);
                        }
                        else
                        {
                            if(mAuth.getCurrentUser().getEmail().equals(email))
                            {
                                Toast.makeText(CourierRegisterActivity.this, "Acest email este deja folosit", Toast.LENGTH_SHORT).show();
                            }
                            else{
                            Toast.makeText(CourierRegisterActivity.this, "Hopa! Înregistrarea a eșuat. Verifică conexiunea la internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void initializeViews() {
        firstNameEdit = findViewById(R.id.register_firstNameEdit);
        lastNameEdit = findViewById(R.id.register_lastNameEdit);
        emailEdit = findViewById(R.id.register_emailEdit);
        passEdit = findViewById(R.id.register_passwordEdit);
        confirmPassEdit = findViewById(R.id.register_confirmPasswordEdit);

        registerButton = findViewById(R.id.courierRegisterButton);
    }
}