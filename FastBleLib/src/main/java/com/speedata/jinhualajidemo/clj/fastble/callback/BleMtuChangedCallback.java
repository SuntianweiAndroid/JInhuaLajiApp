package com.speedata.jinhualajidemo.clj.fastble.callback;


import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

public abstract class BleMtuChangedCallback extends BleBaseCallback {

    public abstract void onSetMTUFailure(BleException exception);

    public abstract void onMtuChanged(int mtu);

}
