package com.speedata.jinhualajidemo.utils;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PairingRequest extends BroadcastReceiver {
    private String strPsw = "1234";
    private final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = null;

        if (action.equals(ACTION_PAIRING_REQUEST)) {

            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                try {
                    ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                    // ClsUtils.cancelPairingUserInput(device.getClass(),
                    // device); //一般调用不成功，前言里面讲解过了
                    Toast.makeText(context, "配对信息" + device.getName(), Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(context, "请求连接错误...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}