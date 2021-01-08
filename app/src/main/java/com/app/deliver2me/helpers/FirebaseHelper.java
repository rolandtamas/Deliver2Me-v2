package com.app.deliver2me.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    public static final DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference("utilizatori inregistrati");
    public static final DatabaseReference couriersDatabase = FirebaseDatabase.getInstance().getReference("curieri");
    public static final DatabaseReference adsDatabase = FirebaseDatabase.getInstance().getReference("anunturi");
}
