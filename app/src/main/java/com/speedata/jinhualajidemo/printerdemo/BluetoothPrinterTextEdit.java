package com.speedata.jinhualajidemo.printerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.speedata.jinhualajidemo.printerdemo.util.BluetoothPrintUtils;
import com.speedata.jinhualajidemo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class BluetoothPrinterTextEdit extends Activity {
    Button btnOpen;
    Button btnSave;
    Button btnSaveAs;
    Button btnPrint;
    EditText editTxt;
    BluetoothPrinter mPrinter = null;

    public static int flag = 0;
    private String curFilePath = "";

    public static final int OPEN_RESULT_CODE = 1;
    public static final int SAVE_RESULT_CODE = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        btnOpen = findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new ClickEvent());
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new ClickEvent());
        btnSaveAs = findViewById(R.id.btnSaveAs);
        btnSaveAs.setOnClickListener(new ClickEvent());
        btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new ClickEvent());
        editTxt = findViewById(R.id.editTxt);

        mPrinter = PrinterSDKDemo_Plus_USB.bPrinter;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnOpen) {
                Intent intent = new Intent(BluetoothPrinterTextEdit.this,PrinterFileManager.class);
                startActivityForResult(intent, OPEN_RESULT_CODE);
                flag = 0;
            } else if (v == btnSave) {
                if(!editTxt.getText().toString().equals("")){ //只有内容不为空时才保存
                    if(!curFilePath.equals("")){ //如果文件已经存在，直接保存
                        File file = new File(curFilePath);
                        try {
                            OutputStream outstream = new FileOutputStream(file);
                            OutputStreamWriter out = new OutputStreamWriter(outstream);
                            out.write(editTxt.getText().toString());
                            out.close();
                            Toast.makeText(BluetoothPrinterTextEdit.this,getResources().getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{ //如果没有指定文件，则开启fileSeletor选择路径，此时跟SaveAs没区别
                        Intent intent = new Intent(BluetoothPrinterTextEdit.this,PrinterFileManager.class);
                        startActivityForResult(intent, SAVE_RESULT_CODE);
                        flag = 1;
                    }
                }
                else //为空时提示
                    Toast.makeText(BluetoothPrinterTextEdit.this, getResources().getString(R.string.toast_save_null), Toast.LENGTH_SHORT).show();
            } else if (v == btnSaveAs) {
                if(!editTxt.getText().toString().equals("")){
                    Intent intent = new Intent(BluetoothPrinterTextEdit.this,PrinterFileManager.class);
                    startActivityForResult(intent, SAVE_RESULT_CODE);
                    flag = 1;
                }
                else
                    Toast.makeText(BluetoothPrinterTextEdit.this, getResources().getString(R.string.toast_save_null), Toast.LENGTH_SHORT).show();
            } else if (v == btnPrint) {
                String mystr = editTxt.getText().toString();
                if(!mystr.equals("")){
                    //System.out.println("++++++++++++++ mystr: " + mystr);
                    BluetoothPrintUtils.printText(mPrinter, mystr+"\n\n");
                }
                else{
                    Toast.makeText(BluetoothPrinterTextEdit.this, getResources().getString(R.string.toast_save_null), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle = null;

        if(OPEN_RESULT_CODE == requestCode){
            if(data!=null && (bundle=data.getExtras())!=null){
                String filePath = bundle.getString("file");
                String content = "";
                //打开文件
                File file = new File(filePath);
                if (!file.isDirectory()){{
                    try {
                        InputStream instream = new FileInputStream(file);
                        if (instream != null) {
                            InputStreamReader inputreader = new InputStreamReader(instream);
                            BufferedReader buffreader = new BufferedReader(inputreader);
                            String line;
                            while (( line = buffreader.readLine()) != null) {
                                content += line + "\n";
                            }
                            editTxt.setText(content);
                            this.setTitle(filePath); //更新title为文件的路径
                            curFilePath = filePath;
                        }
                        instream.close();
                    } catch (java.io.FileNotFoundException e) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
            if(SAVE_RESULT_CODE == requestCode){
                if(data!=null && (bundle=data.getExtras())!=null){
                    String filePath = bundle.getString("file");
                    //没有指定的文件名没有后缀，则自动保存为.txt格式
                    if(!filePath.endsWith(".txt") && !filePath.endsWith(".log"))
                        filePath += ".txt";
                    //保存文件
                    File file = new File(filePath);
                    try {
                        OutputStream outstream = new FileOutputStream(file);
                        OutputStreamWriter out = new OutputStreamWriter(outstream);
                        out.write(editTxt.getText().toString());
                        out.close();
                        this.setTitle(filePath); //更新title为文件的路径
                        curFilePath = filePath;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}