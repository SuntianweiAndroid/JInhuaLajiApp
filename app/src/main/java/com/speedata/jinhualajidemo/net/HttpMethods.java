package com.speedata.jinhualajidemo.net;

/**
 * Created by 张明_ on 2019/2/18.
 * Email 741183142@qq.com
 */
public class HttpMethods {
    public static String BASE_URL = "http://42.81.133.17:18056/";
    public static String PRODUCE_URL = "http://42.81.133.17:18058/";
    public static String TEST_URL = "http://123.150.11.50:4023/logreceive/";
    private static final Object LOCK = new Object();
    private static HttpMethods httpMethods;

    public static HttpMethods getInstance() {
        if (httpMethods == null) {
            synchronized (LOCK) {
                if (httpMethods == null) {
                    httpMethods = new HttpMethods();
                }
            }
        }
        return httpMethods;
    }


//    /**
//     * 支付宝 appSercet
//     *
//     * @param sendData
//     * @param observer
//     */
//    public void appSercet(Map<String, String> params, Observer<AppSercetBackBean> observer) {
//        RetrofitCreateHelper.createApi(ApiService.class, BASE_URL)
//                .appSercet(params)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }



}
