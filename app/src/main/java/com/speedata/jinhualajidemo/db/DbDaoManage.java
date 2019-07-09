package com.speedata.jinhualajidemo.db;

import android.app.Application;



public class DbDaoManage {
    private static DaoSession daoSession;
    /**
     * 初始化数据库
     *
     * @param application
     */
    public static void initDb(Application application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, "jinhua", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getReadableDb());
        daoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
