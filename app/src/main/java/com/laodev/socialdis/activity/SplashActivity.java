package com.laodev.socialdis.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.laodev.socialdis.R;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.PermissionsUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 451;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (PermissionsUtil.hasPermissions(SplashActivity.this)) {
                onCheckApp();
            } else {
                requestPermissions();
            }
        }, 1500);
    }

    private void onCheckApp() {
        HttpUtil.isCheckAppVersion(this, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                if (ret == 10000) {
                    startLoginActivity();
                } else {
                    try {
                        String error = obj.getString("result");
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashActivity.this);
                        dialogBuilder.setTitle(R.string.warning)
                                .setMessage(error)
                                .setPositiveButton(android.R.string.yes, (dialog1, which) -> {
                                    // Continue with delete operation
                                    final String appPackageName = SplashActivity.this.getPackageName();
                                    try {
                                        SplashActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        SplashActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://market.android.com/search?q=pname:" + appPackageName)));
                                    }
                                })
                                .setNegativeButton(android.R.string.no, (dialog1, which) -> {
                                    AppUtil.exitApp(SplashActivity.this);
                                })
                                .show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppUtil.exitApp(SplashActivity.this);
                    }
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                AppUtil.showToast(SplashActivity.this, e.getMessage());
                AppUtil.exitApp(SplashActivity.this);
            }

            @Override
            public void onEventServerError(Exception e) {
                AppUtil.showToast(SplashActivity.this, e.getMessage());
                AppUtil.exitApp(SplashActivity.this);
            }
        });
    }

    private void startLoginActivity() {
        AppUtil.showOtherActivity(this, LoginActivity.class, 0);
        finish();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PermissionsUtil.permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionsUtil.permissionsGranted(grantResults)) {
            onCheckApp();
        } else {
            AppUtil.exitApp(SplashActivity.this);
        }
    }

}
