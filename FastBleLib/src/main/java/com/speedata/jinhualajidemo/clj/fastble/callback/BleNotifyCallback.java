package com.speedata.jinhualajidemo.clj.fastble.callback;


import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

public abstract class BleNotifyCallback extends BleBaseCallback {

    public abstract void onNotifySuccess();

    public abstract void onNotifyFailure(BleException exception);

    public abstract void onCharacteristicChanged(byte[] data);

}
