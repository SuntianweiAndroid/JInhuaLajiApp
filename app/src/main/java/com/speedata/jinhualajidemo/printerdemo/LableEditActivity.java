package com.speedata.jinhualajidemo.printerdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android_print_sdk.Barcode;
import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.printerdemo.util.IdUtils;
import com.speedata.jinhualajidemo.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LableEditActivity extends Activity implements View.OnClickListener,View.OnTouchListener{
    private DisplayMetrics dm;
    private int lastX, lastY;
    private RelativeLayout layout;
    private RelativeLayout.LayoutParams params;
    private Button btText,btBarCode,btImage,btLablePrint,btClear,btRotate,btBlowUp,btNarrow;
    private Spinner spPaperType,spPaperMode;
    private List<String> PaperTypelist = new ArrayList<String>();
    private List<String> PaperModelist = new ArrayList<String>();
    private ArrayAdapter<String> adapter,madapter;
    private BluetoothPrinter bPrint = PrinterSDKDemo_Plus_USB.bPrinter;
    private TextView tvtext;
    private int screenWidth;
    private int screenHeight;
    private int fontSize,magnifyNum,magnifyNums,id,boldNum,codeType,width1;;
    private String text,text_str,barcode_str,image_str,text_barcode,imgStr;
    private byte[] textOfByte;
    private StringBuilder str;
    public boolean bold;
    private int item,mitem = 0,textCount,barcodeCount,imageCount;
    private Bitmap bitmap_pic,bitmap_barcode;
    private SQLiteDatabase db;
    private List<Integer> order = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_edit);
//        if (!getDatabasePath("date.db").exists()) {
//            getDatabasePath("date.db").getParentFile().mkdirs();
//        }
//        db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath("date.db").getAbsolutePath(), null);
//        db.execSQL("create table if not exists people (id integer primary key autoincrement,name text,borndate timestamp)");
        initView();
        setListener();
    }
    private void initView() {
        layout = findViewById(R.id.rl_father);
        btText = findViewById(R.id.btText);
        btBarCode = findViewById(R.id.btBarCode);
        btImage = findViewById(R.id.btImage);
//        btRotate = findViewById(R.id.btRotate);
//        btBlowUp = findViewById(R.id.btBlowUp);
//        btNarrow = findViewById(R.id.btNarrow);
        btLablePrint = findViewById(R.id.btLablePrint);
        spPaperType = findViewById(R.id.spPaperType);
        spPaperMode = findViewById(R.id.spPaperMode);
        btClear = findViewById(R.id.btClear);
        PaperTypelist.add("48x80");
        PaperTypelist.add("60x80");

        PaperModelist.add("票据模式");
        PaperModelist.add("标签模式");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PaperTypelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        madapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,PaperModelist);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPaperMode.setAdapter(madapter);
        spPaperMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mitem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPaperType.setAdapter(adapter);
        spPaperType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = position;
                switch (item){
                    case 0:
//                        screenWidth = 450;
                        screenHeight = 392;
                        Log.d("screenHeight","screenHeight:"+screenHeight);
                        layout.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth,392));
                        break;
                    case 1:
//                        screenWidth = 450;
                        screenHeight = 550;
                        Log.d("screenHeight","screenHeight:"+screenHeight);
                        layout.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth,550));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        str = new StringBuilder("");
        dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
