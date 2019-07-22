package com.speedata.jinhualajidemo.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shehuan.niv.NiceImageView;
import com.speedata.jinhualajidemo.App;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.been.DecryptionBeen;
import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionLoginBeen;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.LoginBeen;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;
import com.speedata.jinhualajidemo.clj.fastble.BleManager;
import com.speedata.jinhualajidemo.clj.fastble.callback.BleNotifyCallback;
import com.speedata.jinhualajidemo.clj.fastble.data.BleDevice;
import com.speedata.jinhualajidemo.clj.fastble.exception.BleException;
import com.speedata.jinhualajidemo.clj.fastble.scan.BleScanRuleConfig;
import com.speedata.jinhualajidemo.utils.DataConversionUtils;
import com.speedata.jinhualajidemo.utils.NetUtils;
import com.speedata.jinhualajidemo.view.HintDialog;
import com.speedata.jinhualajidemo.view.SearchBTDialog;
import com.speedata.jinhualajidemo.view.SearchBleWeightDialog;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 打印机
     */
    private TextView mBlePrinter;
    /**
     * 地磅
     */
    private TextView mBleWeight;
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
    private ConstraintLayout mCardRegister;
    private ConstraintLayout mScanRegister;
    private ConstraintLayout mPhoneNumRegister;
    public static BluetoothPrinter bPrinter;
    BluetoothAdapter mBluetoothAdapter;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    private int REQUEST_CODE_BLUETOOTH_ON = 1;
    private String TAG = "stw";
    private NiceImageView mImageManageHead;
    /**
     * 李亚楠
     */
    private TextView mTvManageName;
    /**
     * 督导员
     */
    private TextView mTvManageType;
    /**
     * 退出
     */
    private Button mBtnOut;
    private ImageView mImageSetting;
    private String loginName;
    private String loginPwd;
    private HintDialog hintDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否登录状态 加载不同布局
        hintDialog = new HintDialog(this, R.style.hintdialog);
        if (!MyApplication.getPreferences().getLogin("login", false)) {
            setContentView(R.layout.activity_login);
            initLoginView();
        } else {
            setContentView(R.layout.activity_menu);
            initView();
        }
    }

    private void initLoginView() {

        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPwd = (EditText) findViewById(R.id.edt_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        String name = MyApplication.getPreferences().getManageloginName("name", "");
        String pwd = MyApplication.getPreferences().getManagePwd("pwd", "");
        if (!name.equals("")) {
            mEdtName.setText(name);
        }
        if (!pwd.equals("")) {
            mEdtPwd.setText(pwd);
        }

        NetUtils.setNetResultCallback(new NetUtils.NetResultCallback() {
            @Override
            public void encryption(EncryptionBeenRet encryptionBeenRet) {
                if (encryptionBeenRet.getCode().equals("1")) {
                    if (encryptionBeenRet != null) {
                        NetUtils.manageLogin(encryptionBeenRet.getContent());
                    }
                } else {
                    MyApplication.showToast(MenuActivity.this, "登录失败");
                    hintDialog.dismiss();
                }
            }

            @Override
            public void decryption(DecryptionBeenRet decryptionBeenRet) {
                if (decryptionBeenRet.getCode().contains("1")) {
                    MyApplication.getPreferences().setLogin("login", true);
                    Gson gson = new Gson();
                    LoginBeen loginBeen = gson.fromJson(decryptionBeenRet.getContent(), LoginBeen.class);
                    MyApplication.getPreferences().setManageName("managename", loginBeen.getName());
                    setContentView(R.layout.activity_menu);
                    hintDialog.dismiss();
                    initView();
                    mTvManageName.setText(loginBeen.getName());
                    MyApplication.getPreferences().setManageloginName("name", loginName);
                    MyApplication.getPreferences().setManageloginName("pwd", loginPwd);
                    initBluetooth();
                    initBle();
                } else {
                    MyApplication.showToast(MenuActivity.this, "登录失败");
                    hintDialog.dismiss();
                }
            }

            @Override
            public void loginRet(LoginBeenRet loginBeenRet) {
//                c4ca4238a0b923820dcc509a6f75849b
                if (loginBeenRet.getCode().equals("1")) {
                    if (loginBeenRet != null) {
                        DecryptionBeen decryptionBeen = new DecryptionBeen(MyApplication.key, MyApplication.pdaIMEI, loginName, MyApplication.pdaIMEI + "_" + System.currentTimeMillis(), System.currentTimeMillis(), loginBeenRet.getContent());
                        NetUtils.deecryptionData(decryptionBeen);
                    }
                } else {
                    MyApplication.showToast(MenuActivity.this, "登录失败");
                    hintDialog.dismiss();
                }
            }

            @Override
            public void checkUserinfo(NetResultBeen userInfaoBeenRet) {

            }

            @Override
            public void getQrcodeInfo(EncryptionBeenRet encryptionBeenRet) {

            }


            @Override
            public void uplodScore(UploadScoreBeenRet uploadScoreBeenRet) {

            }

            @Override
            public void netError(String msg) {
                MyApplication.showToast(MenuActivity.this, "登录失败");
            }
        });
    }

    /**
     * 初始化蓝云ble
     */
    private void initBle() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                // 只扫描指定广播名的设备，可选打印机--地磅
                .setDeviceName(true, "HC-02")
                // 连接时的autoConnect参数，可选，默认false
                .setAutoConnect(false)
                // 扫描超时时间，可选，默认10秒
                .setScanTimeOut(10000)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * 初始化蓝牙  判断是否需要权限 以及开启蓝牙
     */
    private void initBluetooth() {
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
                        Toast.makeText(this, "需要打开位置权限才可以搜索到设备", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        initBluetooth();
        initBle();

    }

    private void initView() {
        mBlePrinter = (TextView) findViewById(R.id.ble_printer);
        mBlePrinter.setOnClickListener(this);
        mBleWeight = (TextView) findViewById(R.id.ble_weight);
        mBleWeight.setOnClickListener(this);
        mCardRegister = (ConstraintLayout) findViewById(R.id.card_register);
        mCardRegister.setOnClickListener(this);
        mScanRegister = (ConstraintLayout) findViewById(R.id.scan_register);
        mScanRegister.setOnClickListener(this);
        mPhoneNumRegister = (ConstraintLayout) findViewById(R.id.phone_num_register);
        mPhoneNumRegister.setOnClickListener(this);
        mImageManageHead = (NiceImageView) findViewById(R.id.image_manage_head);
        mTvManageName = (TextView) findViewById(R.id.tv_manage_name);
        mTvManageType = (TextView) findViewById(R.id.tv_manage_type);
        mBtnOut = (Button) findViewById(R.id.btn_out);
        mBtnOut.setOnClickListener(this);
        mImageSetting = (ImageView) findViewById(R.id.image_setting);
        mImageSetting.setOnClickListener(this);
        String name = MyApplication.getPreferences().getManageName("managename", "");
        if (!name.equals("")) {
            mTvManageName.setText(name);
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ble_weight:
                SearchBleWeightDialog searchTag = new SearchBleWeightDialog(this, R.style.MyDialogStyle);
                searchTag.setCanceledOnTouchOutside(false);
                searchTag.show();
                searchTag.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
                        for (int i = 0; i < deviceList.size(); i++) {
                            initPrintandWeight(deviceList.get(i));
                        }
                    }
                });
                break;
            case R.id.ble_printer:
                SearchBTDialog searchBTDialog = new SearchBTDialog(this, R.style.MyDialogStyle, "ML31_BT");
                searchBTDialog.setCanceledOnTouchOutside(false);
                searchBTDialog.show();
                break;
            case R.id.card_register:
                Intent intent = new Intent(this, RegisterResActivity.class);
                App.cardType = "3";

                intent.putExtra("type_register", 3);
                startActivity(intent);
                break;
            case R.id.scan_register:
                intent = new Intent(this, RegisterResActivity.class);
                intent.putExtra("type_register", 2);
                App.cardType = "2";
                startActivity(intent);
                break;
            case R.id.phone_num_register:
                intent = new Intent(this, RegisterResActivity.class);
                intent.putExtra("type_register", 1);
                App.cardType = "1";
                startActivity(intent);
                break;
            case R.id.btn_login:
                // TODO: 2019/7/11   请求网络 验证后登录
                loginName = mEdtName.getText().toString();
                loginPwd = mEdtPwd.getText().toString();
                if (loginName.equals("") || loginPwd.equals("")) {
                    MyApplication.showToast(this, getResources().getString(R.string.login_show_msg));
                } else {
                    hintDialog.setHintMsg("登陆中...");
                    hintDialog.show();
                    EncryptionLoginBeen.ContentBean contentBean = new EncryptionLoginBeen.ContentBean();
                    contentBean.setPwd(loginPwd);
                    EncryptionLoginBeen encryptionBeen = new EncryptionLoginBeen(MyApplication.pdaIMEI + "_" + System.currentTimeMillis(), contentBean, MyApplication.pdaIMEI, loginName, MyApplication.key, System.currentTimeMillis());
                    final Gson gson = new GsonBuilder().serializeNulls().create();
                    String resultData = gson.toJson(encryptionBeen);
                    Log.i(TAG, "加密发送：：" + resultData);
                    NetUtils.encryptionData(resultData);

                }
                break;
            case R.id.btn_out:
                // TODO: 2019/7/11  退出登录
                MyApplication.getPreferences().setLogin("login", false);
                setContentView(R.layout.activity_login);
                initLoginView();
                break;
            case R.id.image_setting:
                // TODO: 2019/7/11  设置 暂未开发;
                break;
        }
    }


    private void initPrintandWeight(BleDevice bleDevice) {
        if (checkUUIDisBle(bleDevice)) {
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

                            // TODO: 2019/7/12   重量显示 界面
                            String weight = new StringBuffer((DataConversionUtils.byteArrayToAscii(DataConversionUtils.cutBytes(data, 1, data.length - 1)))).reverse().toString();
                            if (weight == null && weight.equals("")) {
                                return;
                            }
                            Log.i("shuju", "onCharacteristicChanged: " + (Double.parseDouble(weight) + "kg"));
//                            mEtWeight.setText("重量:" + Double.valueOf(weight) + "KG");
                        }
                    });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
