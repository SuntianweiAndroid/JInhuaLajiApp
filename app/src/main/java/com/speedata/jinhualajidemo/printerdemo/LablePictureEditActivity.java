package com.speedata.jinhualajidemo.printerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.speedata.jinhualajidemo.printerdemo.util.BitmapConvertor;
import com.speedata.jinhualajidemo.printerdemo.util.PictureUtils;
import com.speedata.jinhualajidemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LablePictureEditActivity extends Activity implements View.OnClickListener{
    private ImageView ivImage;
    private Button btDefault,btLocal,bt_POK,btCamera;
    private static final String TAG = "LableBarCodEditActivity";
    private String remp_dir = null;
    private final static int IMAGE_CAPTURE_FROM_CAMERA = 1;
    private final static int IMAGE_CAPTURE_FROM_GALLERY = 2;
    private BitmapConvertor convertor;
    private ProgressDialog mPd;
    private WindowManager wm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_picture_edit);
        init();
        setListener();
    }
    public  void init(){
        ivImage = findViewById(R.id.ivPicture);
        btDefault =  findViewById(R.id.btDefault);
        btLocal = findViewById(R.id.btLocal);
        btCamera = findViewById(R.id.btCamera);
        bt_POK = findViewById(R.id.bt_PicOk);
        convertor = new BitmapConvertor(this);
        wm = this.getWindowManager();
    }
    public  void setListener(){
        btLocal.setOnClickListener(this);
        bt_POK .setOnClickListener(this);
        btCamera.setOnClickListener(this);
        btDefault.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_PicOk:
                if (monoChromeBitmap!=null){
                    Intent intent = new Intent(LablePictureEditActivity.this,LableEditActivity.class);
                    intent.putExtra("image_pic",monoChromeBitmap);
                    setResult(RESULT_CANCELED,intent);
                    finish();
                }
                break;
            case R.id.btDefault:
                monoChromeBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.logob);
                ivImage.setImageBitmap(monoChromeBitmap);
                break;
            case R.id.btLocal:
                // 拍照我们用Action为Intent.ACTION_GET_CONTENT,
                // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent2, IMAGE_CAPTURE_FROM_GALLERY);
                break;
            case R.id.btCamera:
                remp_dir = getFilePath();
                // 拍照我们用Action为MediaStroe.ACTION_IMAGE_CAPTURE,
                // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent3.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent3.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(remp_dir)));
                startActivityForResult(intent3, IMAGE_CAPTURE_FROM_CAMERA);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Log.i(TAG, "windows height:" + height + "----" + "windows width:"
                + width);

        switch (requestCode) {
            case IMAGE_CAPTURE_FROM_CAMERA:
                if (remp_dir == null)
                    Log.e(TAG, "remp_dir为空！");
                else
                    Log.i(TAG, "remp_dir" + remp_dir);
                File f = new File(remp_dir);
                Uri capturedImage = null;
                Bitmap photoBitmap = null;
                try {
                    capturedImage = Uri
                            .parse(MediaStore.Images.Media
                                    .insertImage(getContentResolver(),
                                            f.getAbsolutePath(), null, null));
                    Log.i(TAG, capturedImage.toString());
                    photoBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), capturedImage);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("camera", "Selected image: " + capturedImage.toString());
                f.delete();
                Log.i(TAG, "height1:" + photoBitmap.getHeight() + "----"
                        + "width1:" + photoBitmap.getWidth());
                new ConvertInBackground().execute(photoBitmap);
                break;
            case IMAGE_CAPTURE_FROM_GALLERY:
                Uri mImageCaptureUri = data.getData();
                // 这个方法是根据Uri获取Bitmap图片的静态方法
                try {
                    // 选择相册
                    Bitmap photoBitmap2 = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), mImageCaptureUri);
                    new ConvertInBackground().execute(photoBitmap2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
    }
    private String getFilePath() {

        String PATH_LOGCAT = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "MyPicture";
            Log.d(TAG, "sdka ");
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = LablePictureEditActivity.this.getFilesDir()
                    .getAbsolutePath() + File.separator + "MyPicture";
            Log.i(TAG, "neicun");
        }
        File dir = new File(PATH_LOGCAT);
        if (!dir.exists()) {
            dir.mkdir(); // 创建文件夹
        }
        remp_dir = PATH_LOGCAT + File.separator + "tmpPhoto.jpg";
        Log.i(TAG, "remp_dir:" + remp_dir);
        return remp_dir;
    }
    Bitmap monoChromeBitmap = null;
    class ConvertInBackground extends AsyncTask<Bitmap, String, Void> {

        @Override
        protected Void doInBackground(Bitmap... params) {
            Bitmap compress = PictureUtils.compress(params[0]);
            Log.i(TAG, "heightC:" + compress.getHeight() + "----"
                    + "widthC:" + compress.getWidth());
            monoChromeBitmap = convertor.convertBitmap(compress);
            if (monoChromeBitmap == null) {
                Log.i(TAG, "monoChromeBitmap为空!");
                return null;
            }
            Log.i(TAG, "heightD:" + monoChromeBitmap.getHeight() + "----"
                    + "widthD:" + monoChromeBitmap.getWidth());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            ivImage.setImageBitmap(monoChromeBitmap);
            mPd.dismiss();
//            if (PrinterInstance.mPrinter == null) {
////				Log.i(TAG, "PrinterInstance.mPrinter为空");
//                Toast.makeText(mContext, getString(R.string.not_support), 1).show();
//            } else {
//                mPrinter.printImage(monoChromeBitmap,PAlign.NONE, 0,false);
//            }
        }

        @Override
        protected void onPreExecute() {
            mPd = ProgressDialog.show(LablePictureEditActivity.this,
                    "Converting Image", "Please Wait", true, false, null);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return  false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

