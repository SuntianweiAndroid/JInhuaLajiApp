package com.speedata.jinhualajidemo.clj.blesample.comm;


import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;

public interface Observable {

    void addObserver(Observer obj);

    void deleteObserver(Observer obj);

    void notifyObserver(BleDevice bleDevice);
}
