package com.softtech.mbook;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class mBook extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //set offline database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
    }
}
