package com.speedata.jinhualajidemo.printerdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jiaming on 2016/3/3.
 */
public class MySQLite extends SQLiteOpenHelper {
    public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCraete","实例化");
        db.execSQL("create table if not exists weburl(id integer primary key autoincrement ,url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("onUpgrade", "数据库版本升级");
    }

    public void update(String sql,Object[] obj){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql, obj);
    }

    public Cursor query(String sql,String[] obj){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,obj);
    }
}
