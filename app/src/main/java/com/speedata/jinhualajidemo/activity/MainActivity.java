package com.speedata.jinhualajidemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.been.CodeBeen;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleNotifyCallback;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;
import com.speedata.jinhualajidemo.clj.fastble.scan.BleScanRuleConfig;
import com.speedata.jinhualajidemo.utils.DataConversionUtils;
import com.speedata.jinhualajidemo.utils.ScanManage;
import com.speedata.jinhualajidemo.view.SearchBtPrintDialog;
import com.speedata.jinhualajidemo.view.SearchBleWeightDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    /**
     * 绿色使命
     */
    private TextView mTvTitle;
    /**
     * 厨余
     */
    private Button mBtnChuyulaji;
    /**
     * 其它
     */
    private Button mBtnOther;
    /**
     * 可回收
     */
    private Button mBtnHuishou;
    /**
     * 住户号:18-2-3
     */
    private EditText mEtName;
    /**
     * 重量:12.6KG
     */
    private EditText mEtWeight;

    /**
     * 扫描
     */
    private Button mBtnScan;
    /**
     * 取证
     */
    private Button mBtnQuzheng;
    /**
     * 打印
     */
    private Button mBtnPrint;
    /**
     * 一般
     */
    private Button mBtnYiban;
    /**
     * 好
     */
    private Button mBtnHao;
    /**
     * 很好
     */
    private Button mBtnHenhao;
    /**
     * 3
     */
    private Button mBtnThree;
    /**
     * 4
     */
    private Button mBtnFour;
    /**
     * 5
     */
    private Button mBtnFive;

    /**
     * 13245896325
     */
    private EditText mEdtName;
    /**
     * admin
     */
    private EditText mEdtPwd;
    /**
     * 登录
     */
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!MyApplication.getPreferences().getLogin("login", false)) {
            setContentView(R.layout.activity_login);
            initLoginView();

        } else {
            setContentView(R.layout.activity_main2);
            EventBus.getDefault().register(this);
            initView();

        }
    }

    private void initLoginView() {
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPwd = (EditText) findViewById(R.id.edt_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    private void initScan() {
        ScanManage.initScan(MainActivity.this);
        ScanManage.getCodeMsg();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScanCode(CodeBeen codeBeen) {
        mEtName.setText("住户号:" + codeBeen.getCodeMsg());
    }

    private void initBle() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initB();
        initBle();
        initScan();
    }


    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        Typeface mtypeface = Typeface.createFromAsset(getAssets(), "fonts/STCAIYUN.TTF");
        mTvTitle.setTypeface(mtypeface);
        mTvTitle.setOnClickListener(this);
        mBtnChuyulaji = (Button) findViewById(R.id.btn_chuyulaji);
        mBtnChuyulaji.setOnClickListener(this);
        mBtnOther = (Button) findViewById(R.id.btn_other);
        mBtnOther.setOnClickListener(this);
        mBtnHuishou = (Button) findViewById(R.id.btn_huishou);
        mBtnHuishou.setOnClickListener(this);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtWeight = (EditText) findViewById(R.id.et_weight);
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(this);
        mBtnQuzheng = (Button) findViewById(R.id.btn_quzheng);
        mBtnQuzheng.setOnClickListener(this);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnPrint.setOnClickListener(this);
        mBtnYiban = (Button) findViewById(R.id.btn_yiban);
        mBtnYiban.setOnClickListener(this);
        mBtnHao = (Button) findViewById(R.id.btn_hao);
        mBtnHao.setOnClickListener(this);
        mBtnHenhao = (Button) findViewById(R.id.btn_henhao);
        mBtnHenhao.setOnClickListener(this);
        mBtnThree = (Button) findViewById(R.id.btn_three);
        mBtnThree.setOnClickListener(this);
        mBtnFour = (Button) findViewById(R.id.btn_four);
        mBtnFour.setOnClickListener(this);
        mBtnFive = (Button) findViewById(R.id.btn_five);
        mBtnFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
                break;
            case R.id.btn_chuyulaji:
                break;
            case R.id.btn_other:
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_huishou:
                break;
            case R.id.btn_scan:

//                scanDecode.starScan();
                ScanManage.startScan();

                break;
            case R.id.btn_quzheng:
                break;
            case R.id.btn_print:
                break;
            case R.id.btn_yiban:
                break;
            case R.id.btn_hao:
                break;
            case R.id.btn_henhao:
                SearchBtPrintDialog searchTags = new SearchBtPrintDialog(this);
                searchTags.setCanceledOnTouchOutside(false);
                searchTags.setTitle("请搜索蓝牙设备");
                searchTags.show();
                break;
            case R.id.btn_three:
                break;
            case R.id.btn_four:
                if (bPrinter == null) {
                    MyApplication.showToast(this, "请先连接打印机");
                    return;
                } else {
                    if (!bPrinter.isConnected()) {
                        MyApplication.showToast(this, "请先连接打印机");
                    }
                }
                bPrinter.setPage(576, 400);
                bPrinter.printText("CENTER\r\n");
                bPrinter.drawText(0, 0, "金华垃圾", 2, 0, 0, false, false);
                bPrinter.drawText(0, 32, "请扫描二维码投放垃圾", 2, 0, 0, false, false);
                bPrinter.drawQrCode(0, 80, 2, 9, "住户号:123-2，重量:12kg，分类:有害垃圾", 0);

                bPrinter.labelPrint(0, 1);
//                Barcode barcode2 = new Barcode(BluetoothPrinter.
//                        BAR_CODE_TYPE_QRCODE, 4, 3, 7, "住户号:123-2，重量:12.5KG，分类:有害垃圾");
//                bPrinter.setCharacterMultiple(0, 0);
//                bPrinter.setLeftMargin(0, 0);
//                bPrinter.printText("金华垃圾" + "\n请扫描二维码投放垃圾");
//                bPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 1);
//                bPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
//                bPrinter.printBarCode(barcode2);//mPrinter 实例化的打
//                bPrinter.printByteData(new byte[]{0x0c});

                break;
            case R.id.btn_five:
                BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                        .setDeviceName(true, "HC-02", "ML31_BT")   // 只扫描指定广播名的设备，可选
                        .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                        .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                        .build();
                BleManager.getInstance().initScanRule(scanRuleConfig);
                SearchBleWeightDialog searchTag = new SearchBleWeightDialog(this);
                searchTag.setCanceledOnTouchOutside(false);
                searchTag.setTitle("请搜索蓝牙设备");
                searchTag.show();
                searchTag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
//                        BleManager.getInstance().isConnected()
                        String strMac = MyApplication.getPreferences().getMac("mac");
                        for (int i = 0; i < deviceList.size(); i++) {
                            if (!strMac.contains(deviceList.get(i).getMac())) {
                                MyApplication.getPreferences().setMac("mac", deviceList.get(i).getMac());
                            }
                            initPrintandWeight(deviceList.get(i));
                        }
                    }
                });
                break;
            case R.id.btn_login:
                String name = mEdtName.getText().toString();
                String pwd = mEdtPwd.getText().toString();
                if (name.equals("") && pwd.equals("")) {
                    MyApplication.showToast(this, "请输入用户名和密码！");
                } else {
                    MyApplication.getPreferences().setLogin("login", true);
                    setContentView(R.layout.activity_main2);
                    EventBus.getDefault().register(this);
                    initView();
                    initB();
                    initBle();
                    initScan();
                }
                break;
        }
    }

    private void initPrintandWeight(BleDevice bleDevice) {
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
                            mEtWeight.setText("重量:" + Double.valueOf(weight) + "KG");
                        }
                    });
        } else {
            // 连接建立之前的先配对
            try {
                if (bleDevice.getDevice().getBondState() == BluetoothDevice.BOND_NONE) {
                    Method creMethod = BluetoothDevice.class.getMethod("createBond");
                    Toast.makeText(this, "正开始进行配对蓝牙...", Toast.LENGTH_SHORT).show();
                    creMethod.invoke(bleDevice.getDevice());
                } else {
                    Toast.makeText(this, "已经配对...", Toast.LENGTH_SHORT).show();
                    initPrinter(bleDevice.getDevice());
                }
            } catch (Exception e) {
                Toast.makeText(this, "无法进行配对...请配对后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkUUIDisBle(BleDevice bleDevice) {
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

    private void initB() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.not_find_bluetooth, Toast.LENGTH_LONG).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果 API level 是大于等于 23（安卓6.0以上）
                //判断是否具有权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //判断是否需要向用户解释为什么需要申请该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Toast.makeText(this, "自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                    }
                    //请求权限
                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                } else {
                    Intent requestBluetoothOn = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // 请求开启 Bluetooth
                    this.startActivityForResult(requestBluetoothOn,
                            REQUEST_CODE_BLUETOOTH_ON);
                }
            }

        }

    }

    public static BluetoothPrinter bPrinter;
    private String TAG = "stw";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    BluetoothAdapter mBluetoothAdapter;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    private int REQUEST_CODE_BLUETOOTH_ON = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启
                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    // TODO 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    Intent requestBluetoothOn = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // 请求开启 Bluetooth
                    this.startActivityForResult(requestBluetoothOn,
                            REQUEST_CODE_BLUETOOTH_ON);
                }
                break;
                default:
                    break;
            }
        }
    }

    // use device to setHintMsg Bluetoothprinter.
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

    @SuppressLint("HandlerLeak")
    private final Handler bHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("STW", "msg.what is : " + msg.what);
            switch (msg.what) {
                case BluetoothPrinter.Handler_Connect_Connecting:
                    //mTitle.setText(R.string.title_connecting);
                    Log.d(TAG, "handleMessage: " + 100);
                    Toast.makeText(getApplicationContext(), R.string.bt_connecting, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Success:
                    Log.d(TAG, "handleMessage: " + 101);
                    // mTitle.setText(getString(R.string.title_connected) + ": "+ mPrinter.getPrinterName());
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_success, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Failed:
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: " + 102);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_failed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Closed:
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: " + 103);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_closed, Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bPrinter != null) {
            bPrinter.closeConnection();
        }
        EventBus.getDefault().unregister(this);
    }
}
