package com.speedata.jinhualajidemo.clj.blesample.comm;


import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
