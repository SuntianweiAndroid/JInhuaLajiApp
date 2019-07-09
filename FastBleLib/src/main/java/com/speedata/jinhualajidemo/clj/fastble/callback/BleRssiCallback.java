package com.speedata.jinhualajidemo.clj.fastble.callback;


import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

public abstract class BleRssiCallback extends BleBaseCallback{

    public abstract void onRssiFailure(BleException exception);

    public abstract void onRssiSuccess(int rssi);

}