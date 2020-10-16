package com.laodev.socialdis.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.laodev.socialdis.R;
import com.laodev.socialdis.model.HistoryModel;
import com.laodev.socialdis.model.PhoneContact;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppUtil {

    private static double amplifyARate = 70.625;
    private static double noiseRate = -1.7857;

    public static int REQUESTCODE = 0;
    public static final int ADD_TEAM_REQUEST = 10001;

    public static List<PhoneContact> phoneContacts = new ArrayList<>();
    public static HistoryModel gSelHistoryModel = new HistoryModel();


    public static void showOtherActivity (Context context, Class<?> cls, int direction) {
        Intent myIntent = new Intent(context, cls);
        ActivityOptions options;
        switch (direction) {
            case 0:
                options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left);
                context.startActivity(myIntent, options.toBundle());
                break;
            case 1:
                options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right);
                context.startActivity(myIntent, options.toBundle());
                break;
            default:
                context.startActivity(myIntent);
                break;
        }
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public static void exitApp(Activity context) {
        context.moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static double getDistanceByMeter(int rssi) {
        return Math.pow(10.0, (rssi + amplifyARate) / (10 * noiseRate));
    }

    public static double getDistanceByFeet(int rssi) {
        return Math.pow(10.0, (rssi + amplifyARate) / (10 * noiseRate))  * 3.281;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void showToast(Context context, String text) {
        DynamicToast.make(context, text, context.getDrawable(R.drawable.ic_alert), context.getColor(R.color.white), context.getColor(R.color.colorPrimary), Toast.LENGTH_SHORT).show();
    }

}
