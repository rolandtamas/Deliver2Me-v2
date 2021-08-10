package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.dashboard.CourierDashboardFragment;
import com.app.deliver2me.activities.ui.home.CourierHomeFragment;
import com.app.deliver2me.activities.ui.logout.CourierLogoutFragment;
import com.app.deliver2me.activities.ui.profile.CourierProfileFragment;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class CourierFrontPageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private CourierDashboardFragment courierDashboardFragment;
    private CourierHomeFragment courierHomeFragment;
    private CourierLogoutFragment courierLogoutFragment;
    private CourierProfileFragment courierProfileFragment;

    private Fragment activeFragment;
    private FirebaseAuth mAuth;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_front_page);
        BottomNavigationView navigationView = findViewById(R.id.courier_nav_view);
        initializeFragments();
        mAuth = FirebaseAuth.getInstance();
        navigationView.setOnNavigationItemSelectedListener(this);
        checkIfUserRecentlyChangedPassword();
        loadFragments();
    }

    private void checkIfUserRecentlyChangedPassword() {
        User oldUserCredentials = StorageHelper.getInstance().getUserModel();
        if(oldUserCredentials == null)
        {
            return;
        }
        Intent intent = getIntent();
        String possibleNewPass = intent.getStringExtra("possibleNewPass");
        if(possibleNewPass != null)
        {
            if((!oldUserCredentials.getPassword().equals(possibleNewPass)) && oldUserCredentials.getImageUri() != null)
            {
                User newUserCredentials = new User(oldUserCredentials.getFirstName(), oldUserCredentials.getLastName(), oldUserCredentials.getEmail(), possibleNewPass, oldUserCredentials.getImageUri(), oldUserCredentials.getCourier());
                StorageHelper.getInstance().setUserModel(newUserCredentials);
                FirebaseHelper.usersDatabase.child(mAuth.getCurrentUser().getUid()).setValue(newUserCredentials);
            }
            else if(!oldUserCredentials.getPassword().equals(possibleNewPass))
            {
                User newUserCredentials = new User(oldUserCredentials.getFirstName(), oldUserCredentials.getLastName(), oldUserCredentials.getEmail(), possibleNewPass,oldUserCredentials.getCourier());
                StorageHelper.getInstance().setUserModel(newUserCredentials);
                FirebaseHelper.usersDatabase.child(mAuth.getCurrentUser().getUid()).setValue(newUserCredentials);
            }
            else
            {
                return ;
            }
        }
        else
        {
            return ;
        }
    }

    private void loadFragments() {
        fragmentManager.beginTransaction().add(R.id.courier_nav_host_fragment, courierHomeFragment,"1").commit();
        fragmentManager.beginTransaction().add(R.id.courier_nav_host_fragment, courierDashboardFragment,"2").hide(courierDashboardFragment).commit();
        fragmentManager.beginTransaction().add(R.id.courier_nav_host_fragment, courierProfileFragment,"3").hide(courierProfileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.courier_nav_host_fragment, courierLogoutFragment,"4").hide(courierLogoutFragment).commit();
    }

    private void initializeFragments() {
        courierDashboardFragment = new CourierDashboardFragment();
        courierHomeFragment = new CourierHomeFragment();
        courierLogoutFragment = new CourierLogoutFragment();
        courierProfileFragment = new CourierProfileFragment();

        activeFragment = courierHomeFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.courier_navigation_home:
                fragmentManager.beginTransaction().hide(activeFragment).show(courierHomeFragment).commit();
                activeFragment = courierHomeFragment;
                return true;
            case R.id.courier_navigation_dashboard:
                fragmentManager.beginTransaction().hide(activeFragment).show(courierDashboardFragment).commit();
                activeFragment = courierDashboardFragment;
                return true;
            case R.id.courier_navigation_profile:
                fragmentManager.beginTransaction().hide(activeFragment).show(courierProfileFragment).commit();
                activeFragment = courierProfileFragment;
                return true;
            case R.id.courier_navigation_logout:
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null)
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Delogare")
                            .setMessage("Sunteti sigur ca doriti sa va delogati?")
                            .setPositiveButton(R.string.Da, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.signOut();
                                    Intent intent = new Intent(CourierFrontPageActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(R.string.Nu, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return true;
        }
        return false;
    }
}