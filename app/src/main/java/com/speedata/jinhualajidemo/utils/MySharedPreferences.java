package com.speedata.jinhualajidemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MySharedPreferences {

    private Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("jinhua", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public void setLogin(String key, boolean login) {
        editor.putBoolean(key, login);
        editor.commit();//提交数据
    }

    public boolean getLogin(String key, boolean login) {
      return   preferences.getBoolean(key, login);
    }


    public void setName(String key, String name) {
        editor.putString(key, name);
        editor.commit();//提交数据
    }

    public String getName(String key, String pwd) {
      return   preferences.getString(key, pwd);
    }


    public void setPwd(String key, String login) {
        editor.putString(key, login);
        editor.commit();//提交数据
    }

    public String getPwd(String key, String pwd) {
       return preferences.getString(key, pwd);
    }

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        String[] str = null;
        String values;
        values = preferences.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    public void setSharedPreference(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        String[] value1;
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            editor.putString(key, str);
            editor.commit();
        }
    }

    public String getMac(String key) {
        return preferences.getString(key, "");
    }

    public void setMac(String key, String values) {
        String regularEx = "#";
        String str = getMac("mac");
        if (values != null) {
            str += values;
            str += regularEx;
        }
        editor.putString(key, str);
        editor.commit();
    }

}
