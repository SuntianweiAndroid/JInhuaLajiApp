package com.speedata.jinhualajidemo.clj.fastble.callback;


import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

public abstract class BleIndicateCallback extends BleBaseCallback{

    public abstract void onIndicateSuccess();

    public abstract void onIndicateFailure(BleException exception);

    public abstract void onCharacteristicChanged(byte[] data);
}
