package com.speedata.jinhualajidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shehuan.niv.NiceImageView;
import com.speedata.jinhualajidemo.App;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.been.UserInfao;
import com.speedata.jinhualajidemo.view.TitleBarView;

public class LaClassifyActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 厨余垃圾
     */
    private TextView mTxChuyu;
    /**
     * 可回收垃圾
     */
    private TextView mTxHuishou;
    /**
     * 有害垃圾
     */
    private TextView mTxYouhai;
    /**
     * 其它垃圾
     */
    private TextView mTxOther;
    private NiceImageView mImageUserHead;
    /**
     * 刘小霞
     */
    private TextView mTvUserName;
    /**
     * ID:4657892
     */
    private TextView mTvUserId;
    /**
     * 256
     */
    private TextView mTvUserScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_la_classify);
        initView();
        setMyAppTitle();
    }

    private void setMyAppTitle() {
        TitleBarView titleBarView = (TitleBarView) this.findViewById(R.id.title_layout);
        titleBarView.initViewsVisible(true, true, false, true);
        titleBarView.setAppTitle("垃圾分类");
        titleBarView.setOnLeftButtonClickListener(new TitleBarView.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mTxChuyu = (TextView) findViewById(R.id.tx_chuyu);
        mTxChuyu.setOnClickListener(this);
        mTxHuishou = (TextView) findViewById(R.id.tx_huishou);
        mTxHuishou.setOnClickListener(this);
        mTxYouhai = (TextView) findViewById(R.id.tx_youhai);
        mTxYouhai.setOnClickListener(this);
        mTxOther = (TextView) findViewById(R.id.tx_other);
        mTxOther.setOnClickListener(this);
        mImageUserHead = (NiceImageView) findViewById(R.id.image_user_head);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvUserId = (TextView) findViewById(R.id.tv_user_id);
        mTvUserScore = (TextView) findViewById(R.id.tv_user_score);
        mTvUserId.setText("ID:"+App.userInfo.getCardId());
        mTvUserName.setText(App.userInfo.getPhone());
        mTvUserScore.setText(App.userInfo.getCredit());

    }

    @Override
    public void onClick(View v) {
        /**
         * 回收品类
         * 1 瓶子，2衣服，3.可回收物，4.有害垃圾，5.纸，6.电子废弃物，7.厨余垃圾，8.其它垃圾，9.干垃圾
         * 10.湿垃圾，11.废旧金属，12.废塑料，13.废旧玻璃
         */
        switch (v.getId()) {
            default:
                break;
            case R.id.tx_chuyu:
                MyApplication.getLajiBeen().setLaClassify("c");
                App.garbageType = 7;
                startAct(CreditScoreActivity.class);
                break;
            case R.id.tx_huishou:
                MyApplication.getLajiBeen().setLaClassify("h");
                App.garbageType = 3;
                startAct(CreditScoreActivity.class);
                break;
            case R.id.tx_youhai:
                MyApplication.getLajiBeen().setLaClassify("y");
                App.garbageType = 4;
                startAct(CreditScoreActivity.class);
                break;
            case R.id.tx_other:
                MyApplication.getLajiBeen().setLaClassify("q");
                App.garbageType = 8;
                startAct(CreditScoreActivity.class);
                break;


        }
    }

    private void startAct(Class aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }
}
