package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private TextInputEditText addressField;
    private FloatingActionButton mapFab;
    private NestedScrollView nestedScrollView;
    private Button addButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();
        //coming from MapActivity
        Intent intent = getIntent();
        if(intent !=null)
        {
            String address = intent.getStringExtra("Address");
            String title = intent.getStringExtra("Title");
            String description = intent.getStringExtra("Description");
            addressField.setText(address);
            titleField.setText(title);
            descriptionField.setText(description);
        }
    }

    private void setOnClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleField.getText().toString();
                final String description = descriptionField.getText().toString();
                final String address = addressField.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();

                usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                            String author="";
                            User model = snapshot.getValue(User.class); //
                            if(model !=null)
                            {
                                author = model.getFirstName() + " " + model.getLastName();
                                adsDatabase.child(title).setValue(new EntryViewModel(title, description, author, model.getImageUri(),address));
                                Toast.makeText(NewEntryActivity.this, "Anuntul a fost adaugat cu succes", Toast.LENGTH_SHORT).show();
                            }
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

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntryActivity.this,MapActivity.class);
                String title = titleField.getText().toString();
                String description = descriptionField.getText().toString();
                if(!(title.isEmpty() && description.isEmpty()))
                {
                    intent.putExtra("Title",title);
                    intent.putExtra("Description", description);
                }
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        titleField = findViewById(R.id.adtitle);
        descriptionField = findViewById(R.id.addescription);
        addButton = findViewById(R.id.addNewAd);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        mapFab = findViewById(R.id.checkMap);
        addressField = findViewById(R.id.address);
    }
}