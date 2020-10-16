package com.laodev.socialdis.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

public class PermissionsUtil {
    //permissions we need
    public static final String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
    };


    public static final String[] videoCallPermissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    //check if user granted all permissions
    public static boolean permissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    //check if the permissions granted or not (without request permissions from user)
    public static boolean hasPermissions(Context context) {
        if (context != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
