package com.laodev.socialdis.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.laodev.socialdis.R;
import com.laodev.socialdis.adapter.UserAdapter;
import com.laodev.socialdis.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ContactDialog extends Dialog {

    private UserAdapter userAdapter;
    private List<UserModel> teamUsers;
    private List<UserModel> userModelList = new ArrayList<>();
    private List<UserModel> showUsers = new ArrayList<>();

    private ContactDialogInterface contactDialogInterface;


    public ContactDialog(@NonNull Context context, List<UserModel> teamUsers) {
        super(context);

        setContentView(R.layout.dig_contact);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTitle(null);
        setCanceledOnTouchOutside(true);

        this.teamUsers = teamUsers;

        initView();
    }

    private void initView() {
        ListView lst_user = findViewById(R.id.lst_user);
        userAdapter = new UserAdapter(getContext(), showUsers, 1);
        userAdapter.setUserAdapterInterface(new UserAdapter.UserAdapterInterface() {
            @Override
            public void onAddTeam(UserModel userModel) {
                dismiss();
                contactDialogInterface.onAddTeamDialogCallback(userModel);
            }
        });
        lst_user.setAdapter(userAdapter);
        initData();
    }

    private void initData() {
        ProgressDialog dialog = ProgressDialog.show(getContext(), "", getContext().getString(R.string.pgr_connect_server));
        UserModel.getAllUsers(getContext(), new UserModel.UserModelInterface() {
            @Override
            public void onSuccess(List<UserModel> userModels) {
                dialog.dismiss();
                userModelList.addAll(userModels);
                showView();
            }

            @Override
            public void onFailed(String error) {
                dialog.dismiss();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showView() {
        showUsers.clear();
        for (UserModel user: userModelList) {
            boolean isTeam = false;
            for (UserModel teamUser: teamUsers) {
                if (user.id.equals(teamUser.id)) {
                    isTeam = true;
                    break;
                }
            }
            if (!isTeam) {
                showUsers.add(user);
            }
        }
        if (showUsers.size() == 0) {
            dismiss();
            Toast.makeText(getContext(), R.string.empty_user, Toast.LENGTH_SHORT).show();
        }
        userAdapter.notifyDataSetChanged();
    }

    public void setContactDialogInterface(ContactDialogInterface contactDialogInterface) {
        this.contactDialogInterface = contactDialogInterface;
    }

    public interface ContactDialogInterface {
        void onAddTeamDialogCallback(UserModel userModel);
    }

}
