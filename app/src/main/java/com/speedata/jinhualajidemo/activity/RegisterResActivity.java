package com.speedata.jinhualajidemo.activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scandecode.ScanDecode;
import com.speedata.jinhualajidemo.App;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.been.CodeBeen;
import com.speedata.jinhualajidemo.been.DecryptionBeen;
import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionUserBeen;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;
import com.speedata.jinhualajidemo.been.UserInfao;
import com.speedata.jinhualajidemo.utils.LogUtil;
import com.speedata.jinhualajidemo.utils.NetUtils;
import com.speedata.jinhualajidemo.utils.NfcUtils;
import com.speedata.jinhualajidemo.utils.ScanManage;
import com.speedata.jinhualajidemo.view.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class RegisterResActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImgReadcardGif;

    private RequestOptions options;
    /**
     * 请输入手机号
     */
    private EditText mEdtPhoneNum;
    private ImageView mImageSearch;
    /**
     * 王鹤（ID:rty7895123）  结果
     */
    private TextView mTvShowRes;
    /**
     * 登记
     */
    private Button mBtnRegister;
    private LinearLayout mLayoutPhone;
    private String TAG = "RegisterResActivity";
    /**
     * 结果
     */
    private TextView mTvShowRes2;
    private LinearLayout mLayoutRet;

    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_res);
        initView();
        setMyAppTitle();
        options = new RequestOptions();
        NetUtils.setNetResultCallback(new NetUtils.NetResultCallback() {
            @Override
            public void encryption(EncryptionBeenRet encryptionBeenRet) {
                switch (encryptionBeenRet.getCode()) {
                    case App.SUCCESS:
                        //调用校验用户接口
                        NetUtils.checkUser(encryptionBeenRet.getContent());
                        break;
                    default:
                        MyApplication.showToast(RegisterResActivity.this, encryptionBeenRet.getMesg());
                        break;
                }

            }

            @Override
            public void decryption(DecryptionBeenRet decryptionBeenRet) {
                switch (decryptionBeenRet.getCode()) {
                    case App.SUCCESS:
                        // TODO: 2019/7/19 展示  解密手机号 返回用户信息  需要保存这些信息  下一个界面展示
                        Gson gson = new Gson();
                        App.userInfo = gson.fromJson(decryptionBeenRet.getContent(), UserInfao.class);
                        if (App.cardType.equals("1")) {
                            isCheck = true;
                            mLayoutRet.setBackground(getResources().getDrawable(R.color.viewfinder_mask));
                            mTvShowRes.setText("ID:" + App.userInfo.getCardId());
                            mTvShowRes2.setText("结果");
                        } else {
                            Intent intent = new Intent(RegisterResActivity.this, LaClassifyActivity.class);
                            startActivity(intent);
                        }

                        MyApplication.showToast(RegisterResActivity.this, "成功");
                        break;

                    default:
                        MyApplication.showToast(RegisterResActivity.this, decryptionBeenRet.getMesg());
                        break;
                }
            }

            @Override
            public void loginRet(LoginBeenRet loginBeenRet) {

            }

            @Override
            public void checkUserinfo(NetResultBeen userInfaoBeenRet) {
                switch (userInfaoBeenRet.getCode()) {
                    case App.SUCCESS:
                        // TODO: 2019/7/19 返回用户信息 进行解密
                        DecryptionBeen decryptionBeen = new DecryptionBeen(MyApplication.key, MyApplication.pdaIMEI, MyApplication.getPreferences().getManageloginName("name", ""), MyApplication.pdaIMEI + "_" + System.currentTimeMillis(), System.currentTimeMillis(), userInfaoBeenRet.getContent());
                        NetUtils.deecryptionData(decryptionBeen);
                        break;

                    default:
                        MyApplication.showToast(RegisterResActivity.this, userInfaoBeenRet.getMesg());
                        break;
                }

            }

            @Override
            public void getQrcodeInfo(EncryptionBeenRet encryptionBeenRet) {

            }


            @Override
            public void uplodScore(UploadScoreBeenRet uploadScoreBeenRet) {

            }

            @Override
            public void netError(String msg) {
                MyApplication.showToast(RegisterResActivity.this, msg);
                LogUtil.e("netError: " + msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (App.cardType) {
            default:
                break;
            case "3":
                //刷卡
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(this).load(R.drawable.slot_card).apply(options).into(mImgReadcardGif);
                initNfc();
                break;
            case "2":
                //扫描
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(this).load(R.drawable.scaning_anmi).apply(options).into(mImgReadcardGif);
                EventBus.getDefault().register(this);
                initScan();

                break;
            case "1":
                //手机号
                mLayoutPhone.setVisibility(View.VISIBLE);
                mImgReadcardGif.setVisibility(View.GONE);
                break;
        }
    }

    private void initNfc() {
        NfcUtils nfcUtils = new NfcUtils(this);
        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }

    private void initScan() {
        ScanManage.initScan(RegisterResActivity.this);
        ScanManage.getCodeMsg();
        ScanManage.startScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScanCode(CodeBeen codeBeen) {
        // TODO: 2019/7/11   网络请求验证  条码登记  跳转下一步骤  记得将获取到user信息缓存
        sendEencyption(codeBeen.getCodeMsg(), 2);

    }

    private void setMyAppTitle() {
        TitleBarView titleBarView = (TitleBarView) this.findViewById(R.id.title_layout);
        titleBarView.initViewsVisible(true, true, false, true);

        titleBarView.setAppTitle("");
        titleBarView.setOnLeftButtonClickListener(new TitleBarView.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mImgReadcardGif = (ImageView) findViewById(R.id.img_readcard_gif);
        mEdtPhoneNum = (EditText) findViewById(R.id.edt_phone_num);
        mImageSearch = (ImageView) findViewById(R.id.image_search);
        mImageSearch.setOnClickListener(this);
        mTvShowRes = (TextView) findViewById(R.id.tv_show_res);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_phone);
        mTvShowRes2 = (TextView) findViewById(R.id.tv_show_res2);
        mLayoutRet = (LinearLayout) findViewById(R.id.layout_ret);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // TODO: 2019/7/11   网络请求验证  卡号登记   跳转下一步骤  记得将获取到user信息缓存
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = NfcUtils.ByteArrayToHexString(tag.getId());
        LogUtil.i("onNewIntent: " + id);
        sendEencyption(id, 3);
    }

    private void sendEencyption(String card, int cardType) {
        EncryptionUserBeen.ContentBean contentBean = new EncryptionUserBeen.ContentBean();
        contentBean.setCard(card);
        contentBean.setCardType(cardType);
        EncryptionUserBeen encryptionBeen = new EncryptionUserBeen(MyApplication.pdaIMEI + "_" + System.currentTimeMillis(), contentBean, MyApplication.pdaIMEI, MyApplication.getPreferences().getManageloginName("name", ""), MyApplication.key, System.currentTimeMillis());
        final Gson gson = new GsonBuilder().serializeNulls().create();
        String resultData = gson.toJson(encryptionBeen);
        LogUtil.i("加密发送：：" + resultData);
        NetUtils.encryptionData(resultData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        switch (App.cardType) {
            default:
                break;
            case "3":
                NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
                break;
            case "2":
                ScanManage.stopScan();
                ScanManage.disScan();
                EventBus.getDefault().unregister(this);
                break;
            case "1":
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.image_search:
                // TODO: 2019/7/10  联网查询
                if (mEdtPhoneNum.getText().toString().equals("")) {
                    MyApplication.showToast(this, "请先输入手机号查询");
                } else {
                    String phoneNum = mEdtPhoneNum.getText().toString();
                    sendEencyption(phoneNum, 1);
                }
                break;
            case R.id.btn_register:
                // TODO: 2019/7/10   跳转下一步骤  记得将获取到user信息缓存
                if (isCheck) {
                    Intent intent = new Intent(this, LaClassifyActivity.class);
                    startActivity(intent);
                } else {
                    MyApplication.showToast(this, "请先输入手机号查询");
                }
                break;
        }
    }
}
