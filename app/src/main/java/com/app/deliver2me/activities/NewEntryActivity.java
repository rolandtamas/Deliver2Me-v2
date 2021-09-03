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
import com.app.deliver2me.helpers.NotificationHelper;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.Token;
import com.app.deliver2me.models.User;
import com.app.deliver2me.notificationpack.APIService;
import com.app.deliver2me.notificationpack.Client;
import com.app.deliver2me.notificationpack.NotificationBuilder;
import com.app.deliver2me.notificationpack.NotificationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.deliver2me.helpers.FirebaseHelper.adsDatabase;
import static com.app.deliver2me.helpers.FirebaseHelper.tokensDatabase;
import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class NewEntryActivity extends AppCompatActivity {

    private TextInputEditText titleField;
    private TextInputEditText descriptionField;
    private TextInputEditText addressField;
    private TextInputEditText phoneNumberField;
    private MaterialCheckBox isUrgent;
    private FloatingActionButton mapFab;
    private NestedScrollView nestedScrollView;
    private Button addButton;
    private FirebaseAuth mAuth;
    private APIService apiService;
    private String author = "";
    private String phoneNumberPattern = "(07([0-9]{8}))";

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
            String phoneNumber = intent.getStringExtra("PhoneNumber");
            boolean isChecked = intent.getBooleanExtra("isChecked",false);
            addressField.setText(address);
            titleField.setText(title);
            descriptionField.setText(description);
            phoneNumberField.setText(phoneNumber);
            isUrgent.setChecked(isChecked);
        }

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);
    }

    private void setOnClickListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleField.getText().toString();
                final String description = descriptionField.getText().toString();
                final String address = addressField.getText().toString();
                final String phoneNumber = phoneNumberField.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();

                if(title.isEmpty() || description.isEmpty() || address.isEmpty() || phoneNumber.isEmpty())
                {
                    Toast.makeText(NewEntryActivity.this, "Nu toate câmpurile sunt completate!", Toast.LENGTH_SHORT).show();
                }
                else if(!phoneNumber.matches(phoneNumberPattern))
                {
                    Toast.makeText(NewEntryActivity.this, "Numărul de telefon are format greșit. Vă rugăm introduceți din nou", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot){

                            User model = snapshot.getValue(User.class); //
                            if(model !=null)
                            {
                                author = model.getFirstName() + " " + model.getLastName();
                                boolean isChecked = isUrgent.isChecked();
                                if(isChecked)
                                {
                                    adsDatabase.child(title).setValue(new EntryViewModel(title, description, author,address, model.getImageUri(),phoneNumber, true));
                                }
                                else
                                {
                                    adsDatabase.child(title).setValue(new EntryViewModel(title, description, author,address, model.getImageUri(),phoneNumber));
                                }
                                Toast.makeText(NewEntryActivity.this, "Anunțul a fost adăugat cu succes", Toast.LENGTH_SHORT).show();

//                                NotificationHelper.displayNotification(NewEntryActivity.this,"Anunt nou", author+" a postat un anunt nou: "+title);
                                //send notification to all users that someone posted an ad
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

                    //get list of all tokens
                    //send notification to everyone registered
                    tokensDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren())
                            {
                                Token entry = ds.getValue(Token.class);
                                NotificationModel notificationModel = new NotificationModel("Anunț nou", author+" a publicat un anunț: "+title);
                                NotificationBuilder notificationBuilder = new NotificationBuilder();

                                notificationBuilder.setNotificationModel(notificationModel);
                                notificationBuilder.setUserToken(entry.getToken());
                                if(!ds.getKey().equals(user.getUid()))
                                {
                                    retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationBuilder);
                                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            //Toast.makeText(NewEntryActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(NewEntryActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntryActivity.this,MapActivity.class);
                String title = titleField.getText().toString();
                String description = descriptionField.getText().toString();
                String phoneNumber = phoneNumberField.getText().toString();
                boolean isChecked = isUrgent.isChecked();
                if(!(title.isEmpty() && description.isEmpty() && phoneNumber.isEmpty()))
                {
                    intent.putExtra("Title",title);
                    intent.putExtra("Description", description);
                    intent.putExtra("PhoneNumber", phoneNumber);
                    intent.putExtra("isChecked", isChecked);
                }
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        titleField = findViewById(R.id.adtitle);
        descriptionField = findViewById(R.id.addescription);
        phoneNumberField = findViewById(R.id.phoneNo);
        addButton = findViewById(R.id.addNewAd);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        mapFab = findViewById(R.id.checkMap);
        addressField = findViewById(R.id.address);
        isUrgent = findViewById(R.id.isUrgent);
    }
}