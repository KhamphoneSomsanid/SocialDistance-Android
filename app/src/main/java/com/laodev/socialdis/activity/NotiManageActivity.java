package com.laodev.socialdis.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;
import com.laodev.socialdis.util.SharedPreferenceUtil;

public class NotiManageActivity extends AppCompatActivity {

    private TextView lbl_sound;
    private Switch swt_user, swt_vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_manage);

        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    private void initView() {
        lbl_sound = findViewById(R.id.lbl_sound);

        swt_user = findViewById(R.id.swt_user);
        swt_vibrate = findViewById(R.id.swt_vibrate);

        initData();
        initEvent();
    }

    private void initData() {
        swt_user.setChecked(SharedPreferenceUtil.isTeamUserEnabled());
        swt_vibrate.setChecked(SharedPreferenceUtil.isVibrateEnabled());
    }

    private void initEvent() {
        lbl_sound.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            } else {
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            }

            startActivity(intent);
        });
        swt_user.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferenceUtil.setTeamUserEnabled(b);
            initData();
        });
        swt_vibrate.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferenceUtil.setVibrateEnabled(b);
            initData();
        });
    }

}
