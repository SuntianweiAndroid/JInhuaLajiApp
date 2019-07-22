package com.speedata.jinhualajidemo.net;


import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 功能描述:请求
 */
public class NetApi {
    private NetApiService service;

    private NetApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(NetApiService.class);
    }

    private static class NetApiHolder {
        private static final NetApi INSTANCE = new NetApi(getOkHttpClient());
    }

    /**
     * getInstance .
     *
     * @return NetApi
     */
    public static NetApi getInstance() {
        return NetApiHolder.INSTANCE;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .retryOnConnectionFailure(true);
        return builder.build();
    }


    public Observable<EncryptionBeenRet> encryption(RequestBody body) {
        return service.encryption(body);
    }

    public Observable<DecryptionBeenRet> decryption(RequestBody body) {
        return service.decryption(body);
    }

    public Observable<LoginBeenRet> manageLogin(RequestBody stringMap) {
        return service.login(stringMap);
    }

    public Observable<NetResultBeen> checkUser(RequestBody stringMap) {
        return service.checkUser(stringMap);
    }

    public Observable<EncryptionBeenRet> qrcode(RequestBody stringMap) {
        return service.qrcode(stringMap);
    }

    public Observable<UploadScoreBeenRet> uploadScore(RequestBody stringMap) {
        return service.uploadScore(stringMap);
    }

}
