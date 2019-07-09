package com.speedata.jinhualajidemo.clj.fastble.callback;

import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
