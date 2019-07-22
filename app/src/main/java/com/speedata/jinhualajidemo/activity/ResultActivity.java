package com.speedata.jinhualajidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.view.TitleBarView;

import java.util.Timer;
import java.util.TimerTask;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBarView mTitleLayout;
    /**
     * 返回主页
     */
    private Button mBtnReturnMenu;
    /**
     * 倒计时:6s
     */
    private TextView mTvTime;
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        setMyAppTitle();
        timer.schedule(task, 1000, 1000);
    }

    private void setMyAppTitle() {
        TitleBarView titleBarView = (TitleBarView) this.findViewById(R.id.title_layout);
        titleBarView.initViewsVisible(false, true, false, false);
        titleBarView.setAppTitle("结果反馈");
    }

    private void initView() {
        mTitleLayout = (TitleBarView) findViewById(R.id.title_layout);
        mBtnReturnMenu = (Button) findViewById(R.id.btn_return_menu);
        mBtnReturnMenu.setOnClickListener(this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_return_menu:
                Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
        }
    }

    int recLen = 6;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {		// UI thread
                @Override
                public void run() {
                    recLen--;
                    mTvTime.setText("倒计时:"+recLen+"s");
                    if(recLen <= 0){
                        timer.cancel();
                        Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };

}
