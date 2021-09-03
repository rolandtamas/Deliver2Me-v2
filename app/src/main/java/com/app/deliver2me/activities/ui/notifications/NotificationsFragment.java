package com.app.deliver2me.activities.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.adapters.NotificationsAdapter;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
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

import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notifications;
    private String thisUser;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.notifications_list);
        notifications = new ArrayList<>();

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

        //final String thisUser = StorageHelper.getInstance().getUserModel().getFirstName() + " " +StorageHelper.getInstance().getUserModel().getLastName();

        FirebaseHelper.notificationsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Notification model = ds.getValue(Notification.class);
                    if(model.getAuthor().equals(thisUser))
                    {
                        notifications.add(model);
                    }

                    notificationsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        notificationsAdapter = new NotificationsAdapter(notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(notificationsAdapter);

        return root;
    }
}