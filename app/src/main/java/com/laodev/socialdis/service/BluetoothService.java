package com.laodev.socialdis.service;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.NotificationUtil;

public class BluetoothService extends Service {

    private static BluetoothAdapter mBluetoothAdapter;

    public BluetoothService() {
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        AppUtil.showToast(this, "The Social Distance Service was just Created.");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil notificationHelper = new NotificationUtil(BluetoothService.this);
            Notification activeCallNotification = notificationHelper.createActiveNotification();
            startForeground(2, activeCallNotification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppUtil.showToast(this, "The Social Distance Service was just Stopped.");
    }

}
