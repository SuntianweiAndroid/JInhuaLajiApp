package com.speedata.jinhualajidemo.net;

import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 功能描述:网络请求接口
 */
public interface NetApiService {
    /**
     * 数据加密接口
     *
     * @param body -
     * @return -
     */
    @POST(Urls.ENCRYPTION)
    Observable<EncryptionBeenRet> encryption(@Body RequestBody body);


    /**
     * 数据解密接口
     *
     * @param body -
     * @return -
     */
    @POST(Urls.DECRYPTION)
    Observable<DecryptionBeenRet> decryption(@Body RequestBody body);


    /**
     * 手持终端登录验证
     *
     * @return
     */
    @POST(Urls.CHECK_LOGIN)
    Observable<LoginBeenRet> login(@Body RequestBody body);

    /**
     * 校验用户信息
     *
     * @param body
     * @return
     */
    @POST(Urls.CHECK_USERINFAO)
    Observable<NetResultBeen> checkUser(@Body RequestBody body);

    /**
     * 打印二维码加密接口
     *
     * @param body
     * @return
     */
    @POST(Urls.PRINT_QRCODE)
    Observable<EncryptionBeenRet> qrcode(@Body RequestBody body);

    /**
     * 信用打分上传
     *
     * @param body
     * @return
     */
    @POST(Urls.UPLOAD_CREDIT_SCORE)
    Observable<UploadScoreBeenRet> uploadScore(@Body RequestBody body);


}