//        screenWidth = 568;
//        screenHeight = dm.heightPixels - 165;
//        screenHeight = 400;

    }

    private void setListener() {
        btText.setOnClickListener(this);
        btBarCode.setOnClickListener(this);
        btImage.setOnClickListener(this);
        btLablePrint.setOnClickListener(this);
        btClear.setOnClickListener(this);
    }

    private void addStr(String notes){
        str.append(notes+"\r\n");
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (resultCode){
                case RESULT_OK:
                    Bundle bundle = data.getExtras();
                     text = bundle.getString("text");
                    Log.d("text",""+text);
                     fontSize =Integer.parseInt(bundle.getString("fontSize"));
                    Log.d("fontsize",""+fontSize);
                     bold = bundle.getBoolean("bold");
                    Log.d("bold",""+bold);
                    magnifyNum = Integer.parseInt(bundle.getString("magnifyNum"));
                    Log.d("magnify",""+magnifyNum);
                    tvtext = (TextView) findViewById(id);
                    tvtext.setText(text);
                    if (fontSize>0){
                        if (magnifyNum>0){
                            tvtext.setTextSize(fontSize*magnifyNum);
                        }else {
                            tvtext.setTextSize(fontSize);
                        }
                    }else {
                        tvtext.setTextSize(24);
                    }
                    if (bold){
                        tvtext.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    if (fontSize == 16){
                        fontSize =55;
                    }else if (fontSize == 24){
                        fontSize = 24;
                    }
                    if (magnifyNum == 1){
                        magnifyNum = 0;
                        magnifyNums = 0;
                    }else if(magnifyNum == 2){
                        magnifyNum =11;
                        magnifyNums = 17;
                    }else if(magnifyNum == 3){
                        magnifyNum =22;
                        magnifyNums =34;
                    }else if(magnifyNum == 4){
                        magnifyNum =33;
                        magnifyNums = 51;
                    }else if(magnifyNum == 5){
                        magnifyNum = 44;
                        magnifyNums = 68;
                    }else if(magnifyNum == 6){
                        magnifyNum = 55;
                        magnifyNums = 85;
                    }else if(magnifyNum == 7){
                        magnifyNum = 66;
                        magnifyNums = 102;
                    }else if(magnifyNum == 8){
                        magnifyNum = 77;
                        magnifyNums = 119;
                    }
                    if (bold){
                        boldNum = 1;
                    }else {
                        boldNum = 0;
                    }
                    break;
                case RESULT_FIRST_USER:
                    text_barcode = data.getStringExtra("text_barcode");
                    bitmap_barcode =  data.getParcelableExtra("image_barcode");
                    codeType = data.getExtras().getInt("codeType");
                    Log.d("codeType",""+codeType);
                    ImageView ivImage_barcode = (ImageView) findViewById(id);
                        ivImage_barcode.setImageBitmap(bitmap_barcode);
                    break;
                case RESULT_CANCELED:
                    bitmap_pic =  data.getParcelableExtra("image_pic");
                    imgStr =  bmpstr(bitmap_pic);
                    if(bitmap_pic.getWidth() % 8 == 0) {
                    width1 = bitmap_pic.getWidth() / 8;
                } else {
                    width1 = bitmap_pic.getWidth() / 8 + 1;
                }
                    RelativeLayout ivImage_pic = (RelativeLayout) findViewById(id);
                    ivImage_pic.setBackgroundDrawable(new BitmapDrawable(bitmap_pic));
                    break;
            }
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btText:
                textCount++;
                final TextView textView = new TextView(LableEditActivity.this);
                id = IdUtils.generateViewId();
//                order.set();
//                id = textCount;
                textView.setId(id);
                layout.addView(textView);//将TextView添加到布局中
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = (int) event.getRawX();//获取控件起始位置
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int dx = (int) event.getRawX() - lastX;
                                int dy = (int) event.getRawY() - lastY;
                                int l = v.getLeft() + dx;
                                int b = v.getBottom() + dy;
                                int r = v.getRight() + dx;
                                int t = v.getTop() + dy;
                                if (l < 0) {
                                    l = 0;
                                    r = l + v.getWidth();

                                }
                                if (t < 0) {
                                    t = 0;
                                    b = t + v.getHeight();
                                }
                                if (r > screenWidth) {

                                    r = screenWidth;
                                    l = r - v.getWidth();
                                }
                                if (b > screenHeight) {
                                    b = screenHeight;
                                    t = b - v.getHeight();
                                }
                                v.layout(l, t, r, b);
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                v.postInvalidate();
                                //保存控件当前布局参数
                                params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                params.leftMargin =    l;
                                params.topMargin =    t;
                                textView.setLayoutParams(params);
                                Log.d("ACTION_MOVE","RawX:"+lastX+"     RawY:"+lastY);
                                if (mitem == 0){
                                    if(bold){
                                        boldNum = 8;
                                    }else {
                                        boldNum = 0;
                                    }
                                    textOfByte = new byte[]{(byte)27, (byte)64,(byte)29, (byte)33, (byte)magnifyNums,(byte)27, (byte)33,(byte)boldNum};
                                }else if (mitem ==1){
                                    text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + l + " " + t + " " + text + "\r\n";
//                                    str.append("SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + l + " " + t + " " + text + "\r\n");
//                                    addStr(text_str);
                                }

                                break;
                        }
                        return true;
                    }
                });
                Intent intent = new Intent(LableEditActivity.this,LableTextEditActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.btBarCode:
                barcodeCount++;
                final ImageView imageView_barcode = new ImageView(this);
                id = IdUtils.generateViewId();
//                id = barcodeCount;
                imageView_barcode.setId(id);
                layout.addView(imageView_barcode);//添加到布局中
                imageView_barcode.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = (int) event.getRawX();//获取控件起始位置
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int dx = (int) event.getRawX() - lastX;
                                int dy = (int) event.getRawY() - lastY;
                                int l = v.getLeft() + dx;
                                int b = v.getBottom() + dy;
                                int r = v.getRight() + dx;
                                int t = v.getTop() + dy;
                                if (l < 0) {
                                    l = 0;
                                    r = l + v.getWidth();
                                }
                                if (t < 0) {
                                    t = 0;
                                    b = t + v.getHeight();
                                }
                                if (r > screenWidth) {
                                    r = screenWidth;
                                    l = r - v.getWidth();
                                }
                                if (b > screenHeight) {
                                    b = screenHeight;
                                    t = b - v.getHeight();
                                }
                                v.layout(l, t, r, b);
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                v.postInvalidate();
                                //保存控件当前布局参数
                                params = (RelativeLayout.LayoutParams) imageView_barcode.getLayoutParams();
                                params.leftMargin =    l;
                                params.topMargin =    t;
                                imageView_barcode.setLayoutParams(params);
                                Log.d("ACTION_MOVE","RawX:"+lastX+"     RawY:"+lastY);
                                 if (mitem==1){
                                    if (codeType ==1){
                                        barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + v.getLeft() + " " + v.getTop() + " " + text_barcode + "\r\n";
//                                    str.append("B" + " 128 " + 3 + " " + 1 + " " + 100 + " " + lastX + " " + lastY + " " + text + "\r\n");
//                                    addStr(barcode_str);
                                    }else {
                                        barcode_str = "B" + " QR " + v.getLeft() + " " + v.getTop() + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
//                                    str.append( "B" + " QR " + lastX + " " + lastY + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text + "\r\n" + "ENDQR" + "\r\n");
//                                    addStr(barcode_str);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

                final Intent intent_barcode = new Intent(LableEditActivity.this,LableBarCodeEditActivity.class);
                startActivityForResult(intent_barcode,0);
                break;
            case R.id.btImage:
                imageCount++;
                final RelativeLayout ivImage = new RelativeLayout(LableEditActivity.this);
                id = IdUtils.generateViewId();
//                id = imageCount;
                ivImage.setId(id);
                layout.addView(ivImage);//将TextView添加到布局中
                ivImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = (int) event.getRawX();//获取控件起始位置
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int dx = (int) event.getRawX() - lastX;
                                int dy = (int) event.getRawY() - lastY;
                                int l = v.getLeft() + dx;
                                int b = v.getBottom() + dy;
                                int r = v.getRight() + dx;
                                int t = v.getTop() + dy;
                                if (l < 0) {
                                    l = 0;
                                    r = l + v.getWidth();
                                }
                                if (t < 0) {
                                    t = 0;
                                    b = t + v.getHeight();
                                }
                                if (r > screenWidth) {
                                    r = screenWidth;
                                    l = r - v.getWidth();
                                }
                                if (b > screenHeight) {
                                    b = screenHeight;
                                    t = b - v.getHeight();
                                }
                                v.layout(l, t, r, b);
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                v.postInvalidate();
                                //保存控件当前布局参数
                                params = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
                                params.leftMargin = l;
                                params.topMargin = t;
                                ivImage.setLayoutParams(params);
                                Log.d("ACTION_MOVE", "RawX:" + lastX + "     RawY:" + lastY);
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + lastX + " " + lastY + " " + imgStr+"\r\n";
                                break;
                        }
                        return true;
                    }
                });

                final Intent intent_image = new Intent(LableEditActivity.this, LablePictureEditActivity.class);
                startActivityForResult(intent_image, 0);
                break;

            case R.id.btLablePrint:
                textOfByte = new byte[]{(byte)27, (byte)64,(byte)29, (byte)33, (byte)magnifyNums,(byte)27, (byte)33,(byte)boldNum};
                BluetoothPrinter bluetoothPrinter = PrinterSDKDemo_Plus_USB.bPrinter;
                if (bluetoothPrinter!=null){
//                    str.delete(0,str.length());
//                    String cmd  = str.append(text_str+barcode_str+image_str).toString();
                    if (mitem == 0){
                        if (text != null&&bitmap_barcode != null&&bitmap_pic != null){
                            bluetoothPrinter.printByteData(textOfByte);
                            bluetoothPrinter.printText(text+"\n");
                            if(codeType==1){
                                Barcode barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2,text_barcode);
                                bluetoothPrinter.printBarCode(barcode);
                            }else if(codeType==2){
                                Barcode barcode1 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6,text_barcode);
                                bluetoothPrinter.printBarCode(barcode1);
                            }
                            bluetoothPrinter.printText("\n");
                            bluetoothPrinter.printImage(bitmap_pic);
                            bluetoothPrinter.printText("\n");
                            text = null;
                            bitmap_pic = null;
                            bitmap_barcode = null;
                        }else if (bitmap_barcode != null&&text != null){
                            bluetoothPrinter.printByteData(textOfByte);
                            bluetoothPrinter.printText(text+"\n");
                            bluetoothPrinter.printImage(bitmap_barcode);
                            bluetoothPrinter.printText("\n");
                            bitmap_pic = null;
                        }else if (bitmap_pic !=null&&text != null){
                            bluetoothPrinter.printByteData(textOfByte);
                            bluetoothPrinter.printText(text+"\n");
                            bluetoothPrinter.printImage(bitmap_pic);
                            bluetoothPrinter.printText("\n");
                            bitmap_barcode = null;
                        }else if (bitmap_pic !=null&&bitmap_barcode != null){
                            if(codeType==1){
                                Barcode barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2,text_barcode);
                                bluetoothPrinter.printBarCode(barcode);
                            }else if(codeType==2){
                                Barcode barcode1 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6,text_barcode);
                                bluetoothPrinter.printBarCode(barcode1);
                            }
                            bluetoothPrinter.printText("\n");
                            bluetoothPrinter.printImage(bitmap_pic);
                            bluetoothPrinter.printText("\n");
                            text = null;
                        }else if(text!=null){
                            bluetoothPrinter.printByteData(textOfByte);
                            bluetoothPrinter.printText(text+"\n");
                            bitmap_pic = null;
                            bitmap_barcode = null;
                        }
                        else if(bitmap_pic!=null){
                            bluetoothPrinter.printImage(bitmap_pic);
                            bluetoothPrinter.printText("\n");
                            text = null;
                            bitmap_barcode = null;
                        }
                        else if(bitmap_barcode!=null){
                            if(codeType==1){
                                Barcode barcode = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_CODE128, 2, 162,2,text_barcode);
                                bluetoothPrinter.printBarCode(barcode);
                            }else if(codeType==2){
                                Barcode barcode1 = new Barcode(BluetoothPrinter.BAR_CODE_TYPE_QRCODE, 0, 76,6,text_barcode);
                                bluetoothPrinter.printBarCode(barcode1);
                            }
                            bluetoothPrinter.printText("\n");
                            text = null;
                            bitmap_pic = null;
                        }
                    }else if (mitem == 1){
                        if (text_str!=null&&barcode_str!=null&&image_str!=null){
                            str.append(text_str+"\r\n"+barcode_str+"\r\n"+image_str+"\r\n");
                            bluetoothPrinter.printText("! 0 200 200 " + screenHeight + " 1\r\nPAGE-WIDTH " + 568 + "\r\n"+"PR " + 0 + "\r\nFORM\r\nPRINT\r\n");
                            str.delete(0,str.length());
                            break;
                        }else if (text_str != null&&barcode_str != null){
                            if (bitmap_pic!=null){
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + lastX + " " + lastY + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }
                        }else if (text_str!=null&&image_str!=null){
                            if (bitmap_barcode!=null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }
                        }else if (barcode_str!=null&&image_str!=null){
                            if (text!=null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                            }
                        }else if (text_str!=null){
                            if (bitmap_barcode!=null&&bitmap_pic!=null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }else if (bitmap_barcode!=null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }else if (bitmap_pic!=null){
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }
                        }else if (barcode_str!=null) {
                            if (text != null && bitmap_pic != null) {
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + lastX + " " + lastY + " " + bmpstr(bitmap_pic) + "\r\n";
                            } else if (text != null) {
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                            } else if (bitmap_pic != null) {
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + lastX + " " + lastY + " " + bmpstr(bitmap_pic) + "\r\n";
                            }
                        }else if (image_str!=null){
                            if (text!=null&&bitmap_barcode!=null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }else if (text!=null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                            }else if (bitmap_barcode != null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }
                        }else{
                            if (text != null&&bitmap_barcode != null&&bitmap_pic != null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }else if (bitmap_barcode != null&&text != null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }else if (bitmap_pic !=null&&text != null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }else if (bitmap_pic !=null&&bitmap_barcode != null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                    barcode_str = "B" + " QR " + 20 + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }else if(text!=null){
                                text_str = "SETBOLD " + boldNum + "\r\n " + "TEXT" + " " + fontSize + " " + magnifyNum + " " + 20 + " " + 20 + " " + text + "\r\n";
                            }
                            else if(bitmap_pic!=null){
                                image_str = "EG " + width1 + " " + bitmap_pic.getHeight() + " " + 20 + " " + 20 + " " + bmpstr(bitmap_pic)+ "\r\n";
                            }
                            else if(bitmap_barcode!=null){
                                if (codeType ==1){
                                    barcode_str = "B" + " 128 " + 2 + " " + 1 + " " + 80 + " " + 20 + " " + 20 + " " + text_barcode + "\r\n";
                                }else {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               barcode_str = "B" + " QR " + v.getLeft() + " " + 20 + " " + " M " + " " + 2 + " " + "U" + " " + 6 + " " + "\r\n" + "MA," + text_barcode + "\r\n" + "ENDQR" + "\r\n";
                                }
                            }else{
                                break;
                            }
                        }
                        str.append(text_str+"\r\n"+barcode_str+"\r\n"+image_str+"\r\n");
                        bluetoothPrinter.printText("! 0 200 200 " + screenHeight + " 1\r\nPAGE-WIDTH " + 568 + "\r\n"+str+"PR " + 0 + "\r\nFORM\r\nPRINT\r\n");
                        str.delete(0,str.length());
                    }
                }
                break;
