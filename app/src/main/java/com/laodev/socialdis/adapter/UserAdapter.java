package com.laodev.socialdis.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.laodev.socialdis.R;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserModel> userModels;
    private int tabIndex;

    private UserAdapterInterface userAdapterInterface;

    public UserAdapter(Context context, List<UserModel> users, int index) {
        mContext = context;
        userModels = users;
        tabIndex = index;
    }

    @Override
    public int getCount() {
        return userModels.size();
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
        UserModel userModel = userModels.get(i);

        view = LayoutInflater.from(mContext).inflate(R.layout.ui_user, null);

        CircleImageView img_avatar = view.findViewById(R.id.civ_avatar);
        TextView lbl_name = view.findViewById(R.id.lbl_name);
        TextView lbl_phone = view.findViewById(R.id.lbl_phone);
        Button btn_invite = view.findViewById(R.id.btn_invite);
        Button btn_remove = view.findViewById(R.id.btn_remove);

        if (userModel.imgurl != null && !userModel.imgurl.isEmpty()) {
            Picasso.get()
                    .load(userModel.imgurl)
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(img_avatar);
        }

        lbl_name.setText(userModel.getShowName());
        lbl_phone.setText(userModel.phone);
        btn_invite.setOnClickListener(v -> {
            UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
            ProgressDialog dialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.pgr_connect_server));
            currentUser.addTeamUser(userModel, new UserModel.UserModelInterface() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    userAdapterInterface.onAddTeam(userModel);
                }

                @Override
                public void onFailed(String error) {
                    dialog.dismiss();
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        btn_remove.setOnClickListener(v -> {
            UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
            ProgressDialog dialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.pgr_connect_server));
            currentUser.removeTeamUser(userModel, new UserModel.UserModelInterface() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    userAdapterInterface.onRemoveTeam(userModel);
                }

                @Override
                public void onFailed(String error) {
                    dialog.dismiss();
                    Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        if (tabIndex == 0) {
            btn_invite.setVisibility(View.GONE);
            btn_remove.setVisibility(View.VISIBLE);
        } else {
            btn_invite.setVisibility(View.VISIBLE);
            btn_remove.setVisibility(View.GONE);
        }

        return view;
    }

    public void setUserAdapterInterface(UserAdapterInterface userAdapterInterface) {
        this.userAdapterInterface = userAdapterInterface;
    }

    public interface UserAdapterInterface {
        default void onAddTeam(UserModel userModel) {}
        default void onRemoveTeam(UserModel userModel) {}
    }

}
