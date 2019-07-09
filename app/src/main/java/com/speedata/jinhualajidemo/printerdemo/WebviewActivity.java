package com.speedata.jinhualajidemo.printerdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.android_print_sdk.usb.USBPrinter;
import com.android_print_sdk.wifi.WiFiPrinter;
import com.speedata.jinhualajidemo.printerdemo.util.BitmapConvertor;
import com.speedata.jinhualajidemo.printerdemo.util.PrefUtils;
import com.speedata.jinhualajidemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

public class WebviewActivity extends Activity {
    private WebView webView;
    private ProgressBar progressBar;
    private Button btPrint;
    private Context mContext;
    private ProgressDialog mPd;
    private static final String TAG = "WebviewActivity";
    private Bitmap monoChromeBitmap                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ,b;
    private BluetoothAdapter mBluetoothAdapter = null;

    private boolean hasRegisteredBoundReceiver = false;
    private BluetoothDevice currentDevice;
    private IntentFilter boundFilter;
    private boolean re_pair = false;

    public static boolean isConnected;
    private String mConnectedDeviceName = null;
    private BluetoothDevice bDevice;
    public static BluetoothPrinter bPrinter;
    public static USBPrinter uPrinter;
    public static WiFiPrinter wPrinter;

    public int type;

    private int paperWidth = 58;
    private boolean is_58mm ;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 3;
    private final static int SCANNIN_GREQUEST_CODE = 4;
    private String barcodeContent;
    private String URL;
    private CreateDialog createDialog;

    public static final String KEY_SOUND_A1 = "bg1";
    public static final String KEY_SOUND_A2 = "bg2";
    public static final String KEY_SOUND_A3 = "bg3";
    public static final String KEY_SOUND_A4 = "bg4";
    public static final String KEY_SOUND_A5 = "bg5";
    public static final String KEY_SOUND_A6 = "bg6";

    private SoundPool mSoundPool;
    private HashMap<String, Integer> soundPoolMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
        if(ContextCompat.checkSelfPermission(WebviewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(WebviewActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
//            webView.loadUrl("http://120.27.212.180/Kz20171211/mbl/Mbl_SignOn.asp");//开始定位
            Toast.makeText(WebviewActivity.this, R.string.Permissions_location,Toast.LENGTH_LONG).show();
        }
//        queryUrl();//查询URL
    }
    private void init(){
        mContext = this;
        Intent intent = getIntent();
        type = intent.getIntExtra("type",1);
        boundFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter Bfilter = new IntentFilter();
        Bfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        Bfilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_FOUND);
        Bfilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        Bfilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        Bfilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        registerReceiver(mStateReceiver, Bfilter);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//如果 API level 是大于等于 23（安卓6.0以上）
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this,"自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        }
        btPrint  = findViewById(R.id.btPrint);
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap webBitmap = getWebViewBitmap(mContext,webView);
                new ConvertInBackground().execute(webBitmap);
            }
        });
//        btPrint.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressbar);//进度条
        webView =  findViewById(R.id.webview);
        createDialog = new CreateDialog(WebviewActivity.this,R.style.MyDialogStyle,onClickListener);
        webView.addJavascriptInterface(this,"android");//    添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        WebSettings webSettings=webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptEnabled(true);//允许使用js
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
         webSettings.setDisplayZoomControls(false);

        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
//        webView.loadUrl("http://120.78.138.79/Kz201709/Pda/pda_signon.asp");
        /*
        加载asset文件夹下html
        webView.loadUrl("file:///android_asset/testwebview.html");
         */
        webView.loadUrl("file:///android_asset/PrintSampIndex.html");

        /*
        使用webview显示html代码
         webView.loadDataWithBaseURL(null,"<html><head><title> 欢迎您 </title></head>" +
                "<body><h2>使用webview显示 html代码</h2></body></html>", "text/html" , "utf-8", null);

         */
        mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d(TAG," "+sampleId);
