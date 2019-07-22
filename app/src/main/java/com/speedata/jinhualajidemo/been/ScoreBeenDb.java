package com.speedata.jinhualajidemo.been;

public class ScoreBeenDb {

    private String htId;// 手持终端 Id String 是

    private String managePhone;// 手持终端登录者手机号

    private String batchId; //流水号  生成规则，终端ID_当前时间戳毫秒值
    private long time;//请求时间当前时间毫秒值
    private String userPhone;//用户主手机号 String 是
    private String address;//  住址 String 是
    private String point;// 可用积分 Int 是 非负整
    /*
     *卡类型   1.手机号 ，2.二维码，3.卡号
     */
    private String cardType;
    //cardId 卡号
    private String cardId;
    // 信用积分 Int 是 可以为负值
    private int credit;
    // 评分图片 String是 base64编码  需要压缩在1M以内
    private String img;
    /**
     * 回收品类
     * 1 瓶子，2衣服，3.可回收物，4.有害垃圾，5.纸，6.电子废弃物，7.厨余垃圾，8.其它垃圾，9.干垃圾
     * 10.湿垃圾，11.废旧金属，12.废塑料，13.废旧玻璃
     */
    private int type;
    // 信用打分标识码String 二维码格式物品类型 :ICM_JH_ 流水号是 手持终端打印的二维码，此二维码在打印的时候需要调用二维码加密接
    private String htCode;


}
