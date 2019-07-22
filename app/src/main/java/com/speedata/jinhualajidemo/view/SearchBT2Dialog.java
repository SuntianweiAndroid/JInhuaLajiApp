package com.speedata.jinhualajidemo.view;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.utils.ClsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchBT2Dialog extends Dialog implements View.OnClickListener {
    private Context context;

    @Override
    public void onClick(View v) {

    }

    public SearchBT2Dialog(Context context) {
        super(context);
        this.context = context;
    }
    public SearchBT2Dialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    Button btnSearch, btnDis, btnExit;
    ToggleButton tbtnSwitch;
    ListView lvBTDevices;
    ArrayAdapter<String> adtDevices;
    List<String> lstDevices = new ArrayList<String>();
    BluetoothAdapter btAdapt;
    public static BluetoothSocket btSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jingdianb_layout);
        // Button 设置
        btnSearch = (Button) this.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new ClickEvent());
        btnExit = (Button) this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        btnDis = (Button) this.findViewById(R.id.btnDis);
        btnDis.setOnClickListener(new ClickEvent());

        // ToogleButton设置
        tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
        tbtnSwitch.setOnClickListener(new ClickEvent());

        // ListView及其数据源 适配器
        lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
        adtDevices = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, lstDevices);
        lvBTDevices.setAdapter(adtDevices);
        lvBTDevices.setOnItemClickListener(new ItemClickEvent());

        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能

        // ========================================================
        // modified by wiley
        /*
         * if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// 读取蓝牙状态并显示
         * tbtnSwitch.setChecked(false); else if (btAdapt.getState() ==
         * BluetoothAdapter.STATE_ON) tbtnSwitch.setChecked(true);
         */
        if (btAdapt.isEnabled()) {
            tbtnSwitch.setChecked(false);
        } else {
            tbtnSwitch.setChecked(true);
        }
        // ============================================================
        // 注册Receiver来获取蓝牙设备相关的结果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        context.registerReceiver(searchDevices, intent);
    }

    private final BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName.toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;
            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    String str = "           未配对|" + device.getName() + "|"
                            + device.getAddress();
                    if (lstDevices.indexOf(str) == -1)// 防止重复添加
                        lstDevices.add(str); // 获取设备名称和mac地址
                    adtDevices.notifyDataSetChanged();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "完成配对");
                        //connect(device);//连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            }

        }
    };

//    @Override
//    protected void onDestroy() {
//        this.unregisterReceiver(searchDevices);
//        super.onDestroy();
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }


    @Override
    protected void onStop() {
        super.onStop();
        context.unregisterReceiver(searchDevices);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (btAdapt.isDiscovering()) {
                btAdapt.cancelDiscovery();
            }
            String str = lstDevices.get(arg2);
            String[] values = str.split("\\|");
            String address = values[2];
            Log.e("address", values[2]);
            BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
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

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnSearch)// 搜索蓝牙设备，在BroadcastReceiver显示结果
            {
                if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
                    Toast.makeText(context, "请先打开蓝牙", 1000)
                            .show();
                    return;
                }
                if (btAdapt.isDiscovering())
                    btAdapt.cancelDiscovery();
                lstDevices.clear();
                Object[] lstDevice = btAdapt.getBondedDevices().toArray();
                for (int i = 0; i < lstDevice.length; i++) {
                    BluetoothDevice device = (BluetoothDevice) lstDevice[i];
                    String str = "    已配对|" + device.getName() + "|"
                            + device.getAddress();
                    lstDevices.add(str); // 获取设备名称和mac地址
                    adtDevices.notifyDataSetChanged();
                }
                setTitle("本机：" + btAdapt.getAddress());
                btAdapt.startDiscovery();
            } else if (v == tbtnSwitch) {// 本机蓝牙启动/关闭
                if (tbtnSwitch.isChecked() == false)
                    btAdapt.enable();

                else if (tbtnSwitch.isChecked() == true)
                    btAdapt.disable();

            } else if (v == btnDis)// 本机可以被搜索
            {
                Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                context.startActivity(discoverableIntent);
            } else if (v == btnExit) {
                try {
                    if (btSocket != null)
                        btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                context.finish();
            }
        }
    }


}
