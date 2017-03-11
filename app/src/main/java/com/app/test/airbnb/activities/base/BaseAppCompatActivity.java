package com.app.test.airbnb.activities.base;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Juan on 09/03/2017.
 */
public class BaseAppCompatActivity extends AppCompatActivity {


    public void start(Fragment fragment, boolean addToBackStack, Integer frameId, String fragmentTag) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction().replace(frameId, fragment, fragmentTag);
        if (addToBackStack) {
            tx.addToBackStack(fragment.getClass().getSimpleName());
        }
        tx.commit();
    }

    public void startWithAnimation(Fragment fragment, int in, int out, boolean addToBackStack, Integer frameId, String fragmentTag) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(in, out);
        tx.replace(frameId, fragment, fragmentTag);
        if (addToBackStack) {
            tx.addToBackStack(fragment.getClass().getSimpleName());
        }
        tx.commit();
    }


    public void startDialog(DialogFragment fragment, String fragmentTag) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        fragment.show(tx, fragmentTag);

    }
}