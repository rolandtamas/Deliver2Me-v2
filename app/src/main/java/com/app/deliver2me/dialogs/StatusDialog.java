package com.app.deliver2me.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.CourierFrontPageActivity;
import com.app.deliver2me.activities.FrontPageActivity;
import com.app.deliver2me.activities.ViewAdActivity;
import com.app.deliver2me.helpers.FirebaseHelper;
import com.app.deliver2me.helpers.NotificationHelper;
import com.app.deliver2me.models.Token;
import com.app.deliver2me.models.User;
import com.app.deliver2me.notificationpack.APIService;
import com.app.deliver2me.notificationpack.Client;
import com.app.deliver2me.notificationpack.NotificationBuilder;
import com.app.deliver2me.notificationpack.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusDialog extends DialogFragment {

    private AutoCompleteTextView autoCompleteTextView;
    private APIService apiService;
    private String tokenForUser,keyForUser;
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        String author,title,phoneNumber;
        Bundle args = getArguments();
        if(args!=null){
            author = args.getString("author");
            phoneNumber = args.getString("phoneNumber");
            title = args.getString("title");
        }
        else{
            author = phoneNumber = title = "";
        }

        String [] statuses = getActivity().getResources().getStringArray(R.array.status);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.item_status_item,statuses);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_status_selection,null);
        autoCompleteTextView = view.findViewById(R.id.statusSelectionBox);
        autoCompleteTextView.setAdapter(arrayAdapter);

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("Aplică", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String statusFromDialog = autoCompleteTextView.getText().toString();
                        switch (statusFromDialog)
                        {
                            case "Livrată":
                                //Toast.makeText(view.getContext(), "Selected livrată", Toast.LENGTH_SHORT).show();
                                NotificationModel notificationModel = new NotificationModel("Deliver2Me","Comanda ta a fost livrată. Mulțumim!");
                                NotificationBuilder notificationBuilder = new NotificationBuilder();

                                FirebaseHelper.usersDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds : snapshot.getChildren())
                                        {
                                            User model = ds.getValue(User.class);
                                            String authorFromModel = model.getFirstName()+" "+model.getLastName();
                                            if(authorFromModel.equals(author))
                                            {
                                                keyForUser = ds.getKey();
                                                FirebaseHelper.tokensDatabase.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ds : snapshot.getChildren())
                                                        {
                                                            if(ds.getKey().equals(keyForUser) && !keyForUser.isEmpty())
                                                            {
                                                                Token model = ds.getValue(Token.class);
                                                                tokenForUser = model.getToken();
                                                                notificationBuilder.setUserToken(tokenForUser);
                                                                notificationBuilder.setNotificationModel(notificationModel);
                                                                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationBuilder);

                                                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                        Toast.makeText(view.getContext(), "FAILED", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                FirebaseHelper.notificationsDatabase.child(title).removeValue();
                                                                Toast.makeText(view.getContext(), "Livrată", Toast.LENGTH_SHORT).show();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Intent intent = new Intent(view.getContext(), CourierFrontPageActivity.class);
                                                                        view.getContext().startActivity(intent);
                                                                        //finish
                                                                    }
                                                                },2000);

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

                                //delete from couriers taken list and send a notification to author
                                break;

                            case "În livrare":
                                Toast.makeText(view.getContext(), "Selected in livrare", Toast.LENGTH_SHORT).show();
                                NotificationModel notificationModel2 = new NotificationModel("Deliver2Me","Comanda ta este în curs de livrare!");
                                NotificationBuilder notificationBuilder2 = new NotificationBuilder();

                                FirebaseHelper.usersDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds : snapshot.getChildren())
                                        {
                                            User model = ds.getValue(User.class);
                                            String authorFromModel = model.getFirstName()+" "+model.getLastName();
                                            if(authorFromModel.equals(author))
                                            {
                                                keyForUser = ds.getKey();
                                                FirebaseHelper.tokensDatabase.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ds : snapshot.getChildren())
                                                        {
                                                            if(ds.getKey().equals(keyForUser) && !keyForUser.isEmpty())
                                                            {
                                                                Token model = ds.getValue(Token.class);
                                                                tokenForUser = model.getToken();
                                                                notificationBuilder2.setUserToken(tokenForUser);
                                                                notificationBuilder2.setNotificationModel(notificationModel2);
                                                                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationBuilder2);

                                                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                        //Toast.makeText(view.getContext(), "DONE", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                        Toast.makeText(view.getContext(), "FAILED", Toast.LENGTH_SHORT).show();
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
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                                //send a notification to author
                                break;

                            case "Anulată":
                                Toast.makeText(view.getContext(), "Selected anulata", Toast.LENGTH_SHORT).show();

                                NotificationModel notificationModel3 = new NotificationModel("Deliver2Me","Comanda ta a fost anulată!");
                                NotificationBuilder notificationBuilder3 = new NotificationBuilder();


                                FirebaseHelper.usersDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds : snapshot.getChildren())
                                        {
                                            User model = ds.getValue(User.class);
                                            String authorFromModel = model.getFirstName()+" "+model.getLastName();
                                            if(authorFromModel.equals(author))
                                            {
                                                keyForUser = ds.getKey();
                                                FirebaseHelper.tokensDatabase.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ds : snapshot.getChildren())
                                                        {
                                                            if(ds.getKey().equals(keyForUser) && !keyForUser.isEmpty())
                                                            {
                                                                Token model = ds.getValue(Token.class);
                                                                tokenForUser = model.getToken();
                                                                notificationBuilder3.setUserToken(tokenForUser);
                                                                notificationBuilder3.setNotificationModel(notificationModel3);
                                                                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationBuilder3);

                                                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                        Toast.makeText(view.getContext(), "FAILED", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                FirebaseHelper.notificationsDatabase.child(title).removeValue();
                                                                Toast.makeText(view.getContext(), "Anulată", Toast.LENGTH_SHORT).show();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Intent intent = new Intent(view.getContext(), CourierFrontPageActivity.class);
                                                                        view.getContext().startActivity(intent);
                                                                        //finish
                                                                    }
                                                                },2000);
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

                                //send a notification to author
                                break;
                            default:
                                Toast.makeText(view.getContext(), "None selected", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setNegativeButton("Anulează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                })
                .setTitle("Selectează statusul");

        builder.setView(view);

        return builder.create();
    }

}


