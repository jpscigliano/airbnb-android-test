package com.app.test.airbnb.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Juan on 12/03/2017.
 */
public class PermissionUtils {


    public static boolean checkPermission(String strPermission, Context _c) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
