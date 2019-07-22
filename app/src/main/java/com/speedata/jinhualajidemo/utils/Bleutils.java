package com.speedata.jinhualajidemo.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import android.widget.Toast;

import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleNotifyCallback;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

import java.lang.reflect.Method;
import java.util.List;

public class Bleutils {

    private static void getConnectDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        String strMac = MyApplication.getPreferences().getMac("mac");
        for (int i = 0; i < deviceList.size(); i++) {
            if (!strMac.contains(deviceList.get(i).getMac())) {
                MyApplication.getPreferences().setMac("mac", deviceList.get(i).getMac());
            }
            initPrintandWeight(deviceList.get(i));
        }
    }

    private static void initPrintandWeight(BleDevice bleDevice) {
        String ss = bleDevice.getName();
        if (ss == null) {
            ss = "HC-02";
        }
        if (checkUUIDisBle(bleDevice) && ss.equals("HC-02")) {
            BleManager.getInstance().notify(
                    bleDevice,
                    "49535343-fe7d-4ae5-8fa9-9fafd205e455",
                    "49535343-1e4d-4bd9-ba61-23c647249616",
                    new BleNotifyCallback() {
                        @Override
                        public void onNotifySuccess() {
                            // 打开通知操作成功
                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                            // 打开通知操作失败
                        }

                        @Override
                        public void onCharacteristicChanged(byte[] data) {
                            // 打开通知后，设备发过来的数据将在这里出现
                            String weight = new StringBuffer((DataConversionUtils.byteArrayToAscii(DataConversionUtils.cutBytes(data, 1, data.length - 1)))).reverse().toString();
                            if (weight == null && weight.equals("")) {
                                return;
                            }
                            Log.i("shuju", "onCharacteristicChanged: " + (Double.valueOf(weight) + "kg"));
//                            mEtWeight.setText("重量:" + Double.valueOf(weight) + "KG");
                        }
                    });
        } else {
            // 连接建立之前的先配对
            try {
                if (bleDevice.getDevice().getBondState() == BluetoothDevice.BOND_NONE) {
                    Method creMethod = BluetoothDevice.class.getMethod("createBond");
//                    Toast.makeText(this, "正开始进行配对蓝牙...", Toast.LENGTH_SHORT).show();
                    creMethod.invoke(bleDevice.getDevice());
                } else {
//                    Toast.makeText(this, "已经配对...", Toast.LENGTH_SHORT).show();
//                    initPrinter(bleDevice.getDevice());
                }
            } catch (Exception e) {
//                Toast.makeText(this, "无法进行配对...请配对后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean checkUUIDisBle(BleDevice bleDevice) {
        List<BluetoothGattService> bluetoothGattServers = BleManager.getInstance().getBluetoothGattServices(bleDevice);
        for (int j = 0; j < bluetoothGattServers.size(); j++) {
            String uuid = bluetoothGattServers.get(j).getUuid().toString();
            if (bluetoothGattServers.get(j).getUuid().toString().equals("49535343-fe7d-4ae5-8fa9-9fafd205e455")) {
                return true;
            } else {
                continue;
            }

        }
        return false;
    }
}
