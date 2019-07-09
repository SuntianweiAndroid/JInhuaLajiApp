package com.speedata.jinhualajidemo.printerdemo.util;
/**
 * Created by xzc-pc on 2016/8/8.
 */
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.android_print_sdk.Barcode;
import com.android_print_sdk.CanvasPrint;
import com.android_print_sdk.FontProperty;
import com.android_print_sdk.PrinterType;
import com.android_print_sdk.Table;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.R;

public class BluetoothPrintUtils {

    public static void printNote(Resources resources, BluetoothPrinter mPrinter) {
        mPrinter.init();
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
        StringBuffer sb = new StringBuffer();
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
        //字符橫向纵向放大一倍
        mPrinter.setCharacterMultiple(1, 1);
        mPrinter.printText(resources.getString(R.string.welcome)+"\n");
        mPrinter.setCharacterMultiple(0, 0);
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_58 ) {
            mPrinter.printText("------------------------------\n");
        }else if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            mPrinter.printText("----------------------------------------------\n");
        }
        mPrinter.printText(resources.getString(R.string.shopping_name)+"\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_LEFT);
        sb.append(resources.getString(R.string.text_one)+"\n");
        sb.append(resources.getString(R.string.text_two)+"\n");
        sb.append(resources.getString(R.string.text_three)+"\n");
        sb.append(resources.getString(R.string.text_four)+"\n");
        sb.append(resources.getString(R.string.text_five)+"\n");
        sb.append(resources.getString(R.string.text_six)+"\n");
        sb.append(resources.getString(R.string.text_seven)+"\n");
        sb.append(resources.getString(R.string.text_eight)+"\n");
        sb.append(resources.getString(R.string.text_nine)+"\n");
        sb.append(resources.getString(R.string.text_ten)+"\n");
        sb.append(resources.getString(R.string.text_eleven)+"\n");
        sb.append(resources.getString(R.string.text_twelve)+"\n");
        sb.append(resources.getString(R.string.text_thirteen)+"\n");
        sb.append(resources.getString(R.string.text_fourteen)+"\n");
        sb.append(resources.getString(R.string.text_fiveteen)+"\n");
        mPrinter.printText(sb.toString());
        mPrinter.setCharacterMultiple(0, 0);
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_58) {
            mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
            mPrinter.printText("------------------------------\n");
        }else if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            mPrinter.printText("----------------------------------------------\n");
        }
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
        mPrinter.cutPaper();
    }

    /**
     * printPng_JGP
     * @param resources
     * @param mPrinter
     * @param temp  default 85
     */
    public static void printPng_JGP(Resources resources, BluetoothPrinter mPrinter, int temp){
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.xf);
        Bitmap b = BitmapConvertor.bitmap2Gray(bitmap);
        Bitmap c = BitmapConvertor.convertToBMW(b,b.getWidth(),b.getHeight(),temp);
        mPrinter.printImage(c);
    }

    public static void printImage(Resources resources, BluetoothPrinter mPrinter, boolean is_thermal) {
        mPrinter.setLeftMargin(0,0);
        BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = true;
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.apple,bfoOptions);
        Matrix matrix = new Matrix();
        //matrix.setScale(X轴缩放,Y轴缩放，，);//后面两个参数是相对于缩放的位置放置，尝试设置，建议数值>100以上进行设置
        matrix.setScale(3f,3f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Bitmap a = BitmapFactory.decodeResource(resources,R.mipmap.amsa);
        a = BitmapConvertor.zoomImg(a,384,0);
        Bitmap b = BitmapConvertor.bitmap2Gray(a);
        Bitmap c = BitmapConvertor.convertToBMW(b,0,0,100);
        if (is_thermal) {
             //单色图片
            Bitmap bitmap1 = BitmapFactory.decodeResource(resources,R.mipmap.black);
//             mPrinter.printImage(bitmap1);
            byte[] bytes= StringUtil.HexString2Bytes("1630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF15011630FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                mPrinter.printByteData(bytes);
             // 打印水印;
            //Bitmap a = BitmapFactory.decodeResource(resources,R.drawable.logob);
//            mPrinter.printImage2(c);
        } else {
             mPrinter.printImageDot(bitmap, 1, 0);
        }
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
        mPrinter.cutPaper();
    }

    public static void printCustomImage(Resources resources, BluetoothPrinter mPrinter, boolean is_thermal) {
        mPrinter.init();
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_LEFT);
        mPrinter.printText(resources.getString(R.string.printCanvas));
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        CanvasPrint cp=new CanvasPrint();
        /**
         * 初始化画布，画布的宽度为变量，一般有两个选择：1.58mm型号打印机实际可用是48mm，48*8=384px
         * 2. 80mm型号打印机实际可用是72mm，72*8=576px，因为画布的高度是无限制的，但从内存分配方面考虑要小于4M比较合适。
         * 所有预置为宽度的5倍。初始化 画笔，默认属性有：1.消除锯齿   2.设置画笔颜色为黑色。
         *
         * init 方法包含cp.initCanvas(576)和cp.initPaint（），M21打印宽度为48mm，其他为72mm
         */
        cp.init(PrinterType.Printer_80);
        //非中文使用空格分隔单词
        cp.setUseSplit(true);
        //阿拉伯文靠右显示
        cp.setTextAlignRight(true);
	/*
	 * 插入图片函数：drawImage(float x,float y, String path)
	 * 其中（x,y）是指插入图片的左上顶点坐标。
	 */
        FontProperty fp=new FontProperty();
        fp.setFont(false, false, false, false, 25, null);
        //通过初始化的字体属性设置画笔1B09  1B 3D 10 1B15
        cp.setFontProperty(fp);
        cp.drawText("Show example of context contains English language is showed in right of parent:");
        fp.setFont(false, false, false, false, 30, null);
        cp.setFontProperty(fp);
        cp.setTextAlignRight(false);
        cp.drawText("打印汉字测试，测试打满一行内容后，是否自动换行，且向左对齐展示。");
        cp.drawImage(20,150,BitmapFactory.decodeResource(resources, R.mipmap.goodwork));
        cp.drawText("\r\n\n\n");

        if(is_thermal){
            mPrinter.printImage(cp.getCanvasImage());
        }else{
            mPrinter.printImageDot(cp.getCanvasImage(),0,0);
        }
        mPrinter.cutPaper();
    }

    public static void printTable(Resources resources, BluetoothPrinter mPrinter) {
        mPrinter.init();
        // TODO Auto-generated method stub
        mPrinter.setCharacterMultiple(0, 0);
        String column =resources.getString(R.string.note_title);
        Table table;
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80) {
            table = new Table(column, ";",new int[]{18, 8, 8, 8});
        } else {
            table = new Table(column, ";",new int[]{12, 6, 6, 6});
        }
        table.add("1,"+resources.getString(R.string.coffee)+";2.00;5.00;10.00");
        table.add("2,"+resources.getString(R.string.tableware)+";2.00;5.00;10.00");
        table.add("3,"+resources.getString(R.string.peanuts)+";1.00;68.00;68.00");
        table.add("4,"+resources.getString(R.string.cucumber)+";1.00;4.00;4.00");
        table.add("5,"+resources.getString(R.string.frog)+"; 1.00;5.00;5.00");
        table.add("6,"+resources.getString(R.string.rice)+";1.00;2.00;2.00");
        table.setHasSeparator(true);
        mPrinter.printTable(table);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
        mPrinter.cutPaper();
    }

    /**
     * 打印光栅位图
     * @param resources
     * @param bluetoothPrinter
     */
    public static void printGSBitmap(Resources resources, BluetoothPrinter bluetoothPrinter){
        Bitmap a = BitmapFactory.decodeResource(resources,R.mipmap.amsa);
        a = BitmapConvertor.zoomImg(a,384,0);
        Bitmap b = BitmapConvertor.bitmap2Gray(a);
        Bitmap bitmap = BitmapConvertor.convertToBMW(b,0,0,100);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width:"+width);
        System.out.println("height:"+height);
        int wbyte = width/8;
        if (width%8 !=0){
            wbyte = wbyte+1;
        }
        byte[] head = new byte[8];
        head[0] = 29;
        head[1] = 118;
        head[2] = 48;
        head[3] = 0;
        head[4] = (byte)(wbyte%256);
        head[5] = (byte)(wbyte/256);
        head[6] = (byte)(height%256);
        head[7] = (byte)(height/256);
        bluetoothPrinter.printByteData(head);
        System.out.println("+++++++++++++++ Total Bytes: " + StringUtil.byte2HexStr(head));
        bluetoothPrinter.printByteData(convertBitmap2bytes(bitmap,0));
    }

    /**
     * 飞扬定制下载位图
     * @param resources 资源对象
     * @param mPrinter 蓝牙打印机对象
     * @param bitmap  位图
     * @param num    位图存放地址
     */
    public static void downloadBitmap(Resources resources, BluetoothPrinter mPrinter,Bitmap bitmap, int num){
        BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = true;
        bitmap = BitmapFactory.decodeResource(resources,R.mipmap.roundone,bfoOptions);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width:"+width);
        System.out.println("height:"+height);
        int realbyte = width/ 8;
        if (width% 8 != 0) {
            realbyte = realbyte+1;
        }
        byte[] head = new byte[7];
        head[0] = (byte)27;
        head[1] = (byte)227;
        head[2] = (byte)num;
        head[3] = (byte)(realbyte%256);
        head[4] = (byte)(realbyte/256);
        head[5] = (byte)(height%256);
        head[6] = (byte)(height/256);
        mPrinter.printByteData(head);
        System.out.println("+++++++++++++++ Total Bytes: " + StringUtil.byte2HexStr(head));
        mPrinter.printByteData(convertBitmap2bytes(bitmap,0));
    }

    /**
     * convert bitmap to bytes
     * @param bitmap 位图
     * @param left 左边距
     * @return byte
     */
    public static byte[] convertBitmap2bytes(Bitmap bitmap, int left) {
        if (left % 8 != 0) {
            left = left / 8 * 8;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int var16;
        if (bitmap.getWidth() % 8 != 0) {
            var16 = width / 8 + 1;
        } else {
            var16 = width / 8;
        }

        Log.i("Utils", "wBytes " + var16);
        int var17;
        if (left % 8 != 0) {
            var17 = left / 8 + 1;
        } else {
            var17 = left / 8;
        }

        int k = bitmap.getWidth() % 8;
        byte[] imgbuf = new byte[(var16 + var17 + 4) * height];
        Object linebuf = null;
        int[] p = new int[8];
        int s = 0;
        Log.i("Utils", "+++++++++++++++ Total Bytes: " + imgbuf.length);

        for(int y = 0; y < height; ++y) {
            byte[] var18 = new byte[var16];

            int n;
            int m;
            for(n = 0; n < width / 8; ++n) {
                for(m = 0; m < 8; ++m) {
                    if (bitmap.getPixel(n * 8 + m, y) == -1) {
                        p[m] = 0;
                    } else {
                        p[m] = 1;
                    }

                    bitmap.getPixel(n * 8 + m, y);
                }

                m = p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7];
                var18[n] = (byte)m;
            }

            if (k > 0) {
                n = 0;

                for(m = 0; m < k; ++m) {
                    n = (int)((double)n + (bitmap.getPixel(bitmap.getWidth() - m - 1, y) == -1 ? 0.0D : Math.pow(2.0D, (double)(7 - m))));
                }

                var18[var16 - 1] = (byte)n;
            }

            for(n = 0; n < left / 8; ++n) {
                ++s;
                imgbuf[s] = 0;
            }

            for(n = 0; n < var16; ++n) {
                ++s;
                imgbuf[s] = var18[n];
            }
        }
        System.out.println("+++++++++++++++ Total Bytes: " + StringUtil.byte2HexStr(imgbuf));
        return imgbuf;
    }

    public static void printText(BluetoothPrinter mPrinter, String content) {
        mPrinter.init();
        mPrinter.printText(content);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_RETURN_STANDARD);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_NEWLINE);
        mPrinter.cutPaper();
    }

    public static void printBarCode(Resources resources,BluetoothPrinter mPrinter, String codeNum, String type) {
        Barcode barcode = null;
        if (type.equals(resources.getString(R.string.all))) {
            mPrinter.init();
            // TODO Auto-generated method stub
            mPrinter.setCharacterMultiple(0, 0);
            mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_CENTER);
            /**
             * 创建 Barcode实例
             *Barcode barcode = new Barcode(byte barcodeType, int param1, int param2, int param3, String content)
             *barcodeType为条码类型，二维条码： PDF417，DATAMATRIX，QRCODE，除此外是一维条码。
             * param1，param2，param3为具体条码参数：
             * 条码类型 type为一维条码时，三个参数表示：
             * param1：条码横向宽度 ，2<=n<=6，默认为 2
             * param2：条码高度 1<=n<=255，默认 162
             * param3：条码注释位置，0不打印，1上方，2下方，3上下方均有 。
             * 条码 类型 type为二维条码时，三个参数表示不同的意思：
             * 1. PDF417
             * param1：表示每行字符数，1<=n<=30。
             * param2：表示纠错等级，0<=n<=8。
             * param3：表示纵向放大倍数。
             * 2. DATAMATRIX
             * param1：表示图形高，0<=n<=144(0:自动选择)。
             * param2：表示图形宽，8<=n<=144(param1 为 0时,无效)。
             * param3：表示纵向放大倍数。
             * 3. QRCODE
             * param1：表示图形版本号，1<=n<=30(0:自动选择)。
             * param2：表示纠错等级，n = 76,77,81,72(L:7%,M:15%,Q:25%,H:30%)。
             * param3：表示纵向放大倍数。 content为条码数据
             *
             */
            Barcode barcode1 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2,  "No.123456");
            Barcode barcode2 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE39, 2, 162, 2, "123456");
            Barcode barcode3 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODABAR, 2, 162, 2, "123456");
            Barcode barcode4 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_ITF, 2, 162, 2,  "123456");
            Barcode barcode5 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE93, 2, 162,2,  "123456");
            Barcode barcode6 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_UPC_A, 2, 162, 2, "123456789012");
            Barcode barcode7 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_JAN13, 2, 162, 2, "123456789012");
            Barcode barcode8 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_JAN8, 2, 162, 2, "1234567");
            Barcode barcode9 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6,"12345678901");
            Barcode barcode10 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_PDF417, 10, 8,1,"123456789012");
            Barcode barcode11 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_DATAMATRIX, 8, 0,6,"123456789012");
            mPrinter.printText(resources.getString(R.string.print_128code)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode1);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_code39)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode2);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_codebar)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode3);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_itf)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode4);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_code93)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode5);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_upc_a)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode6);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_jan13)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode7);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_jan8)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode8);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_qrcode)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode9);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_pdf417)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode10);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            mPrinter.printText(resources.getString(R.string.print_datamatrix)+"");
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.printBarCode(barcode11);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            mPrinter.cutPaper();
        } else {
            mPrinter.init();
            // TODO Auto-generated method stub
            mPrinter.setCharacterMultiple(0, 0);
            mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_CENTER);
            if (type.equals("UPC_A")) {
                mPrinter.printText(resources.getString(R.string.print_upc_a));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_UPC_A, 2, 162,2,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("JAN13(EAN13)")) {
                mPrinter.printText(resources.getString(R.string.print_jan13));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_JAN13, 2, 162,2,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("JAN8(EAN8)")) {
                mPrinter.printText(resources.getString(R.string.print_jan8));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_JAN8, 2, 162,2,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("CODE39")) {
                mPrinter.printText(resources.getString(R.string.print_code39));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE39, 2, 162,2,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("ITF")) {
                mPrinter.printText(resources.getString(R.string.print_itf));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_ITF, 2, 162, 2, codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("CODEBAR")) {
                mPrinter.printText(resources.getString(R.string.print_codebar));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODABAR, 2, 162, 2, codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("CODE93")) {
                mPrinter.printText(resources.getString(R.string.print_code93));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE93, 2, 162,2, codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("CODE128")) {
                mPrinter.printText(resources.getString(R.string.print_128code));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2, codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("PDF417")) {
                mPrinter.printText(resources.getString(R.string.print_pdf417));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_PDF417, 10, 8,1,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("DATAMATRIX")) {
                mPrinter.printText(resources.getString(R.string.print_datamatrix));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_DATAMATRIX, 8, 0,6,  codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            } else if (type.equals("QRCODE")) {
                mPrinter.printText(resources.getString(R.string.print_qrcode));
                barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6, codeNum);
                mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            }
            mPrinter.printBarCode(barcode);
            mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
            mPrinter.cutPaper();
        }
    }

    public static int getPrinterStatus(BluetoothPrinter mPrinter){
        int ret = mPrinter.getPrinterStatus();
        return ret;
    }

    public static void lablePrint(Resources resources,BluetoothPrinter iPrinter) {
        //设置纸长宽
        iPrinter.setPage(576, 400);
//		打印二维码
        iPrinter.drawLine(1,12,32 , 564, 32);
        iPrinter.drawLine(1,12,150 , 564, 150);
        iPrinter.drawLine(1,12,270 , 564, 270);
        iPrinter.drawLine(1,12,370 , 564, 370);
        iPrinter.drawLine(1,12,32 , 12, 370);
        iPrinter.drawLine(1,564,32 , 564, 370);
        iPrinter.drawLine(1,64,32 , 64, 370);
        iPrinter.drawLine(1,290,150 ,290, 270);

        iPrinter.drawText(0,24,0,20,28,resources.getString(R.string.lable_six));
        iPrinter.drawText(0,24,0,20,53,resources.getString(R.string.lable_seven));
        iPrinter.drawText(0,24,0,20,78,resources.getString(R.string.lable_eight));
        iPrinter.drawText(0,24,0,20,98,resources.getString(R.string.lable_nine));
        iPrinter.drawText(0,24,11,64,38,resources.getString(R.string.lable_one));
        iPrinter.drawText(0,24,0,64,75,resources.getString(R.string.lable_two));
        // iPrinter.drawText(450, 40, 590, 800, PAlign.CENTER,
        //        PAlign.CENTER, 0, 0, "标准快运", fontSizeMid, 0, 1,
        //        0, 0, PRotate.Rotate_0);
        iPrinter.drawText(0,24,0,236,85,resources.getString(R.string.lable_three));
        iPrinter.drawText(0,24,0,20,143,resources.getString(R.string.lable_ten));
        iPrinter.drawText(0,24,0,20,168,resources.getString(R.string.lable_eleven));
        iPrinter.drawText(0,24,0,20,193,resources.getString(R.string.lable_twelve));
        iPrinter.drawText(0,24,0,20,215,resources.getString(R.string.lable_thirteen));
        iPrinter.drawText(0,24,0,68,160,"5/5");
        iPrinter.drawText(0,24,0,68,195,"200kg/1m");
        iPrinter.drawBarcode(1,1,100,298,153,"360024139575");
        iPrinter.drawText(0,24,0,20,268,resources.getString(R.string.lable_fourteen));
        iPrinter.drawText(0,24,0,20,300,resources.getString(R.string.lable_fifteen));
        iPrinter.drawText(0,24,0,64,276,resources.getString(R.string.lable_four));
        iPrinter.drawText(0,24,0,308,276,resources.getString(R.string.lable_five));
        iPrinter.drawText(0,24,0,64,308,"2016/08/05 13:37:54");
        iPrinter.labelPrint(0,1);
    }

    public static void M22Test(BluetoothPrinter iPrinter,Resources resources) {
        Barcode barcode = null;
        barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2, "4003425775825");
        iPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
        iPrinter.printBarCode(barcode);
        iPrinter.printText("货号:019228 尺码:5\n");
        iPrinter.printText("名称:四季歌 颜色:黑色\n");
        iPrinter.printByteData(new byte[]{(byte)12});
    }

    public static void printLable(Resources resources,BluetoothPrinter iPrinter) {
        //设置纸长宽
        iPrinter.setPage(568, 1600);
        //第一个表格线
        //drawLine( 线宽，起始X，起始Y，结尾X，结尾Y);
        iPrinter.drawLine(1, 12, 45, 568, 45);
        iPrinter.drawLine(1, 12, 45, 12, 648);
        iPrinter.drawLine(1, 568, 45, 568, 648);

        iPrinter.drawLine(1, 12, 120, 568, 120);
        iPrinter.drawLine(1, 12, 280, 568, 280);
        iPrinter.drawLine(1, 12, 360, 568, 360);
        iPrinter.drawLine(1, 12, 424, 568, 424);
        iPrinter.drawLine(1, 12, 544, 568, 544);
        iPrinter.drawLine(1, 12, 648, 568, 648);
        //
        iPrinter.drawLine(1, 12, 824, 568, 824);
        iPrinter.drawLine(1, 284, 360, 284, 424);
        iPrinter.drawLine(1, 56, 424, 56, 648);
        //
        iPrinter.drawLine(1, 284, 648, 284, 824);
        iPrinter.drawLine(1, 12, 936, 568, 936);
//修改内容
        iPrinter.drawLine(1, 56, 650, 56, 825);
        iPrinter.drawLine(1, 568, 650, 568, 825);
        iPrinter.drawLine(1, 12, 650, 12, 825);



        //第一行内容
        iPrinter.drawText(1,24,0,304,35,"代收货款");
        iPrinter.inverse(290,20,450,20,50);
        iPrinter.drawText(1,24,0,432,30,":￥￥");
        iPrinter.drawText(1,24,0,475,30,"29995");

        //打印二维码
        iPrinter.drawBarcode(3,1,96,80,100,"360024139575");
        iPrinter.drawText(0,24,0,72,210,"360 024 139 575");

        //表格一第一行
        iPrinter.drawText(1,24,0,72,255,"哈尔滨转齐齐哈尔");
        iPrinter.drawText(0,24,0,13,350,"哈尔滨转齐齐哈尔");
        iPrinter.drawText(0,24,0,332,350,"2015-12-24");
        //第二行
        iPrinter.drawText(1,55,0,16,416,"收");
        iPrinter.drawText(1,55,0,16,440,"件");
        iPrinter.drawText(1,55,0,16,464,"信");
        iPrinter.drawText(1,55,0,16,488,"息");

        iPrinter.drawText(0,24,0,64,400,"收件人：肖凤财　　手机／电话：13590000000");
        iPrinter.drawText(0,24,0,64,430,"地址:上海 上海市 青浦区 华新镇华志璐100号");
        iPrinter.drawText(0,24,0,64,460,"XXX住宅区XX幢XX单元XXX室");

        //第三行
        iPrinter.drawText(1,55,0,16,530,"寄");
        iPrinter.drawText(1,55,0,16,550,"件");
        iPrinter.drawText(1,55,0,16,574,"信");
        iPrinter.drawText(1,55,0,16,598,"息");
        iPrinter.drawText(0,24,0,64,520,"寄件人：李小小 手机／电话：021-69783707");
        iPrinter.drawText(0,24,0,64,548,"地址:上海 上海市 青浦区 华新镇华志璐1684");
        iPrinter.drawText(0,24,0,64,572,"号中通快递");


        //第四行
        iPrinter.drawText(1,55,0,16,686,"服");
        iPrinter.drawText(1,55,0,16,710,"务");

        iPrinter.drawText(0,24,0,64,634,"内容品名: 衣服");
        iPrinter.drawText(0,24,0,64,666,"计费重量: 210(kg)");
        iPrinter.drawText(0,24,0,64,698,"声明价值: ￥29995");
        iPrinter.drawText(0,24,0,64,730,"代收金额: ￥29995");
        iPrinter.drawText(0,24,0,64,762,"到付金额: ￥29995");
        iPrinter.drawText(0,24,0,284,620,"签收人/签收时间");
        iPrinter.drawText(0,24,0,442,768,"月   日");



        //第二个表格线
        iPrinter.drawLine(1, 12, 936, 568, 936);
        iPrinter.drawLine(1, 12, 984, 568, 984);
        iPrinter.drawLine(1, 12, 1104, 568, 1104);
        iPrinter.drawLine(1, 12, 1152, 568, 1152);
        iPrinter.drawLine(1, 12, 1200, 568, 1200);
        iPrinter.drawLine(1, 284, 936, 284, 1104);
        iPrinter.drawLine(1, 112, 1104, 112, 1200);
        iPrinter.drawLine(1, 224, 1104, 224, 1200);
        iPrinter.drawLine(1, 344, 1104, 344, 1200);
        iPrinter.drawLine(1, 456, 1104, 456, 1200);
        iPrinter.drawLine(1, 568, 1104, 568, 1200);

        iPrinter.drawLine(1, 12, 1304, 568, 1304);
        iPrinter.drawLine(1, 12, 1424, 568, 1424);
        iPrinter.drawLine(1, 12, 1472, 568, 1472);
        iPrinter.drawLine(1, 12, 1520, 568, 1520);
        iPrinter.drawLine(1, 12, 1600, 568, 1600);
        iPrinter.drawLine(1, 284, 1200, 284, 1424);
        iPrinter.drawLine(1, 112, 1424, 112, 1520);

        iPrinter.drawLine(1, 224, 1424, 224, 1520);
        iPrinter.drawLine(1, 344, 1424, 344, 1520);
        iPrinter.drawLine(1, 456, 1424, 456, 1520);
        iPrinter.drawLine(1, 568, 1424, 568, 1520);
        iPrinter.drawLine(1, 284, 1520, 284, 1600);

        iPrinter.drawLine(1, 12, 940, 12, 1600);
        iPrinter.drawLine(1, 568, 940, 568, 1600);

        //表格二第一行
        iPrinter.drawText(0,24,0,16,922,"运单号：360024139575");
        iPrinter.drawText(0,24,0,308,922,"订单号：Lp0000185685");

//        //第二行
        iPrinter.drawText(0,24,0,16,970,"收件方信息：");
        iPrinter.drawText(0,24,0,16,994,"肖凤财 13590000000");
        iPrinter.drawText(0,24,0,16,1018,"广东省深圳市南山软件");
        iPrinter.drawText(0,24,0,16,1042,"产业基地1栋B座16楼");
        iPrinter.drawText(0,24,0,308,970,"寄件方信息");
        iPrinter.drawText(0,24,0,308,994,"李小小 021-69783707");
        iPrinter.drawText(0,24,0,308,1018,"广东省深圳市南山软件");
        iPrinter.drawText(0,24,0,308,1042,"产业基地1栋B座16楼");
        //第三行

        iPrinter.drawText(0,24,0,16,1090,"内容品名");
        iPrinter.drawText(0,24,0,120,1090,"计费重量");
        iPrinter.drawText(0,24,0,240,1090,"声明价值");
        iPrinter.drawText(0,24,0,352,1090,"代付金额");
        iPrinter.drawText(0,24,0,464,1090,"到付金额");

        //第四行
        iPrinter.drawText(0,24,0,16,1128,"衣服");
        iPrinter.drawText(0,24,0,152,1128,"210");
        iPrinter.drawText(0,24,0,248,1128,"29995");
        iPrinter.drawText(0,24,0,368,1128,"29995");
        iPrinter.drawText(0,24,0,480,1128,"29995");

        //第五行条形码
        iPrinter.drawBarcode(1,1,64,20,1180,"360024139575");
        iPrinter.drawText(0,24,0,16,1246,"360  024  139  575");

        //第六行
        iPrinter.drawText(0,24,0,16,1290,"收件方信息");
        iPrinter.drawText(0,24,0,16,1314,"肖凤财 13590000000");
        iPrinter.drawText(0,24,0,16,1338,"广东省深圳市南山软件");
        iPrinter.drawText(0,24,0,16,1362,"产业基地1栋B座16楼");
        iPrinter.drawText(0,24,0,308,1290,"寄件方信息：");
        iPrinter.drawText(0,24,0,308,1314,"李小小 021-69783707");
        iPrinter.drawText(0,24,0,308,1338,"广东省深圳市南山软件");
        iPrinter.drawText(0,24,0,308,1362,"产业基地1栋B座16楼");

        //第七行
        iPrinter.drawText(0,24,0,16,1410,"内容品名");
        iPrinter.drawText(0,24,0,120,1410,"计费重量");
        iPrinter.drawText(0,24,0,240,1410,"声明价值");
        iPrinter.drawText(0,24,0,352,1410,"代付金额");
        iPrinter.drawText(0,24,0,464,1410,"到付金额");

        //第八行
        iPrinter.drawText(0,24,0,16,1458,"衣服");
        iPrinter.drawText(0,24,0,152,1458,"210");
        iPrinter.drawText(0,24,0,248,1458,"29995");
        iPrinter.drawText(0,24,0,368,1458,"29995");
        iPrinter.drawText(0,24,0,480,1458,"29995");

        //第九行
        iPrinter.drawText(0,24,0,16,1506,"打印时间");
        iPrinter.drawText(0,24,0,316,1506,"签收人/签收时间");
        iPrinter.drawText(0,24,0,16,1538,"2015/12/28 16:52");
        iPrinter.drawText(0,24,0,484,1538,"月  日");
//        iPrinter.drawGraphic(50,50,0,0,bmp2);
        Bitmap bmp2 = BitmapFactory.decodeResource(resources, R.mipmap.cungen);

        iPrinter.labelPrint(0, 1);
    }

    public static void printDingzhi(Resources resources,BluetoothPrinter iPrinter) {
        iPrinter.setPage(576, 800);
        iPrinter.drawText(0,24,0,0,0,"Z2F010129288-");
        iPrinter.drawText(0,24,0,250,0,"143/156/160/106");
        iPrinter.drawText(0,24,0,0,24,"1CHNT201801300038 1187");
        iPrinter.drawBarCode(0,50,"P020300038180130D15489", 1, false, 2, 60);
        iPrinter.drawText(0,24,2,300,108,"25义乌");
        iPrinter.drawText(0,24,0,300,130,"晨光超级飞侠4D超轻粘土36色收纳盒JKE03981");
        iPrinter.drawText(1,24,1,0,6,"经济快递 10-19 08:27 福建");
        iPrinter.drawText(1,55,11,450,0,"邮特 2014");
        iPrinter.drawLine(0,0,54,250,54);
        iPrinter.drawText(0,24,0,32,85,"一票多件签回单");
        iPrinter.drawLine(0,0,102,250,102);
        iPrinter.drawText(1,24,0,0,108,"东街揽投部东街揽投部");
        iPrinter.drawText(1,24,0,250,132,"局收  号码   AAAA");
        iPrinter.drawLine(0,0,162,250,162);
        iPrinter.drawLine(0,400,162,500,162);
        iPrinter.drawText(1,24,0,0,168,"福州机场长乐中心55件");
        iPrinter.drawText(1,24,0,250,180,"局发  重量 2.00");
        iPrinter.drawLine(0,0,192,250,192);
        iPrinter.drawLine(0,450,192,500,192);
        iPrinter.drawBarCode(0,208,"10080003-10000000-2-121-6666-0000-1-0",1,false,0,48);
        iPrinter.drawText(0,24,0,0,256,"10080003-10000000-2-121-6666-0000-1-0");
        iPrinter.labelPrint(0, 0);
    }

    public static void printNotes(Resources resources,BluetoothPrinter mPrinter){
        mPrinter.init();
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
        mPrinter.setCharacterMultiple(0,0);
        mPrinter.setLeftMargin(0,0);
        mPrinter.printText("XX市公安局交通巡逻警察支队\n");
        mPrinter.setCharacterMultiple(0,1);
        mPrinter.printText("公安交通管理简易程序处罚决定书\n");
        mPrinter.setCharacterMultiple(0,0);
        mPrinter.printText("决定书编号:4101051700000012\n");
        Barcode barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 72,0,  "No.123456");
        mPrinter.printBarCode(barcode);
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append("被处罚人: 张三"+"\n");
        sb.append("机动车驾驶证档案编号: 410100350774"+"\n");
        sb.append("机动车驾驶证/居民身份证号码: 410122197912182710"+"\n");
        sb.append("准驾车型: C1E"+"\n");
        sb.append("联系方式: 12345678911"+"\n");
        sb.append("车辆牌号: 豫A66E88"+"\n");
        sb.append("车辆类型: 小型轿车"+"\n");
        sb.append("发证机关: 豫A"+"\n");
        sb.append("    被处罚人: 于2012年05月25日16时39分XX省XX市解放路实施机动车不安交" +
                "通信号灯规定同行的违法行为(代码13022)。违反了【违法规定】。根据:【中华人" +
                "民共和国道路安全法】之规定，决定处以200元罚款。"+"\n");
        sb.append("    请持本决定书在十五日内到本市是去工商银行缴纳罚款。逾期不缴纳的，每日按" +
                "罚款数额的百分之三加处罚款"+"\n");
        sb.append("    如不服本决定，可以在收到本决定书之日起六十日内向xx市公安局或xx市人民政" +
                "府申请行政复议；或者依照《中华人民共和国行政诉讼法》在三个月内向xx市二七区" +
                "人民法院提起行政诉讼。处罚地点:xx省xx市解放路"+"\n");
        sb.append("交通警察:"+"\n");
        mPrinter.printText(sb.toString());
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.name);
        mPrinter.printImage(bitmap);
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_RIGHT);
        mPrinter.printText("2012年05月25日\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_LEFT);
        mPrinter.printText("被处罚人签名:________\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_RIGHT);
        mPrinter.printText("年    月    日\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_LEFT);
        mPrinter.printText("备注:________\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_CENTER);
        mPrinter.printText("根据《机动车驾驶证申领和使用规定》记3分\n");
        mPrinter.printText("------------------------"+"\n");
        mPrinter.printText("第一联:送达被处罚人\n");
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        mPrinter.cutPaper();
    }

    public static void yuantongLable(Resources resources,BluetoothPrinter iPrinter){
        iPrinter.setPage(576,400);
        iPrinter.drawBarCode(400,0,"573901",1,false,1,50);
        iPrinter.printText("CENTER\n");
        iPrinter.drawText(420,60,"573901",2,0,0,false,false);
        iPrinter.printText("LEFT\n");
        //派件联
        iPrinter.drawText(0,96,"376-900-014",4,0,0,false,false);
        iPrinter.drawLine(1,0,160,576,160);
        iPrinter.drawBarCode(50,176,"813844304539",1,false,2,80);
        iPrinter.drawText(200,256,"813844304539",2,0,0,false,false);
        iPrinter.drawLine(1,0,286,576,286);
        iPrinter.drawText(0,310,"收",3,0,0,false,false);
        iPrinter.drawText(0,342,"件",3,0,0,false,false);
        iPrinter.drawText(0,374,"人",3,0,0,false,false);
        iPrinter.drawLine(1,32,294,32,582);
        iPrinter.drawText(32,294,"谷靖 0573-87506693",3,0,0,false,false);
        iPrinter.drawText(32,342,488,96,"浙江省-嘉兴市-海宁市洛隆路625号二栋3楼",3,0,0,false,false);
        iPrinter.drawLine(1,528,294,528,582);
        iPrinter.drawText(528,310,"派",2,0,0,false,false);
        iPrinter.drawText(528,342,"件",2,0,0,false,false);
        iPrinter.drawText(528,374,"联",2,0,0,false,false);
        iPrinter.drawLine(1,0,474,528,474);
        iPrinter.drawText(0,486,"寄",2,0,0,false,false);
        iPrinter.drawText(0,510,"件",2,0,0,false,false);
        iPrinter.drawText(0,534,"人",2,0,0,false,false);
        iPrinter.drawText(32,478,"冯平 17710921767",2,0,0,false,false);
        iPrinter.drawText(32,502,384,72,"上海市-浦东新区横盖江路398号",2,0,0,false,false);
        iPrinter.drawLine(1,400,474,400,582);
        iPrinter.drawText(408,478,"签收人:",2,0,0,false,false);
        iPrinter.drawText(416,554,"日期:",2,0,0,false,false);
        //收件联
        iPrinter.drawText(0,590,"运单号:813844304539  寄件码:101582",2,0,0,false,false);
        iPrinter.drawLine(1,0,618,576,618);
        iPrinter.drawText(0,630,"收",2,0,0,false,false);
        iPrinter.drawText(0,654,"件",2,0,0,false,false);
        iPrinter.drawText(0,678,"人",2,0,0,false,false);
        iPrinter.drawLine(1,32,618,32,1128);
        iPrinter.drawLine(1,528,618,528,1128);
        iPrinter.drawText(36,622,"谷靖 0573-87506693",2,0,0,false,false);
        iPrinter.drawText(36,646,492,96,"浙江省-嘉兴市-海宁市洛隆路625号二栋3楼",2,0,0,false,false);
        iPrinter.drawLine(1,0,726,528,726);
        iPrinter.drawText(528,690,"收",2,0,0,false,false);
        iPrinter.drawText(528,714,"件",2,0,0,false,false);
        iPrinter.drawText(528,738,"联",2,0,0,false,false);
        iPrinter.drawText(0,726,"内",2,0,0,false,false);
        iPrinter.drawText(0,750,"容",2,0,0,false,false);
        iPrinter.drawText(0,774,"品",2,0,0,false,false);
        iPrinter.drawText(0,798,"名",2,0,0,false,false);
        iPrinter.drawText(44,762,"衣服",2,0,0,false,false);
        iPrinter.drawLine(1,32,806,528,806);
        iPrinter.drawText(36,810,"数量:1",2,0,0,false,false);
        iPrinter.drawLine(1,300,806,300,834);
        iPrinter.drawText(304,810,"重量:0kg",2,0,0,false,false);
        //寄件联
//        Bitmap bmp2 = BitmapFactory.decodeResource(resources, R.drawable.cungen);
//        iPrinter.drawWatermark(50,930,0,0,bmp2);
        iPrinter.drawWatermarkText("客户存根联",50,1000,2);
        iPrinter.drawBarCode(100,850,"813844304539",1,false,2,45);
        iPrinter.drawText(300,900,"813844304539",2,0,0,false,false);
        iPrinter.drawLine(1,0,928,576,928);
        iPrinter.drawText(0,928,"收",2,0,0,false,false);
        iPrinter.drawText(0,952,"件",2,0,0,false,false);
        iPrinter.drawText(0,976,"人",2,0,0,false,false);
        iPrinter.drawLine(1,32,928,32,1300);
        iPrinter.drawLine(1,528,928,528,1300);
        iPrinter.drawText(44,932,"谷靖 0573-87506693",2,0,0,false,false);
        iPrinter.drawText(44,960,530,96,"浙江省-嘉兴市-海宁市洛隆路625号二栋3楼",2,0,0,false,false);
        iPrinter.drawLine(1,0,1012,528,1012);
        iPrinter.drawText(0,1024,"寄",2,0,0,false,false);
        iPrinter.drawText(0,1048,"件",2,0,0,false,false);
        iPrinter.drawText(0,1072,"人",2,0,0,false,false);
        iPrinter.drawText(36,1032,"冯平 17710921767",2,0,0,false,false);
        iPrinter.drawText(36,1060,384,72,"上海市-浦东新区横盖江路398号",2,0,0,false,false);
        iPrinter.drawLine(1,0,1108,528,1108);
        iPrinter.drawText(0,1104,"内",2,0,0,false,false);
        iPrinter.drawText(0,1128,"容",2,0,0,false,false);
        iPrinter.drawText(0,1152,"品",2,0,0,false,false);
        iPrinter.drawText(0,1176,"名",2,0,0,false,false);
        iPrinter.drawText(44,1140,"衣服",2,0,0,false,false);
        iPrinter.drawLine(1,32,1176,528,1176);
        iPrinter.drawText(36,1176,"数量:1",2,0,0,false,false);
        iPrinter.drawText(304,1176,"重量:0kg",2,0,0,false,false);
        iPrinter.drawText(528,1024,"寄",2,0,0,false,false);
        iPrinter.drawText(528,1048,"件",2,0,0,false,false);
        iPrinter.drawText(528,1072,"联",2,0,0,false,false);
        iPrinter.labelPrint(0,1 );
    }

    public static void printImage(Bitmap bitmap, BluetoothPrinter bPrinter,
                                  int pageWidth, int pageHeight,
                                  int startX, int startY) {
        if (bPrinter != null) {
            bPrinter.setPage(pageWidth, pageHeight);
            bPrinter.drawGraphic(startX, startY, 0, 0, bitmap);
            bPrinter.labelPrint(0, 1);
        }
    }
    public static void printImage(Bitmap bitmap, BluetoothPrinter bPrinter){
        if (bPrinter!=null){
            bPrinter.printImage(bitmap);
            bPrinter.printText("\n");
            //开启黑标模式的指令
//            bPrinter.printByteData(new byte[]{0x1f,0x11,0x1f,0x44,0x01,0x1f,0x1f});
            //发送灵敏度的指令21
//            bPrinter.printByteData(new byte[]{0x1B,0x09,0x1B,0x3D,0x15,0x1B,0x15});
            bPrinter.printByteData(new byte[]{0x0c});
        }
    }

    public static void printAlpay(BluetoothPrinter bPrinter,String moneyStr){
        bPrinter.init();
        bPrinter.setCharacterMultiple(1,1);
        bPrinter.printText(moneyStr+" dollar");
        bPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 8);
        bPrinter.cutPaper();
    }
}

