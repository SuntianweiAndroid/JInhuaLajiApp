package com.speedata.jinhualajidemo.utils;

import android.content.Context;
import android.os.SystemClock;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.jinhualajidemo.been.CodeBeen;

import org.greenrobot.eventbus.EventBus;

public class ScanManage {
    private Context context;
    private static ScanInterface scanDecode;

    public static void initScan(Context context) {
        scanDecode = new ScanDecode(context);
        scanDecode.initService("true");
    }

    public static void startScan() {
        scanDecode.starScan();
//        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
//            @Override
//            public void getBarcode(String s) {
//
//            }
//
//            @Override
//            public void getBarcodeByte(byte[] bytes) {
//
//            }
//        });
    }

    public static void getCodeMsg() {
        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
            @Override
            public void getBarcode(String s) {
                EventBus.getDefault().post(new CodeBeen(s));
            }

            @Override
            public void getBarcodeByte(byte[] bytes) {

            }
        });
    }

    public static void stopScan() {
        scanDecode.stopScan();

    }

    public static void disScan() {
        scanDecode.onDestroy();
    }

}
