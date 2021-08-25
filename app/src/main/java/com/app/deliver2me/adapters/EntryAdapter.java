package com.app.deliver2me.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ViewAdActivity;
import com.app.deliver2me.holders.EntryViewHolder;
import com.app.deliver2me.models.EntryViewModel;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private List<EntryViewModel> entryViewModelList;
    private Context context;


    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_entry_list,parent,false);
        EntryViewHolder viewHolder = new EntryViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        final EntryViewModel entryViewModel = entryViewModelList.get(position);
        holder.setValues(entryViewModel.getTitle(), entryViewModel.getAuthor(), entryViewModel.getImageUri(), entryViewModel.getPriority());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ViewAdActivity.class);
                intent.putExtra("Title", entryViewModel.getTitle());
                intent.putExtra("Author", entryViewModel.getAuthor());
                intent.putExtra("Content", entryViewModel.getContent());
                intent.putExtra("Address", entryViewModel.getAddress());
                intent.putExtra("phoneNo", entryViewModel.getPhoneNo());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return entryViewModelList.size();
    }

    public EntryAdapter(List<EntryViewModel> entryViewModelList) {
        this.entryViewModelList = entryViewModelList;
    }
}
