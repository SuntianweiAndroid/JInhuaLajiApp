package com.speedata.jinhualajidemo;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.speedata.jinhualajidemo.activity.CheckBConnectActivity;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.db.DbDaoManage;
import com.speedata.jinhualajidemo.been.LajiBeen;
import com.speedata.jinhualajidemo.utils.LogUtil;
import com.speedata.jinhualajidemo.utils.MySharedPreferences;

public class MyApplication extends Application {

    public static MySharedPreferences mySharedPreferences;
    public static LajiBeen lajiBeen;
    public static final String key = "6d2a37dcb5834dabb1a9e43d90a43881";
    public static String pdaIMEI = "";
    @Override
    public void onCreate() {
        super.onCreate();
        mySharedPreferences = new MySharedPreferences(this);
        DbDaoManage.initDb(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(searchDevices, intent);
        LogUtil.tagPrefix = "stw";
        pdaIMEI = pdaImei();
    }

    public String pdaImei() {
        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.this.getSystemService(TELEPHONY_SERVICE);

        String imei = telephonyManager.getDeviceId();
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        return imei;
    }

    public static MySharedPreferences getPreferences() {
        return mySharedPreferences;
    }

    public static LajiBeen getLajiBeen() {
        if (lajiBeen == null) {
            lajiBeen = new LajiBeen();
        }
        return lajiBeen;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName().contains("ML31_BT")) {
                    MyApplication.getLajiBeen().setbPrinter(null);
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "ML31_BT");
                    intent.putExtras(bundle);
                    intent.setClass(context, CheckBConnectActivity.class);
                    context.startActivity(intent);

                } else if (device.getName().contains("HC-02")) {
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "HC-02");
                    intent.putExtras(bundle);
                    intent.setClass(context, CheckBConnectActivity.class);
                    context.startActivity(intent);
                }
            }
        }
    };


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("stw", "onTerminate");
        super.onTerminate();
        if (MyApplication.getLajiBeen().getbPrinter() != null && MyApplication.getLajiBeen().getbPrinter().isConnected()) {
            MyApplication.getLajiBeen().getbPrinter().closeConnection();
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}
