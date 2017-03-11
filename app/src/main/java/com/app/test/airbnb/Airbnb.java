package com.app.test.airbnb;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Juan on 09/03/2017.
 */
public class Airbnb extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
