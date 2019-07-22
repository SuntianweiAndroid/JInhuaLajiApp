package com.speedata.jinhualajidemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.speedata.jinhualajidemo.App;
import com.speedata.jinhualajidemo.MyApplication;
import com.speedata.jinhualajidemo.R;
import com.speedata.jinhualajidemo.been.DecryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionBeenRet;
import com.speedata.jinhualajidemo.been.EncryptionScoreBeenRet;
import com.speedata.jinhualajidemo.been.LoginBeenRet;
import com.speedata.jinhualajidemo.been.NetResultBeen;
import com.speedata.jinhualajidemo.been.UploadScoreBeenRet;
import com.speedata.jinhualajidemo.utils.LogUtil;
import com.speedata.jinhualajidemo.utils.NetUtils;
import com.speedata.jinhualajidemo.view.HintDialog;
import com.speedata.jinhualajidemo.view.SearchBtPrintDialog;
import com.speedata.jinhualajidemo.view.TitleBarView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class PutRecordActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * +3
     */
    private TextView mTxClassify;
    /**
     * -3
     */
    private TextView mTxScore;
    private ImageView mBmpRes;
    /**
     * 上传数据并打印二维码
     */
    private Button mBtnUp;
    /**
     * 点击拍照
     */
    private TextView mTvShow;
    private String bg64;
    private String qrinfo;
    private String qrCode;
    /**
     * 刘小霞
     */
    private TextView mTvUserName;
    /**
     * ID:4657892
     */
    private TextView mTvUserId;
    /**
     * 256
     */
    private TextView mTvUserScore;

    private boolean isUplod = false;
    private HintDialog hintDialog;
    private String printMsg = "未知垃圾";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_record);
        hintDialog = new HintDialog(this, R.style.hintdialog);
        hintDialog.setHintMsg("数据打印并上传中...");
        initView();
        setMyAppTitle();

        switch (App.garbageType) {
            case 7:
                setLClassify(R.mipmap.icon_garbage1_s, "厨余垃圾");
                printMsg = "厨余垃圾";
                break;
            case 3:
                printMsg = "可回收垃圾";
                setLClassify(R.mipmap.icon_garbage2_s, "可回收垃圾");
                break;
            case 4:
                printMsg = "有害垃圾";
                setLClassify(R.mipmap.icon_garbage3_s, "有害垃圾");
                break;
            case 8:
                printMsg = "其它垃圾";
                setLClassify(R.mipmap.icon_garbage4_s, "其它垃圾");
                break;
            default:
                printMsg = "未知垃圾";
                setLClassify(R.mipmap.icon_garbage4_s, "未知垃圾");
                break;
        }
        switch (App.score) {
            case 5:
                setScore(R.mipmap.icon_5_s, "+5");
                break;
            case -5:
                setScore(R.mipmap.icon_fu5_s, "-5");
                break;
            case 3:
                setScore(R.mipmap.icon_3_s, "+3");
                break;
            case -3:
                setScore(R.mipmap.icon_fu3_s, "-3");
                break;
            case 0:
                setScore(R.mipmap.icon_0_s, "0");
                break;
            default:
                setScore(R.mipmap.icon_0_s, "沒有打分");
                break;
        }
        // TODO: 2019/7/17  最先拿到要生成 的 二维SkJpegCodec码数据
        JsonObject jsonObject = new JsonObject();
        qrCode = App.garbageType + ":ICM_JH_" + MyApplication.pdaIMEI + "_" + System.currentTimeMillis();
        jsonObject.addProperty("htCode", qrCode);

        LogUtil.i("qrcode==" + qrCode);
        NetUtils.getQrcodeInfo(jsonObject.toString());
