package com.speedata.jinhualajidemo.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.clj.blesample.adapter.DeviceAdapter;
import com.speedata.jinhualajidemo.clj.blesample.comm.ObserverManager;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleGattCallback;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleScanCallback;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;
import com.speedata.jinhualajidemo.utils.ClsUtils;

import java.lang.reflect.Method;
import java.util.List;

public class SearchTagDialog extends Dialog implements View.OnClickListener {
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;
    private Button btn_scan, btn_close;
    private Context context;
    private ImageView img_loading;
    private Animation operatingAnim;
    private String saveMac;
    private String[] mac;


    public SearchTagDialog(Context context) {
        super(context);
        this.context = context;
        this.mDeviceAdapter = mDeviceAdapter;
    }

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_RE_PAIR = "re_pair";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        initView();
        saveMac = MyApplication.getPreferences().getMac("mac");
        mac = saveMac.split("#");
    }

    private void initView() {
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_scan.setText(context.getString(R.string.start_scan));
        btn_scan.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        context.registerReceiver(searchDevices, intent);
        img_loading = (ImageView) findViewById(R.id.img_loading);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(context);

        mDeviceAdapter = new DeviceAdapter(context);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    if (bleDevice.getDevice().getName().contains("ML31_BT")) {
                        BluetoothDevice btDev = BleManager.getInstance().getBluetoothAdapter().getRemoteDevice(bleDevice.getMac());
                        if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                            connect(bleDevice);
                        } else {
                            try {
                                Boolean returnValue = false;
                                if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                                    Toast.makeText(context, "远程设备发送蓝牙配对请求", 5000).show();
                                    //这里只需要createBond就行了
                                    ClsUtils.createBond(btDev.getClass(), btDev);
                                } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                                    Toast.makeText(context, btDev.getBondState() + " ....正在连接..", 1000).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        connect(bleDevice);
                    }
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

    private final BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle b = intent.getExtras();
            // 显示所有收到的消息及其细节
            BluetoothDevice device = null;
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "完成配对");
                        connect(device.getAddress());//连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            }

        }
    };

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
                img_loading.startAnimation(operatingAnim);
                img_loading.setVisibility(View.VISIBLE);
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
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
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
                if (bleDevice.getDevice().getName().contains("ML31_BT")) {
                    BluetoothDevice btDev = BleManager.getInstance().getBluetoothAdapter().getRemoteDevice(bleDevice.getMac());
                    if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                        connect(bleDevice);
                    } else {
                        try {
                            Boolean returnValue = false;
                            if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                                Toast.makeText(context, "远程设备发送蓝牙配对请求", 5000).show();
                                //这里只需要createBond就行了
                                ClsUtils.createBond(btDev.getClass(), btDev);
                            } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                                Toast.makeText(context, btDev.getBondState() + " ....正在连接..", 1000).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
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

    private void connect(final String mac) {
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
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
