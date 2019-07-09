package com.speedata.jinhualajidemo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.speedata.jinhualajidemo.db.DbDaoManage;
import com.speedata.jinhualajidemo.utils.MySharedPreferences;

public class MyApplication extends Application {

    public static MySharedPreferences mySharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mySharedPreferences = new MySharedPreferences(this);
        DbDaoManage.initDb(this);
    }

    public static MySharedPreferences getPreferences() {
        return mySharedPreferences;
    }

    public static void showToast(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
