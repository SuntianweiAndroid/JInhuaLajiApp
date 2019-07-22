package com.speedata.jinhualajidemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.speedata.jinhualajidemo.R;

public class HintDialog extends Dialog {

    private String hintMsg;
    private TextView mTvshow;

    public HintDialog(Context context) {
        super(context);
    }

    public HintDialog(Context context, int theme) {
        super(context, theme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint_dialog_layout);
        mTvshow = findViewById(R.id.tv_load_dialog);
        mTvshow.setText(hintMsg);
        //設置点击空白 不关闭
        setCanceledOnTouchOutside(false);
    }

    public void setHintMsg(String hintMsg) {
        this.hintMsg = hintMsg;
    }

}
