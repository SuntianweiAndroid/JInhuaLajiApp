//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.speedata.jinhualajidemo.printerdemo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class  BitmapConvertor {
    private int mDataWidth;
    private byte[] mRawBitmapData;
    private byte[] mDataArray;
    private static final String TAG = "BitmapConvertor";
    private ProgressDialog mPd;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private String mStatus;
    private String mFileName;
    private File file = null;
    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase("mounted")?Environment.getExternalStorageDirectory().getAbsolutePath():"/mnt/sdcard";
    private static final String SAVA_REAL_PATH;

    static {
        SAVA_REAL_PATH = SAVE_PIC_PATH + "/good/savaPic";
    }

    public BitmapConvertor(Context context) {
        this.mContext = context;
    }

    public Bitmap convertBitmap(Bitmap inputBitmap) {
        Bitmap bitmap = null;
        this.mWidth = inputBitmap.getWidth();
        this.mHeight = inputBitmap.getHeight();
        this.mFileName = "my_monochrome_image";
        this.mDataWidth = (this.mWidth + 31) / 32 * 4 * 8;
        this.mDataArray = new byte[this.mDataWidth * this.mHeight];
        this.mRawBitmapData = new byte[this.mDataWidth * this.mHeight / 8];
        this.convertArgbToGrayscale(inputBitmap, this.mWidth, this.mHeight);
        this.createRawMonochromeData();
        this.mStatus = this.saveImage(this.mFileName, this.mWidth, this.mHeight);
        Log.i("BitmapConvertor", "mStatus:" + this.mStatus);
        if(this.mStatus .equals("Success")) {
            bitmap = getmonoChromeImage(this.file.getPath());
        }

        return bitmap;
    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width, int height) {
        int k = 0;
        boolean B = false;
        boolean G = false;
        boolean R = false;

        try {
            for(int e = 0; e < height; ++e) {
                int p;
                for(p = 0; p < width; ++k) {
                    int pixel = bmpOriginal.getPixel(p, e);
                    int var14 = Color.red(pixel);
                    int var13 = Color.green(pixel);
                    int var12 = Color.blue(pixel);
                    var14 = (int)(0.299D * (double)var14 + 0.587D * (double)var13 + 0.114D * (double)var12);
                    if(var14 < 128) {
                        this.mDataArray[k] = 0;
                    } else {
                        this.mDataArray[k] = 1;
                    }

                    ++p;
                }

                if(this.mDataWidth > width) {
                    for(p = width; p < this.mDataWidth; ++k) {
                        this.mDataArray[k] = 1;
                        ++p;
                    }
                }
            }
        } catch (Exception var11) {
            Log.e("BitmapConvertor", var11.toString());
        }

    }

    private void createRawMonochromeData() {
        int length = 0;

        for(int i = 0; i < this.mDataArray.length; i += 8) {
            byte first = this.mDataArray[i];

            for(int j = 0; j < 7; ++j) {
                byte second = (byte)(first << 1 | this.mDataArray[i + j]);
                first = second;
            }

            this.mRawBitmapData[length] = first;
            ++length;
        }

    }

    private String saveImage(String fileName, int width, int height) {
        BMPFile bmpFile = new BMPFile();
        String PATH_LOGCAT = null;
        if(Environment.getExternalStorageState().equals("mounted")) {
            PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Logs";
        } else {
            PATH_LOGCAT = this.mContext.getFilesDir().getAbsolutePath() + File.separator + "Logs";
        }

        File dir = new File(PATH_LOGCAT);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(PATH_LOGCAT, fileName + ".bmp");
        Log.i("fdh", this.file.getPath());

        FileOutputStream fileOutputStream;
        try {
            this.file.createNewFile();
            fileOutputStream = new FileOutputStream(this.file);
        } catch (IOException var9) {
            var9.printStackTrace();
            return "Memory Access Denied";
        } catch (Exception var10) {
            var10.printStackTrace();
            return "Memory Access Denied";
        }

        bmpFile.saveBitmap(fileOutputStream, this.mRawBitmapData, width, height);
        return "Success";
    }

    public static Bitmap getmonoChromeImage(String filePath) {
        FileInputStream is = null;

        try {
            is = new FileInputStream(filePath);
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        return BitmapFactory.decodeStream(is);
    }

    class ConvertInBackground extends AsyncTask<Bitmap, String, Void> {
        ConvertInBackground() {
        }

        protected Void doInBackground(Bitmap... params) {
            BitmapConvertor.this.convertArgbToGrayscale(params[0], BitmapConvertor.this.mWidth, BitmapConvertor.this.mHeight);
            BitmapConvertor.this.createRawMonochromeData();
            BitmapConvertor.this.mStatus = BitmapConvertor.this.saveImage(BitmapConvertor.this.mFileName, BitmapConvertor.this.mWidth, BitmapConvertor.this.mHeight);
            Log.i("BitmapConvertor", "mStatus:" + BitmapConvertor.this.mStatus);
            return null;
        }

        protected void onPostExecute(Void result) {
            BitmapConvertor.this.mPd.dismiss();
            Toast.makeText(BitmapConvertor.this.mContext, "Monochrome bitmap created successfully. Please check in sdcard", 1).show();
            Bitmap bitmap = BitmapConvertor.getmonoChromeImage(BitmapConvertor.this.file.getPath());
            Log.i("BitmapConvertor", "bitmap:" + bitmap);
        }

        protected void onPreExecute() {
            BitmapConvertor.this.mPd = ProgressDialog.show(BitmapConvertor.this.mContext, "Converting Image", "Please Wait", true, false, (OnCancelListener)null);
        }
    }

    /**	 * 对图片进行灰度化处理
     * * @param 原始图片
     * * @return 灰度化图片
     * */
    public static Bitmap bitmap2Gray(Bitmap bmSrc)
    {
        // 得到图片的长和宽
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        // 创建目标灰度图像
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;
    }

    /**
     * 转为二值图像
     *
     * @param bmp
     * 原图bitmap
     * @param w
     * 转换后的宽
     * @param h
     * 转换后的高
     * @return
     */
    public static Bitmap convertToBMW(Bitmap bmp, int w, int h,int tmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为100
        //tmp = 180;
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
                        | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                } else {
                    pixels[width * i + j] = -16777216;
                }
            }
        }
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
//        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, w, h);
        return newBmp;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = scaleWidth;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
