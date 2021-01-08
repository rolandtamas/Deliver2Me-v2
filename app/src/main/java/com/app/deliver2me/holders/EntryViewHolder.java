package com.app.deliver2me.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;

public class EntryViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView author;

    public EntryViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews() {
        title = itemView.findViewById(R.id.row_entry_title);
        author = itemView.findViewById(R.id.row_entry_author);
    }

    public void setValues(String title, String author)
    {
        this.title.setText(title);
        this.author.setText(author);
    }
}
