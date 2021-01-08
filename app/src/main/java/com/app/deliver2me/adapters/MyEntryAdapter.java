package com.app.deliver2me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.holders.EntryViewHolder;
import com.app.deliver2me.holders.MyEntryViewHolder;
import com.app.deliver2me.models.EntryViewModel;

import java.util.List;

public class MyEntryAdapter extends RecyclerView.Adapter<MyEntryViewHolder> {

    private List<EntryViewModel> myEntryViewModelList;
    private Context context;

    public MyEntryAdapter(List<EntryViewModel> entryViewModelList) {
        this.myEntryViewModelList = entryViewModelList;
    }

    @NonNull
    @Override
    public MyEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.my_row_entry_list,parent,false);
        MyEntryViewHolder viewHolder = new MyEntryViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyEntryViewHolder holder, int position) {
        final EntryViewModel myEntryViewModel = myEntryViewModelList.get(position);
        holder.setValues(myEntryViewModel.getTitle(), myEntryViewModel.getAuthor());
    }

    @Override
    public int getItemCount() {
        return myEntryViewModelList.size();
    }
}
