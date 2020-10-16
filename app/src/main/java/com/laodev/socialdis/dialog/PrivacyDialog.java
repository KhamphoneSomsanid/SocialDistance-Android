package com.laodev.socialdis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.laodev.socialdis.R;

public class PrivacyDialog extends Dialog {

    public PrivacyDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dig_privacy_policy);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTitle(null);
        setCanceledOnTouchOutside(true);

        initView();
    }

    private void initView() {
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(view -> dismiss());
        Button btn_agree = findViewById(R.id.btn_agree);
        btn_agree.setOnClickListener(view -> dismiss());
        WebView webView = findViewById(R.id.wbv_privacy);
        webView.loadUrl("http://privacy.daxiao-itdev.com/SocialDistance/");
    }

}