//                加载完成就播放
//                showSound("bg1");
            }
        });
        //添加音频文件
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(KEY_SOUND_A1, mSoundPool.load(this, R.raw.bg1, 1));
        soundPoolMap.put(KEY_SOUND_A2, mSoundPool.load(this, R.raw.bg2, 1));
        soundPoolMap.put(KEY_SOUND_A3, mSoundPool.load(this, R.raw.bg3, 1));
        soundPoolMap.put(KEY_SOUND_A4, mSoundPool.load(this, R.raw.bg4, 1));
        soundPoolMap.put(KEY_SOUND_A5, mSoundPool.load(this, R.raw.bg5, 1));
        soundPoolMap.put(KEY_SOUND_A6, mSoundPool.load(this, R.raw.bg6, 1));

        is_58mm=PrinterSDKDemo_Plus_USB.is_58mm;
        if (is_58mm){
            paperWidth =384;
        }else{
            paperWidth =576;
        }
    }
    private void queryUrl(){
        //查询url是否有值。
        MySQLite mySQLite = new MySQLite(WebviewActivity.this,"weburl.db",null,3);
        Cursor cursor = mySQLite.query("select * from weburl where id =?",new String[]{"1"});
        int id  = 10;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
            Log.d("id",""+ cursor.getInt(0));
            URL = cursor.getString(1);
            Log.d("url",""+ cursor.getString(1));
        }
        cursor.close();
        if(URL==null||URL.equals("")){
            createDialog.show();
        }else {
            webView.loadUrl(URL);
        }
    }
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            btPrint.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            btPrint.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen","拦截url:"+url);
            if(url.equals("http://www.google.com/")){
                Toast.makeText(WebviewActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            if(url.equals("http://222.76.217.249:8080/XiaMenXinShiJin/MblXSJ/Mbl_SignOn.asp")){
                String name = PrefUtils.getString(WebviewActivity.this,"name","");
                String pwd =  PrefUtils.getString(WebviewActivity.this,"pwd","");
                Log.i("进入登录页面", "name:"+name+" pwd:"+pwd);
                transfer(name,pwd);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient=new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            Log.i("ansen","JSAlert:"+url);
            Log.i("ansen","JSAlert:"+message);
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();
            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen","网页标题:"+title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+webView.canGoBack());
        if (
            //点击返回按钮的时候判断有没有上一页
            webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){
            // goBack()表示返回webView的上一页面
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * JS调用android的方法
     * @param
     * @return 1670   698   1299
     */
    @JavascriptInterface //仍然必不可少
    public void  getClient(){
        Log.i("ansen","html调用客户端");
        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bPrinter = PrinterSDKDemo_Plus_USB.bPrinter;
                    if (bPrinter == null) {
                        Log.i(TAG, "bPrinter is null");
                        Toast.makeText(mContext,"打印机断开连接" , 1).show();
                        return;
                    } else {
                        Bitmap webBitmap = getWebViewBitmap(mContext,webView);
                        new ConvertInBackground().execute(webBitmap);
                    }
                }
            });
    }

    @JavascriptInterface
    public void  getBluetooth(){
        Log.i("ansen","html调用客户端");
        //提示打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }else {
            if (isConnected) {
                Toast.makeText(WebviewActivity.this, "打印机已连接", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        }
    }

    @JavascriptInterface //仍然必不可少
    public void  getPrompt(final String filename){
        Log.i("ansen","html调用客户端");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // showSound(filename);
            }
        });
    }

    @JavascriptInterface //仍然必不可少
    public void  refreshPage(String url){
        Log.i("ansen","html调用客户端");
        MySQLite mySQLite = new MySQLite(WebviewActivity.this,"weburl.db",null,3);
        if (url!=null&&!url.equals("")){
            URL = url;
            mySQLite.update("update weburl set url=? where id=?", new Object[]{URL, 1});
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(URL);
            }
        });
    }

    @JavascriptInterface //仍然必不可少
    public void  getLogin(String name,String pwd){
        Log.i("ansen"," 保存账号密码.name:"+name+" pwd:"+pwd);
        PrefUtils.setString(WebviewActivity.this,"name",name);
        PrefUtils.setString(WebviewActivity.this,"pwd",pwd);
    }

    /**
     * Android调用JS的方法
     */
    public void transfer(String name,String pwd){
        Log.i("ansen", "transfer: 调用JS");
        webView.loadUrl("javascript.transfer('admin','1')");
//        webView.post(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadUrl("javascript.transfer('admin','1')");
//            }
//        });
    }

    public void setBarcode(String content) {
        Log.i("ansen", "setBarcode: 调用JS  "+content);
        //android 4.4前
        webView.loadUrl("javascript:setBarcode('" + content + "')");
//        webView.loadUrl("javascript:setBarcode('\" + content + \"')");
        //android 4.4后
//        webView.evaluateJavascript("", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//            }
//        });
        //传动态参数格式
        //传字符串：
//        String a=content;
//        String method ="javascript:setBarcode(\""+a+"\")" ;
//        webView.loadUrl(method);
//        webView.evaluateJavascript(method, new ValueCallback<String>() {
//            @Override public void onReceiveValue(String value) {
//            }
//        });
        //传json
//        try {
//            //当页面加载完成后，调用js方法
//        // mWebview.loadUrl("javascript:方法名(参数)");
//         JSONObject json = new JSONObject();
//         json.put("name", "安卓");
//         json.put("city", "北京");
//            webView.loadUrl("javascript:showMessage("+json.toString()+")");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        webView.destroy();
        webView=null;
    }

    private static Bitmap getViewBitmapWithoutBottom(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight()-100);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
