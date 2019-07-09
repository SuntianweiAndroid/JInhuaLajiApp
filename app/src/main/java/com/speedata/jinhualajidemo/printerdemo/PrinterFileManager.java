package com.speedata.jinhualajidemo.printerdemo;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.speedata.jinhualajidemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrinterFileManager extends ListActivity {
    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = "/sdcard";
    private String filePath = "";
    private EditText mPath;

    //private View ov = null; //old view

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filemanager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mPath = findViewById(R.id.mPath);
        mPath.setEnabled(false);

        Button buttonConfirm =  findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                Intent data = new Intent(SPRTFileManager.this, SPRTTextEdit.class);
//                Bundle bundle = new Bundle();
//                filePath = mPath.getText().toString();
//                bundle.putString("file", filePath);
//                data.putExtras(bundle);
//                finish();

            	myDialog("");
            }
        });

        if(UsbPrinterTextEdit.flag == 0){
        	buttonConfirm.setEnabled(false);
        }


        Button buttonCancle = findViewById(R.id.buttonCancle);
        buttonCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        getFileDir(rootPath);
    }
    private void getFileDir(String path) {
        mPath.setText(path);
        items = new ArrayList();
        paths = new ArrayList();
        File f = new File(path);
        File[] files = f.listFiles();
        if (!path.equals(rootPath)) {
            items.add("ROOTDIR");
            paths.add(rootPath);
            items.add("PREVDIR");
            paths.add(f.getParent());
        }
        if(files == null){
        	return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory() && !file.getName().startsWith(".")){
            	items.add(file.getName());
            	paths.add(file.getPath());
            }
            else{
            	String[] subnames = file.getName().split("\\.");
            	if (subnames.length > 1){
            		if(subnames[subnames.length-1].equals("txt") || subnames[subnames.length-1].equals("log")||subnames[subnames.length-1].equals("pdf")){
            			items.add(file.getName());
            			paths.add(file.getPath());
            		}
            	}
            }
        }
        setListAdapter(new PrinterAdapter(this, items, paths));
//      }catch(ArrayIndexOutOfBoundsException e1){
//        	  System.out.println(e1);
//        	}catch(IndexOutOfBoundsException e2){
//        		System.out.println(e2);
//        	}
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	//super.onListItemClick(l, v, position, id);
        File file = new File(paths.get(position));
        if (file.isDirectory()) {
            getFileDir(paths.get(position));

        } else {
        	//mPath.setText(paths.get(position));
        	if(UsbPrinterTextEdit.flag == 0){
              Intent data = new Intent(PrinterFileManager.this, UsbPrinterTextEdit.class);
              Bundle bundle = new Bundle();
              bundle.putString("file", paths.get(position));
              data.putExtras(bundle);
              setResult(2, data);
              finish();
        	}
        	else{
        		String[] subn = paths.get(position).split("/");
        		String fn = subn[subn.length-1];
        		myDialog(fn);
        	}
        }

//        if(ov == null){
//    		v.setBackgroundColor(Color.BLUE);
//    		ov = v;
//    	}
//        else{
//    		ov.setBackgroundColor(Color.WHITE);
//    		v.setBackgroundColor(Color.BLUE);
//    		ov = v;
//    	}

//        if(ov != null){
//        	ov.setSelected(false);
//        	ov.setBackgroundColor(Color.WHITE);
//        }
//        v.setSelected(true);
//        v.setBackgroundColor(Color.BLUE);
//        ov = v;
	}

    public void myDialog(String n) {
    	Builder dialog = new Builder(this);
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog, null);
    	dialog.setView(layout);
    	final EditText nameTxt = layout.findViewById(R.id.name);
    	if(n != ""){
    		nameTxt.setText(n);
    	}
    	dialog.setPositiveButton(R.string.save_ok, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
	    		String namestr = nameTxt.getText().toString();
	    		if(!namestr.equals("")){
					filePath = mPath.getText() + "/" + namestr;
					Intent data = new Intent(PrinterFileManager.this, UsbPrinterTextEdit.class);
					Bundle bundle = new Bundle();
					bundle.putString("file", filePath);
					data.putExtras(bundle);
					setResult(2, data);
					finish();
	    		}
	    		else
	    			Toast.makeText(PrinterFileManager.this, R.string.toast_input_name, Toast.LENGTH_SHORT).show();
//    	     Intent intent = new Intent();
//    	     Bundle bundle = new Bundle();
//    	     bundle.putString("search", searchC);
//    	     intent.putExtras(bundle);
//    	     intent.setClass(ViewResultActivity.this, SearchResult.class);
//    	     ViewResultActivity.this.startActivity(intent);
    		}
    	});
    	dialog.setNegativeButton(R.string.save_cancel, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			//finish();
    		}
    	});
    	dialog.show();
	}
}