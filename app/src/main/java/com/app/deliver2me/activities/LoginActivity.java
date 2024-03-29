package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.Token;
import com.app.deliver2me.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private String refreshedToken;

    private TextInputEditText emailField;
    private TextInputEditText passField;
    private CheckBox rememberMeCheck;
    private Button loginButton;
    private Button registerButton;
    private Button forgotPasswordButton;
    private FirebaseAuth mAuth;

    private ProgressBar loginSpinner;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser() != null)
//        {
//            Intent intent = new Intent(LoginActivity.this, FrontPageActivity.class);
//            startActivity(intent);
//            finish();
//        }
        Intent intent = getIntent();
        emailField.setText(intent.getStringExtra("email"));
        passField.setText(intent.getStringExtra("password"));
        populateInputFields();
        getTokenFromUserDevice();
    }

    private void getTokenFromUserDevice() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(task.isSuccessful())
                {
                    refreshedToken = task.getResult();
                    Log.d(TAG,"---->!!REFRESHED TOKEN IS: "+refreshedToken);
                    storeToken(refreshedToken);
                }
            }
        });
    }

    private void storeToken(String refreshedToken) {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            FirebaseHelper.tokensDatabase.child(user.getUid()).setValue(new Token(user.getEmail(),refreshedToken));
        }
    }

    private void populateInputFields() {
        SharedPreferences pref = getSharedPreferences("Remember",MODE_PRIVATE);
        email = pref.getString("Email","");
        password = pref.getString("Password","");

        emailField.setText(email);
        passField.setText(password);
    }

    private void initializeViews() {
        emailField = findViewById(R.id.login_emailEdit);
        passField = findViewById(R.id.login_passwordEdit);
        rememberMeCheck = findViewById(R.id.rememberMeCheckBox);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordButton = findViewById(R.id.forgotPassButton);

        loginSpinner = findViewById(R.id.loginSpinner);
        loginSpinner.setVisibility(View.INVISIBLE);

    }

    private void setOnClickListeners()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailField.getText().toString();
                password = passField.getText().toString();
                loginSpinner.setVisibility(View.VISIBLE);
                doLogin();
                SharedPreferences.Editor editor = getSharedPreferences("Remember", MODE_PRIVATE).edit();
                if(rememberMeCheck.isChecked())
                {
                    editor.putString("Email", email);
                    editor.putString("Password",password);
                    rememberMeCheck.setChecked(true);
                    editor.apply();

                }
                else
                {
                    editor.remove("Email");
                    editor.remove("Password");
                    editor.apply();

                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("resetEmail", email);
                startActivity(intent);
            }
        });

    }

    private void doLogin() {
        final String email = emailField.getText().toString();
        final String password = passField.getText().toString();
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(LoginActivity.this, "Nu ați completat toate câmpurile", Toast.LENGTH_SHORT).show();
            loginSpinner.setVisibility(View.INVISIBLE);
        }
        else {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user.isEmailVerified())
                                {
                                    FirebaseHelper.usersDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User userModel = snapshot.getValue(User.class);
                                            StorageHelper.getInstance().setUserModel(userModel);
                                            if(userModel.getCourier())
                                            {
                                                getTokenFromUserDevice();
                                                Intent intent = new Intent(LoginActivity.this, CourierFrontPageActivity.class);
                                                intent.putExtra("possibleNewPass",password);
                                                loginSpinner.setVisibility(View.INVISIBLE);
                                                startActivity(intent);
                                            }
                                            else{
                                                getTokenFromUserDevice();
                                                Intent intent = new Intent(LoginActivity.this,FrontPageActivity.class);
                                                intent.putExtra("possibleNewPass",password);
                                                loginSpinner.setVisibility(View.INVISIBLE);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Conectare eșuată. Încercați din nou sau verificati-va e-mail-ul", Toast.LENGTH_SHORT).show();
                                    loginSpinner.setVisibility(View.INVISIBLE);
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Conectare eșuată. Încercați din nou sau verificati-va e-mail-ul", Toast.LENGTH_SHORT).show();
                                loginSpinner.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }
}