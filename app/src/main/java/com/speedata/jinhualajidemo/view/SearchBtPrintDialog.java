package com.speedata.jinhualajidemo.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.App;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.clj.blesample.adapter.DeviceAdapter;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.utils.ClsUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class SearchBtPrintDialog extends Dialog implements View.OnClickListener {
    private Context context;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (btn_scan.getText().equals(context.getString(R.string.start_scan))) {
                    startScan();
                } else if (btn_scan.getText().equals(context.getString(R.string.stop_scan))) {
                    if (btAdapt.isDiscovering()) {
                        btAdapt.cancelDiscovery();
                        btn_scan.setText(context.getString(R.string.start_scan));
                    }
                }
                break;
            case R.id.btn_close:
                this.dismiss();
                break;

            default:
                break;
        }
    }

    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;
    private TextView btn_scan, btn_close;
    private Animation operatingAnim;
    private String saveMac;
    private String[] mac;
    private String[] BlName = null;
    private BluetoothDevice device = null;

    public SearchBtPrintDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SearchBtPrintDialog(Context context, int theme, String... Blname) {
        super(context, theme);
        this.context = context;
        this.BlName = Blname;
    }


    private BluetoothAdapter btAdapt;
    private boolean isConnect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        bPrinter = App.bPrinter;
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
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(searchDevices, intent);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        progressDialog = new ProgressDialog(context);

        mDeviceAdapter = new DeviceAdapter(context, false);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (btAdapt.isDiscovering()) {
                    btAdapt.cancelDiscovery();
                    btn_scan.setText(context.getString(R.string.start_scan));
                }
                BluetoothDevice btDev = BleManager.getInstance().getBluetoothAdapter().getRemoteDevice(bleDevice.getMac());
                if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                    initPrinter(btDev);
                } else {
                    try {
                        Boolean returnValue = false;
                        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                            Toast.makeText(context, "远程设备发送蓝牙配对请求", Toast.LENGTH_SHORT).show();
                            //这里只需要createBond就行了
                            ClsUtils.createBond(btDev.getClass(), btDev);
                        } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                            Toast.makeText(context, btDev.getBondState() + " ....正在连接..", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (btAdapt.isDiscovering()) {
                    btAdapt.cancelDiscovery();
                    btn_scan.setText(context.getString(R.string.start_scan));
                }
                if (bPrinter != null) {
                    if (bPrinter.isConnected()) {
                        bPrinter.closeConnection();
                        App.bPrinter = null;
                    }
                }
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDetail(BleDevice bleDevice) {

            }
        });
        ListView listView_device = (ListView) findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);
        showConnectedDevice();
        startScan();
    }

    private void showConnectedDevice() {
        Object[] lstDevice = btAdapt.getBondedDevices().toArray();
        for (int i = 0; i < lstDevice.length; i++) {
            BluetoothDevice device = (BluetoothDevice) lstDevice[i];
            String str = "    已配对|" + device.getName() + "|"
                    + device.getAddress();
            Log.i(TAG, "startScan: " + str);

            if (mac.length >= 1 && mac[0] != "") {
                for (int j = 0; j < mac.length; j++) {
                    if (mac[j].equals(device.getAddress())) {
                        if (bPrinter == null) {
                            initPrinter(device);
                        } else {
                            if (!bPrinter.isConnected()) {
                                initPrinter(device);
                            }
                        }

                    }
                }
            }
            mDeviceAdapter.addDevice(new BleDevice(device));
            mDeviceAdapter.notifyDataSetChanged();
        }
    }

    private void startScan() {
        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
            Toast.makeText(context, "请先打开蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        if (btAdapt.isDiscovering()) {
            btAdapt.cancelDiscovery();
        }
        mDeviceAdapter.clearConnectedDevice();
        btn_scan.setText(context.getString(R.string.stop_scan));
        btAdapt.startDiscovery();
    }

    private final BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    BleDevice bleDevice = new BleDevice(device);
                    if (BlName != null && BlName.length > 0) {
                        AtomicBoolean equal = new AtomicBoolean(false);
                        for (String name : BlName) {
                            String remoteName = bleDevice.getName();

                            if (remoteName == null) {
                                remoteName = "";
                            }
                            if (true ? remoteName.contains(name) : remoteName.equals(name)) {
                                equal.set(true);
                            }
                        }
                        if (!equal.get()) {
                            return;
                        }
                    }
                    mDeviceAdapter.addDevice(bleDevice);
                    String str = "           未配对|" + device.getName() + "|"
                            + device.getAddress();
                    Log.i(TAG, "onReceive: " + str);
                    mDeviceAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "完成配对");
                        initPrinter(device);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MyApplication.showToast(context, device.getName());
                if (device != null && bPrinter != null) {
                    if (bPrinter.getMacAddress().equals(device.getAddress())) {
                        bPrinter.closeConnection();
                        App.bPrinter = null;
                    }
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                btn_scan.setText(context.getString(R.string.start_scan));
            }

        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        context.unregisterReceiver(searchDevices);
        if (btAdapt.isDiscovering()) {
            btAdapt.cancelDiscovery();
            btAdapt = null;
        }
    }


    private BluetoothPrinter bPrinter;

    /**
     * 初始化打印机
     *
     * @param device 蓝牙对象
     */
    private void initPrinter(BluetoothDevice device) {
        if (bPrinter != null) {
            if (bPrinter.isConnected()) {
                bPrinter.closeConnection();
            }
        }
        bPrinter = new BluetoothPrinter(device);
        bPrinter.setCurrentPrintType(PrinterType.Printer_58);
        //set handler for receive message of connect state from sdk.
        bPrinter.setEncoding("GBK");
        bPrinter.setHandler(bHandler);
        //返回值验证
        bPrinter.setNeedVerify(false);
        bPrinter.openConnection();

    }

    private String TAG = "stw";
    @SuppressLint("HandlerLeak")
    private final Handler bHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("STW", "msg.what is : " + msg.what);
            switch (msg.what) {
                case BluetoothPrinter.Handler_Connect_Connecting:
                    //mTitle.setText(R.string.title_connecting);
                    Log.d(TAG, "handleMessage: " + 100);
                    Toast.makeText(context, R.string.bt_connecting, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Success:
                    Log.d(TAG, "handleMessage: " + 101);
                    App.bPrinter = bPrinter;
                    mDeviceAdapter.notifyDataSetChanged();
                    String strMac = MyApplication.getPreferences().getMac("mac");
                    if (!strMac.contains(device.getAddress())) {
                        MyApplication.getPreferences().setMac("mac", device.getAddress());
                    }
                    isConnect = true;
                    // mTitle.setText(getString(R.string.title_connected) + ": "+ mPrinter.getPrinterName());
                    Toast.makeText(context, R.string.bt_connect_success, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Failed:
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: " + 102);
                    isConnect = false;
                    Toast.makeText(context, R.string.bt_connect_failed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Closed:
                    //mTitle.setText(R.string.title_not_connected);
                    device = null;
                    mDeviceAdapter.clearScanDevice();
                    mDeviceAdapter.clear();
                    mDeviceAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage: " + 103);
                    Toast.makeText(context, R.string.bt_connect_closed, Toast.LENGTH_SHORT).show();
                case BluetoothPrinter.Handler_Message_Read:
                    if (msg != null) {
                        if (msg.obj != null) {
                            int states = (int) msg.obj;
                            Log.d(TAG, "handleMessage: states:" + states);
                        } else {
                            Log.d(TAG, "handleMessage: 没有返回值");
                        }
                    } else {
                        Log.d(TAG, "handleMessage: msg消息为空");
                    }
                    break;
                default:
                    break;

            }
        }
    };

}
