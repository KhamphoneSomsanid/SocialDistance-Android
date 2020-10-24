package com.laodev.socialdis.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laodev.socialdis.R;
import com.laodev.socialdis.fragment.IntroFragment;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import me.relex.circleindicator.CircleIndicator;

public class IntroActivity extends AppCompatActivity {

    private IntroModel[] introDatas = new IntroModel[] {
            new IntroModel(R.drawable.intro_svg, "Introduction", "Social Distance helps promote social \ndistance. It notifies you when other bluetooth users\n are detected close to you."),
            new IntroModel(R.drawable.paused_svg, "Device detection", "Detection will run unless paused.\nWhile paused, the app will not detect other\n devices or be able to be detected."),
            new IntroModel(R.drawable.teams_svg, "Teams", "Use this for teams of people.\nEveryone's app must be turned ON.\nAll data is stored locally and server."),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initView();
    }

    private void initView() {
        FloatingActionButton floating = findViewById(R.id.floating);
        floating.setOnClickListener(view -> {
            SharedPreferenceUtil.setIntroStatus(true);
            AppUtil.showOtherActivity(IntroActivity.this, MainActivity.class, 0);
            finish();
        });
        floating.hide();

        ViewPager vpg_intro = findViewById(R.id.vpg_intro);
        vpg_intro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    floating.show();
                } else {
                    floating.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        IntroPageAdapter pageAdapter = new IntroPageAdapter(getSupportFragmentManager());
        vpg_intro.setAdapter(pageAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(vpg_intro);
        pageAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private class IntroPageAdapter extends FragmentStatePagerAdapter {

        public IntroPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return introDatas.length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new IntroFragment(introDatas[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    public class IntroModel {
        public int res = 0;
        public String title = "";
        public String description = "";

        public IntroModel(int res, String title, String description) {
            this.res = res;
            this.title = title;
            this.description = description;
        }
    }

}
