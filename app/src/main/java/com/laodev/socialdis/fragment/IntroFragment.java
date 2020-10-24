package com.laodev.socialdis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.IntroActivity;

public class IntroFragment extends Fragment {

    private IntroActivity.IntroModel introModel;

    public IntroFragment(IntroActivity.IntroModel model) {
        introModel = model;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_intro, container, false);
        initView(fragment);
        return fragment;
    }

    private void initView(View fragment) {
        ImageView img_logo = fragment.findViewById(R.id.img_logo);
        TextView lbl_title = fragment.findViewById(R.id.lbl_title);
        TextView lbl_desc = fragment.findViewById(R.id.lbl_desc);

        img_logo.setImageDrawable(getContext().getDrawable(introModel.res));
        lbl_title.setText(introModel.title);
        lbl_desc.setText(introModel.description);
    }

}
