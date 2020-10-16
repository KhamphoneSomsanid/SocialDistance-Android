package com.laodev.socialdis.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.MainActivity;
import com.laodev.socialdis.listener.MainActivityListener;
import com.laodev.socialdis.service.BluetoothService;
import com.laodev.socialdis.util.SharedPreferenceUtil;

public class HomeFragment extends Fragment {

    private final static int QRSIZE = 500;

    private MainActivity mActivity;
    private TextView lbl_status;
    private ImageView img_status;


    public HomeFragment(MainActivity mainActivity) {
        mActivity = mainActivity;
        mActivity.setMainActivityListener(new MainActivityListener() {
            @Override
            public void onBLEScanningStart() {
                Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(eintent, 1);

                lbl_status.setText(R.string.search_nearby_run);
                lbl_status.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
                img_status.setImageResource(R.drawable.ic_bluetooth_searching);

                SharedPreferenceUtil.setBLEStatus(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mActivity.startForegroundService(new Intent(mActivity, BluetoothService.class));
                } else {
                    mActivity.startService(new Intent(mActivity, BluetoothService.class));
                }
            }

            @Override
            public void onBLEScanningStop() {
                lbl_status.setText(R.string.search_nearby_stop);
                lbl_status.setTextColor(ContextCompat.getColor(mActivity, R.color.grey));
                img_status.setImageResource(R.drawable.ic_bluetooth_warning);

                SharedPreferenceUtil.setBLEStatus(false);
                mActivity.stopService(new Intent(mActivity, BluetoothService.class));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);
        initView(fragment);
        return fragment;
    }

    private void initView(View fragment) {
        ImageView img_qrcode = fragment.findViewById(R.id.img_qrcode);
        try {
            img_qrcode.setImageBitmap(encodeQRCode(SharedPreferenceUtil.getCurrentUser().device_id));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        lbl_status = fragment.findViewById(R.id.lbl_status);
        img_status = fragment.findViewById(R.id.img_status);
        if (SharedPreferenceUtil.getBLEStatus()) {
            Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(eintent, 1);

            lbl_status.setText(R.string.search_nearby_run);
            lbl_status.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
            img_status.setImageResource(R.drawable.ic_bluetooth_searching);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mActivity.startForegroundService(new Intent(mActivity, BluetoothService.class));
            } else {
                mActivity.startService(new Intent(mActivity, BluetoothService.class));
            }
        } else {
            lbl_status.setText(R.string.search_nearby_stop);
            lbl_status.setTextColor(ContextCompat.getColor(mActivity, R.color.grey));
            img_status.setImageResource(R.drawable.ic_bluetooth_warning);
        }
    }

    private Bitmap encodeQRCode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRSIZE, QRSIZE, null
            );
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(mActivity, R.color.black):ContextCompat.getColor(mActivity, R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, QRSIZE, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
