package com.speedata.jinhualajidemo.printerdemo.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.Table;
import com.android_print_sdk.usb.USBPrinter;
import com.speedata.jinhualajidemo.R;

public class UsbPrintUtils {

    public static void printNote(Resources resources, USBPrinter mPrinter) {
        mPrinter.init();
        StringBuffer sb = new StringBuffer();
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN, USBPrinter.COMM_ALIGN_CENTER);
        //字符橫向纵向放大一倍
        mPrinter.setCharacterMultiple(0, 1);
        mPrinter.printText(resources.getString(R.string.welcome)+"\n");
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_58 ) {
            mPrinter.printText("------------------------------\n");
        }else if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            mPrinter.printText("----------------------------------------------\n");
        }
        mPrinter.setCharacterMultiple(0, 0);
        mPrinter.printText(resources.getString(R.string.shopping_name)+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN, USBPrinter.COMM_ALIGN_LEFT);

        sb.append(resources.getString(R.string.text_one)+"\n");
        sb.append(resources.getString(R.string.text_two)+"\n");
        //sb.append(resources.getString(R.string.longline)+"\n");
        sb.append(resources.getString(R.string.text_three)+"\n");
        sb.append(resources.getString(R.string.text_four)+"\n");
        sb.append(resources.getString(R.string.text_five)+"\n");
        sb.append(resources.getString(R.string.text_six)+"\n");
        //sb.append(resources.getString(R.string.longline)+"\n");
        sb.append(resources.getString(R.string.text_seven)+"\n");
        sb.append(resources.getString(R.string.text_eight)+"\n");
        //sb.append(resources.getString(R.string.longline)+"\n");
        sb.append(resources.getString(R.string.text_nine)+"\n");
        sb.append(resources.getString(R.string.text_ten)+"\n");
        sb.append(resources.getString(R.string.text_eleven)+"\n");
        sb.append(resources.getString(R.string.text_twelve)+"\n");
        sb.append(resources.getString(R.string.text_thirteen)+"\n");
        sb.append(resources.getString(R.string.text_fourteen)+"\n");
        sb.append(resources.getString(R.string.text_fiveteen)+"\n");
        mPrinter.printText(sb.toString());
        mPrinter.setCharacterMultiple(0, 1);
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_58) {
            mPrinter.printText("------------------------------\n");
        }else if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80){
            mPrinter.printText("----------------------------------------------\n");
        }
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        //打印换行
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
        //切纸
        mPrinter.cutPaper();
    }

    public static void printImage(Resources resources, USBPrinter mPrinter, boolean is_thermal) {
        mPrinter.init();
        BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = true;
        Bitmap bitmap = BitmapFactory.decodeResource(resources,R.mipmap.apple,bfoOptions);
        Matrix matrix = new Matrix();
        //matrix.setScale(X轴缩放,Y轴缩放，，);//后面两个参数是相对于缩放的位置放置，尝试设置，建议数值>100以上进行设置
        matrix.setScale(3f,3f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        if (is_thermal) {
            //单色图片
            mPrinter.printImage(resizeBmp);
            // 打印水印;
            //Bitmap a = BitmapFactory.decodeResource(resources,R.drawable.logob);
            //mPrinter.printImage2(a);
        } else {
            mPrinter.printImageDot(bitmap, 1, 0);
        }
        //走两行纸
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        //打印换行
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
        //切纸
        mPrinter.cutPaper();
    }

    public static void printTable(Resources resources, USBPrinter mPrinter) {
        mPrinter.init();
        // TODO Auto-generated method stub
        mPrinter.setCharacterMultiple(0, 0);
        String column =resources.getString(R.string.note_title);
        Table table;
        if (mPrinter.getCurrentPrintType() == PrinterType.Printer_80) {
            table = new Table(column, ";", new int[]{18, 8, 8, 8});
        } else {
            table = new Table(column, ";", new int[]{12, 6, 6, 6});
        }
        table.add("1,"+resources.getString(R.string.coffee)+";	    2.00;    5.00;   10.00");
        table.add("2,"+resources.getString(R.string.tableware)+";   2.00;   5.00;    10.00");
        table.add("3,"+resources.getString(R.string.peanuts)+";   1.00;   68.00;   68.00");
        table.add("4,"+resources.getString(R.string.cucumber)+";   1.00;   4.00;    4.00");
        table.add("5,"+resources.getString(R.string.frog)+"; 1.00;   5.00;    5.00");
        table.add("6,"+resources.getString(R.string.rice)+";	    1.00;   2.00;    2.00");
        mPrinter.printTable(table);
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        //打印换行
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
        //切纸
        mPrinter.cutPaper();
        // 蜂鸣器
        // mPrinter.ringBuzzer((byte) 0x20);
        // 开钱箱
        //  mPrinter.openCashbox(true, true);
    }

    public static void printText(USBPrinter mPrinter, String content) {
        mPrinter.init();
        //打印走纸
        mPrinter.setPrinter(USBPrinter.COMM_LINE_HEIGHT, 80);
        //打印文本
        mPrinter.printText(content);
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 4);
        //打印换行
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
        //切纸
        mPrinter.cutPaper();
    }

    public static void printBarCode(USBPrinter mPrinter, String codeNum, String type) {
        if (type.equals("全部")) {
            for (int i = 0; i < 10; i++) {
                mPrinter.init();
                // TODO Auto-generated method stub
                Log.i("12345", "xxxxxxxxxxxxxxxxxxx");
                mPrinter.setCharacterMultiple(0, 0);
                mPrinter.setLeftMargin(15, 0);
                switch (i) {
                    case 0:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_UPC_A);
                        codeNum = "123456789012";
                        break;
                    case 1:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_JAN13);
                        codeNum = "123456789012";
                        break;
                    case 2:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_JAN8);
                        codeNum = "1234567";
                        break;
                    case 3:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_CODE39);
                        codeNum = "123456";
                        break;
                    case 4:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_ITF);
                        codeNum = "123456";
                        break;
                    case 5:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_CODE93);
                        codeNum = "123456";
                        break;
                    case 6:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_CODE128);
                        codeNum = "No.123456";
                        break;
                    case 7:
                        mPrinter.setBarCode(2, 3, 6, USBPrinter.BAR_CODE_TYPE_PDF417);
                        codeNum = "123,45,67,89,01";
                        break;
                    case 8:
                        mPrinter.setBarCode(2, 3, 6, USBPrinter.BAR_CODE_TYPE_DATAMATRIX);
                        codeNum = "123,45,67,89,01";
                        break;
                    case 9:
                        mPrinter.setBarCode(2, 3, 6, USBPrinter.BAR_CODE_TYPE_QRCODE);
                        codeNum = "123,45,67,89,01";
                        break;
                    case 10:
                        mPrinter.setBarCode(2, 150, 2, USBPrinter.BAR_CODE_TYPE_CODABAR);
                        codeNum = "123456";
                        break;
                }
                mPrinter.printBarCode(codeNum);
                mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
                //打印换行
                mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
                //切纸
                mPrinter.cutPaper();
                Log.i("codeNom++++", codeNum);
            }
        } else {
            mPrinter.init();
            // TODO Auto-generated method stub
            Log.i("12345", "xxxxxxxxxxxxxxxxxxx");
            mPrinter.setCharacterMultiple(0, 0);
            mPrinter.setLeftMargin(15, 0);
            //mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_LEFT);

            /**
             * ����1: ������������� 2<=n<=6,Ĭ��Ϊ2
             * ����2: ��������߶� 1<=n<=255,Ĭ��162
             * ����3: ��������ע�ʹ�ӡλ��.0����ӡ,1�Ϸ�,2�·�,3���·�����,Ĭ��Ϊ0
             * ����4: ������������.USBPrinter.BAR_CODE_TYPE_ ��ͷ�ĳ���,Ĭ��ΪCODE128
             */
            byte codeType = USBPrinter.BAR_CODE_TYPE_CODE128;
            if (type.equals("UPC_A")) {
                codeType = USBPrinter.BAR_CODE_TYPE_UPC_A;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("UPC_E")) {
                codeType = USBPrinter.BAR_CODE_TYPE_UPC_E;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("JAN13(EAN13)")) {
                codeType = USBPrinter.BAR_CODE_TYPE_JAN13;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("JAN8(EAN8)")) {
                codeType = USBPrinter.BAR_CODE_TYPE_JAN8;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("CODE39")) {
                codeType = USBPrinter.BAR_CODE_TYPE_CODE39;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("ITF")) {
                codeType = USBPrinter.BAR_CODE_TYPE_ITF;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("CODEBAR")) {
                codeType = USBPrinter.BAR_CODE_TYPE_CODABAR;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("CODE93")) {
                codeType = USBPrinter.BAR_CODE_TYPE_CODE93;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("CODE128")) {
                codeType = USBPrinter.BAR_CODE_TYPE_CODE128;
                mPrinter.setBarCode(2, 150, 2, codeType);
            } else if (type.equals("PDF417")) {
                codeType = USBPrinter.BAR_CODE_TYPE_PDF417;
                mPrinter.setBarCode(2, 3, 6, codeType);
            } else if (type.equals("DATAMATRIX")) {
                codeType = USBPrinter.BAR_CODE_TYPE_DATAMATRIX;
                mPrinter.setBarCode(2, 3, 6, codeType);
            } else if (type.equals("QRCODE")) {
                codeType = USBPrinter.BAR_CODE_TYPE_QRCODE;
                mPrinter.setBarCode(2, 3, 6, codeType);
            }
            mPrinter.printBarCode(codeNum);
            mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            //打印换行
            mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_NEWLINE);
            //切纸
            mPrinter.cutPaper();
        }
    }
    public static void lablePrint(Resources resources,USBPrinter iPrinter) {
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
        iPrinter.setPage(576, 400);
        iPrinter.drawText(0,24,0,12,12,"打印测试文字");
        iPrinter.labelPrint(0,1);
    }
    public static void printLable(Resources resources,USBPrinter iPrinter) {
        //设置纸长宽
        Bitmap a = BitmapFactory.decodeResource(resources,R.mipmap.logob);
        iPrinter.setPage(568, 1650);
        iPrinter.drawGraphic(10,10,0,0,a);
        //第一个表格线
        //drawLine( 线宽，起始X，起始Y，结尾X，结尾Y);
        iPrinter.drawLine(-1, 12, 45, 568, 45);
        iPrinter.drawLine(0, 12, 45, 12, 648);
        iPrinter.drawLine(0, 568, 45, 568, 648);

        iPrinter.drawLine(-1, 12, 120, 568, 120);
        iPrinter.drawLine(-1, 12, 280, 568, 280);
        iPrinter.drawLine(-1, 12, 360, 568, 360);
        iPrinter.drawLine(-1, 12, 424, 568, 424);
        iPrinter.drawLine(-1, 12, 544, 568, 544);
        iPrinter.drawLine(-1, 12, 648, 568, 648);
        //
        iPrinter.drawLine(-1, 12, 824, 568, 824);
        iPrinter.drawLine(0, 284, 360, 284, 424);
        iPrinter.drawLine(0, 56, 424, 56, 648);
        //
        iPrinter.drawLine(0, 284, 648, 284, 824);
        iPrinter.drawLine(-1, 12, 936, 568, 936);

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

        iPrinter.labelPrint(0, 1);
    }
    public static void printNotes(Resources resources,USBPrinter mPrinter){
        mPrinter.init();
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN, USBPrinter.COMM_ALIGN_CENTER);
        mPrinter.setCharacterMultiple(0,0);
        mPrinter.setLeftMargin(0,0);
        mPrinter.printText("XX市公安局交通巡逻警察支队"+"\n");
        mPrinter.setCharacterMultiple(0,1);
        mPrinter.printText("公安交通管理简易程序处罚决定书"+"\n");
        mPrinter.setCharacterMultiple(0,0);
        mPrinter.printText("决定书编号:4101051700000012"+"\n");
        mPrinter.setBarCode(2, 150, 0, USBPrinter.BAR_CODE_TYPE_CODE128);
        mPrinter.printBarCode("No.123456");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN, USBPrinter.COMM_ALIGN_LEFT);
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
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_RIGHT);
        mPrinter.printText("2012年05月25日"+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_LEFT);
        mPrinter.printText("被处罚人签名:________"+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_RIGHT);
        mPrinter.printText("年    月    日"+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_LEFT);
        mPrinter.printText("备注:________"+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_ALIGN,USBPrinter.COMM_ALIGN_CENTER);
        mPrinter.printText("根据《机动车驾驶证申领和使用规定》记3分"+"\n");
        mPrinter.printText("------------------------"+"\n");
        mPrinter.printText("第一联:送达被处罚人"+"\n");
        mPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 2);
        //切纸
        mPrinter.cutPaper();
        // 找缝隙
        //  mPrinter.printByteData(new byte[]{0x0c});
    }

    public static void printAlpay(USBPrinter bPrinter,String moneyStr){
        bPrinter.init();
        bPrinter.setCharacterMultiple(1,1);
        bPrinter.printText(moneyStr+" dollar");
        bPrinter.setPrinter(USBPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 8);
        bPrinter.cutPaper();
    }
}
