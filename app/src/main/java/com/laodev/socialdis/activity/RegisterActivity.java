package com.laodev.socialdis.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;
import com.laodev.socialdis.ui.CustomEditText;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CustomEditText cet_name, cet_email, cet_phone, cet_pass, cet_repass;
    private CircleImageView civ_avatar;
    private Bitmap bitmap = null;

    private void initEvent() {
        civ_avatar.setOnClickListener(view -> CropImage.activity()
                .setMaxCropResultSize(2048, 2048)
                .setAutoZoomEnabled(false)
                .start(RegisterActivity.this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initToolBar();
        initView();
        initEvent();
    }

    private void initView() {
        civ_avatar = findViewById(R.id.civ_avatar);
        cet_name = findViewById(R.id.cet_name);
        cet_email = findViewById(R.id.cet_email);
        cet_phone = findViewById(R.id.cet_phone);
        cet_pass = findViewById(R.id.cet_pass);
        cet_repass = findViewById(R.id.cet_repass);
    }

    private void initToolBar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    civ_avatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    public void onClickRegisterBtn(View view) {
        String name = cet_name.getInputText();
        if (name.isEmpty()) {
            AppUtil.showToast(this, "Your name is empty.");
            return;
        }
        String email = cet_email.getInputText();
        if (email.isEmpty()) {
            AppUtil.showToast(this, "Your email is empty.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            AppUtil.showToast(this, "Your email type is wrong.");
            return;
        }
        String phone = cet_phone.getInputText();
        if (phone.isEmpty()) {
            AppUtil.showToast(this, "Your phone number is empty.");
            return;
        }
        String pass = cet_pass.getInputText();
        String repass = cet_repass.getInputText();
        if (pass.isEmpty()) {
            AppUtil.showToast(this, "Your password is empty.");
            return;
        }
        if (!pass.equals(repass)) {
            AppUtil.showToast(this, "Your password is not matched.");
            return;
        }
        if (bitmap == null) {
            AppUtil.showToast(this, "Please choose your avatar image.");
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("phone", cet_phone.getFullPhoneNumber());
        params.put("password", pass);
        params.put("base64", base64);
        params.put("mac_address", AppUtil.getMacAddress());
        params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...");
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_register, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                dialog.dismiss();
                try {
                    AppUtil.showToast(RegisterActivity.this, obj.getString("result"));
                    if (ret == 10000) {
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtil.showToast(RegisterActivity.this, e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(RegisterActivity.this, e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(RegisterActivity.this, e.getMessage());
            }
        });
    }

    public void onClickLoginLbl(View view) {
        onBackPressed();
    }

}
