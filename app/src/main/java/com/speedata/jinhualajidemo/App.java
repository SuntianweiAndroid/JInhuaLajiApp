package com.speedata.jinhualajidemo;

import com.speedata.jinhualajidemo.been.UserInfao;

public class App {
    public static final String SUCCESS = "1"; //成功
    public static final String ERROTCODE_PARAM_MISS = "100";// 参数不全
    public static final String ERROTCODE_ENCRYPTION = "101";//加密异常
    public static final String ERROTCODE_DECRYPTION = "102";//解密异常
    public static final String ERROTCODE_NOTHING = "200";//未查询到相关内容

    public static UserInfao userInfo;
    /**
     * 回收品类
     * 1 瓶子，2衣服，3.可回收物，4.有害垃圾，5.纸，6.电子废弃物，7.厨余垃圾，8.其它垃圾，9.干垃圾
     * 10.湿垃圾，11.废旧金属，12.废塑料，13.废旧玻璃
     */
    public static int garbageType;

    /*
     *卡类型   1.手机号 ，2.二维码，3.卡号
     */
    public static String cardType;

    // 信用积分 Int 是 可以为负值
    public static int score;

}
