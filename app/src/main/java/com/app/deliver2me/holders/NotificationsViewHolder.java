package com.app.deliver2me.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;

public class NotificationsViewHolder extends RecyclerView.ViewHolder {
    private TextView author;

    public NotificationsViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews() {
        author = itemView.findViewById(R.id.notifications_user_name);
    }

    public void setValues(String author) {
        this.author.setText(author);
    }
}
