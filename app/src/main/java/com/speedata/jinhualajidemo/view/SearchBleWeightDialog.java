package com.speedata.jinhualajidemo.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.clj.blesample.adapter.DeviceAdapter;
import com.speedata.jinhualajidemo.clj.blesample.comm.ObserverManager;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleGattCallback;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleScanCallback;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;

import java.util.List;

public class SearchBleWeightDialog extends Dialog implements View.OnClickListener {
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;
    private TextView btn_scan, btn_close;
    private Context context;
    private Animation operatingAnim;
    private String saveMac;
    private String[] mac;


    public SearchBleWeightDialog(Context context) {
        super(context);
        this.context = context;
        this.mDeviceAdapter = mDeviceAdapter;
    }

    public SearchBleWeightDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.mDeviceAdapter = mDeviceAdapter;
    }

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_RE_PAIR = "re_pair";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        saveMac = MyApplication.getPreferences().getMac("mac");
        mac = saveMac.split("#");
        initView();
    }

    private void initView() {
        btn_scan = (TextView) findViewById(R.id.btn_scan);
        btn_close = (TextView) findViewById(R.id.btn_close);
        btn_scan.setText(context.getString(R.string.start_scan));
        btn_scan.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(context);

        mDeviceAdapter = new DeviceAdapter(context, true);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                }
            }
        });
        ListView listView_device = (ListView) findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);
        showConnectedDevice();
        startScan();
    }

    private BluetoothPrinter bPrinter;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (btn_scan.getText().equals(context.getString(R.string.start_scan))) {
                    startScan();
                } else if (btn_scan.getText().equals(context.getString(R.string.stop_scan))) {
                    BleManager.getInstance().cancelScan();
                }
                break;
            case R.id.btn_close:
                this.dismiss();
                break;

            default:
                break;
        }

    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                btn_scan.setText(context.getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();


            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                btn_scan.setText(context.getString(R.string.start_scan));
                if (mac.length >= 1 && mac[0] != "") {
                    for (int i = 0; i < mac.length; i++) {
                        for (int j = 0; j < scanResultList.size(); j++) {
                            if (!BleManager.getInstance().isConnected(scanResultList.get(j))) {
                                if (mac[i].equals(scanResultList.get(j).getMac())) {
                                    connectMac(mac[i]);
                                }
                            }
                        }

                    }
                }
                showConnectedDevice();
            }
        });
    }

    private void connectMac(final String mac) {

        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
//                connect(bleDevice);
                if (isActiveDisConnected) {
                    Toast.makeText(context, context.getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, bleDevice.getName() + "ble断开连接", Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                btn_scan.setText(context.getString(R.string.start_scan));
                progressDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                String strMac = MyApplication.getPreferences().getMac("mac");
                if (!strMac.contains(bleDevice.getMac())) {
                    MyApplication.getPreferences().setMac("mac", bleDevice.getMac());
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
//                connect(bleDevice);
                if (isActiveDisConnected) {
                    Toast.makeText(context, context.getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, bleDevice.getName() + "ble断开连接", Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    private void connect(final String mac) {
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                btn_scan.setText(context.getString(R.string.start_scan));
                progressDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
//                connect(bleDevice);
                if (isActiveDisConnected) {
                    Toast.makeText(context, context.getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, bleDevice.getName() + "ble断开连接", Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        BleManager.getInstance().cancelScan();
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }
}
