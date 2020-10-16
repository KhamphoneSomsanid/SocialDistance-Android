package com.laodev.socialdis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.laodev.socialdis.R;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTeamDialog extends Dialog {

    private String userid;
    private AddTeamDialogListener addTeamDialogListener;


    public AddTeamDialog(@NonNull Context context, String id) {
        super(context);

        setContentView(R.layout.dig_add_team);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTitle(null);
        setCanceledOnTouchOutside(true);

        userid = id;

        initView();
    }

    private void initView() {
        CircleImageView civ_avatar = findViewById(R.id.civ_avatar);
        TextView lbl_name = findViewById(R.id.lbl_name);
        TextView lbl_phone = findViewById(R.id.lbl_phone);
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(view -> {
            dismiss();
            addTeamDialogListener.onFailed();
        });
        Button btn_add = findViewById(R.id.btn_add);
        
        UserModel.getUserByUserID(userid, new UserModel.UserModelInterface() {
            @Override
            public void onSuccess(UserModel userModel) {
                if (userModel.imgurl != null && !userModel.imgurl.isEmpty()) {
                    Picasso.get()
                            .load(userModel.imgurl)
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(civ_avatar);
                }
                lbl_name.setText(userModel.getShowName());
                lbl_phone.setText(userModel.phone);

                btn_add.setOnClickListener(view -> {
                    UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
                    currentUser.addTeamUser(userModel, new UserModel.UserModelInterface() {
                        @Override
                        public void onSuccess() {
                            dismiss();
                            Toast.makeText(getContext(), "Success added to team", Toast.LENGTH_SHORT).show();
                            addTeamDialogListener.onSuccess(userModel);
                        }

                        @Override
                        public void onFailed(String error) {
                            dismiss();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            addTeamDialogListener.onFailed();
                        }
                    });
                });
            }

            @Override
            public void onFailed(String error) {
                dismiss();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                addTeamDialogListener.onFailed();
            }
        });
    }

    public void setAddTeamDialogListener(AddTeamDialogListener addTeamDialogListener) {
        this.addTeamDialogListener = addTeamDialogListener;
    }

    public interface AddTeamDialogListener {
        void onSuccess(UserModel userModel);
        void onFailed();
    }

}
