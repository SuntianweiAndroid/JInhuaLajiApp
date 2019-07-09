package com.speedata.jinhualajidemo.printerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.speedata.jinhualajidemo.printerdemo.zxing.encoding.EncodingHandler;
import com.speedata.jinhualajidemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LableBarCodeEditActivity extends Activity {
    private ImageView iv_Barcode;
    private Spinner spinner;
    private EditText et_Barcode;
    private Button bt_ok;
    private TextView tvBarcode;
    private List<String> Barcodelist = new ArrayList();
    private ArrayAdapter<String> adapter;
    private Bitmap barCodeBitmap;
    private String contentString;
    private int item;
    private int codeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_bar_code_edit);
        init();
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)et_Barcode.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);inputManager.showSoftInput(et_Barcode, 0);
                           }
                       },
                998);
        Barcodelist.add("扫描二维码");
        Barcodelist.add("CODE_128");
        Barcodelist.add("QR_CODE");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Barcodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = position;
//                if (contentString!=null&&!contentString.equals("")){
                    switch (item) {
                        case 0:
                            Intent intent = new Intent();
                            intent.setClass(LableBarCodeEditActivity.this, MipcaActivityCapture.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, 1);
                            break;
                        case 1:
                            codeType = 1;
                            contentString = "No.123456";
                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 300, 100, BarcodeFormat.CODE_128, true);
                            break;
                        case 2:
                            codeType = 2;
                            contentString = "123456789012";
                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 350, BarcodeFormat.QR_CODE);
                            break;
//                        case 3:
//                            contentString = "123456789012";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.UPC_A, true);
//                            break;
//                        case 4:
//                            contentString = "1234567";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.EAN_8, true);
//                            break;
//                        case 5:
//                            contentString = "123456789012";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.EAN_13, true);
//                            break;
//                        case 6:
//                            contentString = "123456";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODE_39, true);
//                            break;
//                        case 7:
//                            contentString = "123456";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODE_93, true);
//                            break;
//                        case 8:
//                            contentString = "123456";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.ITF, true);
//                            break;
//                        case 9:
//                            contentString = "123456";
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODABAR, true);
//                            break;
//                        case 10:
//                            contentString = "123456789012";
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.PDF417);
//                            break;
//                        case 11:
//                            contentString = "123456789012";
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.DATA_MATRIX);
//                            break;
//                        case 12:
//                            contentString = "123456789012";
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.QR_CODE);
//                            break;
                    }
                    et_Barcode.setText(contentString);
                    //根据字符串生成条码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    if(barCodeBitmap!=null){
                        iv_Barcode.setImageBitmap(barCodeBitmap);
                    }
                    else {
                        Toast.makeText(LableBarCodeEditActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
                    //根据字符串生成条码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    if(barCodeBitmap!=null){
                        iv_Barcode.setImageBitmap(barCodeBitmap);
                    }
                    else {
                        Toast.makeText(LableBarCodeEditActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
//                }
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LableBarCodeEditActivity.this,LableEditActivity.class);
                intent.putExtra("image_barcode",barCodeBitmap);
                intent.putExtra("codeType",codeType);
                intent.putExtra("text_barcode",contentString);
                setResult(RESULT_FIRST_USER,intent);
                finish();
            }
        });
        et_Barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contentString = et_Barcode.getText().toString();
                if (!contentString.equals("")) {
                    switch (item) {
                        case 0:
                            Intent intent = new Intent();
                            intent.setClass(LableBarCodeEditActivity.this, MipcaActivityCapture.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, 1);
                            break;
                        case 1:
                            codeType = 1;
                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 144, 80, BarcodeFormat.CODE_128, false);
                            break;
                        case 2:
                            codeType = 2;
                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 200,BarcodeFormat.QR_CODE);
                            break;
//                        case 3:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.UPC_A, false);
//                            break;
//                        case 4:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.EAN_8, false);
//                            break;
//                        case 5:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.EAN_13, false);
//                            break;
//                        case 6:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODE_39, false);
//                            break;
//                        case 7:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODE_93, false);
//                            break;
//                        case 8:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.ITF, false);
//                            break;
//                        case 9:
//                            barCodeBitmap = EncodingHandler.creatBarcode(LableBarCodeEditActivity.this, contentString, 100, 50, BarcodeFormat.CODABAR, false);
//                            break;
//                        case 10:
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.PDF417);
//                            break;
//                        case 11:
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.DATA_MATRIX);
//                            break;
//                        case 12:
//                            barCodeBitmap = EncodingHandler.createQRCode(contentString, 150, BarcodeFormat.QR_CODE);
//                            break;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             }
                    //根据字符串生成条码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    if(barCodeBitmap!=null){
                        iv_Barcode.setImageBitmap(barCodeBitmap);
                    }
                    else {
                        Toast.makeText(LableBarCodeEditActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
                    //根据字符串生成条码图片并显示在界面上，第二个参数为图片的大小（350*350）
                    if(barCodeBitmap!=null){
                        iv_Barcode.setImageBitmap(barCodeBitmap);
                    }
                    else {
                        Toast.makeText(LableBarCodeEditActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LableBarCodeEditActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(){
        iv_Barcode = findViewById(R.id.iv_barCode);
        spinner = findViewById(R.id.sp_barCode);
        et_Barcode = findViewById(R.id.et_barCode);
        bt_ok = findViewById(R.id.bt_barCodeOk);
        tvBarcode = findViewById(R.id.tvBarcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    tvBarcode.setText(bundle.getString("result"));
//                    barCodeBitmap  = (Bitmap) data.getParcelableExtra("bitmap");
                    //显示
                    iv_Barcode.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
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
