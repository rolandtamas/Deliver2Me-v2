package com.app.deliver2me.activities.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.NewEntryActivity;
import com.app.deliver2me.adapters.EntryAdapter;
import com.app.deliver2me.adapters.MyEntryAdapter;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.app.deliver2me.helpers.FirebaseHelper.adsDatabase;
import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FloatingActionButton addFab;
    private MyEntryAdapter entryAdapter;
    private List<EntryViewModel> entryViewModelList;
    private RecyclerView recyclerView;
    private TextView emptyRecyclerViewText;
    private String thisUser;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User model = snapshot.getValue(User.class);
                    thisUser = model.getFirstName()+" "+model.getLastName();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }



        recyclerView = root.findViewById(R.id.myads);
        emptyRecyclerViewText = root.findViewById(R.id.emptyRecyclerViewText);

        if(entryViewModelList.isEmpty())
        {
            emptyRecyclerViewText.setText("Nu aveți niciun anunț postat.");
        }

        entryViewModelList = new ArrayList<>();
        //final String thisUser = StorageHelper.getInstance().getUserModel().getFirstName() + " " +StorageHelper.getInstance().getUserModel().getLastName();
        adsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    EntryViewModel model = ds.getValue(EntryViewModel.class);
                    if(model.getAuthor().equals(thisUser))
                    {
                        entryViewModelList.add(model);
                        emptyRecyclerViewText.setText(null);
                    }
                }
                entryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        entryAdapter = new MyEntryAdapter(entryViewModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(entryAdapter);

        //initialize fab
        addFab = root.findViewById(R.id.addNewEntryFab);

        //set on click listeners
        addFab.setOnClickListener(v -> {
            startActivity(new Intent(root.getContext(), NewEntryActivity.class));
        });
        return root;
    }

}