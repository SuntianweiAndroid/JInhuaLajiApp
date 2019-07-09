package com.speedata.jinhualajidemo.clj.fastble.callback;


import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;

public abstract class BleScanAndConnectCallback extends BleGattCallback implements BleScanPresenterImp {

    public abstract void onScanFinished(BleDevice scanResult);

    public void onLeScan(BleDevice bleDevice) {
    }

}
