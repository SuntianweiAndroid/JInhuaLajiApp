package com.speedata.jinhualajidemo.printerdemo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.android_print_sdk.CanvasPrint;
import com.android_print_sdk.FontProperty;
import com.android_print_sdk.PrinterType;
import com.android_print_sdk.bluetooth.BluetoothPrinter;

import java.util.Hashtable;

public class CanvasUtils {
    private static String TAG="com.printer.demo.utils";
    /**
     * 画布模版  图片跟文字并行
     * @param resources 资源对象
     * @param mPrinter 蓝牙打印机对象
     * @param is58mm  是否是58纸
     */
    public void printCustomImage2(Resources resources, BluetoothPrinter mPrinter, boolean is58mm){
        //调用下面的方法生成二维码
        Bitmap bitmapCODE39 = createBitmapQR_CODE("123456789", 270, 270);
        //创建画布
        CanvasPrint cp=new CanvasPrint();
        //初始化画布
        cp.init(PrinterType.Printer_80);
        //将二维码画到画布上（0,0）处坐标
        cp.drawImage(0, 0, bitmapCODE39);
        //创建字体
        FontProperty fp=new FontProperty();
        //字体属性赋值 此处参数个数根据SDK版本不同，有略微差别，酌情增减。
        fp.setFont(true, false, false, false, 40, null);
        //设置字体
        cp.setFontProperty(fp);
        //将文字画到画布上指定坐标处
        cp.drawText(250,80,"扫一扫 升级");
        cp.drawText(250, 120, "您的智能车生活");
        cp.drawText(250,180,"彩码头客服电话");
        cp.drawText(250, 220, "4008 317 317");
        //将画布保存成图片并进行打印
        mPrinter.printImage(cp.getCanvasImage());

    }
    /**
     * 生成二维码图片
     * @param str 内容
     * @param param1 宽度
     * @param param2 高度
     * @return 位图
     */
    public static Bitmap createBitmap(String str, int param1, int param2,int type) {
        try {
//		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.PDF417, param1, param2);
//		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, param1, param2);
//		new MultiFormatWriter().en
            Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
            if(type == 0){
                hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            }else if(type == 1){
                hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            }else if(type == 2){
                hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            }else{
                hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            }
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, param1, param1, hint);
            int width = param1;
//		int width = matrix.width;
            int height = param2;
//		int height = matrix.height;
            int[] pixels = new int[width * height];
            for (int y = 0; y < width; ++y) {
                for (int x = 0; x < height; ++x) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000; // black pixel
                    } else {
                        pixels[y * width + x] = 0xffffffff; // white pixel
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            Log.i(TAG, "createBitmap-width:" + bmp.getWidth());
            Log.i(TAG, "createBitmap-height:" + bmp.getHeight());
            return  bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成QR_CODE类型二维码图片
     * @param str 内容
     * @param param1 宽度
     * @param param2 高度
     * @return 位图
     */
    public static Bitmap createBitmapQR_CODE(String str, int param1, int param2) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, param1, param2);
            int width = matrix.width;
            int height = matrix.height;
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000; // black pixel
                    } else {
                        pixels[y * width + x] = 0xffffffff; // white pixel
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            Log.i(TAG, "createBitmapCODE39-width:" + bmp.getWidth());
            Log.i(TAG, "createBitmapCODE39-height:" + bmp.getHeight());
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
