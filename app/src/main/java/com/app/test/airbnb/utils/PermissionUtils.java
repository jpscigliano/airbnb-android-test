package com.app.test.airbnb.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/**
 * Created by Juan on 12/03/2017.
 */
public class PermissionUtils {

    public  static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1010;

    public static boolean checkPermission(String strPermission, Context _c) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

}
