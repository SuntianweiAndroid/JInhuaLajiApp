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
import com.speedata.jinhualajidemo.view.TitleBarView;

public class CreditScoreActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * +5
     */
    private TextView mTxZ5;
    /**
     * +3
     */
    private TextView mTxZ3;
    /**
     * -5
     */
    private TextView mTxF5;
    /**
     * -3
     */
    private TextView mTxF3;
    /**
     * 0
     */
    private TextView mTx0;
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
        setContentView(R.layout.activity_credit_score);
        initView();
        setMyAppTitle();
    }

    private void setMyAppTitle() {
        TitleBarView titleBarView = (TitleBarView) this.findViewById(R.id.title_layout);
        titleBarView.initViewsVisible(true, true, false, true);

        titleBarView.setAppTitle("信用评分");
        titleBarView.setOnLeftButtonClickListener(new TitleBarView.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mTxZ5 = (TextView) findViewById(R.id.tx_z5);
        mTxZ5.setOnClickListener(this);
        mTxZ3 = (TextView) findViewById(R.id.tx_z3);
        mTxZ3.setOnClickListener(this);
        mTxF5 = (TextView) findViewById(R.id.tx_f5);
        mTxF5.setOnClickListener(this);
        mTxF3 = (TextView) findViewById(R.id.tx_f3);
        mTxF3.setOnClickListener(this);
        mTx0 = (TextView) findViewById(R.id.tx_0);
        mTx0.setOnClickListener(this);
        mImageUserHead = (NiceImageView) findViewById(R.id.image_user_head);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvUserId = (TextView) findViewById(R.id.tv_user_id);
        mTvUserScore = (TextView) findViewById(R.id.tv_user_score);
        mTvUserId.setText("ID:" + App.userInfo.getCardId());
        mTvUserName.setText(App.userInfo.getPhone());
        mTvUserScore.setText(App.userInfo.getCredit());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tx_z5:
                App.score = 5;
                startAct(PutRecordActivity.class);
                break;
            case R.id.tx_z3:
                App.score = 3;
                startAct(PutRecordActivity.class);
                break;
            case R.id.tx_f5:
                App.score = -5;
                startAct(PutRecordActivity.class);
                break;
            case R.id.tx_f3:
                App.score = -3;

                startAct(PutRecordActivity.class);
                break;
            case R.id.tx_0:
                App.score = 0;
                startAct(PutRecordActivity.class);
                break;
        }
    }

    private void startAct(Class aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }
}
