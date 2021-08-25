package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.Transliterator;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.PositionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {
    private TextView addressMap;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Button confirmAddressButton;
    private static final LatLng INEU = new LatLng(46.4298819, 21.8240082);
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int DEFAULT_ZOOM = 15;
    private Location location;
    private String tempTitle, tempDesc, tempPhoneNumber;
    private boolean tempIsChecked;
    private LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initializeViews();
        askPermissionLocation();
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if(mapFragment!=null)
        {
            mapFragment.getMapAsync(this);
        }
        setOnClickListeners();
        Intent intent = getIntent();
        tempTitle = intent.getStringExtra("Title");
        tempDesc = intent.getStringExtra("Description");
        tempPhoneNumber = intent.getStringExtra("PhoneNumber");
        tempIsChecked = intent.getBooleanExtra("isChecked",false);
    }

    private void setMapListeners() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveListener(this);
        map.setOnCameraMoveStartedListener(this);
    }

    private void setOnClickListeners() {
        confirmAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressMap.getText().toString();
                if (address.isEmpty() || address.equals("Se cauta adresa...")) {
                    Toast.makeText(MapActivity.this, "Adresa incorecta sau inexistenta", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MapActivity.this, NewEntryActivity.class);
                    intent.putExtra("Address", address);
                    intent.putExtra("Title", tempTitle);
                    intent.putExtra("Description", tempDesc);
                    intent.putExtra("PhoneNumber", tempPhoneNumber);
                    intent.putExtra("isChecked",tempIsChecked);
                    PositionHelper.getInstance().setPosition(position);
                    startActivity(intent);
                }
            }
        });
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
                        Toast.makeText(MapActivity.this, "Nu s-a putut accesa locatia curenta", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        addressMap = findViewById(R.id.addressMap);

        confirmAddressButton = findViewById(R.id.confirmButtonMap);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geo = new Geocoder(MapActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(INEU.latitude,INEU.longitude,1);
            if(addresses.isEmpty())
            {
                addressMap.setText("Se cauta adresa...");
            }
            else {
                if(addresses.size() > 0)
                {
                    addressMap.setText(addresses.get(0).getThoroughfare() + ", " +addresses.get(0).getFeatureName() + ", "+addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraIdle() {
        List<Address> addresses;
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geo.getFromLocation(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude,1);
            addressMap.setText(addresses.get(0).getThoroughfare() + ", " +addresses.get(0).getFeatureName() + ", "+addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea());
            position = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        addressMap.setText("Se cauta adresa...");
    }

    @Override
    public void onCameraMoveStarted(int i) {
        addressMap.setText("Se cauta adresa...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        addressMap.setText("Se cauta adresa...");
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(INEU,DEFAULT_ZOOM));
        setMapListeners();
    }
}