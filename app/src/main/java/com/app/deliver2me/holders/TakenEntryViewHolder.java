package com.app.deliver2me.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static android.Manifest.permission.CALL_PHONE;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.dashboard.CourierDashboardFragment;
import com.app.deliver2me.dialogs.StatusDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

public class TakenEntryViewHolder extends RecyclerView.ViewHolder {
    private TextView takenTitle;
    private TextView takenAuthor;
    private TextView takenPhoneNumber;
    private MaterialButton statusButton;
    private ImageButton phoneButton;
    private CircularImageView userImage;
    private FragmentManager fragmentManager;

    private FirebaseAuth mAuth;

    public TakenEntryViewHolder(@NonNull @NotNull View itemView, FragmentManager fragmentManager) {
        super(itemView);
        this.fragmentManager = fragmentManager;
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
    }

    private void setOnClickListeners() {
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoneApp(takenPhoneNumber.getText().toString());
            }
        });

    }

    private void openPhoneApp(String phoneNumber) {
        itemView.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phoneNumber,null)));
    }

    private void openDialog() {
        StatusDialog statusDialog = new StatusDialog();
        Bundle args = new Bundle();
        args.putString("title",takenTitle.getText().toString());
        args.putString("author",takenAuthor.getText().toString());
        args.putString("phoneNumber", takenPhoneNumber.getText().toString());
        statusDialog.setArguments(args);
        statusDialog.show(fragmentManager, "tag");

    }

    private void initializeViews() {
        takenTitle = itemView.findViewById(R.id.taken_row_entry_title);
        takenAuthor = itemView.findViewById(R.id.taken_row_entry_author);
        takenPhoneNumber = itemView.findViewById(R.id.phoneNumber);
        statusButton = itemView.findViewById(R.id.set_status_button);
        phoneButton = itemView.findViewById(R.id.callButton);
        userImage = itemView.findViewById(R.id.userImage);;
    }

    public void setValues(String title, String author)
    {
        takenTitle.setText(title);
        takenAuthor.setText(author);
    }

    public void setValues(String title, String author, String imageUri)
    {
        takenTitle.setText(title);
        takenAuthor.setText(author);
        Glide.with(itemView.getContext()).load(imageUri).into(userImage);
    }

    public void setValues(String title, String author, String imageUri, String phoneNumber)
    {
        takenTitle.setText(title);
        takenAuthor.setText(author);
        takenPhoneNumber.setText(phoneNumber);
        Glide.with(itemView.getContext()).load(imageUri).into(userImage);
    }

}
