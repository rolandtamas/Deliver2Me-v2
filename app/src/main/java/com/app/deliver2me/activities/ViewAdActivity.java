package com.app.deliver2me.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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
import com.app.deliver2me.models.Notification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

public class ViewAdActivity extends AppCompatActivity {

    private TextView title;
    private TextView author;
    private TextView content;

    private Button takeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);
        initializeViews();
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("Title"));
        author.setText(intent.getStringExtra("Author"));
        content.setText(intent.getStringExtra("Content"));
        setOnTouchListeners();
        setOnClickListeners();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

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

        takeOrder = findViewById(R.id.sendOrderButton);
    }
}