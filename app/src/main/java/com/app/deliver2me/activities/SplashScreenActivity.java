package com.app.deliver2me.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 3000;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isConnectedToInternet())
                {
                    user = mAuth.getCurrentUser();
                    if(user!=null)
                    {
                        FirebaseHelper.usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                userModel = snapshot.getValue(User.class);
                                if(userModel.getCourier())
                                {
                                    Intent intent = new Intent(SplashScreenActivity.this, CourierFrontPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Intent intent = new Intent(SplashScreenActivity.this, FrontPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });


                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Intent intent = new Intent(SplashScreenActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN_TIMEOUT);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if( (wifiConnection!=null && wifiConnection.isConnected()) || (mobileConnection!=null && mobileConnection.isConnected()) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}