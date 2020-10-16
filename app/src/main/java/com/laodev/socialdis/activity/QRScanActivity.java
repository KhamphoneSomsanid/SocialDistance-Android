package com.laodev.socialdis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.laodev.socialdis.R;
import com.laodev.socialdis.dialog.AddTeamDialog;
import com.laodev.socialdis.model.UserModel;
import com.google.gson.Gson;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        initUIView();
    }

    private void initUIView() {
        mScannerView = findViewById(R.id.zx_view);
        mScannerView.setAspectTolerance(0.5f);
    }

    @Override
    public void handleResult(Result rawResult) {
        onEventGetResult(rawResult.getContents());
    }

    private void onEventGetResult(String contents) {
        if (UserModel.isContainTeamUserByUserID(contents)) {
            Toast.makeText(this, "This user was already added in your team.", Toast.LENGTH_SHORT).show();
            onResume();
            return;
        }
        AddTeamDialog teamDialog = new AddTeamDialog(this, contents);
        teamDialog.setAddTeamDialogListener(new AddTeamDialog.AddTeamDialogListener() {
            @Override
            public void onSuccess(UserModel userModel) {
                Intent data = new Intent();
                String jsonCard = new Gson().toJson(userModel);
                data.putExtra("USERINFO", jsonCard);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailed() {
                onResume();
            }
        });
        teamDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

}