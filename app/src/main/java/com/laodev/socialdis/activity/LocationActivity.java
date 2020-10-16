package com.laodev.socialdis.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;
import com.laodev.socialdis.util.AppUtil;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initToolbar();
        initUIView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUIView() {
        WebView webView = findViewById(R.id.wbv_location);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com/maps/?q=" + AppUtil.gSelHistoryModel.latitude + "," + AppUtil.gSelHistoryModel.longitude);
    }

    private void initToolbar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

}
