package com.laodev.socialdis.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.laodev.socialdis.R;
import com.hbb20.CountryCodePicker;


public class CustomEditText extends LinearLayout {

    private int resourceLeftID, resourceRightID;
    private String inputStr, hintStr;
    private boolean isShowCCP, isShowRight;

    private EditText txt_input;
    private CountryCodePicker ccp_country;
    private ImageView img_right, img_left;

    private CustomEditTextCallback inputLayoutCallback;


    public CustomEditText(Context context) {
        super(context);

        resourceLeftID = R.drawable.ic_phone;
        resourceRightID = R.drawable.ic_delete;
        inputStr = "Default";
        isShowCCP = false;
        isShowRight = false;
        hintStr = "";

        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.ui_edit_text, this, true);

        initView();
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        this.resourceLeftID = arr.getResourceId(R.styleable.CustomEditText_srcLeft, R.drawable.ic_phone);
        this.resourceRightID = arr.getResourceId(R.styleable.CustomEditText_srcRight, R.drawable.ic_delete);
        this.inputStr = arr.getString(R.styleable.CustomEditText_inputType);
        if (inputStr == null) {
            inputStr = "Default";
        }
        this.hintStr = arr.getString(R.styleable.CustomEditText_hint);
        if (hintStr == null) {
            hintStr = "";
        }
        this.isShowCCP = arr.getBoolean(R.styleable.CustomEditText_isccp, false);
        this.isShowRight = arr.getBoolean(R.styleable.CustomEditText_drawShow, false);
        arr.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.ui_edit_text, this, true);

        initView();
    }

    private void initView() {
        img_left = findViewById(R.id.img_left);
        img_left.setImageResource(resourceLeftID);

        txt_input = findViewById(R.id.txt_input);
        txt_input.setHint(hintStr);
        switch (inputStr) {
            case "Number" :
                txt_input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "Email" :
                txt_input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "Phone" :
                txt_input.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "Pass" :
                txt_input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            default:
                txt_input.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }

        ccp_country = findViewById(R.id.ccp_country);
        if (isShowCCP) {
            ccp_country.setVisibility(VISIBLE);
        } else {
            ccp_country.setVisibility(GONE);
        }

        img_right = findViewById(R.id.img_right);
        img_right.setImageResource(resourceRightID);
        img_right.setOnClickListener(view -> {
            if (inputLayoutCallback != null) {
                inputLayoutCallback.onClickRightIcon();
            }
        });
        setShowRight(isShowRight);
    }

    public String getInputText() {
        return txt_input.getText().toString();
    }

    public String getFullPhoneNumber() {
        return ccp_country.getSelectedCountryCodeWithPlus() + txt_input.getText().toString();
    }

    public void setDisEditable() {
        txt_input.setInputType(InputType.TYPE_NULL);
        txt_input.setOnClickListener(v -> {
            if (inputLayoutCallback != null) {
                inputLayoutCallback.onClickEditText();
            }
        });
    }

    public void setInputText(String str) {
        txt_input.setText(str);
    }

    public void setShowRight(boolean flag) {
        isShowRight = flag;
        if (isShowRight) {
            img_right.setVisibility(VISIBLE);
        } else {
            img_right.setVisibility(GONE);
        }
    }

    public void setResourceLeftID(int id) {
        resourceLeftID = id;
        img_left.setImageResource(resourceLeftID);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        txt_input.setEnabled(enabled);
    }

    public void setCustomEditTextCallback(CustomEditTextCallback inputLayoutCallback) {
        this.inputLayoutCallback = inputLayoutCallback;
    }

    public void setHintStr(String hint) {
        hintStr = hint;
        txt_input.setHint(hintStr);
    }

    public interface CustomEditTextCallback {
        default void onClickEditText() {}
        default void onClickRightIcon() {}
    }

}
