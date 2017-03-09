package com.app.test.airbnb.activities;

import android.os.Bundle;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.base.BaseAppCompatActivity;

public class LoginActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MainActivity.start(this);
        finish();
    }
}
