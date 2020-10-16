package com.laodev.socialdis.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.laodev.socialdis.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtil {

    private final static String USERKEY = "CURRENTUSER";
    private final static String BLESTATUS = "BLESTATUS";
    private final static String TEAMUSERS = "TEAMUSERS";
    private final static String WARNINGDISTACE = "WARNINGDISTACE";
    private final static String WARNINGCOLOR = "WARNINGCOLOR";
    private final static String DAMAGEDISTACE = "DAMAGEDISTACE";
    private final static String DAMAGECOLOR = "DAMAGECOLOR";
    private final static String VIBRATE = "VIBRATE";
    private final static String SOUND = "SOUND";
    private final static String UNIT = "UNIT";
    private final static String TEAMUSER = "TEAMUSER";
    private final static String TRACKINGFREQUENCY = "TRACKINGFREQUENCY";

    private final static String PREFEMAIL = "PREFEMAIL";
    private final static String PREFPASS = "PREFPASS";
    private final static String PREFREMEMBER = "PREFREMEMBER";


    private static SharedPreferences mSharedPreference;

    public static void getInstance(Context context) {
        mSharedPreference = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
    }

    public static void saveCurrentUser(String jsonUser) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(USERKEY, jsonUser);
        editor.apply();
    }

    public static UserModel getCurrentUser() {
        String jsonUser = mSharedPreference.getString(USERKEY, "");
        return new Gson().fromJson(jsonUser, UserModel.class);
    }

    public static boolean getBLEStatus() {
        return mSharedPreference.getBoolean(BLESTATUS, false);
    }

    public static void setBLEStatus(boolean flag) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(BLESTATUS, flag);
        editor.apply();
    }

    public static void setTeamUsers(String info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString(TEAMUSERS, info);
            editor.apply();
        }
    }

    public static List<UserModel> getTeamUsers() {
        List<UserModel> teamUsers = new ArrayList<>();
        String paymentsStr = mSharedPreference.getString(TEAMUSERS, null);
        if (paymentsStr != null) {
            Type listType = new TypeToken<List<UserModel>>(){}.getType();
            teamUsers.addAll(new Gson().fromJson(paymentsStr, listType));
        }
        return teamUsers;
    }

    public static float getWaningDistance() {
        return mSharedPreference.getFloat(WARNINGDISTACE, (float) 15.0);
    }

    public static void setWaningDistance(float info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putFloat(WARNINGDISTACE, info);
            editor.apply();
        }
    }

    public static String getWaningColor() {
        return mSharedPreference.getString(WARNINGCOLOR, "#ffff00");
    }

    public static void setWarningColor(String info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString(WARNINGCOLOR, info);
            editor.apply();
        }
    }

    public static float getDamageDistance() {
        return mSharedPreference.getFloat(DAMAGEDISTACE, (float) 6.0);
    }

    public static void setDamageDistance(float info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putFloat(DAMAGEDISTACE, info);
            editor.apply();
        }
    }

    public static String getDamageColor() {
        return mSharedPreference.getString(DAMAGECOLOR, "#ff0000");
    }

    public static void setDamageColor(String info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString(DAMAGECOLOR, info);
            editor.apply();
        }
    }

    public static int getTrackingFrequence() { return mSharedPreference.getInt(TRACKINGFREQUENCY, 60); }

    public static void setTrackingFrequence(int info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putInt(TRACKINGFREQUENCY, info);
            editor.apply();
        }
    }

    public static String getUnit() {
        return mSharedPreference.getString(UNIT, "Feet");
    }

    public static void setUnit(String info) {
        if (mSharedPreference != null) {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString(UNIT, info);
            editor.apply();
        }
    }

    public static boolean isVibrateEnabled() {
        return mSharedPreference.getBoolean(VIBRATE, true);
    }

    public static void setVibrateEnabled(boolean flag) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(VIBRATE, flag);
        editor.apply();
    }

    public static boolean isTeamUserEnabled() {
        return mSharedPreference.getBoolean(TEAMUSER, false);
    }

    public static void setTeamUserEnabled(boolean flag) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(TEAMUSER, flag);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void clear() {
        mSharedPreference.edit().clear();
        mSharedPreference.edit().apply();
    }

    public static void setLoginRemember(String email, String pass, boolean isRemember) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(PREFEMAIL, email);
        editor.putString(PREFPASS, pass);
        editor.putBoolean(PREFREMEMBER, isRemember);
        editor.apply();
    }

    public static boolean getRemember() {
        return mSharedPreference.getBoolean(PREFREMEMBER, false);
    }

    public static String getEmail() {
        return mSharedPreference.getString(PREFEMAIL, "");
    }

    public static String getPass() {
        return mSharedPreference.getString(PREFPASS, "");
    }

}
