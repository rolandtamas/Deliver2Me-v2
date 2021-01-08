package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.home.HomeFragment;
import com.app.deliver2me.adapters.EntryAdapter;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.app.deliver2me.helpers.FirebaseHelper.adsDatabase;
import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class NewEntryActivity extends AppCompatActivity {

    private TextInputEditText titleField;
    private TextInputEditText descriptionField;

    private Button addButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setOnClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleField.getText().toString();
                final String description = descriptionField.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();

                usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                            String author="";
                            User model = snapshot.getValue(User.class); //
                            if(model !=null)
                            {author = model.getFirstName() + " " +model.getLastName();}
                            adsDatabase.child(title).setValue(new EntryViewModel(title,description,author));
                            Toast.makeText(NewEntryActivity.this, "Anuntul a fost adaugat cu succes", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(NewEntryActivity.this, FrontPageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },2000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void initializeViews() {
        titleField = findViewById(R.id.adtitle);
        descriptionField = findViewById(R.id.addescription);

        addButton = findViewById(R.id.addNewAd);
    }
}