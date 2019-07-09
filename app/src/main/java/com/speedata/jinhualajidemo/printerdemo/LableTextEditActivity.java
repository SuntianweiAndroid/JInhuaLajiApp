package com.speedata.jinhualajidemo.printerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.speedata.jinhualajidemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LableTextEditActivity extends Activity implements View.OnClickListener{
    private TextView showText,tvfontSize,tvbold,tvmagnify;
    private Button btOk;
    private EditText editText;
    private List<String> list = new ArrayList();
//    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    public String fontSize = "24",magnifyNum = "1",text,item;
    public boolean bold;
    private int mposition,bposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable_text_edit);
        init();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run()
            {
                InputMethodManager inputManager =
                (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);inputManager.showSoftInput(editText, 0);
                }
            },
        998);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = editText.getText().toString()+"";
                showText.setText(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void init(){
        showText =  findViewById(R.id.showText);
        tvfontSize =  findViewById(R.id.fontSize);
        tvbold =  findViewById(R.id.bold);
        tvmagnify =  findViewById(R.id.magnify);
        btOk =  findViewById(R.id.btOk);
        editText =  findViewById(R.id.etText);
        setListener();
    }

    public void setListener(){
        tvfontSize.setOnClickListener(this);
        tvbold.setOnClickListener(this);
        btOk.setOnClickListener(this);
        tvmagnify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.bold:
                    bold = true;
                    tvbold.setTextColor(getResources().getColor(R.color.blue));
                    tvmagnify.setTextColor(getResources().getColor(R.color.black));
                    tvfontSize.setTextColor(getResources().getColor(R.color.black));
                    Spinner spinner = new Spinner(this);
                    //第一步: 填充list
                    list.clear();
                    list.add("是");
                    list.add("否");
                    //第二步：为下拉列表定义一个适配器，用到里前面定义的list。
                    adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
                    //第三步：为适配器设置下拉列表下拉时的菜单样式。
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //第四步：将适配器添加到下拉列表上
                    spinner.setAdapter(adapter);
                    //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
                    spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            bposition = arg2;
                             if(bposition == 0){
                                 bold = true;
                             }else {
                                 bold = false;
                             }
                            /* 将mySpinner 显示*/
                            arg0.setVisibility(View.VISIBLE);
                        }
                        public void onNothingSelected(AdapterView arg0) {
                            // TODO Auto-generated method stub
                            //myTextView.setText("NONE");
                            arg0.setVisibility(View.VISIBLE);
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("加粗").setIcon(android.R.drawable.ic_dialog_info).setView(spinner).setNegativeButton("Cancel",null);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (bposition){
                                case 0:
                                    showText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    break;
                                case 1:
                                    showText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    break;
                            }
                        }
                    });
                    builder.show();

                    break;
                case R.id.magnify:
                    tvbold.setTextColor(getResources().getColor(R.color.black));
                    tvmagnify.setTextColor(getResources().getColor(R.color.blue));
                    tvfontSize.setTextColor(getResources().getColor(R.color.black));
                    Spinner spinner1 = new Spinner(this);
                    //第一步: 填充list
                    list.clear();
                    list.add("1 倍");
                    list.add("2 倍");
                    list.add("3 倍");
                    list.add("4 倍");
                    list.add("5 倍");
                    list.add("6 倍");
                    list.add("7 倍");
                    list.add("8 倍");
                    //第二步：为下拉列表定义一个适配器，用到里前面定义的list。
                    adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
                    //第三步：为适配器设置下拉列表下拉时的菜单样式。
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //第四步：将适配器添加到下拉列表上
                    spinner1.setAdapter(adapter);
                    //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
                    spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            mposition = arg2;
                            /* 将mySpinner 显示*/
                            arg0.setVisibility(View.VISIBLE);
                        }
                        public void onNothingSelected(AdapterView arg0) {
                            // TODO Auto-generated method stub
                            //myTextView.setText("NONE");
                            arg0.setVisibility(View.VISIBLE);
                        }

                    });
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("放大倍数").setIcon(android.R.drawable.ic_dialog_info).setView(spinner1).setNegativeButton("Cancel",null);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (mposition){
                                case 0:
                                    magnifyNum = "1";
                                    break;
                                case 1:
                                    magnifyNum = "2";
                                    break;
                                case 2:
                                    magnifyNum = "3";
                                    break;
                                case 3:
                                    magnifyNum = "4";
                                    break;
                                case 4:
                                    magnifyNum = "5";
                                    break;
                                case 5:
                                    magnifyNum = "6";
                                    break;
                                case 6:
                                    magnifyNum = "7";
                                    break;
                                case 7:
                                    magnifyNum = "8";
                                    break;
                            }
                            if (!magnifyNum.equals("")){
                                if(fontSize.equals("16")){
                                    showText.setTextSize(16*Integer.parseInt(magnifyNum));
                                }else {
                                    showText.setTextSize(24*Integer.parseInt(magnifyNum));
                                }
                            }else {
                                showText.setTextSize(Integer.parseInt(fontSize));
                            }
                        }
                    });
                    builder1.show();
                    break;
                case R.id.fontSize:
                    tvbold.setTextColor(getResources().getColor(R.color.black));
                    tvmagnify.setTextColor(getResources().getColor(R.color.black));
                    tvfontSize.setTextColor(getResources().getColor(R.color.blue));
                    Spinner spinner2 = new Spinner(this);
                    //第一步: 填充list
                    list.clear();
                    list.add("16x16点阵");
                    list.add("24x24点阵");
                    //第二步：为下拉列表定义一个适配器，用到里前面定义的list。
                    adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, list);
                    //第三步：为适配器设置下拉列表下拉时的菜单样式。
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //第四步：将适配器添加到下拉列表上
                    spinner2.setAdapter(adapter);
                    //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
                    spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            item = adapter.getItem(arg2);

                            /* 将mySpinner 显示*/
                            arg0.setVisibility(View.VISIBLE);
                        }
                        public void onNothingSelected(AdapterView arg0) {
                            // TODO Auto-generated method stub
                            //myTextView.setText("NONE");
                            arg0.setVisibility(View.VISIBLE);
                        }

                    });
                    AlertDialog.Builder builders = new AlertDialog.Builder(LableTextEditActivity.this);
                    builders.setTitle("字体大小").setIcon(android.R.drawable.ic_dialog_info).setView(spinner2).setNegativeButton("Cancel",null);
                    builders.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(item.equals("16x16点阵") ){
                                fontSize = "16";
                            }else if(item.equals("24x24点阵")){
                                fontSize = "24";
                            }
                            showText.setTextSize(Integer.parseInt(fontSize));
                        }
                    });
                    builders.show();
                    break;
                case R.id.btOk:
                    Intent intent = new Intent(LableTextEditActivity.this,LableEditActivity.class);
                    intent.putExtra("text",text);
                    intent.putExtra("fontSize",fontSize);
                    intent.putExtra("bold",bold);
                    intent.putExtra("magnifyNum",magnifyNum);
                    setResult(RESULT_OK, intent);
                     finish();
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
