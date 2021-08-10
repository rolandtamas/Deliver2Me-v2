package com.app.deliver2me.activities.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.logout.CourierLogoutFragmentViewModel;

import org.jetbrains.annotations.NotNull;

public class CourierLogoutFragment extends Fragment {
    private CourierLogoutFragmentViewModel courierLogoutFragmentViewModel;
    private TextView logout;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        courierLogoutFragmentViewModel = new ViewModelProvider(this).get(CourierLogoutFragmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_courier_logout, container, false);

        logout = root.findViewById(R.id.textView4);

        return root;


    }


}
