package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.PositionHelper;
import com.app.deliver2me.models.Notification;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewAdActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView title;
    private TextView author;
    private TextView content;
    private TextView address;
    private Button takeOrder;

    private static final LatLng INEU = new LatLng(46.4298819, 21.8240082);
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int DEFAULT_ZOOM = 12;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;

    private LatLng addressOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);
        initializeViews();
        askPermissionLocation();
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if(mapFragment!=null)
        {
            mapFragment.getMapAsync(this);
        }
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("Title"));
        author.setText(intent.getStringExtra("Author"));
        content.setText(intent.getStringExtra("Content"));
        address.setText(intent.getStringExtra("Address"));
        setOnTouchListeners();
        setOnClickListeners();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }


    }

    private LatLng getLocationFromAddress(Context applicationContext, String address) {
        Geocoder geo = new Geocoder(applicationContext);
        List<Address> addressList;
        LatLng street=null;

        try
        {
           addressList = geo.getFromLocationName(address,5);
           if(addressList == null)
           {
               return null;
           }
           Address location = addressList.get(0);
            street = new LatLng(location.getLatitude(),location.getLongitude());

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return street;
    }

    private void setOnClickListeners() {
        takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("pushNotification");
                Notification notification = new Notification(title.getText().toString(),content.getText().toString(),author.getText().toString());
                FirebaseHelper.notificationsDatabase.child(title.getText().toString()).setValue(notification);
                FirebaseHelper.adsDatabase.child(title.getText().toString()).removeValue();
                Toast.makeText(ViewAdActivity.this, "Order Completed", Toast.LENGTH_SHORT).show();



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ViewAdActivity.this, FrontPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        });
    }

    private void setOnTouchListeners() {
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }

    private void initializeViews() {
        title = findViewById(R.id.ad_title);
        author = findViewById(R.id.ad_author);
        content = findViewById(R.id.ad_content);
        address = findViewById(R.id.addressTextView);
        takeOrder = findViewById(R.id.sendOrderButton);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        addressOnMap = getLocationFromAddress(getApplicationContext(),address.getText().toString());
        markerOptions.position(addressOnMap);
        markerOptions.title(address.getText().toString());
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(addressOnMap,DEFAULT_ZOOM));
    }

    private void askPermissionLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull @NotNull Task task) {
                    if(task.isSuccessful())
                    {
                        location = (Location) task.getResult();
                        if(location != null)
                        {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),DEFAULT_ZOOM));
                        }
                    }

                    else
                    {
                        Toast.makeText(ViewAdActivity.this, "Nu s-a putut accesa locatia curenta", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}