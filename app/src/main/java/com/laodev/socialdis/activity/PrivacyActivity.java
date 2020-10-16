package com.laodev.socialdis.activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        initToolbar();
        initUIView();
    }

    private void initUIView() {
        WebView webView = findViewById(R.id.wbv_privacy);
        webView.loadUrl("http://privacy.daxiao-itdev.com/SocialDistance/");
    }

    private void initToolbar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }


}