//        if (App.bPrinter == null || !App.bPrinter.isConnected()) {
//            MyApplication.showToast(this, "请先连接打印机");
//            SearchBtPrintDialog searchBTDialog = new SearchBtPrintDialog(this, R.style.MyDialogStyle, "ML31_BT");
//            searchBTDialog.setCanceledOnTouchOutside(false);
//            searchBTDialog.show();
//            return;
//        }
        NetUtils.setNetResultCallback(new NetUtils.NetResultCallback() {
            @Override
            public void encryption(EncryptionBeenRet encryptionBeenRet) {
                switch (encryptionBeenRet.getCode()) {
                    case App.SUCCESS:
                        NetUtils.uploadScore(encryptionBeenRet.getContent());
                        break;
                    default:
                        isUplod = false;
                        hintDialog.dismiss();
                        MyApplication.showToast(PutRecordActivity.this, encryptionBeenRet.getMesg());
                        break;
                }

            }

            @Override
            public void decryption(DecryptionBeenRet decryptionBeenRet) {

            }

            @Override
            public void loginRet(LoginBeenRet loginBeenRet) {


            }

            @Override
            public void checkUserinfo(NetResultBeen userInfaoBeenRet) {

            }

            @Override
            public void getQrcodeInfo(EncryptionBeenRet encryptionBeenRet) {
                switch (encryptionBeenRet.getCode()) {
                    case App.SUCCESS:
                        qrinfo = encryptionBeenRet.getContent();
                        break;
                    default:
                        isUplod = false;
                        hintDialog.dismiss();
                        MyApplication.showToast(PutRecordActivity.this, encryptionBeenRet.getMesg());
                        break;
                }


            }


            @Override
            public void uplodScore(UploadScoreBeenRet uploadScoreBeenRet) {

                switch (uploadScoreBeenRet.getCode()) {
                    case App.SUCCESS:
                        isUplod = true;
                        Intent intent = new Intent(PutRecordActivity.this, ResultActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        isUplod = false;
                        hintDialog.dismiss();
                        MyApplication.showToast(PutRecordActivity.this, uploadScoreBeenRet.getMesg());
                        break;
                }

            }

            @Override
            public void netError(String msg) {
                isUplod = false;
                hintDialog.dismiss();
                MyApplication.showToast(PutRecordActivity.this, msg);
            }
        });
    }


    private void setLClassify(int id, String msg) {
        Drawable drawable = getResources().getDrawable(id);// 找到资源图片
// 这一步必须要做，否则不会显示。
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
        mTxClassify.setCompoundDrawables(null, null, drawable, null);// 设置到控件中
        mTxClassify.setText(msg);
    }

    private void setScore(int id, String msg) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
        mTxScore.setCompoundDrawables(null, null, drawable, null);// 设置到控件中
        mTxScore.setText(msg);
    }

    private void setMyAppTitle() {
        TitleBarView titleBarView = (TitleBarView) this.findViewById(R.id.title_layout);
        titleBarView.initViewsVisible(true, true, false, true);
        titleBarView.setAppTitle("投放垃圾处理单");
        titleBarView.setOnLeftButtonClickListener(new TitleBarView.OnLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mTxClassify = (TextView) findViewById(R.id.tx_classify);
        mTxScore = (TextView) findViewById(R.id.tx_score);
        mBmpRes = (ImageView) findViewById(R.id.bmp_res);
        mBtnUp = (Button) findViewById(R.id.btn_up);
        mBtnUp.setOnClickListener(this);
        mBmpRes.setOnClickListener(this);
        mBtnUp.setVisibility(View.GONE);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        mTvShow.setOnClickListener(this);

        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvUserId = (TextView) findViewById(R.id.tv_user_id);
        mTvUserScore = (TextView) findViewById(R.id.tv_user_score);
        mTvUserId.setText("ID:" + App.userInfo.getCardId());
        mTvUserName.setText(App.userInfo.getPhone());
        mTvUserScore.setText(App.userInfo.getCredit());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            try {
                /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
//                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                Bitmap bitmap = getBitmapFormUri(mImageUri);

                bg64 = bitmapToBase64(bitmap);
                Log.i("base64", "onActivityResult: " + bg64);
                if (bitmap != null) {
                    mTvShow.setVisibility(View.GONE);
                    mBmpRes.setVisibility(View.VISIBLE);
                    BitmapDrawable bd = new BitmapDrawable(bitmap);
//                    bd.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.MIRROR);
//                    bd.setDither(true);
                    mBmpRes.setBackground(bd);
//                    mBmpRes.setBackground(bitmap);
                    mBtnUp.setVisibility(View.VISIBLE);
                } else {
                    // TODO: 2019/7/12   提示重新拍照
                    MyApplication.showToast(this, "请重新拍照");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * bitmap转base64
     * */
    private static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_up:

                if (qrinfo != null) {
                    hintDialog.show();
                    print(App.bPrinter, qrinfo);
                } else {
                    MyApplication.showToast(PutRecordActivity.this, "二维码未生成");
                    return;
                }
                break;
            case R.id.tv_show:
                takePhoto();
                break;
        }
    }

    private void sendEencyption() {
        EncryptionScoreBeenRet.ContentBean contentBean = new EncryptionScoreBeenRet.ContentBean(App.userInfo.getCardId(), App.score, bg64, App.garbageType, qrCode);

        EncryptionScoreBeenRet encryptionBeen = new EncryptionScoreBeenRet(MyApplication.pdaIMEI + "_" + System.currentTimeMillis(), contentBean, MyApplication.pdaIMEI, MyApplication.getPreferences().getManageloginName("name", ""), MyApplication.key, System.currentTimeMillis());
        final Gson gson = new GsonBuilder().serializeNulls().create();
        String resultData = gson.toJson(encryptionBeen);
        LogUtil.i("加密发送：：" + resultData);
        NetUtils.encryptionData(resultData);
    }

    private void print(final BluetoothPrinter bPrinter, final String msg) {
        if (bPrinter == null || !bPrinter.isConnected()) {
            MyApplication.showToast(this, "请先连接打印机");
            final SearchBtPrintDialog searchBTDialog = new SearchBtPrintDialog(this, R.style.MyDialogStyle, "ML31_BT");
            searchBTDialog.setCanceledOnTouchOutside(false);
            searchBTDialog.show();
            searchBTDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (App.bPrinter == null) {
                        searchBTDialog.show();
                    } else {
                        App.bPrinter.setPage(576, 400);
                        App.bPrinter.printText("CENTER\r\n");
                        App.bPrinter.drawText(0, 0, "您投入的是" + printMsg, 2, 0, 0, false, false);
                        App.bPrinter.drawText(0, 32, "请扫描二维码投放垃圾", 2, 0, 0, false, false);
                        App.bPrinter.drawQrCode(0, 80, 2, 7, msg, 0);

                        App.bPrinter.labelPrint(0, 1);
                        sendEencyption();
                    }
                }
            });
        } else {
            App.bPrinter.setPage(576, 400);
            App.bPrinter.printText("CENTER\r\n");
            App.bPrinter.drawText(0, 0, "您投入的是" + printMsg, 2, 0, 0, false, false);
            App.bPrinter.drawText(0, 32, "请扫描二维码投放垃圾", 2, 0, 0, false, false);
            App.bPrinter.drawQrCode(0, 80, 2, 7, msg, 0);

            App.bPrinter.labelPrint(0, 1);
            sendEencyption();
        }
    }

    private Uri mImageUri;

    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            File imageFile = createImageFile();//创建用来保存照片的文件
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, "com.speedata.jinhualajidemo.provider", imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, 0);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public Bitmap getBitmapFormUri(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }

        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = 1;
        //如果宽度大的话根据宽度固定大小缩放
        if (originalWidth > originalHeight && originalWidth > ww) {
            be = (int) (originalWidth / ww);
            //如果高度高的话根据宽度固定大小缩放
        } else if (originalWidth < originalHeight && originalHeight > hh) {
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        //设置缩放比例
        bitmapOptions.inSampleSize = be;
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
//再进行质量压缩
        return compressImage(bitmap);
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            //这里压缩options，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
            if (options <= 0) {
                break;
            }
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap;
    }
}
