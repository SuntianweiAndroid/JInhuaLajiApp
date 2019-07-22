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

    /**
     * 启动扫描
     */
    public static void startScan() {
        scanDecode.starScan();
    }

    /**
     * 获取结果
     */
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

    /**
     * 停止扫描
     */
    public static void stopScan() {
        scanDecode.stopScan();
    }

    /**
     * 销毁扫描
     */
    public static void disScan() {
        scanDecode.onDestroy();
    }

}
