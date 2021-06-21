package com.app.deliver2me.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.models.OnBoardingItem;

import org.jetbrains.annotations.NotNull;

public class OnBoardingViewHolder extends RecyclerView.ViewHolder {

    private TextView textTitle;
    private TextView textDescription;
    private ImageView imageOnBoarding;

    public OnBoardingViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews() {
        textTitle = itemView.findViewById(R.id.textTitle);
        textDescription = itemView.findViewById(R.id.textDescription);
        imageOnBoarding = itemView.findViewById(R.id.image_onBoarding);
    }

    public void setValues(OnBoardingItem item)
    {
        textTitle.setText(item.getTitle());
        textDescription.setText(item.getDescription());
        imageOnBoarding.setImageResource(item.getImage());
    }
}
