package com.laodev.socialdis;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;

import com.laodev.socialdis.receiver.BluetoothReceiver;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.ContactUtils;
import com.laodev.socialdis.util.SharedPreferenceUtil;

public class BLEApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.phoneContacts = ContactUtils.getRawContacts(this);
        SharedPreferenceUtil.getInstance(this);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(new BluetoothReceiver(), filter1);
        registerReceiver(new BluetoothReceiver(), filter2);
    }

}
