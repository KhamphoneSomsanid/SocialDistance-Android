package com.laodev.socialdis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.laodev.socialdis.R;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class ColorPickerDialog extends Dialog {

    private ColorPickerView cpv_region;
    private Button btn_cancel, btn_add;

    private String regionColor = "#ffffff";
    private ColorPickerDialogInterface colorPickerDialogInterface;

    public ColorPickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dig_color_picker);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTitle(null);
        setCanceledOnTouchOutside(true);

        initView();
        initEvent();
    }

    private void initEvent() {
        cpv_region.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> regionColor = "#" + envelope.getHexCode());
        btn_cancel.setOnClickListener(view -> dismiss());
        btn_add.setOnClickListener(view -> {
            dismiss();
            colorPickerDialogInterface.onChooseColor(regionColor);
        });
    }

    private void initView() {
        cpv_region = findViewById(R.id.cpv_region);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
    }

    public void setColorPickerDialogInterface(ColorPickerDialogInterface colorPickerDialogInterface) {
        this.colorPickerDialogInterface = colorPickerDialogInterface;
    }

    public interface ColorPickerDialogInterface {
        void onChooseColor(String color);
    }

}
