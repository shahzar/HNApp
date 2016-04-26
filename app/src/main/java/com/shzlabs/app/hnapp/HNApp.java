package com.shzlabs.app.hnapp;

import com.firebase.client.Firebase;

/**
 * Created by shaz on 24/4/16.
 */
public class HNApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
