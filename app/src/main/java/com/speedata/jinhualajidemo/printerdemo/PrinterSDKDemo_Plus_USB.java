package com.speedata.jinhualajidemo.printerdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.android_print_sdk.usb.USBPrinter;
import com.android_print_sdk.wifi.WiFiPrinter;
import com.speedata.jinhualajidemo.printerdemo.global.GlobalContants;
import com.speedata.jinhualajidemo.printerdemo.util.BluetoothPrintUtils;
import com.speedata.jinhualajidemo.printerdemo.util.UsbPrintUtils;
import com.speedata.jinhualajidemo.printerdemo.util.WifiPrintUtils;
import com.speedata.jinhualajidemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class PrinterSDKDemo_Plus_USB extends Activity implements View.OnClickListener{
    private final static String TAG = "PrinterSDKDemo_Plus_USB";
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private Button openConnect;
    private Button printImage;

    private Button printText;
    private Button printTable;
    private Button printPDF;
    private Button prnLable;
    private Button prnWeb;

    private Button printNote;
    private Button printBarCode;
    private Button getState;
    private Button prnAlpay;

//    private Button SetBlack;
//    private Button prnM22;

    private RadioButton paperWidth_58;
    private RadioButton paperWidth_80;

    private RadioButton printer_type_remin;
    private RadioButton printer_type_styuls;

    private RadioButton bluetooth;
    private RadioButton usb;
    private RadioButton wifi;

    private EditText mBarCodeNum;
    private EditText mMoneyNum;

    public int typeConn;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    public static USBPrinter mPrinter;
    private UsbDevice mUsbDevice;
    private BluetoothDevice bDevice;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;

    //menu context
    private static final int MENU_CONNECT = 0;
    private static final int MENU_CONNECT_OTHER = 1;
    private static final int MENU_DISCONNECT = 2;
    private int ret = 0;

    private Context mContext;
    public static boolean isConnected;
    private PendingIntent pendingIntent;

    //条码类型集合
    private List<String> list = new ArrayList<>();
    private Spinner barCodeSpinner;
    private ArrayAdapter<String> adapter;

    //编码类型
    private List<String> encodingTypeList = new ArrayList<>();
    private Spinner encodingTypeSpinner;
    private ArrayAdapter<String> encodingTypeAdapter;
    private String encodeType;

    public static boolean is_58mm;
    private boolean is_thermal;

    private BluetoothAdapter mBluetoothAdapter = null;
    public static BluetoothPrinter bPrinter;
    public static WiFiPrinter wiFiPrinter;
    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean hasRegisteredBoundReceiver;
    private BluetoothDevice currentDevice;
    private IntentFilter boundFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    private boolean re_pair = false;
    private boolean isConnecting;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    private static long lastClickTime;
    private CreateDialog createDialog;
    private String URL="http://120.27.212.180/Kz20171211/mbl/mbl_SignOn.asp";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = this;
        initView();
//        prnAlpay.setVisibility(View.GONE);
//        mMoneyNum.setVisibility(View.GONE);


        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);

        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        registerReceiver(mUsbReceiver, filter);

        // Get local Bluetooth adapter
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.not_find_bluetooth, Toast.LENGTH_LONG).show();
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //如果 API level 是大于等于 23（安卓6.0以上）
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                {
                    Toast.makeText(this,"自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        //请求权限
//            ActivityCompat.requestPermissions(PrinterSDKDemo_Plus_USB.this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//        //判断是否需要 向用户解释，为什么要申请该权限
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_CONTACTS)) {
//                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
//            }
//        }
        IntentFilter Bfilter = new IntentFilter();
        Bfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        Bfilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_FOUND);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        Bfilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        registerReceiver(mStateReceiver, Bfilter);

        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
             mNetworkInfo.isAvailable();
        }
    }

    // receive the state change of the bluetooth.
    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i(TAG, action);
            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                if(bPrinter != null && device != null && isConnected){
                    if(bPrinter.getMacAddress().equals(device.getAddress()))
                    {
                        bPrinter.closeConnection();
                    }
                }
                setButtonState();
            }
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, action);
            Toast.makeText(mContext, action, Toast.LENGTH_SHORT).show();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice Device =  intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.e(TAG, "获取权限成功：" + Device.getDeviceName());
                        if(!isConnected && Device.equals(mUsbDevice)){
                            openConnection(mUsbDevice);
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + mUsbDevice);
                    }
                }
            }

            else if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
            {
                UsbDevice device =  intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device.getProductId()==22304&&device.getVendorId()==1155){
                    UsbManager mUsbManager = (UsbManager) getSystemService(USB_SERVICE);

                    mUsbManager.requestPermission(device, pendingIntent); // 该代码执行后，系统弹出一个对话框
                }
            }else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){
                UsbDevice device =  intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(device != null && isConnected && device.equals(mPrinter.getCurrentDevice())){
                    closeConnection();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (bluetooth.isChecked()){
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mPrinter != null) {
                closeConnection();
            }else if (bPrinter != null){
                bPrinter.closeConnection();
            }
            isConnected = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(isConnected){
            menu.add(0, MENU_DISCONNECT, 0, R.string.disconnect).setIcon(android.R.drawable.ic_media_ff);
        }else{
            menu.add(0, MENU_CONNECT, 0, R.string.connect).setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONNECT:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case MENU_DISCONNECT:
                // disconnect
                if (mPrinter!=null){
                    closeConnection();
                }else if(bPrinter!=null){
                    bPrinter.closeConnection();
                }
                return true;
        }
        return false;
    }

    public void openConnection(UsbDevice device) {
        // TODO Auto-generated method stub

        isConnected = mPrinter.openConnection(device);
        Log.d(TAG,""+device);
        Log.d(TAG,"isConnected:"+isConnected);
        setButtonState();
    }

    public void closeConnection(){
        mPrinter.closeConnection();
        isConnected = false;
        setButtonState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (usb.isChecked()) {
            switch (requestCode) {
                case REQUEST_CONNECT_DEVICE:
                    if (resultCode == Activity.RESULT_OK) {
                        UsbDevice device = data.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        mConnectedDeviceName = device.getDeviceName();
                        Toast.makeText(mContext, "REQUEST_CONNECT_DEVICE return", Toast.LENGTH_LONG).show();
                        Log.d(TAG,""+mConnectedDeviceName);
                        initPrinter(device);
                    }
                    break;
                default:
                    break;
            }
        }else if(bluetooth.isChecked()){
            switch (requestCode) {
                case REQUEST_CONNECT_DEVICE:
                    if (resultCode == Activity.RESULT_OK) {
                        String address = data.getExtras().getString(BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        mConnectedDeviceName =device.getName();
                        Log.i(TAG, "connected device name is : " + mConnectedDeviceName + ", address is : " + address);
                        Log.i(TAG, "device.getBondState() is : " + device.getBondState());

                        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                            Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_NONE");
                            PairOrRePairDevice(false, device);
                        }
                        else if(device.getBondState() == BluetoothDevice.BOND_BONDED)
                        {
                            re_pair = data.getExtras().getBoolean(BluetoothDeviceListActivity.EXTRA_RE_PAIR);
                            Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_BONDED, re_pair is: " + re_pair);
                            if (re_pair) {
                                PairOrRePairDevice(true, device);
                            } else {
                                initPrinter(device);
                            }
                        }
                    }
                    break;
                case REQUEST_ENABLE_BT:
                    // When the request to enable Bluetooth returns
                    if (resultCode != Activity.RESULT_OK){
                        // User did not enable Bluetooth or an error occured
                        Toast.makeText(this, R.string.not_find_bluetooth, Toast.LENGTH_SHORT).show();
                    }
            }
        }else if(wifi.isChecked()){
            switch (requestCode) {

                case REQUEST_CONNECT_DEVICE:
                    if (resultCode == Activity.RESULT_OK) {
//                        devicesName = "Net device";
                        String devicesAddress = data.getStringExtra("ip_address");
                        wiFiPrinter = new WiFiPrinter(devicesAddress,9100,wHandler);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO
                                wiFiPrinter.open();
                            }
                        }).start();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean PairOrRePairDevice(boolean re_pair, BluetoothDevice device)
    {
        boolean success ;
        try {
            if (!hasRegisteredBoundReceiver) {
                currentDevice = device;
                registerReceiver(boundDeviceReceiver, boundFilter);
                hasRegisteredBoundReceiver = true;
            }
            if (re_pair) {
                Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
                success = (Boolean) removeBondMethod.invoke(device);
                Log.i(TAG, "removeBond is success? : " + success);
            }
            else
            {
//                Method setPinMethod = BluetoothDevice.class.getMethod("setPin");
//                setPinMethod.invoke(device, 1234);
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(device);
                Log.i(TAG, "createBond is success? : " + success);
            }
        } catch (Exception e) {
            Log.i(TAG, "removeBond or createBond failed.");
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    // receive bound broadcast to open connect.
    private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!currentDevice.equals(device)){
                    return;
                }
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.i(TAG, "bounding......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.i(TAG, "bound success");
                        // if bound success, auto init BluetoothPrinter. open connect.
                        if(hasRegisteredBoundReceiver){
                            unregisterReceiver(boundDeviceReceiver);
                            hasRegisteredBoundReceiver = false;
                        }
                        initPrinter(device);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        if (re_pair) {
                            re_pair = false;
                            Log.i(TAG, "removeBond success, wait create bound.");
                            PairOrRePairDevice(false, device);
                        } else if(hasRegisteredBoundReceiver){
                            unregisterReceiver(boundDeviceReceiver);
                            hasRegisteredBoundReceiver = false;
                            Log.i(TAG, "bound cancel");
                        }
                    default:
                        break;
                }
            }
        }
    };

    private void initView(){
        bluetooth =  this.findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(this);

        usb =  this.findViewById(R.id.usb);
        usb.setOnClickListener(this);

        wifi =  this.findViewById(R.id.wifi);
        wifi.setOnClickListener(this);

        openConnect =  this.findViewById(R.id.btnOpen);
        openConnect.setOnClickListener(this);

        printText =  this.findViewById(R.id.btnPrintText);
        printText.setOnClickListener(this);

        printTable =  this.findViewById(R.id.btnPrintTable);
        printTable.setOnClickListener(this);

        printImage =  this.findViewById(R.id.btnPrintImage);
        printImage.setOnClickListener(this);

        getState =  findViewById(R.id.btGetState);
        getState.setOnClickListener(this);

//        prnAlpay = findViewById(R.id.btAlpay);
//        prnAlpay.setOnClickListener(this);

//        prnM22 = (Button) this.findViewById(R.id.btM22Test);
//        prnM22.setOnClickListener(this);
//
//        SetBlack = (Button) findViewById(R.id.btSetBlack);
//        SetBlack.setOnClickListener(this);

        printPDF =  findViewById(R.id.btPrintPDF);
        printPDF.setOnClickListener(this);

        prnLable =  findViewById(R.id.btPrnLable);
        prnLable.setOnClickListener(this);

        prnWeb =  findViewById(R.id.btPrintWeb);
        prnWeb.setOnClickListener(this);

        printNote =  this.findViewById(R.id.btnPrintNote);
        printNote.setOnClickListener(this);

        printBarCode =  this.findViewById(R.id.btnPrintBarCode);
        printBarCode.setOnClickListener(this);

        paperWidth_58 = findViewById(R.id.width_58mm);
        paperWidth_58.setOnClickListener(this);
        is_58mm = paperWidth_58.isChecked();

        paperWidth_80 = findViewById(R.id.width_80mm);
        paperWidth_80.setOnClickListener(this);

        printer_type_remin = findViewById(R.id.type_remin);
        printer_type_remin.setOnClickListener(this);
        is_thermal = printer_type_remin.isChecked();

        printer_type_styuls = findViewById(R.id.type_styuls);
        printer_type_styuls.setOnClickListener(this);

//        mMoneyNum = findViewById(R.id.etMoney);
//         Set up the custom title
        mBarCodeNum = findViewById(R.id.bar_code_num);

        //编码方式部分
        encodingTypeSpinner = findViewById(R.id.spinner_encoding_type);
        encodingTypeList.add("GBK");
        encodingTypeList.add("BIG5");
        encodingTypeList.add("GB18030");
        encodingTypeList.add("GB2312");

        //第二步：为下拉列表定义一个适配器，用到里前面定义的list。
        encodingTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, encodingTypeList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        encodingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        encodingTypeSpinner.setAdapter(encodingTypeAdapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        encodingTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                //myTextView.setText("您选择的是："+ adapter.getItem(arg2));
                encodeType = encodingTypeAdapter.getItem(arg2);
                if(usb.isChecked()){
                    if(mPrinter != null)
                    {
                        mPrinter.setEncoding(encodeType);
                    }
               }else if(bluetooth.isChecked()){
                    if (bPrinter!=null){
                        bPrinter.setEncoding(encodeType);
                    }
                }
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });

        //条码类型部分设置-------start
        barCodeSpinner = findViewById(R.id.spinner_barcode_type);
        //第一步: 填充list
        list.add(getString(R.string.all));
        list.add("UPC_A");
        //list.add("UPC_E");
        list.add("JAN13(EAN13)");
        list.add("JAN8(EAN8)");
        list.add("CODE39");
        list.add("ITF");
        list.add("CODEBAR");
        list.add("CODE93");
        list.add("CODE128");
        list.add("PDF417");
        list.add("DATAMATRIX");
        list.add("QRCODE");

        //第二步：为下拉列表定义一个适配器，用到里前面定义的list。
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        barCodeSpinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        barCodeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                //myTextView.setText("您选择的是："+ adapter.getItem(arg2));
                String item = adapter.getItem(arg2);
                if(item.equals("UPC_A") || item.equals("UPC_E") || item.equals("JAN13(EAN13)")){
                    mBarCodeNum.setText("123456789012");
                }else if(item.equals("CODE128")){
                    mBarCodeNum.setText("No.1234567890123456");
                }else if(item.equals("JAN8(EAN8)")){
                    mBarCodeNum.setText("1234567");
                }else if(item.equals("PDF417") || item.equals("DATAMATRIX") || item.equals("QRCODE"))
                {
                    mBarCodeNum.setText("123456789012");
                } else{
                    mBarCodeNum.setText("123456");
                }
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
//        barCodeSpinner.setOnTouchListener(new Spinner.OnTouchListener(){
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                /* 将mySpinner 隐藏，不隐藏也可以，看自己爱好*/
//                v.setVisibility(View.INVISIBLE);
//                return false;
//            }
//        });
//        /*下拉菜单弹出的内容选项焦点改变事件处理*/
//        barCodeSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
//        public void onFocusChange(View v, boolean hasFocus) {
//        // TODO Auto-generated method stub
//            v.setVisibility(View.VISIBLE);
//        }
//        });
        //条码类型部分设置-------end

        setButtonState();
    }

    private void setButtonState(){
        openConnect.setText(isConnected ?getString(R.string.disconnect) : getString(R.string.connect));
        printTable.setEnabled(isConnected);
        printText.setEnabled(isConnected);
        printImage.setEnabled(isConnected);
        printNote.setEnabled(isConnected);
        mBarCodeNum.setEnabled(isConnected);
//        mMoneyNum.setEnabled(isConnected);
        getState.setEnabled(isConnected);
        printPDF.setEnabled(isConnected);
//        prnAlpay.setEnabled(isConnected);
//        prnM22.setEnabled(isConnected);
//        SetBlack.setEnabled(isConnected);
        prnLable.setEnabled(isConnected);
        prnWeb.setEnabled(isConnected);
        printBarCode.setEnabled(isConnected);
        printer_type_remin.setEnabled(isConnected);
        printer_type_styuls.setEnabled(isConnected);
        paperWidth_58.setEnabled(isConnected);
        paperWidth_80.setEnabled(isConnected);
        encodingTypeSpinner.setEnabled(isConnected);
        barCodeSpinner.setEnabled(isConnected);
    }

    // use device to init usbprinter.
    private void initPrinter(UsbDevice device){
        mUsbDevice = device;
        mPrinter = new USBPrinter(mContext);
        if(is_thermal)
        {
            mPrinter.setCurrentPrintType(is_58mm ? PrinterType.Printer_58 : PrinterType.Printer_80);
        }else{
            mPrinter.setCurrentPrintType(PrinterType.Printer_80);
        }
        mPrinter.setEncoding(encodeType);
        UsbManager mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        mUsbManager.requestPermission(mUsbDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
        if(mUsbManager.hasPermission(mUsbDevice))
        {
            Log.d(TAG,"initPrinter()has permission and conn.");
            openConnection(mUsbDevice);
        }else{
            Log.d(TAG,"initPrinter()requestPermission");
            // 没有权限询问用户是否授予权限
            mUsbManager.requestPermission(mUsbDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
        }
    }

    // use device to init Bluetoothprinter.
    private void initPrinter(BluetoothDevice device){
        bDevice =device;
        bPrinter = new BluetoothPrinter(device);
        if(is_thermal)
        {
            bPrinter.setCurrentPrintType(is_58mm ? PrinterType.Printer_58 : PrinterType.Printer_80);
        }else{
            bPrinter.setCurrentPrintType(PrinterType.Printer_80);
        }
        //set handler for receive message of connect state from sdk.
        bPrinter.setEncoding(encodeType);
        bPrinter.setHandler(bHandler);
//        bPrinter.setAutoReceiveData(true);
        //返回值验证
        bPrinter.setNeedVerify(false);
        bPrinter.openConnection();
    }

    // use device to init bluetoothprinter.
    @SuppressWarnings({ "deprecation", "unused" })
    private void initPrinter(String deviceName){
        bPrinter = new BluetoothPrinter(deviceName);
        if(bPrinter.isPrinterNull()){
            Toast.makeText(mContext, getString(R.string.no_bounded_device, deviceName), Toast.LENGTH_LONG).show();
            return;
        }
        if(is_thermal)
        {
            bPrinter.setCurrentPrintType(is_58mm ? PrinterType.Printer_58 : PrinterType.Printer_80);
        }else{
            bPrinter.setCurrentPrintType(PrinterType.Printer_80);
        }
        //set handler for receive message of connect state from sdk.
        bPrinter.setHandler(bHandler);
        bPrinter.setEncoding(encodeType);
        bPrinter.openConnection();
        Log.d("openConnection","openConnection");
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (usb.isChecked()) {
            usb.setChecked(true);
            bluetooth.setChecked(false);
            wifi.setChecked(false);
            if (view == openConnect) {
                if (isConnected) {
                    mPrinter.closeConnection();
                    isConnected = false;
                    setButtonState();
                } else {
                    Intent serverIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
            } else if (view == printText) {
                Intent edit_intent = new Intent(mContext, UsbPrinterTextEdit.class);
                startActivity(edit_intent);
            } else if (view == printTable) {
                UsbPrintUtils.printTable(mContext.getResources(), mPrinter);
            } else if (view == printImage) {
                if (isFastDoubleClick()) {
                    return;
                } else {
                    new Thread(new TimerTask() {
                        @Override
                        public void run() {
                            UsbPrintUtils.printImage(mContext.getResources(), mPrinter, is_thermal);
                        }
                    }).start();
                }
            } else if (view == printNote) {
                UsbPrintUtils.printNotes(mContext.getResources(), mPrinter);
            } else if (view == printBarCode) {
                UsbPrintUtils.printBarCode(mPrinter, mBarCodeNum.getText().toString(), barCodeSpinner.getSelectedItem().toString());
            } else if (view == prnLable) {
                if (mPrinter.getPrinterName().equals("ML31_BT_Printer")) {
                    UsbPrintUtils.printLable(mContext.getResources(), mPrinter);
                } else {
                    //使用创建器创建单选对话框
                    Builder builder = new Builder(this);
                    //设置对话框的图标
                    builder.setIcon(android.R.drawable.alert_light_frame);
                    //设置对话框的标题
                    builder.setTitle(getString(R.string.tip));
                    //设置文本
                    builder.setMessage(getString(R.string.tip_content));
                    //设置确定按钮
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UsbPrintUtils.printLable(mContext.getResources(), mPrinter);
                        }
                    });
                    //设置取消按钮
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }
            } else if (view == printPDF) {
                Intent intent2 = new Intent(PrinterSDKDemo_Plus_USB.this,
                        PdfPrintActivity.class);
                intent2.putExtra("type",1);
                startActivity(intent2);
            } else if (view == prnWeb) {
                Intent intent = new Intent(mContext,WebviewActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }else if (view == prnAlpay){
                String moneyStr = mMoneyNum.getText().toString();
                UsbPrintUtils.printAlpay(mPrinter,moneyStr);
            } else if (view == paperWidth_58) {
                is_58mm = true;
                paperWidth_58.setChecked(true);
                paperWidth_80.setChecked(false);
                if (is_thermal) {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == paperWidth_80) {
                is_58mm = false;
                paperWidth_58.setChecked(false);
                paperWidth_80.setChecked(true);
                if (is_thermal) {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_80);
                } else {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_58);
                }
            } else if (view == printer_type_remin) {
                is_thermal = true;
                printer_type_remin.setChecked(true);
                printer_type_styuls.setChecked(false);
                if (is_58mm) {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == printer_type_styuls) {
                is_thermal = false;
                printer_type_remin.setChecked(false);
                printer_type_styuls.setChecked(true);
                if (is_58mm) {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    mPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == getState) {
                int ret = mPrinter.getPrinterStatus();
                Log.v("ret的值为：", "" + ret);
                StausMesg(ret);
            }
        } else if (bluetooth.isChecked()) {
            usb.setChecked(false);
            wifi.setChecked(false);
            if (view == openConnect) {
                //提示打开蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }else {
                    if (isConnected) {
                        bPrinter.closeConnection();
                    } else {
                        Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            } else if (view == printText) {
                Intent edit_intent = new Intent(mContext, BluetoothPrinterTextEdit.class);
                startActivity(edit_intent);
            } else if (view == printTable) {
                BluetoothPrintUtils.printTable(mContext.getResources(), bPrinter);
            } else if (view == printImage) {
                BluetoothPrintUtils.printImage(mContext.getResources(), bPrinter, is_thermal);
//                自定义图片打印
//                BluetoothPrintUtils.printCustomImage(mContext.getResources(), bPrinter, is_thermal);
            } else if (view == getState) {
                int ret = BluetoothPrintUtils.getPrinterStatus(bPrinter);
                Log.v("ret的值为：", "" + ret);
                StausMesg(ret);
            } else if (view == printNote) {
                BluetoothPrintUtils.printNotes(mContext.getResources(), bPrinter);
            } else if (view == printBarCode) {
                if (!barCodeSpinner.getSelectedItem().toString().equals(getString(R.string.all))) {
                    if (mBarCodeNum.getText().equals("")) {
                        Toast.makeText(PrinterSDKDemo_Plus_USB.this,getString(R.string.default_char), Toast.LENGTH_SHORT).show();
                    }
                }
                BluetoothPrintUtils.printBarCode(mContext.getResources(), bPrinter, mBarCodeNum.getText().toString(), barCodeSpinner.getSelectedItem().toString());
            } else if (view == printPDF) {
                Intent intent2 = new Intent(PrinterSDKDemo_Plus_USB.this,
                        PdfPrintActivity.class);
                intent2.putExtra("type",2);
                startActivity(intent2);
            } else if (view == prnLable) {
                if (bPrinter.getPrinterName().contains("ML31")) {
//                  Intent intent = new Intent(PrinterSDKDemo_Plus_USB.this,LableEditActivity.class);
//                  startActivity(intent);
                    BluetoothPrintUtils.lablePrint(mContext.getResources(), bPrinter);
                } else {
                    //使用创建器创建单选对话框
                    Builder builder = new Builder(this);
                    //设置对话框的图标
                    builder.setIcon(android.R.drawable.alert_light_frame);
                    //设置对话框的标题
                    builder.setTitle(getString(R.string.tip));
                    //设置文本
                    builder.setMessage(getString(R.string.tip_content));
                    //设置确定按钮
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                                 BluetoothPrintUtils.lablePrint(mContext.getResources(), bPrinter);
                        }
                    });
                    //设置取消
                    //设置取消按钮
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }
            } else if (view == prnWeb) {
                Intent intent = new Intent(mContext,WebviewActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);             //开始切换
            }else if (view == prnAlpay){
                String moneyStr = mMoneyNum.getText().toString();
                BluetoothPrintUtils.printAlpay(bPrinter,moneyStr);
            }else if (view == paperWidth_58) {
                is_58mm = true;
                paperWidth_58.setChecked(true);
                paperWidth_80.setChecked(false);
                if (is_thermal) {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == paperWidth_80) {
                is_58mm = false;
                paperWidth_58.setChecked(false);
                paperWidth_80.setChecked(true);
                if (is_thermal) {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_80);
                } else {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_58);
                }
            } else if (view == printer_type_remin) {
                is_thermal = true;
                printer_type_remin.setChecked(true);
                printer_type_styuls.setChecked(false);
                if (is_58mm) {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                bPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == printer_type_styuls) {
                is_thermal = false;
                printer_type_remin.setChecked(false);
                printer_type_styuls.setChecked(true);
                if (is_58mm) {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    bPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            }
//            else if(view ==SetBlack){
//                showConnectingDialog();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Looper.prepare();
//                        byte[] tmp = StringUtil.HexString2Bytes("1f111f44011f460f1f1f");
//                        bPrinter.printByteData(tmp);
//                        try {
//                            Thread.sleep(10000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        dismissConnectingDialog();
//                        new AlertDialog.Builder(PrinterSDKDemo_Plus_USB.this)
//                                .setTitle("提示" )
//                                .setMessage("设置完成,请重启打印机。" )
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        bPrinter.closeConnection();
//                                    }
//                                }).show();
//                        Looper.loop();
//                    }
//                }).start();
//            }else if (view == prnM22){
//                BluetoothPrintUtils.M22Test(bPrinter,mContext.getResources());
//            }
        }else if (wifi.isChecked()){
            usb.setChecked(false);
            bluetooth.setChecked(false);
            if (view == openConnect) {
                if (isConnected) {
                    wiFiPrinter.close();
                } else {
                    Intent intent = new Intent(mContext, IpEditActivity.class);
                    intent.putExtra(GlobalContants.WIFINAME, getWiFiName());
                    startActivityForResult(intent, 1);
                }
            } else if (view == printText) {
                Intent edit_intent = new Intent(mContext, WifiPrinterTextEdit.class);
                startActivity(edit_intent);
            } else if (view ==  printTable) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiPrintUtils.printTable(mContext.getResources(),wiFiPrinter);
                    }
                }).start();
            } else if (view == printImage) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiPrintUtils.printImage(mContext.getResources(), wiFiPrinter, is_thermal);
                    }
                }).start();
            } else if (view == getState) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ret = WifiPrintUtils.getPrinterStatus(wiFiPrinter);
                        Log.v("ret的值为：", "" + ret);
                    }
                }).start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StausMesg(ret);
            } else if (view == printNote) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiPrintUtils.printNotes(mContext.getResources(), wiFiPrinter);
                    }
                }).start();
            } else if (view == printBarCode) {
                if (!barCodeSpinner.getSelectedItem().toString().equals(getString(R.string.all))) {
                    if (mBarCodeNum.getText().equals("")) {
                        Toast.makeText(PrinterSDKDemo_Plus_USB.this, getString(R.string.default_char), Toast.LENGTH_SHORT).show();
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiPrintUtils.printBarCode(mContext.getResources(), wiFiPrinter, mBarCodeNum.getText().toString(), barCodeSpinner.getSelectedItem().toString());
                    }
                }).start();
            } else if (view == printPDF) {
                Intent intent2 = new Intent(PrinterSDKDemo_Plus_USB.this,
                        PdfPrintActivity.class);
                intent2.putExtra("type",3);
                startActivity(intent2);
            } else if (view == prnLable) {
//                Intent i ntent = new Intent(PrinterSDKDemo_Plus_USB.this,LableEditActivity.class);
//                startActivity(intent);
//                Toast.makeText(PrinterSDKDemo_Plus_USB.this, ""+bPrinter.getPrinterName(), Toast.LENGTH_SHORT).show();
                if (wiFiPrinter.getPrinterName().equals("ML31_BT_Printer")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WifiPrintUtils.printLable(mContext.getResources(), wiFiPrinter);
                        }
                    }).start();
                } else {
                    //使用创建器创建单选对话框
                    Builder builder = new Builder(this);
                    //设置对话框的图标
                    builder.setIcon(android.R.drawable.alert_light_frame);
                    //设置对话框的标题
                    builder.setTitle(getString(R.string.tip));
                    //设置文本
                    builder.setMessage(getString(R.string.tip_content));
                    //设置确定按钮
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    WifiPrintUtils.printLable(mContext.getResources(), wiFiPrinter);
                                }
                            }).start();
                        }
                    });
                    //设置取消
                    //设置取消按钮n
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }
            } else if (view == prnWeb) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext,WebviewActivity.class);
                        intent.putExtra("type",3);
                        startActivity(intent);
                    }
                }).start();
            }else if (view == prnAlpay){
                String moneyStr = mMoneyNum.getText().toString();
                WifiPrintUtils.printAlpay(wiFiPrinter,moneyStr);
            } else if (view == paperWidth_58) {
                is_58mm = true;
                paperWidth_58.setChecked(true);
                paperWidth_80.setChecked(false);
                if (is_thermal) {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            } else if (view == paperWidth_80) {
                is_58mm = false;
                paperWidth_58.setChecked(false);
                paperWidth_80.setChecked(true);
                if (is_thermal) {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_80);
                } else {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_58);
                }
            } else if (view == printer_type_remin) {
                is_thermal = true;
                printer_type_remin.setChecked(true);
                printer_type_styuls.setChecked(false);
                if (is_58mm) {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }

            } else if (view == printer_type_styuls) {
                is_thermal = false;
                printer_type_remin.setChecked(false);
                printer_type_styuls.setChecked(true);
                if (is_58mm) {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_58);
                } else {
                    wiFiPrinter.setCurrentPrintType(PrinterType.Printer_80);
                }
            }
        }
    }

    private String getWiFiName(){
        String wifiName = "";
        WifiManager mWifi = (WifiManager) mContext.getApplicationContext().
                getSystemService(Context.WIFI_SERVICE);
        if (!mWifi.isWifiEnabled()) {
            mWifi.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        wifiName = wifiInfo.getSSID();
        wifiName = wifiName.replaceAll("\"", "");
        return wifiName;

    }

    public String getFromAssets(String fileName) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName),"UTF-8");
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line="";
        String Result="";
        while((line = bufReader.readLine()) != null)
            Result += line;
        return Result;
    }

    private  Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            byte[] data = (byte[]) msg.obj;
            StringBuffer dataHexStr = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                dataHexStr.append(String.format("%x", data[i]));
            }
            Toast.makeText(mContext, "receive data is: " + dataHexStr.toString() + " ...length is : " + msg.arg1,Toast.LENGTH_SHORT).show();
        }
    };



    // The Handler that gets information back from the bluetooth printer.
    private final Handler bHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "msg.what is : " + msg.what);
            switch (msg.what) {
                case BluetoothPrinter.Handler_Connect_Connecting:
                    isConnecting = true;
                    //mTitle.setText(R.string.title_connecting);
                    Log.d(TAG, "handleMessage: "+100);
                    Toast.makeText(getApplicationContext(), R.string.bt_connecting,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Success:
                    isConnected = true;
                    isConnecting = false;
                    Log.d(TAG, "handleMessage: "+101);
                   // mTitle.setText(getString(R.string.title_connected) + ": "+ mPrinter.getPrinterName());
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_success,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Failed:
                    isConnected = false;
                    isConnecting = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: "+102);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_failed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Closed:
                    isConnected = false;
                    isConnecting = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: "+103);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_closed, Toast.LENGTH_SHORT).show();
                case BluetoothPrinter.Handler_Message_Read:
                    if (msg!=null){
                        if (msg.obj!=null){
                            int states = (int) msg.obj;
                            Log.d(TAG, "handleMessage: states:"+states);
                        }else {
                            Log.d(TAG, "handleMessage: 没有返回值");
                        }
                    }else {
                        Log.d(TAG, "handleMessage: msg消息为空");
                    }
            }
            setButtonState();
        }
    };

    // The Handler that gets information back from the wifiPrinter.
    private final Handler wHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "msg.what is1 : " + msg.what);
            switch (msg.what) {
                case BluetoothPrinter.Handler_Connect_Connecting:
                    isConnecting = true;
                    //mTitle.setText(R.string.title_connecting);
                    Log.d(TAG, "handleMessage1: "+100);
                    Toast.makeText(getApplicationContext(), R.string.bt_connecting,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Success:
                    isConnected = true;
                    isConnecting = false;
                    Log.d(TAG, "handleMessage1: "+101);
                    // mTitle.setText(getString(R.string.title_connected) + ": "+ mPrinter.getPrinterName());
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_success,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Failed:
                    isConnected = false;
                    isConnecting = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage1: "+102);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_failed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Closed:
                    isConnected = false;
                    isConnecting = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage1: "+103
                    );
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_closed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Message_Read:
                    int length=  msg.arg1;
                    break;
            }
            setButtonState();
        }
    };

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1500) {
            //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    URL= createDialog.text_name.getText().toString().trim();
                    Log.d(TAG, "onClick: "+URL);
                    if (URL==null||URL.equals("")){
                        Toast.makeText(PrinterSDKDemo_Plus_USB.this, "网址不为空。", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(mContext,WebviewActivity.class);
                        Bundle bundle = new Bundle();      //创建Bundle对象
                        bundle.putString("url", URL);     //装入数据
                        intent.putExtras(bundle);          //把Bundle塞入Intent里面
                        startActivity(intent);             //开始切换
                    }
                    break;
            }
        }
    };

    private ProgressDialog connectingDialog;
    private void showConnectingDialog() {
        try {
            connectingDialog = new ProgressDialog(this);
            connectingDialog.setTitle("正在设置打印机...");
            connectingDialog.setMessage("正在设置...");
            connectingDialog.setCancelable(false);
            connectingDialog.setCanceledOnTouchOutside(false);
            connectingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissConnectingDialog() {
        try {
            if (connectingDialog != null && connectingDialog.isShowing()
                    && !isFinishing())
                connectingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StausMesg(int ret){
        switch (ret) {
            case 0:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this, R.string.states_ok, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this, R.string.states_lack_of_paper, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this, R.string.states_uncap, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_lack_of_paper_and_cover, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_and_cutter_error, Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_and_cutter_error_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_error_in_opening_cover_and_cutting_knife, Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_open_cover_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 9:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_uncover_and_cutter_errors_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 10:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_and_Opening_and_cutter_error, Toast.LENGTH_SHORT).show();
                break;
            case 11:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_opening_cover_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 12:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_paper_shortage_open_cover_and_excessive_temperature_and_Cutter_Error, Toast.LENGTH_SHORT).show();
                break;
            case 13:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_cutter_error, Toast.LENGTH_SHORT).show();
                break;
            case 14:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_overtemperature, Toast.LENGTH_SHORT).show();
                break;
            case 15:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_cutter_error_and_over_temperature, Toast.LENGTH_SHORT).show();
                break;
            case 16:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_communication_anomaly, Toast.LENGTH_SHORT).show();
                break;
            case 17:
                Toast.makeText(PrinterSDKDemo_Plus_USB.this,R.string.states_other_error, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

/*----------------------------------------------JSON-------------------------------------------------------*/
//   byte[] tmp = "! 0 200 200 1500 1 PAGE-WIDTH 832\r\n".getBytes();
//   bPrinter.printByteData(tmp);

//   NewLablePrinter myLablePrinter = new NewLablePrinter();
//   List<byte[]> data = new ArrayList<byte[]>();
//  String json = null;
//   try {
//        json =  ("zhilai2.json");
//  } catch (IOException e) {
//      e.printStackTrace();
//  }
//  if (json!= null){
//      Log.d(TAG, "onClick: json解析成功"+json);
//      data = myLablePrinter.parserFromJson(json);
//   }else {
//       Log.d(TA//   }
//   StringBuffer  sb  = new StringBuffer();
//   String str;
//  for (byte[] b : data) {
//      str = new String(b);
//      sb.append(str);
//     bPrinter.printByteData(b);
// }
// Log.d(TAG, "onClick: "+sb.toString());
//  tmp = "FORM\r\nPRINT\r\n".getBytes();
//  bPrinter.printByteData(tmp);