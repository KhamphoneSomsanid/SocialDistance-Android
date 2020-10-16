package com.laodev.socialdis.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.laodev.socialdis.R;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.ui.CustomEditText;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView civ_avatar;
    private ImageButton imb_edit;
    private CustomEditText cet_name;
    private CustomEditText cet_phone;

    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolBar();
        initView();
    }

    private void initToolBar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    private void initView() {
        civ_avatar = findViewById(R.id.civ_avatar);
        imb_edit = findViewById(R.id.imb_edit);
        cet_name = findViewById(R.id.cet_name);
        cet_phone = findViewById(R.id.cet_phone);
        CustomEditText cet_email = findViewById(R.id.cet_email);
        cet_email.setInputText(SharedPreferenceUtil.getCurrentUser().email);
        cet_email.setDisEditable();

        initData();
        initEvent();
    }

    private void initData() {
        UserModel currentUser = SharedPreferenceUtil.getCurrentUser();

        if (currentUser.imgurl != null && !currentUser.imgurl.isEmpty()) {
            Picasso.get()
                    .load(HttpUtil.AVATAR_URL + currentUser.imgurl)
                    .centerCrop().fit()
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(civ_avatar);
        }
        cet_name.setInputText(currentUser.getShowName());
        cet_phone.setInputText(currentUser.phone);
    }

    private void initEvent() {
        imb_edit.setOnClickListener(view -> CropImage.activity()
                .setMaxCropResultSize(2048, 2048)
                .setAutoZoomEnabled(false)
                .start(ProfileActivity.this));
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

    public void onClickUpdateBtn(View view) {
        String name = cet_name.getInputText();
        if (name.isEmpty()) {
            AppUtil.showToast(this, "Your name is empty.");
            return;
        }
        String phone = cet_phone.getInputText();
        if (phone.isEmpty()) {
            AppUtil.showToast(this, "Your phone number is empty.");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("id", SharedPreferenceUtil.getCurrentUser().id);
        params.put("name", name);
        params.put("phone", phone);
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            params.put("base64", base64);
        }

        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...");
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_update_profile, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                dialog.dismiss();
                try {
                    if (ret == 10000) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        SharedPreferenceUtil.saveCurrentUser(new Gson().toJson(userModel));
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    AppUtil.showToast(ProfileActivity.this, e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(ProfileActivity.this, e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(ProfileActivity.this, e.getMessage());
            }
        });
    }

}
