package com.app.deliver2me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.holders.OnBoardingViewHolder;
import com.app.deliver2me.models.OnBoardingItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingViewHolder> {

    private Context context;
    private List<OnBoardingItem> onBoardingItemList;

    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItemList) {
        this.onBoardingItemList = onBoardingItemList;
    }

    @NonNull
    @NotNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_container_onboarding, parent,false);
        OnBoardingViewHolder viewHolder = new OnBoardingViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OnBoardingViewHolder holder, int position) {
        holder.setValues(onBoardingItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItemList.size();
    }
}
