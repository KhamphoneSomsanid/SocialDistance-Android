package com.laodev.socialdis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.laodev.socialdis.R;
import com.laodev.socialdis.fragment.FriendFragment;
import com.laodev.socialdis.fragment.HistoryFragment;
import com.laodev.socialdis.fragment.HomeFragment;
import com.laodev.socialdis.fragment.SettingFragment;
import com.laodev.socialdis.listener.MainActivityListener;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab_control;

    private int frgIndex = 0;
    private MainActivityListener mainActivityListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIView();
    }

    private void initUIView() {
        fab_control = findViewById(R.id.fab_control);
        fab_control.setOnClickListener(view -> {
            if (frgIndex == 0) {
                boolean flg = !SharedPreferenceUtil.getBLEStatus();
                SharedPreferenceUtil.setBLEStatus(flg);
                if (flg) {
                    fab_control.setImageResource(R.drawable.ic_pause);
                    mainActivityListener.onBLEScanningStart();
                } else {
                    fab_control.setImageResource(R.drawable.ic_play);
                    mainActivityListener.onBLEScanningStop();
                }
            } else {
                mainActivityListener.onClickFabBtn();
            }
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        loadFragmentByIndex(0);
    }

    private void loadFragmentByIndex(int index) {
        frgIndex = index;

        Fragment frg = null;
        switch (index) {
            case 0:
                fab_control.show();
                if (SharedPreferenceUtil.getBLEStatus()) {
                    fab_control.setImageResource(R.drawable.ic_pause);
                } else {
                    fab_control.setImageResource(R.drawable.ic_play);
                }
                frg = new HomeFragment(this);
                break;
            case 1:
                fab_control.show();
                fab_control.setImageResource(R.drawable.ic_add);
                frg = new FriendFragment(this);
                break;
            case 2:
                fab_control.hide();
                frg = new HistoryFragment(this);
                break;
            case 3:
                fab_control.hide();
                frg = new SettingFragment(this);
                break;
        }
        onLoadFragment(frg);
    }

    private void onLoadFragment(Fragment frg) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction frgTran = fm.beginTransaction();
        frgTran.replace(R.id.frg_body, frg);
        frgTran.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragmentByIndex(0);
                break;
            case R.id.navigation_friend:
                loadFragmentByIndex(1);
                break;
            case R.id.navigation_history:
                loadFragmentByIndex(2);
                break;
            case R.id.navigation_setting:
                loadFragmentByIndex(3);
                break;
        }
        return true;
    }

    public void setMainActivityListener(MainActivityListener mainActivityListener) {
        this.mainActivityListener = mainActivityListener;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_friend_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppUtil.REQUESTCODE == AppUtil.ADD_TEAM_REQUEST) {
            UserModel userModel;
            if (data != null) {
                userModel = new Gson().fromJson(data.getStringExtra("USERINFO"), UserModel.class);
                if (userModel != null) {
                    mainActivityListener.onAddTeamSuccess(userModel);
                }
            }
        }
    }

}