package com.app.deliver2me.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.FrontPageActivity;
import com.app.deliver2me.activities.NewEntryActivity;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.app.deliver2me.helpers.FirebaseHelper.adsDatabase;

public class MyEntryViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView author;
    private ImageButton deleteButton;
    private ImageView userImage;
    private FirebaseAuth mAuth;


    public MyEntryViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
        setOnClickListeners();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
    }

    private void setOnClickListeners() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletes my ad
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Stergere intrare")
                        .setMessage("Sunteti sigur ca doriti sa stergeti intrarea?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adsDatabase.child(title.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(itemView.getContext(), "S-a sters cu succes", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(itemView.getContext(), FrontPageActivity.class);
                                                itemView.getContext().startActivity(intent);
                                                //finish
                                            }
                                        },2000);
                                    }


                                });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void initializeViews() {
        title = itemView.findViewById(R.id.my_row_entry_title);
        author = itemView.findViewById(R.id.my_row_entry_author);
        deleteButton = itemView.findViewById(R.id.my_row_delete);
        userImage = itemView.findViewById(R.id.userImage);
    }

    public void setValues(String title, String author)
    {
        this.title.setText(title);
        this.author.setText(author);
    }

}
