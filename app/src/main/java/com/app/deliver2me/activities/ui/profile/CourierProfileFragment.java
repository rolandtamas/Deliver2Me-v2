package com.app.deliver2me.activities.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ui.profile.CourierProfileFragmentViewModel;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.StorageHelper;
import com.app.deliver2me.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;
import static com.app.deliver2me.helpers.FirebaseHelper.usersDatabase;

public class CourierProfileFragment extends Fragment {
    private CourierProfileFragmentViewModel courierProfileFragmentViewModel;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private CircularImageView userPicture;
    private TextView userName;
    private EditText firstName, lastName, email, password;

    private Button saveChanges;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private StorageReference mStorageRef;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        courierProfileFragmentViewModel = new ViewModelProvider(this).get(CourierProfileFragmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_courier_profile, container, false);

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
            Glide.with(this.getContext()).load(StorageHelper.getInstance().getUserModel().getImageUri()).into(userPicture);
        }

        userName.setText(StorageHelper.getInstance().getUserModel().getFirstName() +" "+StorageHelper.getInstance().getUserModel().getLastName());
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
                saveChanges();
            }
        });

        return root;


    }

    private void saveChanges() {
        User oldUser = StorageHelper.getInstance().getUserModel();
        if(imageUri !=null)
        {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Incarcare poza");
            pd.show();
            StorageReference imageRef = mStorageRef.child(imageUri.toString());

            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    double percentage = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage((int)percentage + "%");
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            pd.dismiss();
                        }
                    });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Uri downloadUri = task.getResult();
                                String downloadUrl = downloadUri.toString();
                                user.updateEmail(email.getText().toString());
                                User newUserCredentials = new User(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString(),downloadUrl,oldUser.getCourier());
                                StorageHelper.getInstance().setUserModel(newUserCredentials);
                                FirebaseHelper.usersDatabase.child(user.getUid()).setValue(newUserCredentials);
                                Toast.makeText(getContext(), "Modificarile au fost salvate", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Eroare la incarcare", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else
        {
            if(oldUser.getImageUri()!=null)
            {
                user.updateEmail(email.getText().toString());
                User newUserCredentials = new User(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),password.getText().toString(),oldUser.getImageUri(), oldUser.getCourier());
                StorageHelper.getInstance().setUserModel(newUserCredentials);
                usersDatabase.child(user.getUid()).setValue(newUserCredentials);
                Toast.makeText(getContext(), "Modificarile au fost salvate", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            imageUri = data.getData();
            Glide.with(this.getContext()).load(imageUri).into(userPicture);
        }
    }
}
