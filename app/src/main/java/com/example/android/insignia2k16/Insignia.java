package com.example.android.insignia2k16;

import com.firebase.client.Firebase;

/**
 * Created by vengalrao on 29-06-2016.
 */
public class Insignia extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
