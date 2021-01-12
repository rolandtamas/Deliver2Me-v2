package com.app.deliver2me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.holders.EntryViewHolder;
import com.app.deliver2me.holders.NotificationsViewHolder;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder> {
    private Context context;
    private List<Notification> notificationList;

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_notifications_list,parent,false);
        NotificationsViewHolder viewHolder = new NotificationsViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        final Notification model = notificationList.get(position);
        holder.setValues(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public NotificationsAdapter(List<Notification> notifications) {
        this.notificationList = notifications;
    }
}
