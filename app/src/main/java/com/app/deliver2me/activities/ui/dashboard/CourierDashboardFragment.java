package com.app.deliver2me.activities.ui.dashboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.adapters.TakenEntryAdapter;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.models.EntryViewModel;
import com.app.deliver2me.models.Notification;
import com.app.deliver2me.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class CourierDashboardFragment extends Fragment {
    private CourierDashboardFragmentViewModel dashboardViewModel;
    private List<EntryViewModel> entryViewModelList;
    private RecyclerView recyclerView;
    private TextView emptyRecyclerViewText;
    private TakenEntryAdapter takenEntryAdapter;
    private User loggedInUser;
    private String author, firstName, lastName;
    private static final String TAG = "CourierDashboard";

    private FirebaseAuth mAuth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(CourierDashboardFragmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_courier_dashboard, container, false);
        this.getChildFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.takenAds);
        entryViewModelList = new ArrayList<>();
        emptyRecyclerViewText = root.findViewById(R.id.emptyRecyclerViewText);
        if(entryViewModelList.isEmpty())
        {
            emptyRecyclerViewText.setText("Nu aveți niciun anunț preluat.");
        }

        FirebaseUser user = mAuth.getCurrentUser();

        takenEntryAdapter = new TakenEntryAdapter(entryViewModelList);

        ActivityCompat.requestPermissions(this.getActivity(),new String[] {CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
        //get user data from db
        if(user!=null)
        {
            FirebaseHelper.usersDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    loggedInUser = snapshot.getValue(User.class);
                    if(loggedInUser!=null)
                    {
                        firstName = loggedInUser.getFirstName();
                        lastName = loggedInUser.getLastName();
                        author = firstName +" "+lastName;
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseHelper.notificationsDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Notification takenAd = dataSnapshot.getValue(Notification.class);
                        if(takenAd.getTakenBy().equals(author))
                        {
                            FirebaseHelper.usersDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren())
                                    {
                                        User model = ds.getValue(User.class);
                                        String originalAuthor = model.getFirstName() + " "+model.getLastName();
                                        if(takenAd.getAuthor().equals(originalAuthor))
                                        {
                                            EntryViewModel newEntry = new EntryViewModel(takenAd.getTitle(),takenAd.getBody(),takenAd.getAuthor(),takenAd.getAddress(), model.getImageUri(),takenAd.getAuthorPhoneNo());
                                            entryViewModelList.add(newEntry);
                                            emptyRecyclerViewText.setText(null);
                                            takenEntryAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(takenEntryAdapter);

        return root;


    }

}