//        Bitmap bp = Bitmap .createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom()-100);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bmp;
    }

    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
//        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight()-100);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取 WebView 视图截图
     * @param context
     * @param view
     * @return
     */
    public  Bitmap getWebViewBitmap(Context context, WebView view) {
        if (null == view) return null;
        view.scrollTo(0, 0);
        view.buildDrawingCache(true);
        view.setDrawingCacheEnabled(true);
        view.setVerticalScrollBarEnabled(false);
        b = getViewBitmapWithoutBottom(view);
        // 可见高度
//        int vh = view.getHeight()-100;
        int vh = view.getHeight();
        Log.i("ansen", "可见高度:"+vh);
        // 容器内容实际高度
//        int th = (int)(view.getContentHeight()*view.getScale())-100;
        int th = (int)(view.getContentHeight()*view.getScale());
        Log.i("ansen", "实际高度:"+th);

        int w = getScreenWidth(context);
        Log.i("ansen", "getWebViewBitmap: "+w);

        Bitmap temp = null;
        if (th > vh) {
//            int absVh = vh - view.getPaddingTop() - view.getPaddingBottom()-100;
            int absVh = vh - view.getPaddingTop() - view.getPaddingBottom();
            do {
                int restHeight = th - vh;
                if (restHeight <= absVh) {
                    view.scrollBy(0, restHeight);
                    vh += restHeight;
                    temp = getViewBitmap(view);
                } else {
                    view.scrollBy(0, absVh);
                    vh += absVh;
                    temp = getViewBitmapWithoutBottom(view);
                }
                b = mergeBitmap(vh, w, temp, 0, view.getScrollY(), b, 0, 0);
            } while (vh < th);
        }
        // 回滚到顶部
        view.scrollTo(0, 0);
        view.setVerticalScrollBarEnabled(true);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 拼接图片
     * @param newImageH
     * @param newImageW
     * @param background
     * @param backX
     * @param backY
     * @param foreground
     * @param foreX
     * @param foreY
     * @return
     */
    private static Bitmap mergeBitmap(int newImageH, int newImageW, Bitmap background, float backX, float backY, Bitmap foreground, float foreX, float foreY) {
        if (null == background || null == foreground) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(newImageW, newImageH, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(bitmap);
        cv.drawBitmap(background, backX, backY, null);
        cv.drawBitmap(foreground, foreX, foreY, null);
        cv.save();
        cv.restore();
        return bitmap;
    }

    /**
     * get the width of screen
     */
    public static int getScreenWidth(Context ctx) {
//        int w = 0;
//        if (Build.VERSION.SDK_INT > 13) {
//            Point p = new Point();
//            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(p);
//            w = p.x;
//        } else {
//            w = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
//        }
//        return w;
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size.x;
    }

    /**
     * 保存图片
     * @param context
     * @param bitmap
     * @param file
     * @param quality
     * @return
     */
    public static boolean save(Context context,Bitmap bitmap, File file, int quality) {
        if (bitmap == null) return false;
        // 获得后缀格式
        String abs = file.getAbsolutePath();
        String suffix = abs.substring(abs.lastIndexOf(".")+1).toLowerCase();
        Bitmap.CompressFormat format;
        if ("jpg".equals(suffix) || ("jpe" +
                "g").equals(suffix)) {
            format = Bitmap.CompressFormat.JPEG;
        } else {
            format = Bitmap.CompressFormat.PNG;
            quality = 100;
        }
        if (file.exists() && ! file.delete()) return false;
        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(format, quality, stream);
            stream.flush();
            stream.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private class ConvertInBackground extends AsyncTask<Bitmap, String, Void> {
        @Override
        protected Void doInBackground(Bitmap... params) {
            Bitmap webBitmap = BitmapConvertor.zoomImg(params[0],paperWidth,params[0].getHeight());
            Bitmap grayBitmap = BitmapConvertor.bitmap2Gray(webBitmap);
            monoChromeBitmap = BitmapConvertor.convertToBMW(grayBitmap,grayBitmap.getWidth(),grayBitmap.getHeight(),95);
            if (monoChromeBitmap == null) {
                Log.i(TAG, "monoChromeBitmap为空!");
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPd.dismiss();
            bPrinter = PrinterSDKDemo_Plus_USB.bPrinter;
            uPrinter = PrinterSDKDemo_Plus_USB.mPrinter;
            wPrinter = PrinterSDKDemo_Plus_USB.wiFiPrinter;
            if (!PrinterSDKDemo_Plus_USB.isConnected ) {
                Toast.makeText(mContext,"打印机未连接" , 1).show();
                return;
            } else {
                switch (type){
                    case 1:
                        if (uPrinter!=null){
                            uPrinter.printImage(monoChromeBitmap);
                        }
                        break;
                    case 2:
                        if (bPrinter!=null){
                            bPrinter.printImage(monoChromeBitmap);
                        }
                        break;
                    case 3:
                        if (wPrinter!=null){
                            wPrinter.printImage(monoChromeBitmap);
                        }
                        break;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            mPd = ProgressDialog.show(WebviewActivity.this,
                    "Converting Image",
                    "Please Wait",
                    true,
                    false,
                    null);
        }
    }

    /**
     * 语音提示
     * @param raw
     */
    protected void showSound(String filename) {
        Log.d("ansen", "语音提示.");
//        调用系统提示音
//        Uri mUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        获取系统默认的notification提示音,Uri:通用资源标志符
//        Ringtone mRingtone = RingtoneManager.getRingtone(mContext, mUri);
//        mRingtone.play();
//        播放音频提示音
        mSoundPool.play(soundPoolMap.get(filename),
                1,
                1,
                0,
                0,
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    mConnectedDeviceName = device.getName();
                    Log.i(TAG, "connected device name is : " + mConnectedDeviceName + ", address is : " + address);
                    Log.i(TAG, "device.getBondState() is : " + device.getBondState());
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                        Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_NONE");
                        PairOrRePairDevice(false, device);
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        re_pair = data.getExtras().getBoolean(BluetoothDeviceListActivity.EXTRA_RE_PAIR);
                        Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_BONDED, re_pair is: " + re_pair);
                        if (re_pair) {
                            PairOrRePairDevice(true, device);
                        } else {
                            initPrinter(device);
                        }
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.not_find_bluetooth, Toast.LENGTH_SHORT).show();
                }
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Log.e(TAG, "执行onActivityResult方法");
                    // 得到扫描后的内容
                    Bundle bundle = data.getExtras();
                    barcodeContent = bundle.getString("result");
                    Log.e(TAG, "--条码内容" + barcodeContent);
                    if (barcodeContent!=null&&barcodeContent!=""){
                        setBarcode(barcodeContent);
                    }
                }
        }
    }

    private boolean PairOrRePairDevice(boolean re_pair, BluetoothDevice device)
    {
        boolean success = false;
        try {
            if (!hasRegisteredBoundReceiver) {
                currentDevice = device;
                registerReceiver(boundDeviceReceiver, boundFilter);
                hasRegisteredBoundReceiver = true;
            }
            if (re_pair) {
                Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
                success = (Boolean) removeBondMethod.invoke(device);
                Log.i(TAG, "removeBond is success? : " + success);
            }
            else
            {
//                 Method setPinMethod = BluetoothDevice.class.getMethod("setPin");
//                 setPinMethod.invoke(device, 1234);
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(device);
                Log.i(TAG, "createBond is success? : " + success);
            }
        } catch (Exception e) {
            Log.i(TAG, "removeBond or createBond failed.");
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    // receive bound broadcast to open connect.
    private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!currentDevice.equals(device)){
                    return;
                }
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.i(TAG, "bounding......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.i(TAG, "bound success");
                        // if bound success, auto init BluetoothPrinter. open connect.
                        if(hasRegisteredBoundReceiver){
                            unregisterReceiver(boundDeviceReceiver);
                            hasRegisteredBoundReceiver = false;
                        }
                        initPrinter(device);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        if (re_pair) {
                            re_pair = false;
                            Log.i(TAG, "removeBond success, wait create bound.");
                            PairOrRePairDevice(false, device);
                        } else if(hasRegisteredBoundReceiver){
                            unregisterReceiver(boundDeviceReceiver);
                            hasRegisteredBoundReceiver = false;
                            Log.i(TAG, "bound cancel");
                        }
                    default:
                        break;
                }
            }
        }
    };

    // use device to init Bluetoothprinter.
    private void initPrinter(BluetoothDevice device){
        bDevice =device;
        bPrinter = new BluetoothPrinter(device);
        bPrinter.setCurrentPrintType(PrinterType.Printer_80);
        //set handler for receive message of connect state from sdk.
        bPrinter.setEncoding("GBK");
        bPrinter.setHandler(bHandler);
//        bPrinter.setAutoReceiveData(true);
        bPrinter.setNeedVerify(true);
        bPrinter.openConnection();
    }

    // The Handler that gets information back from the bluetooth printer.
    private final Handler bHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "msg.what is : " + msg.what);
            switch (msg.what) {
                case BluetoothPrinter.Handler_Connect_Connecting:
                    //mTitle.setText(R.string.title_connecting);
                    Log.d(TAG, "handleMessage: "+100);
                    Toast.makeText(getApplicationContext(), R.string.bt_connecting,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Success:
                    isConnected = true;
                    Log.d(TAG, "handleMessage: "+101);
                    // mTitle.setText(getString(R.string.title_connected) + ": "+ mPrinter.getPrinterName());
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_success,Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Failed:
                    isConnected = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: "+102);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_failed, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothPrinter.Handler_Connect_Closed:
                    isConnected = false;
                    //mTitle.setText(R.string.title_not_connected);
                    Log.d(TAG, "handleMessage: "+103);
                    Toast.makeText(getApplicationContext(), R.string.bt_connect_closed, Toast.LENGTH_SHORT).show();
                case BluetoothPrinter.Handler_Message_Read:
                    if (msg!=null){
                        if (msg.obj!=null){
                            int states = (int) msg.obj;
                            Log.d(TAG, "handleMessage: states:"+states);
                        }else {
                            Log.d(TAG, "handleMessage: 没有返回值");
                        }
                    }else {
                        Log.d(TAG, "handleMessage: msg消息为空");
                    }
            }
        }
    };

    // receive the state change of the Bluetooth.
    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i(TAG, action);
            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                if(bPrinter != null && device != null && isConnected){
                    if(bPrinter.getMacAddress().equals(device.getAddress()))
                    {
                        bPrinter.closeConnection();
                    }
                }
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    URL= createDialog.text_name.getText().toString().trim();
                    if (URL==null||URL.equals("")){
                        Toast.makeText(WebviewActivity.this, "网址不为空,。", Toast.LENGTH_SHORT).show();
                    }else {
                        if (URL.indexOf("http://")==-1){
                            URL="http://"+URL;
                        }
                        Log.d(TAG, "onClick: "+URL);
                        createDialog.cancel();
                        MySQLite mySQLite = new MySQLite(WebviewActivity.this,"weburl.db",null,3);
                        mySQLite.update("insert into weburl(url) values(?)",new Object[]{URL});
                        webView.loadUrl(URL);//加载url
                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
//            刚才的识别码
            case 200:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    用户同意权限,执行我们的操作
//                    webView.loadUrl("http://120.27.212.180/Kz20171211/mbl/Mbl_SignOn.asp");//开始定位
                }else{
//                    用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(WebviewActivity.this,"未开启定位权限,请手动到设置去开启权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }

    private void scanBarcode(){
        Intent intent = new Intent();
        intent.setClass(mContext, MipcaActivityCapture.class);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }
}