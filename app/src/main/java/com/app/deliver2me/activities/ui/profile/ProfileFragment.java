package com.app.deliver2me.activities.ui.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.FrontPageActivity;
import com.app.deliver2me.activities.NewEntryActivity;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView userPicture;
    private TextView userName;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;

    private Button saveChanges;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private StorageReference mStorageRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        userPicture = root.findViewById(R.id.circular_image);
        userName = root.findViewById(R.id.userName);
        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);

        mStorageRef = FirebaseStorage.getInstance().getReference("pozeProfil/");

        saveChanges = root.findViewById(R.id.saveChanges);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(StorageHelper.getInstance().getUserModel().getImageUri() == null)
        {
            userPicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            userPicture.setImageURI(Uri.parse(StorageHelper.getInstance().getUserModel().getImageUri()));
        }
        userName.setText(StorageHelper.getInstance().getUserModel().getFirstName() + " " +StorageHelper.getInstance().getUserModel().getLastName());
        firstName.setText(StorageHelper.getInstance().getUserModel().getFirstName());
        lastName.setText(StorageHelper.getInstance().getUserModel().getLastName());
        email.setText(StorageHelper.getInstance().getUserModel().getEmail());
        password.setText(StorageHelper.getInstance().getUserModel().getPassword());

        userPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri !=null)
                {
                    StorageReference imageRef = mStorageRef.child(imageUri.toString());

                    UploadTask uploadTask = imageRef.putFile(imageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {throw task.getException();}
                            return imageRef.getDownloadUrl();
                        }

                    })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        Uri downloadUri = task.getResult();
                                        String downloadUrl = downloadUri.toString();
                                        user.updateEmail(email.getText().toString());
                                        User newUserCredentials = new User(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),password.getText().toString(),downloadUrl);
                                        StorageHelper.getInstance().setUserModel(newUserCredentials);
                                        usersDatabase.child(user.getUid()).setValue(newUserCredentials);
                                        Toast.makeText(root.getContext(), "Modificarile au fost salvate", Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                        Toast.makeText(root.getContext(), "Eroare la incarcare", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(root.getContext(), FrontPageActivity.class);
                        startActivity(intent);
                    }
                },2000);
            }
        });


        return root;
    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null)
        {
            imageUri = data.getData();
            Glide.with(this.getContext()).load(imageUri).into(userPicture);
        }
    }
}
