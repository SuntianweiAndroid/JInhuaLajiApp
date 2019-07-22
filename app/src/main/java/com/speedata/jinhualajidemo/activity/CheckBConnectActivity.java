package com.speedata.jinhualajidemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.view.SearchBtPrintDialog;
import com.speedata.jinhualajidemo.view.SearchBleWeightDialog;

import java.util.List;

public class CheckBConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle.getString("type").contains("HC-02")) {
            SearchBleWeightDialog searchTag = new SearchBleWeightDialog(this, R.style.MyDialogStyle);
            searchTag.setCanceledOnTouchOutside(false);
            searchTag.show();
            searchTag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
                    for (int i = 0; i < deviceList.size(); i++) {
                    }
                }
            });
            searchTag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        } else if (bundle.getString("type").contains("ML31_BT")) {
            SearchBtPrintDialog searchBTDialog = new SearchBtPrintDialog(this, R.style.MyDialogStyle, "ML31_BT");
            searchBTDialog.setCanceledOnTouchOutside(false);
            searchBTDialog.show();
            searchBTDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
    }
}
