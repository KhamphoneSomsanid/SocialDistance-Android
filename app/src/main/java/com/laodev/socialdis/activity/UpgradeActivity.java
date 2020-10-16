package com.laodev.socialdis.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.laodev.socialdis.R;
import com.laodev.socialdis.model.UserModel;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.laodev.socialdis.util.PaypalUtil.configuration;

public class UpgradeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 10001;

    private Button btn_prime;
    private RadioGroup rdg_prime;

    private String payCode = "";

    private void initEvent() {
        btn_prime.setOnClickListener(view -> {
            switch (rdg_prime.getCheckedRadioButtonId()) {
                case R.id.rdb_prime_one:
                    payCode = "6";
                    getPayment("Prime membership for a month", payCode, "USD");
                    break;
                case R.id.rdb_prime_three:
                    payCode = "15";
                    getPayment("Prime membership for 3 months", payCode, "USD");
                    break;
                case R.id.rdb_prime_six:
                    payCode = "25";
                    getPayment("Prime membership for 6 months", payCode, "USD");
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        initToolBar();
        initView();
        initEvent();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
    }

    private void initView() {
        btn_prime = findViewById(R.id.btn_prime);
        rdg_prime = findViewById(R.id.rdg_prime);
        rdg_prime.check(R.id.rdb_prime_one);
    }

    private void initToolBar() {
        Toolbar toolBar = findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            toolBar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }

    private void getPayment(String title, String pay, String unit) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(pay), unit,
                title, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private void onSuccessPayment(String payCode) {
        UserModel currentUser = SharedPreferenceUtil.getCurrentUser();

        Map<String, String> params = new HashMap<>();
        params.put("userid", currentUser.id);
        params.put("paycode", payCode);

        ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...");
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_upgrade_membership, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                dialog.dismiss();
                try {
                    if (ret == 10000) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        SharedPreferenceUtil.saveCurrentUser(new Gson().toJson(userModel));
                        onBackPressed();
                    } else {
                        AppUtil.showToast(UpgradeActivity.this, obj.getString("result"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtil.showToast(UpgradeActivity.this, e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(UpgradeActivity.this, e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                AppUtil.showToast(UpgradeActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        JSONObject object = confirm.toJSONObject();
                        String response = object.getString("response_type");
                        if (response.equals("payment")) {
                            onSuccessPayment(payCode);
                        }
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                AppUtil.showToast(UpgradeActivity.this, "Payment Cancelled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                AppUtil.showToast(UpgradeActivity.this, "Payment Invalid.");
            }
        }
    }

}
