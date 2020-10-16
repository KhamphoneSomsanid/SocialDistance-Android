package com.laodev.socialdis.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.laodev.socialdis.R;
import com.laodev.socialdis.dialog.PrivacyDialog;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.ui.CustomEditText;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private CustomEditText cet_email, cet_pass;
    private CheckBox chk_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        cet_email = findViewById(R.id.cet_email);
        cet_pass = findViewById(R.id.cet_pass);
        chk_remember = findViewById(R.id.chk_remember);

        if (SharedPreferenceUtil.getRemember()) {
            cet_email.setInputText(SharedPreferenceUtil.getEmail());
            cet_pass.setInputText(SharedPreferenceUtil.getPass());
            chk_remember.setChecked(true);
        }
    }

    @SuppressLint("HardwareIds")
    public void onClickVerifyBtn(View view) {
        if (cet_email.getInputText().length() == 0) {
            AppUtil.showToast(this, "Your email is empty.");
            return;
        }
        if (!cet_email.getInputText().contains("@") || !cet_email.getInputText().contains(".")) {
            AppUtil.showToast(this, "Your email type is wrong.");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("email", cet_email.getInputText());
        params.put("password", cet_pass.getInputText());
        params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...");
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_login, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                dialog.dismiss();
                try {
                    if (ret == 10000) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        if (userModel.is_active == 1) {
                            if (chk_remember.isChecked()) {
                                SharedPreferenceUtil.setLoginRemember(cet_email.getInputText(), cet_pass.getInputText(), true);
                            } else {
                                SharedPreferenceUtil.setLoginRemember("", "", false);
                            }
                            SharedPreferenceUtil.saveCurrentUser(new Gson().toJson(userModel));
                            AppUtil.showOtherActivity(LoginActivity.this, MainActivity.class, 0);
                            finish();
                        } else {
                            AppUtil.showToast(LoginActivity.this, "Please check your email.");
                        }
                    } else if (ret == 10003) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        SharedPreferenceUtil.saveCurrentUser(new Gson().toJson(userModel));
                        AppUtil.showOtherActivity(LoginActivity.this, ExpireActivity.class, 0);
                        AppUtil.showToast(LoginActivity.this, "Your account was expired.");
                    } else {
                        AppUtil.showToast(LoginActivity.this, obj.getString("result"));
                    }
                } catch (JSONException e) {
                    AppUtil.showToast(LoginActivity.this, e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(LoginActivity.this, e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(LoginActivity.this, e.getMessage());
            }
        });
    }

    public void onClickPrivacyLbl(View view) {
        PrivacyDialog dialog = new PrivacyDialog(this);
        dialog.show();
    }

    public void onClickForgotPassLbl(View view) {
        AppUtil.showOtherActivity(this, ForgotActivity.class, 0);
    }

    public void onClickRegisterLbl(View view) {
        AppUtil.showOtherActivity(this, RegisterActivity.class, 0);
    }

    public void onClickChangeDeviceLbl(View view) {
        AppUtil.showOtherActivity(this, DeviceChangeActivity.class, 0);
    }

}
