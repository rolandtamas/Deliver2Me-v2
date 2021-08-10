package com.app.deliver2me.adapters;

import android.app.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.holders.TakenEntryViewHolder;
import com.app.deliver2me.models.EntryViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TakenEntryAdapter extends RecyclerView.Adapter<TakenEntryViewHolder> {

    private List<EntryViewModel> takenEntryViewModelList;
    private Context context;
    private FragmentManager fragmentManager;

    public TakenEntryAdapter(List<EntryViewModel> list)
    {
        takenEntryViewModelList = list;
    }

    @NonNull
    @NotNull
    @Override
    public TakenEntryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        try
        {
            fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        }catch (Exception ex)
        {
            Log.d("TAG","Cant get fragment manager");
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.taken_row_entry_list,parent,false);
        TakenEntryViewHolder viewHolder = new TakenEntryViewHolder(contactView,fragmentManager);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TakenEntryViewHolder holder, int position) {
        final EntryViewModel takenEntryViewModel = takenEntryViewModelList.get(position);
        holder.setValues(takenEntryViewModel.getTitle(), takenEntryViewModel.getAuthor(), takenEntryViewModel.getImageUri(), takenEntryViewModel.getPhoneNo());
    }

    @Override
    public int getItemCount() {
        return takenEntryViewModelList.size();
    }
}
