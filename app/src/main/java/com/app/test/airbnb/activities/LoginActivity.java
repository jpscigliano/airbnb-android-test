package com.app.test.airbnb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.test.airbnb.R;
import com.app.test.airbnb.activities.base.BaseAppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends BaseAppCompatActivity {


    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (isFacebookLoggedIn()) {
            MainActivity.start(LoginActivity.this);
            finish();
        } else {
            fbLoginButton = (LoginButton) findViewById(R.id._fb_login);
            callbackManager = CallbackManager.Factory.create();
            fbLoginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {
                            MainActivity.start(LoginActivity.this);
                            finish();
                        }

                        @Override
                        public void onCancel() {

                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.cancel_FB,
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(final FacebookException exception) {
                            // App code
                            Toast.makeText(
                                    LoginActivity.this,
                                    R.string.error_FB,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }
}

