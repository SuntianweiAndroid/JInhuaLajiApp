package com.speedata.jinhualajidemo.net;

/**
 * @author :Reginer in  2017/9/7 23:17.
 * 联系方式:QQ:282921012
 * 功能描述:urls
 */
class Urls {
    //            1.    数据加密接口    http://123.56.109.205:33333/api/v1/data/encryption
//            2.    数据解密接口    http://123.56.109.205:33333/api/v1/data/decryption
//            3.    校验用户信息    http://123.56.109.205:33333/api/v1/ht/user
//            4.    信用打分上传    http://123.56.109.205:33333/api/v1/ht/credit
//            5.    手持终端登录验证    http://123.56.109.205:33333/api/v1/ht/worker
//            6.    打印二维码加密接口    http://123.56.109.205:33333/api/v1/ht/encode
//            7.      加密秘钥为：icm_jh

    static final String BASE_URL = "http://123.56.109.205:33333/api/v1/";
    /**
     * 数据加密接口
     */
    static final String ENCRYPTION = "data/encryption";
    /**
     * 数据解密接口
     */
    static final String DECRYPTION = "data/decryption";
    /**
     * 校验用户信息
     */
    static final String CHECK_USERINFAO = "ht/user";
    /**
     * 信用打分上传
     */
    static final String UPLOAD_CREDIT_SCORE = "ht/credit";
    /**
     * 手持终端登录验证
     */
    static final String CHECK_LOGIN = "ht/worker";
    /**
     * 打印二维码加密接口
     */
    static final String PRINT_QRCODE = "ht/encode";
}
