package com.app.deliver2me.holders;

import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

public class EntryViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView author;
    private ImageView userImage;
    private ImageView priorityImage;
    private FirebaseUser user;

    public EntryViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews() {
        title = itemView.findViewById(R.id.row_entry_title);
        author = itemView.findViewById(R.id.row_entry_author);
        userImage = itemView.findViewById(R.id.userImage);
        priorityImage = itemView.findViewById(R.id.priority);
    }

    public void setValues(String title, String author)
    {
        this.title.setText(title);
        author = FirebaseHelper.usersDatabase.child(user.getUid()).child("firstName") + " " +FirebaseHelper.usersDatabase.child(user.getUid()).child("lastName");
        this.author.setText(author);
    }

    public void setValues(String title, String author, String imageUri)
    {
        this.title.setText(title);
        this.author.setText(author);
        Glide.with(itemView.getContext()).load(imageUri).into(userImage);
    }

    public void setValues(String title, String author, String imageUri, String priority)
    {
        this.title.setText(title);
        this.author.setText(author);
        if(priority.equals("urgent"))
        {
            priorityImage.setImageResource(R.drawable.priority);
            priorityImage.setColorFilter(Color.RED);
            itemView.findViewById(R.id.cardLayout).setBackgroundColor(Color.rgb(255,165,0));
        }
        Glide.with(itemView.getContext()).load(imageUri).into(userImage);
    }
}
