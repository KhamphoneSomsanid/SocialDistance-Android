package com.laodev.socialdis.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;
import com.laodev.socialdis.ui.CustomEditText;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceChangeActivity extends AppCompatActivity {

    private CustomEditText cet_email, cet_pass;
    private EditText txt_reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_change);

        initToolBar();
        initView();
    }

    private void initView() {
        cet_email = findViewById(R.id.cet_email);
        cet_pass = findViewById(R.id.cet_pass);
        txt_reason = findViewById(R.id.txt_reason);
    }

    private void initToolBar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    @SuppressLint("HardwareIds")
    public void onClickChangeBtn(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("email", cet_email.getInputText());
        params.put("password", cet_pass.getInputText());
        params.put("other", txt_reason.getText().toString());
        params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...");
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_change_device, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                dialog.dismiss();
                try {
                    AppUtil.showToast(DeviceChangeActivity.this, obj.getString("result"));
                } catch (JSONException e) {
                    AppUtil.showToast(DeviceChangeActivity.this, e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(DeviceChangeActivity.this, e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(DeviceChangeActivity.this, e.getMessage());
            }
        });
    }

}
