package com.laodev.socialdis.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.laodev.socialdis.R;
import com.laodev.socialdis.dialog.ColorPickerDialog;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import java.util.Locale;

public class TrackMangerActivity extends AppCompatActivity {

    private View viw_warning, viw_damage;
    private TextView lbl_warning, lbl_damage, lbl_freq_time, lbl_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_manager);

        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    private void initView() {
        viw_warning = findViewById(R.id.viw_warning);
        viw_damage = findViewById(R.id.viw_damage);

        lbl_warning = findViewById(R.id.lbl_warning);
        lbl_damage = findViewById(R.id.lbl_damage);
        lbl_freq_time = findViewById(R.id.lbl_freq_time);
        lbl_unit = findViewById(R.id.lbl_unit);

        initData();
        initEvent();
    }

    private void initData() {
        viw_warning.setBackgroundColor(Color.parseColor(SharedPreferenceUtil.getWaningColor()));
        viw_damage.setBackgroundColor(Color.parseColor(SharedPreferenceUtil.getDamageColor()));

        String unit = "m";
        if (SharedPreferenceUtil.getUnit().equals(getString(R.string.feet))) {
            unit = "ft";
        }
        lbl_warning.setText(String.format(Locale.getDefault(), "%.2f %s", SharedPreferenceUtil.getWaningDistance(), unit));
        lbl_damage.setText(String.format(Locale.getDefault(), "%.2f %s", SharedPreferenceUtil.getDamageDistance(), unit));
        lbl_freq_time.setText(String.format(Locale.getDefault(), "%d s", SharedPreferenceUtil.getTrackingFrequence()));
        lbl_unit.setText(unit);
    }

    private void initEvent() {
        viw_warning.setOnClickListener(view -> {
            ColorPickerDialog dialog = new ColorPickerDialog(TrackMangerActivity.this);
            dialog.setColorPickerDialogInterface(color -> {
                SharedPreferenceUtil.setWarningColor(color);
                initData();
            });
            dialog.show();
        });
        viw_damage.setOnClickListener(view -> {
            ColorPickerDialog dialog = new ColorPickerDialog(TrackMangerActivity.this);
            dialog.setColorPickerDialogInterface(color -> {
                SharedPreferenceUtil.setDamageColor(color);
                initData();
            });
            dialog.show();
        });

        lbl_warning.setOnClickListener(view -> showEditTextDialog("Please input your warning distance.", text -> {
            SharedPreferenceUtil.setWaningDistance(Float.parseFloat(text));
            initData();
        }));
        lbl_damage.setOnClickListener(view -> showEditTextDialog("Please input your damage distance.", text -> {
            SharedPreferenceUtil.setDamageDistance(Float.parseFloat(text));
            initData();
        }));
        lbl_freq_time.setOnClickListener(view -> showEditTextDialog("Please input your frequence time.", text -> {
            SharedPreferenceUtil.setTrackingFrequence(Integer.parseInt(text));
            initData();
        }));
        lbl_unit.setOnClickListener(view -> {
            if (SharedPreferenceUtil.getUnit().equals(getString(R.string.feet))) {
                SharedPreferenceUtil.setUnit(getString(R.string.meter));
            } else {
                SharedPreferenceUtil.setUnit(getString(R.string.feet));
            }
            initData();
        });
    }

    private void showEditTextDialog(String message, final EditTextDialogListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setMessage(message);
        alert.setView(edittext);
        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            if (listener != null)
                listener.onOk(edittext.getText().toString());
        });
        alert.setNegativeButton(R.string.cancel, null);
        alert.show();
    }

    private interface EditTextDialogListener {
        void onOk(String text);
    }

}
