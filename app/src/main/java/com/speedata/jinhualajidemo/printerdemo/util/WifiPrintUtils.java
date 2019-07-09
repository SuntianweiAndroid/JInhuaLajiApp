package com.speedata.jinhualajidemo.printerdemo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.android_print_sdk.Barcode;
import com.android_print_sdk.CanvasPrint;
import com.android_print_sdk.FontProperty;
import com.android_print_sdk.PrinterType;
import com.android_print_sdk.Table;
import com.android_print_sdk.wifi.WiFiPrinter;
import com.speedata.jinhualajidemo.R;

/**
 * Created by xzc-pc on 2018-09-17.
 */
public class WifiPrintUtils {

    public static void printNote(Resources resources, WiFiPrinter wPrinter) {
        wPrinter.init();
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_CENTER);
        StringBuffer sb = new StringBuffer();
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_CENTER);
        //字符橫向纵向放大一倍
        wPrinter.setCharacterMultiple(1, 1);
        wPrinter.printText(resources.getString(R.string.welcome)+"\n");
        wPrinter.setCharacterMultiple(0, 0);
        if (wPrinter.getCurrentPrintType() == PrinterType.Printer_58 ) {
            wPrinter.printText("------------------------------\n");
        }else if (wPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            wPrinter.printText("----------------------------------------------\n");
        }
        wPrinter.printText(resources.getString(R.string.shopping_name)+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_LEFT);
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
        wPrinter.printText(sb.toString());
        wPrinter.setCharacterMultiple(0, 0);
        if (wPrinter.getCurrentPrintType() == PrinterType.Printer_58) {
            wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_CENTER);
            wPrinter.printText("------------------------------\n");
        }else if (wPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            wPrinter.printText("----------------------------------------------\n");
        }
        wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
        wPrinter.cutPaper();
    }

    public static void printImage(Resources resources, WiFiPrinter wPrinter, boolean is_thermal) {
        wPrinter.init();
        wPrinter.setLeftMargin(0,0);
        BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.apple,bfoOptions);
        Matrix matrix = new Matrix();
        //matrix.setScale(X轴缩放,Y轴缩放，，);//后面两个参数是相对于缩放的位置放置，尝试设置，建议数值>100以上进行设置
        matrix.setScale(4.5f,4.5f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        if (is_thermal) {
            //单色图片
            Bitmap a = BitmapFactory.decodeResource(resources,R.mipmap.codeqr);
            wPrinter.printImage(a);
            //打印水印;
//             Bitmap a = BitmapFactory.decodeResource(resources,R.drawable.logob);
//           wPrinter.printImage2(a);
        } else {
            wPrinter.printImageDot(bitmap, 1, 0);
        }
        wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
        wPrinter.cutPaper();

    }

    public static void printCustomImage(Resources resources, WiFiPrinter wPrinter, boolean is_thermal) {
        wPrinter.init();
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_LEFT);
        wPrinter.printText(resources.getString(R.string.printCanvas));
        wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        CanvasPrint cp=new CanvasPrint();
        /**
         * 初始化画布，画布的宽度为变量，一般有两个选择：1.58mm型号打印机实际可用是48mm，48*8=384px
         * 2. 80mm型号打印机实际可用是72mm，72*8=576px，因为画布的高度是无限制的，但从内存分配方面考虑要小于4M比较合适。
         * 所有预置为宽度的5倍。初始化画笔，默认属性有：1.消除锯齿   2.设置画笔颜色为黑色。
         *
         * init 方法包含cp.initCanvas(550)和cp.initPaint（），M21打印宽度为48mm，其他为72mm
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
            wPrinter.printImage(cp.getCanvasImage());
        }else{
            wPrinter.printImageDot(cp.getCanvasImage(),0,0);
        }
        wPrinter.cutPaper();

    }

    public static void printTable(Resources resources, WiFiPrinter wPrinter) {
        wPrinter.init();
        // TODO Auto-generated method stub
        wPrinter.setCharacterMultiple(0, 0);
        String column =resources.getString(R.string.note_title);
        Table table;
        if (wPrinter.getCurrentPrintType() == PrinterType.Printer_80) {
            table = new Table(column, ";",     new int[]{18, 8, 8, 8});
        } else {
            table = new Table(column, ";", new int[]{12, 6, 6, 6});
        }
        table.add("1,"+resources.getString(R.string.coffee)+";2.00;5.00;10.00");
        table.add("2,"+resources.getString(R.string.tableware)+";2.00;5.00;10.00");
        table.add("3,"+resources.getString(R.string.peanuts)+";1.00;68.00;68.00");
        table.add("4,"+resources.getString(R.string.cucumber)+";1.00;4.00;4.00");
        table.add("5,"+resources.getString(R.string.frog)+"; 1.00;5.00;5.00");
        table.add("6,"+resources.getString(R.string.rice)+";1.00;2.00;2.00");
        wPrinter.printTable(table);
        wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
        wPrinter.cutPaper();
    }

    public static void printText(WiFiPrinter wPrinter, String content) {
        wPrinter.init();
        wPrinter.printText(content+"\n\n\n\n");
        wPrinter.cutPaper();
    }

    public static void printBarCode(Resources resources,WiFiPrinter wPrinter, String codeNum, String type) {
        Barcode barcode = null;
        if (type.equals(resources.getString(R.string.all))) {
            wPrinter.init();
            // TODO Auto-generated method stub
            wPrinter.setCharacterMultiple(0, 0);
            wPrinter.setLeftMargin(30, 0);
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
            Barcode barcode1 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2,  "No.123456");
            Barcode barcode2 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE39, 2, 162, 2, "123456");
            Barcode barcode3 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODABAR, 2, 162, 2, "123456");
            Barcode barcode4 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_ITF, 2, 162, 2,  "123456");
            Barcode barcode5 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE93, 2, 162,2,  "123456");
            Barcode barcode6 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_UPC_A, 2, 162, 2, "123456789012");
            Barcode barcode7 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_JAN13, 2, 162, 2, "123456789012");
            Barcode barcode8 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_JAN8, 2, 162, 2, "1234567");
            Barcode barcode9 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6,"12345678901");
            Barcode barcode10 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_PDF417, 10, 8,1,"123456789012");
            Barcode barcode11 = new Barcode(WiFiPrinter.BAR_CODE_TYPE_DATAMATRIX, 8, 0,6,"123456789012");
            wPrinter.printText(resources.getString(R.string.print_128code)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode1);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_code39)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode2);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_codebar)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode3);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_itf)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode4);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_code93)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode5);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_upc_a)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode6);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_jan13)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode7);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_jan8)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode8);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_qrcode)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode9);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_pdf417)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode10);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            wPrinter.printText(resources.getString(R.string.print_datamatrix)+"");
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode11);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
            wPrinter.cutPaper();
        } else {
            wPrinter.init();
            // TODO Auto-generated method stub
            wPrinter.setCharacterMultiple(0, 0);
            wPrinter.setLeftMargin(15, 0);
            if (type.equals("UPC_A")) {
                wPrinter.printText(resources.getString(R.string.print_upc_a));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_UPC_A, 2, 162,2,  codeNum);
            } else if (type.equals("JAN13(EAN13)")) {
                wPrinter.printText(resources.getString(R.string.print_jan13));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_JAN13, 2, 162,2,  codeNum);
            } else if (type.equals("JAN8(EAN8)")) {
                wPrinter.printText(resources.getString(R.string.print_jan8));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_JAN8, 2, 162,2,  codeNum);
            } else if (type.equals("CODE39")) {
                wPrinter.printText(resources.getString(R.string.print_code39));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE39, 2, 162,2,  codeNum);
            } else if (type.equals("ITF")) {
                wPrinter.printText(resources.getString(R.string.print_itf));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_ITF, 2, 162, 2, codeNum);
            } else if (type.equals("CODEBAR")) {
                wPrinter.printText(resources.getString(R.string.print_codebar));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODABAR, 2, 162, 2, codeNum);
            } else if (type.equals("CODE93")) {
                wPrinter.printText(resources.getString(R.string.print_code93));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE93, 2, 162,2, codeNum);
            } else if (type.equals("CODE128")) {
                wPrinter.printText(resources.getString(R.string.print_128code));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2, codeNum);
            } else if (type.equals("PDF417")) {
                wPrinter.printText(resources.getString(R.string.print_pdf417));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_PDF417, 10, 8,1,  codeNum);
            } else if (type.equals("DATAMATRIX")) {
                wPrinter.printText(resources.getString(R.string.print_datamatrix));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_DATAMATRIX, 8, 0,6,  codeNum);
            } else if (type.equals("QRCODE")) {
                wPrinter.printText(resources.getString(R.string.print_qrcode));
                barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6, codeNum);
            }
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            wPrinter.printBarCode(barcode);
            wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
            wPrinter.cutPaper();
        }
    }

    public static int getPrinterStatus(WiFiPrinter wPrinter){
        int ret = wPrinter.getPrinterStatus();
        return ret;
    }

    public static void lablePrint(Resources resources,WiFiPrinter iPrinter) {
        //设置纸长宽
        iPrinter.setPage(576, 400);
        //打印二维码
        iPrinter.drawLine(0,12,32 , 576, 32);
        iPrinter.drawLine(0,12,150 , 576, 150);
        iPrinter.drawLine(0,12,270 , 576, 270);
        iPrinter.drawLine(0,12,370 , 576, 370);
        iPrinter.drawLine(0,12,32 , 12, 370);
        iPrinter.drawLine(0,576,32 , 576, 370);
        iPrinter.drawLine(0,64,32 , 64, 370);
        iPrinter.drawLine(0,290,150 ,290, 270);

        iPrinter.drawText(0,24,0,20,28,resources.getString(R.string.lable_six));
        iPrinter.drawText(0,24,0,20,53,resources.getString(R.string.lable_seven));
        iPrinter.drawText(0,24,0,20,78,resources.getString(R.string.lable_eight));
        iPrinter.drawText(0,24,0,20,98,resources.getString(R.string.lable_nine));
        iPrinter.drawText(0,24,0,64,38,resources.getString(R.string.lable_one));
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
//         iPrinter.drawWatermark(100,70,400,400,resizeBmp);
        iPrinter.labelPrint(0,1);
    }

    public static void M21Test(WiFiPrinter iPrinter,Resources resources) {

        iPrinter.printText(resources.getString(R.string.print_qrcode));
        Barcode barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6, "No.123456");
        iPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        iPrinter.printBarCode(barcode);
        iPrinter.printText("第一行测试内容\n");
        iPrinter.printText("第二行测试内容\n");
        iPrinter.printText("第三行测试内容\n");
        iPrinter.printText("第四行测试内容\n");
        iPrinter.printText("第五行测试内容\n");
        iPrinter.write(new byte[]{(byte)12});
    }

    public static void printLable(Resources resources,WiFiPrinter iPrinter) {

        //设置纸长宽
        iPrinter.setPage(568, 1600);
        //第一个表格线
        // drawLine( 线宽，起始X，起始Y，结尾X，结尾Y);
        iPrinter.drawLine(-1, 12, 45, 568, 45);
        iPrinter.drawLine(0, 12, 45, 12, 648);
        iPrinter.drawLine(0, 568, 45, 568, 648);

        iPrinter.drawLine(-1, 12, 120, 568, 120);
        iPrinter.drawLine(-1, 12, 280, 568, 280);
        iPrinter.drawLine(-1, 12, 360, 568, 360);
        iPrinter.drawLine(-1, 12, 424, 568, 424);
        iPrinter.drawLine(-1, 12, 544, 568, 544);
        iPrinter.drawLine(-1, 12, 648, 568, 648);

        iPrinter.drawLine(-1, 12, 824, 568, 824);
        iPrinter.drawLine(0, 284, 360, 284, 424);
        iPrinter.drawLine(0, 56, 424, 56, 648);

        iPrinter.drawLine(0, 284, 648, 284, 824);
        iPrinter.drawLine(-1, 12, 936, 568, 936);
        //修改内容
        iPrinter.drawLine(0, 56, 650, 56, 825);
        iPrinter.drawLine(0, 568, 650, 568, 825);
        iPrinter.drawLine(0, 12, 650, 12, 825);

        //第一行内容
        iPrinter.drawText(1,24,0,304,35,"代收货款");
        iPrinter.inverse(290,20,450,20,50);
        iPrinter.drawText(1,24,0,432,30,":￥");
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
        iPrinter.drawLine(-1, 12, 936, 568, 936);
        iPrinter.drawLine(-1, 12, 984, 568, 984);
        iPrinter.drawLine(-1, 12, 1104, 568, 1104);
        iPrinter.drawLine(-1, 12, 1152, 568, 1152);
        iPrinter.drawLine(-1, 12, 1200, 568, 1200);
        iPrinter.drawLine(0, 284, 936, 284, 1104);
        iPrinter.drawLine(0, 112, 1104, 112, 1200);
        iPrinter.drawLine(0, 224, 1104, 224, 1200);
        iPrinter.drawLine(0, 344, 1104, 344, 1200);
        iPrinter.drawLine(0, 456, 1104, 456, 1200);
        iPrinter.drawLine(0, 568, 1104, 568, 1200);

        iPrinter.drawLine(-1, 12, 1304, 568, 1304);
        iPrinter.drawLine(-1, 12, 1424, 568, 1424);
        iPrinter.drawLine(-1, 12, 1472, 568, 1472);
        iPrinter.drawLine(-1, 12, 1520, 568, 1520);
        iPrinter.drawLine(-1, 12, 1600, 568, 1600);
        iPrinter.drawLine(0, 284, 1200, 284, 1424);
        iPrinter.drawLine(0, 112, 1424, 112, 1520);

        iPrinter.drawLine(0, 224, 1424, 224, 1520);
        iPrinter.drawLine(0, 344, 1424, 344, 1520);
        iPrinter.drawLine(0, 456, 1424, 456, 1520);
        iPrinter.drawLine(0, 568, 1424, 568, 1520);
        iPrinter.drawLine(0, 284, 1520, 284, 1600);

        iPrinter.drawLine(0, 12, 940, 12, 1600);
        iPrinter.drawLine(0, 568, 940, 568, 1600);

        //表格二第一行
        iPrinter.drawText(0,24,0,16,922,"运单号：360024139575");
        iPrinter.drawText(0,24,0,308,922,"订单号：Lp0000185685");

        //第二行
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
        iPrinter.drawWatermark(50,50,0,0,bmp2);
        iPrinter.labelPrint(0, 1);
    }

    public static void printDingzhi(Resources resources,WiFiPrinter iPrinter) {
        iPrinter.setPage(568, 600);
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
        iPrinter.DrawWatermarkText("始发地",100,100,3);
        iPrinter.drawText(1,24,0,32,54,"一票多件签回单");
        iPrinter.inverse(0,10,300,10,200);
        iPrinter.labelPrint(0, 1);
    }

    public static void printNotes(Resources resources,WiFiPrinter wPrinter){
        wPrinter.init();
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_CENTER);
        wPrinter.setCharacterMultiple(0,0);
        wPrinter.setLeftMargin(0,0);
        wPrinter.printText("XX市公安局交通巡逻警察支队"+"\n");
        wPrinter.setCharacterMultiple(0,1);
        wPrinter.printText("公安交通管理简易程序处罚决定书"+"\n");
        wPrinter.setCharacterMultiple(0,0);
        wPrinter.printText("决定书编号:4101051700000012"+"\n");
        Barcode barcode = new Barcode(WiFiPrinter.BAR_CODE_TYPE_CODE128, 2, 72,0,  "No.123456");
        wPrinter.printBarCode(barcode);
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN, WiFiPrinter.COMM_ALIGN_LEFT);
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
        wPrinter.printText(sb.toString());
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.name);
        wPrinter.printImage(bitmap);
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN,WiFiPrinter.COMM_ALIGN_RIGHT);
        wPrinter.printText("2012年05月25日"+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN,WiFiPrinter.COMM_ALIGN_LEFT);
        wPrinter.printText("被处罚人签名:________"+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN,WiFiPrinter.COMM_ALIGN_RIGHT);
        wPrinter.printText("年    月    日"+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN,WiFiPrinter.COMM_ALIGN_LEFT);
        wPrinter.printText("备注:________"+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_ALIGN,WiFiPrinter.COMM_ALIGN_CENTER);
        wPrinter.printText("根据《机动车驾驶证申领和使用规定》记3分"+"\n");
        wPrinter.printText("------------------------"+"\n");
        wPrinter.printText("第一联:送达被处罚人"+"\n");
        wPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 6);
        wPrinter.cutPaper();
    }

    public static void yuantongLable(Resources resources,WiFiPrinter iPrinter){
//        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.drawable.yt);
        iPrinter.setPage(576,1200);
        iPrinter.drawBarCode(400,0,"573901",1,false,1,50);
        iPrinter.drawText(420,60,"573901",2,0,0,false,false);
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
//        iPrinter.drawGraphic(0,830,0,0,bitmap);
        Bitmap bmp2 = BitmapFactory.decodeResource(resources, R.mipmap.cungen);
        iPrinter.drawWatermark(50,930,0,0,bmp2);

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
        iPrinter.labelPrint(0,1);
    }

    public static void printAlpay(WiFiPrinter bPrinter,String moneyStr){
        bPrinter.init();
        bPrinter.setCharacterMultiple(1,1);
        bPrinter.printText(moneyStr+" dollar");
        bPrinter.setPrinter(WiFiPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 8);
        bPrinter.cutPaper();
    }
}
