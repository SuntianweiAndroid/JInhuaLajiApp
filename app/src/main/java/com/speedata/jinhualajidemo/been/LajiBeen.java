package com.speedata.jinhualajidemo.been;

import android.graphics.Bitmap;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;

public class LajiBeen {
    /**
     * 用户登记方式 0 刷卡  1 扫描  2手机号
     */
    private int register;

    private String cardNum;
    private String phoneNum;
    private String barcodeNum;
    /**
     * 厨余垃圾=c   可回收垃圾 k  有害垃圾 y  其它垃圾  q
     */
    private String laClassify;
    /**
     * 打分
     */
    private String score;
    /**
     * 取证垃圾图片缓存
     */
    private Bitmap bitmap;

    public  BluetoothPrinter bPrinter;

    public BleDevice bleDevice;

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public BluetoothPrinter getbPrinter() {
        return bPrinter;
    }

    public void setbPrinter(BluetoothPrinter bPrinter) {
        this.bPrinter = bPrinter;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBarcodeNum() {
        return barcodeNum;
    }

    public void setBarcodeNum(String barcodeNum) {
        this.barcodeNum = barcodeNum;
    }

    public String getLaClassify() {
        return laClassify;
    }

    public void setLaClassify(String laClassify) {
        this.laClassify = laClassify;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "LajiBeen{" +
                "register=" + register +
                ", cardNum='" + cardNum + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", barcodeNum='" + barcodeNum + '\'' +
                ", laClassify='" + laClassify + '\'' +
                ", score='" + score + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
