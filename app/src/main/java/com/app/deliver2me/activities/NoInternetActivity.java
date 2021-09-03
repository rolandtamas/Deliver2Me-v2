package com.app.deliver2me.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.app.deliver2me.R;

public class NoInternetActivity extends AppCompatActivity {

    private SwipeRefreshLayout noInternetSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        initializeViews();
        setOnRefreshListener();
    }

    private void setOnRefreshListener() {
        noInternetSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noInternetSwipe.setRefreshing(false);
                if(isConnected())
                {
                    Intent intent = new Intent(NoInternetActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private boolean isConnected() {
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

    private void initializeViews() {
        noInternetSwipe = findViewById(R.id.noInternetSwipe);
    }
}