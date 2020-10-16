package com.laodev.socialdis.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laodev.socialdis.R;
import com.laodev.socialdis.model.HistoryModel;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.service.BluetoothService;
import com.laodev.socialdis.service.GPSService;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.NotificationUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BluetoothReceiver extends BroadcastReceiver {
    
    private Context context;

    private static List<HistoryModel> historyModels = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
            onUploadTrackingData(rssi, device);

            Log.d("Receiver : ", device.toString());
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            BluetoothService.getBluetoothAdapter().startDiscovery();
        }
    }
    
    private void onUploadTrackingData(int rssi, BluetoothDevice device) {
        if (!(device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.Major.PHONE ||
                device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.Major.UNCATEGORIZED
        )) {
            return;
        }

        if (SharedPreferenceUtil.getBLEStatus()) {
            boolean isContain = false;
            boolean isDiff = false;
            int selIndex = 0;
            for (int i = 0; i < historyModels.size(); i++) {
                HistoryModel model = historyModels.get(i);
                if (model.address.equals(device.getAddress())) {
                    isContain = true;
                    long timeStamp = new Date().getTime();
                    long diff = timeStamp - model.timeStamp;
                    float timeDiff = (float) (diff / 1000.0 / SharedPreferenceUtil.getTrackingFrequence());
                    if (timeDiff > 1) {
                        isDiff = true;
                        selIndex = i;
                    }
                    break;
                }
            }

            double distance = AppUtil.getDistanceByMeter(rssi);
            if (SharedPreferenceUtil.getUnit().equals(context.getString(R.string.feet))) {
                distance = AppUtil.getDistanceByFeet(rssi);
            }
            if (distance < SharedPreferenceUtil.getWaningDistance()) {
                HistoryModel historyModel = new HistoryModel();
                historyModel.userid = SharedPreferenceUtil.getCurrentUser().id;
                historyModel.address = device.getAddress();
                historyModel.timeStamp = new Date().getTime();
                historyModel.rssi = rssi;
                historyModel.isMember = UserModel.isContainTeamUserByUUID(device.getAddress())? 1 : 0;

                GPSService gpsTracker = new GPSService(context);
                if (gpsTracker.canGetLocation()){
                    historyModel.latitude = gpsTracker.getLatitude();
                    historyModel.longitude = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }

                if (distance < SharedPreferenceUtil.getDamageDistance()) {
                    historyModel.status = context.getString(R.string.danger);
                }
                if (!isContain) {
                    historyModels.add(historyModel);
                    historyModel.add(new HistoryModel.HistoryModelInterface() {
                        @Override
                        public void onSuccess(HistoryModel model) {
                            if (!(historyModel.isMember == 1) || SharedPreferenceUtil.isTeamUserEnabled()) {
                                NotificationUtil notificationHelper = new NotificationUtil(context);
                                notificationHelper.createActiveNotification(historyModel);
                            }
                        }

                        @Override
                        public void onFailed(String error) { }
                    });
                }
                if (isDiff) {
                    historyModels.set(selIndex, historyModel);
                    historyModel.add(new HistoryModel.HistoryModelInterface() {
                        @Override
                        public void onSuccess(HistoryModel model) {
                            if (!(historyModel.isMember == 1) || SharedPreferenceUtil.isTeamUserEnabled()) {
                                NotificationUtil notificationHelper = new NotificationUtil(context);
                                notificationHelper.createActiveNotification(historyModel);
                            }
                        }

                        @Override
                        public void onFailed(String error) { }
                    });
                }
            }
        }
    }

}
