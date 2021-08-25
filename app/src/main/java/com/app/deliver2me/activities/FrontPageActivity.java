package com.app.deliver2me.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.dashboard.DashboardFragment;
import com.app.deliver2me.activities.ui.home.HomeFragment;
import com.app.deliver2me.activities.ui.logout.LogoutFragment;
import com.app.deliver2me.activities.ui.notifications.NotificationsFragment;
import com.app.deliver2me.activities.ui.profile.ProfileFragment;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FrontPageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeFragment homeFragment;
    private DashboardFragment dashboardFragment;
    private NotificationsFragment notificationsFragment;
    private LogoutFragment logoutFragment;
    private ProfileFragment profileFragment;

    private Fragment activeFragment;
    private FirebaseAuth mAuth;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        initializeFragments();
        mAuth = FirebaseAuth.getInstance();
        navView.setOnNavigationItemSelectedListener(this);
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
            return;
        }
    }

    private void loadFragments() {
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFragment,"1").commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, dashboardFragment,"2").hide(dashboardFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, notificationsFragment,"3").hide(notificationsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, profileFragment,"4").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, logoutFragment,"5").hide(logoutFragment).commit();
    }

    private void initializeFragments() {
        homeFragment = new HomeFragment();
        dashboardFragment = new DashboardFragment();
        notificationsFragment = new NotificationsFragment();
        logoutFragment = new LogoutFragment();
        profileFragment = new ProfileFragment();

        activeFragment = homeFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.navigation_home:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                return true;
            case R.id.navigation_dashboard:
                fragmentManager.beginTransaction().hide(activeFragment).show(dashboardFragment).commit();
                activeFragment = dashboardFragment;
                return true;
            case R.id.navigation_notifications:
                fragmentManager.beginTransaction().hide(activeFragment).show(notificationsFragment).commit();
                activeFragment = notificationsFragment;
                return true;
            case R.id.navigation_profile:
                fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                activeFragment = profileFragment;
                return true;
            case R.id.navigation_logout:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                FirebaseUser user = mAuth.getCurrentUser();
                if(user !=null)
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Delogare")
                            .setMessage("Sunteti sigur ca doriti sa va delogati?")
                            .setPositiveButton(R.string.Da, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.signOut();
                                    Intent intent = new Intent(FrontPageActivity.this, LoginActivity.class);
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