//            case R.id.btRotate:
//                break;
//            case R.id.btBlowUp:
//                break;
//            case R.id.btNarrow:
//                break;
            case R.id.btClear:
                layout.removeAllViews();
                text = null;
                bitmap_barcode = null;
                bitmap_pic = null;
                text_str = null;
                barcode_str = null;
                image_str = null;
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case  0:

            break;
        }
        return false;
    }
    private String bmpstr(Bitmap bmp) {
        String temp = ""+bmp.getHeight();
        String ret = "";

        for(int i = 0; i < bmp.getHeight(); ++i) {
            int k = bmp.getWidth() % 8;

            int tmp;
            int ret1;
            for(tmp = 0; tmp < bmp.getWidth() - k; tmp += 8) {
                ret1 = bmp.getPixel(tmp + 0, i) == -1?0:128;
                int var161 = bmp.getPixel(tmp + 1, i) == -1?0:64;
                int n3 = bmp.getPixel(tmp + 2, i) == -1?0:32;
                int n4 = bmp.getPixel(tmp + 3, i) == -1?0:16;
                int n5 = bmp.getPixel(tmp + 4, i) == -1?0:8;
                int n6 = bmp.getPixel(tmp + 5, i) == -1?0:4;
                int n7 = bmp.getPixel(tmp + 6, i) == -1?0:2;
                int n8 = bmp.getPixel(tmp + 7, i) == -1?0:1;
                int num = ret1 + var161 + n3 + n4 + n5 + n6 + n7 + n8;
                byte ret11 = (byte)num;
                ret = ret + this.int2String(ret11);
            }

            if(k > 0) {
                tmp = 0;

                for(ret1 = 0; ret1 < k; ++ret1) {
                    tmp = (int)((double)tmp + (bmp.getPixel(bmp.getWidth() - ret1 - 1, i) == -1?0.0D:Math.pow(2.0D, (double)(7 - ret1))));
                }

                byte var16 = (byte)tmp;
                ret = ret + this.int2String(var16);
            }
        }

        return ret;
    }

    public String int2String(byte num) {
        byte b1 = (byte)((num & 240) >> 4);
        byte b2 = (byte)(num & 15);
        if(b1 >= 0 && b1 <= 9) {
            b1 = (byte)(b1 | 48);
        }

        if(b1 >= 10 && b1 <= 15) {
            b1 = (byte)(65 + b1 - 10);
        }

        if(b2 >= 0 && b2 <= 9) {
            b2 = (byte)(b2 | 48);
        }

        if(b2 >= 10 && b2 <= 15) {
            b2 = (byte)(65 + b2 - 10);
        }

        byte[] bb = new byte[]{b1, b2};
        String retStr = "";

        try {
            retStr = new String(bb, "GBK");
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

        return retStr;
    }
}
