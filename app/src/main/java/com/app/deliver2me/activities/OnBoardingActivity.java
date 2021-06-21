package com.app.deliver2me.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.deliver2me.R;
import com.app.deliver2me.adapters.OnBoardingAdapter;
import com.app.deliver2me.models.OnBoardingItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout onBoardingIndicators;
    private MaterialButton onBoardingButton;
    private String prevStarted = "prevStarted";

    private final String TEST1 = "Poți alege tot ce îți dorești";
    private final String DESC1 = "Ai posibilitatea de a descrie în anunț orice produs dorești";

    private final String TEST2 = "Serviciu Rapid";
    private final String DESC2 = "Ai avantajele livrării rapide ale produselor tale dorite";

    private final String TEST3 = "Te bucuri de fiecare moment!";
    private final String DESC3 = "Evită timpul pierdut, ca să îl fructifici în alte momente.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        setOnBoardingItems();
        initializeViews();
        setOnClickListeners();
        setUpOnBoardingIndicators();
        setCurrentOnBoardingIndicator(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(prevStarted, false))
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(prevStarted,Boolean.TRUE);
            editor.apply();
        }
        else
        {
            startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
        }
    }

    private void setOnClickListeners() {
        onBoardingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
            }
        });
    }

    private void initializeViews() {
        ViewPager2 viewPager = findViewById(R.id.on_boarding_pager);
        viewPager.setAdapter(onBoardingAdapter);

        onBoardingIndicators = findViewById(R.id.on_boarding_indicators);
        onBoardingButton = findViewById(R.id.on_boarding_button);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);
            }
        });
    }

    private void setUpOnBoardingIndicators() {
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++)
        {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            onBoardingIndicators.addView(indicators[i]);
        }
    }

    private void setOnBoardingItems() {
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();
        OnBoardingItem itemOne = new OnBoardingItem();
        itemOne.setTitle(TEST1);
        itemOne.setDescription(DESC1);
        itemOne.setImage(R.drawable.grocerybag);

        OnBoardingItem itemTwo = new OnBoardingItem();
        itemTwo.setTitle(TEST2);
        itemTwo.setDescription(DESC2);
        itemTwo.setImage(R.drawable.delivery);

        OnBoardingItem itemThree = new OnBoardingItem();
        itemThree.setTitle(TEST3);
        itemThree.setDescription(DESC3);
        itemThree.setImage(R.drawable.spaghetti);

        onBoardingItems.add(itemOne);
        onBoardingItems.add(itemTwo);
        onBoardingItems.add(itemThree);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);
    }

    private void setCurrentOnBoardingIndicator(int index)
    {
        int childCount = onBoardingIndicators.getChildCount();
        for(int i=0;i<childCount;i++)
        {
            ImageView imageView = (ImageView)onBoardingIndicators.getChildAt(i);
            if(i==index)
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.onboarding_indicator_active
                ));
            }
            else
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.onboarding_indicator_inactive
                ));
            }
        }
    }

}