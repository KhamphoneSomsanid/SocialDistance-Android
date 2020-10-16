package com.laodev.socialdis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.LocationActivity;
import com.laodev.socialdis.model.HistoryModel;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.LocationUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<HistoryModel> historyModels;


    public HistoryAdapter(Context context, List<HistoryModel> histories) {
        mContext = context;
        historyModels = histories;
    }

    @Override
    public int getCount() {
        return historyModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HistoryModel historyModel = historyModels.get(i);

        view = LayoutInflater.from(mContext).inflate(R.layout.ui_history, null);

        TextView lbl_status = view.findViewById(R.id.lbl_status);
        ImageView img_status = view.findViewById(R.id.img_status);
        if (historyModel.status.equals(mContext.getString(R.string.warning))) {
            lbl_status.setText(mContext.getString(R.string.warning));
            lbl_status.setTextColor(Color.parseColor(SharedPreferenceUtil.getWaningColor()));
            img_status.setColorFilter(Color.parseColor(SharedPreferenceUtil.getWaningColor()));
        } else {
            lbl_status.setText(mContext.getString(R.string.danger));
            lbl_status.setTextColor(Color.parseColor(SharedPreferenceUtil.getDamageColor()));
            img_status.setColorFilter(Color.parseColor(SharedPreferenceUtil.getDamageColor()));
        }

        TextView lbl_name = view.findViewById(R.id.lbl_name);
        TextView lbl_location = view.findViewById(R.id.lbl_location);
        CircleImageView civ_avatar = view.findViewById(R.id.civ_avatar);
        UserModel.getUserByMacAddress(historyModel.address, new UserModel.UserModelInterface() {
            @Override
            public void onSuccess(UserModel userModel) {
                lbl_name.setText(userModel.getShowName());
                lbl_location.setText(LocationUtil.getAddressFromLatlng(mContext, historyModel.latitude, historyModel.longitude));
                if (userModel.imgurl != null && !userModel.imgurl.isEmpty()) {
                    Picasso.get()
                            .load(userModel.imgurl)
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(civ_avatar);
                }
            }

            @Override
            public void onNoFound() {
                lbl_name.setText("Unkown User");
                lbl_location.setText("Unkown User");
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });

        TextView lbl_time = view.findViewById(R.id.lbl_time);
        lbl_time.setText(AppUtil.getDate(historyModel.timeStamp));
        TextView lbl_distance = view.findViewById(R.id.lbl_distance);
        String unit = "m";
        double distance = AppUtil.getDistanceByMeter(historyModel.rssi);
        if (SharedPreferenceUtil.getUnit().equals(mContext.getString(R.string.feet))) {
            unit = "ft";
            distance = AppUtil.getDistanceByFeet(historyModel.rssi);
        }
        lbl_distance.setText(String.format(Locale.getDefault(), "%.2f %s", distance, unit));
        view.setOnClickListener(view1 -> {
            UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
            if (currentUser.is_prime == 0) {
                AppUtil.showToast(mContext, "This feature can be used by Prime User only.");
                return;
            }
            AppUtil.gSelHistoryModel = historyModel;
            AppUtil.showOtherActivity(mContext, LocationActivity.class, 0);
        });
        return view;
    }

}
