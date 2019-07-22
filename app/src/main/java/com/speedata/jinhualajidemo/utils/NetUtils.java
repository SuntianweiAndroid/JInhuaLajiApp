package com.speedata.jinhualajidemo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.speedata.jinhualajidemo.been.DecryptionBeen;
import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;
import com.speedata.jinhualajidemo.net.NetApi;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NetUtils {

    public interface NetResultCallback {
        void encryption(EncryptionBeenRet encryptionBeenRet);

        void decryption(DecryptionBeenRet decryptionBeenRet);

        void loginRet(LoginBeenRet loginBeenRet);

        void checkUserinfo(NetResultBeen userInfaoBeenRet);

        void getQrcodeInfo(EncryptionBeenRet encryptionBeenRet);

        void uplodScore(UploadScoreBeenRet uploadScoreBeenRet);

        void netError(String msg);
    }

    private static NetResultCallback netResultCallbacks;

    public static void setNetResultCallback(NetResultCallback netResultCallback) {
        netResultCallbacks = netResultCallback;
    }

    /**
     * 加密
     *
     * @param json 加密json数据
     */
    public static void encryptionData(String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        NetApi.getInstance().encryption(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<EncryptionBeenRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EncryptionBeenRet encryptionBeenRet) {
                        LogUtil.i("加密结果onNext: " + encryptionBeenRet.toString());
                        netResultCallbacks.encryption(encryptionBeenRet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                        netResultCallbacks.netError(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 解密
     *
     * @param decryptionBeen 解密数据
     */
    public static void deecryptionData(DecryptionBeen decryptionBeen) {
        final Gson gson = new GsonBuilder().serializeNulls().create();
        String rusultData = gson.toJson(decryptionBeen);
        LogUtil.i("解密发送::" + rusultData);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rusultData);

        NetApi.getInstance().decryption(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<DecryptionBeenRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DecryptionBeenRet decryptionBeenRet) {
                        LogUtil.i("解密結果onNext: " + decryptionBeenRet.toString());
                        netResultCallbacks.decryption(decryptionBeenRet);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                        netResultCallbacks.netError(e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 督导员登录
     *
     * @param pwd 督导员面密码
     */
    public static void manageLogin(String pwd) {
        LogUtil.i("登录发送::" + pwd);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), pwd);
        NetApi.getInstance().manageLogin(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBeenRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginBeenRet loginBeenRet) {
                        LogUtil.i("登录结果onNext: " + loginBeenRet.toString());
                        netResultCallbacks.loginRet(loginBeenRet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                        netResultCallbacks.netError(e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 用户校验
     *
     * @param type
     */
    public static void checkUser(String type) {
        LogUtil.i("校验用户发送::" + type);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), type);
        NetApi.getInstance().checkUser(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetResultBeen>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NetResultBeen userInfaoBeenRet) {
                        LogUtil.i("校验用户onNext: " + userInfaoBeenRet.toString());
                        netResultCallbacks.checkUserinfo(userInfaoBeenRet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                        netResultCallbacks.netError(e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取加密后的二维码数据
     *
     * @param qrcodeInfo 原始二维码数据
     */
    public static void getQrcodeInfo(String qrcodeInfo) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), qrcodeInfo);
        NetApi.getInstance().qrcode(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<EncryptionBeenRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EncryptionBeenRet requestBody) {
                        LogUtil.i("加密二维码结果onNext: " + requestBody.toString());
                        netResultCallbacks.getQrcodeInfo(requestBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传积分
     *
     * @param msg 积分json数据
     */
    public static void uploadScore(String msg) {
        LogUtil.i("上传积分发送uploadScore: " + msg);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), msg);
        NetApi.getInstance().uploadScore(requestBody).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadScoreBeenRet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UploadScoreBeenRet uploadScoreBeenRet) {
                        LogUtil.i("上传积分结果onNext: " + uploadScoreBeenRet.toString());
                        netResultCallbacks.uplodScore(uploadScoreBeenRet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}