package com.laodev.socialdis.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.MainActivity;
import com.laodev.socialdis.adapter.HistoryAdapter;
import com.laodev.socialdis.model.HistoryModel;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private MainActivity mActivity;
    private HistoryAdapter historyAdapter;
    private List<HistoryModel> historyModelList = new ArrayList<>();

    public HistoryFragment(MainActivity mainActivity) {
        mActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_history, container, false);
        initView(fragment);
        return fragment;
    }

    private void initView(View fragment) {
        ListView lst_history = fragment.findViewById(R.id.lst_history);
        historyAdapter = new HistoryAdapter(mActivity, historyModelList);
        lst_history.setAdapter(historyAdapter);

        initData();
    }

    private void initData() {
        ProgressDialog dialog = ProgressDialog.show(mActivity, "",getString(R.string.pgr_connect_server));
        HistoryModel.getAllHistories(new HistoryModel.HistoryModelInterface() {
            @Override
            public void onSuccess(List<HistoryModel> models) {
                dialog.dismiss();
                historyModelList.clear();
                for (HistoryModel historyModel: models) {
                    if (!(historyModel.isMember == 1) || SharedPreferenceUtil.isTeamUserEnabled()) {
                        historyModelList.add(historyModel);
                    }
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                dialog.dismiss();
                Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
