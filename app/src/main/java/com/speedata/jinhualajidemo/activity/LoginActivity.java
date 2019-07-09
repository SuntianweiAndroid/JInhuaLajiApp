package com.speedata.jinhualajidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
        if (MyApplication.getPreferences().getLogin("login",false)) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtPwd = (EditText) findViewById(R.id.edt_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                String name = mEdtName.getText().toString();
                String pwd = mEdtPwd.getText().toString();
                if (name.equals("") && pwd.equals("")) {
                    MyApplication.showToast(this, "请输入用户名和密码！");
                } else {
                    MyApplication.getPreferences().setLogin("login", true);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                break;
        }
    }
}
