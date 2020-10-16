package com.laodev.socialdis.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.LoginActivity;
import com.laodev.socialdis.activity.MainActivity;
import com.laodev.socialdis.activity.NotiManageActivity;
import com.laodev.socialdis.activity.PrivacyActivity;
import com.laodev.socialdis.activity.ProfileActivity;
import com.laodev.socialdis.activity.TrackMangerActivity;
import com.laodev.socialdis.activity.UpgradeActivity;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    private MainActivity mActivity;
    private ImageView img_edit;
    private CircleImageView civ_avatar;
    private TextView lbl_name, lbl_phone, lbl_member, lbl_expire, lbl_regdate;
    private ImageView img_member;
    private Button btn_upgrade;


    public SettingFragment(MainActivity mainActivity) {
        mActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(fragment);
        return fragment;
    }

    private void initView(View fragment) {
        img_edit = fragment.findViewById(R.id.img_edit);
        civ_avatar = fragment.findViewById(R.id.civ_avatar);
        lbl_name = fragment.findViewById(R.id.lbl_name);
        lbl_phone = fragment.findViewById(R.id.lbl_phone);
        lbl_regdate = fragment.findViewById(R.id.lbl_regdate);

        img_member = fragment.findViewById(R.id.img_member);
        lbl_member = fragment.findViewById(R.id.lbl_member);
        lbl_expire = fragment.findViewById(R.id.lbl_expire);
        btn_upgrade = fragment.findViewById(R.id.btn_upgrade);
        btn_upgrade.setOnClickListener(view -> AppUtil.showOtherActivity(mActivity, UpgradeActivity.class, 0));

        Button btn_rate = fragment.findViewById(R.id.btn_rate);
        btn_rate.setOnClickListener(view -> {
            final String appPackageName = mActivity.getPackageName();
            try {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        Button btn_share = fragment.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invitation_title));
            shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.invitation_text),
                    getContext().getPackageName()));
            startActivity(Intent.createChooser(shareIntent, "Share using.."));
        });

        Button btn_bluetooth = fragment.findViewById(R.id.btn_bluetooth);
        btn_bluetooth.setOnClickListener(view -> AppUtil.showOtherActivity(mActivity, TrackMangerActivity.class, 0));
        Button btn_notification = fragment.findViewById(R.id.btn_notification);
        btn_notification.setOnClickListener(view -> AppUtil.showOtherActivity(mActivity, NotiManageActivity.class, 0));
        Button btn_logout = fragment.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view -> {
            SharedPreferenceUtil.clear();
            AppUtil.showOtherActivity(mActivity, LoginActivity.class, 1);
            mActivity.finish();
        });
        Button btn_privacy = fragment.findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(view -> AppUtil.showOtherActivity(mActivity, PrivacyActivity.class, 0));

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
        lbl_name.setText(currentUser.getShowName());
        lbl_phone.setText(currentUser.phone);
        lbl_regdate.setText(currentUser.regdate);

        if (currentUser.is_prime == 1) {
            lbl_member.setText(R.string.prime_member);
            lbl_member.setTextColor(mActivity.getColor(R.color.colorPrimary));
            img_member.setImageDrawable(mActivity.getDrawable(R.drawable.ic_prime_user));
            btn_upgrade.setVisibility(View.GONE);
        }
        lbl_expire.setText(currentUser.expiredate);
    }

    private void initEvent() {
        img_edit.setOnClickListener(view -> AppUtil.showOtherActivity(mActivity, ProfileActivity.class, 0));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

}
