package com.app.deliver2me.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.adapters.EntryAdapter;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.EntryViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CourierHomeFragment extends Fragment {

    private CourierHomeFragmentViewModel homeViewModel;
    private RecyclerView recyclerView;
    private EntryAdapter entryAdapter;
    private List<EntryViewModel> entryViewModelList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(CourierHomeFragmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_courier_home, container, false);
        recyclerView = root.findViewById(R.id.adlist);
        entryViewModelList = new ArrayList<>();
        FirebaseHelper.adsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    EntryViewModel model = dataSnapshot.getValue(EntryViewModel.class);
                    entryViewModelList.add(model);
                }
                entryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        entryAdapter = new EntryAdapter(entryViewModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(entryAdapter);
        return root;


    }
}
