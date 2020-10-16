package com.laodev.socialdis.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.MainActivity;
import com.laodev.socialdis.activity.QRScanActivity;
import com.laodev.socialdis.adapter.UserAdapter;
import com.laodev.socialdis.dialog.ContactDialog;
import com.laodev.socialdis.listener.MainActivityListener;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {

    private MainActivity mActivity;
    private UserAdapter userAdapter;
    private List<UserModel> teamUsers = new ArrayList<>();


    public FriendFragment(MainActivity mainActivity) {
        mActivity = mainActivity;
        mActivity.setMainActivityListener(new MainActivityListener() {
            @Override
            public void onAddTeamSuccess(UserModel userModel) {
                teamUsers.add(userModel);
                SharedPreferenceUtil.setTeamUsers(new Gson().toJson(teamUsers));
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickFabBtn() {
                ContactDialog dialog = new ContactDialog(mActivity, teamUsers);
                dialog.setContactDialogInterface(userModel -> {
                    teamUsers.add(userModel);
                    SharedPreferenceUtil.setTeamUsers(new Gson().toJson(teamUsers));
                    userAdapter.notifyDataSetChanged();
                });
                dialog.show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_friend, container, false);
        initToolbar(fragment);
        initView(fragment);
        return fragment;
    }

    private void initToolbar(View fragment) {
        Toolbar toolBar = fragment.findViewById(R.id.toolbar);
        if (toolBar != null) {
            mActivity.setSupportActionBar(toolBar);
            toolBar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.navigation_scan) {
                    AppUtil.REQUESTCODE = AppUtil.ADD_TEAM_REQUEST;
                    startActivityForResult(new Intent(getActivity(), QRScanActivity.class), AppUtil.ADD_TEAM_REQUEST);
                    return true;
                }
                return false;
            });
        }
    }

    private void initView(View fragment) {
        ListView lst_user = fragment.findViewById(R.id.lst_user);
        userAdapter = new UserAdapter(getContext(), teamUsers, 0);
        userAdapter.setUserAdapterInterface(new UserAdapter.UserAdapterInterface() {
            @Override
            public void onRemoveTeam(UserModel userModel) {
                teamUsers.remove(userModel);
                SharedPreferenceUtil.setTeamUsers(new Gson().toJson(teamUsers));
                userAdapter.notifyDataSetChanged();
            }
        });
        lst_user.setAdapter(userAdapter);
        initData();
    }

    private void initData() {
        ProgressDialog dialog = ProgressDialog.show(mActivity, "", getString(R.string.pgr_connect_server));
        UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
        currentUser.getTeamUsers(new UserModel.UserModelInterface() {
            @Override
            public void onSuccess(List<UserModel> userModels) {
                dialog.dismiss();
                teamUsers.addAll(userModels);
                SharedPreferenceUtil.setTeamUsers(new Gson().toJson(teamUsers));
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                dialog.dismiss();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
