package com.speedata.jinhualajidemo.printerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android_print_sdk.bluetooth.BluetoothPrinter;
import com.android_print_sdk.pdfdocument.CodeDocument;
import com.android_print_sdk.pdfdocument.CodePage;
import com.android_print_sdk.usb.USBPrinter;
import com.android_print_sdk.wifi.WiFiPrinter;
import com.speedata.jinhualajidemo.printerdemo.global.GlobalContants;
import com.speedata.jinhualajidemo.printerdemo.util.PrefUtils;
import com.speedata.jinhualajidemo.R;

import org.vudroid.pdfdroid.codec.PdfContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class  PdfPrintActivity extends Activity implements OnClickListener {
	private Button btnPrintPdf;
	private Button btnPrintthis;
	private Button btnGetPdf;
	private Button btnDefaultPdf;
	private Button btnPrevious;
	private Button btnNext;
	private ImageView ivShowPdf;
	private TextView tvShowPage;
	private LinearLayout header;
	private BluetoothPrinter bPrinter = null;
	private USBPrinter usbPrinter = null;
	private WiFiPrinter wiFiPrinter = null;

	private int pageCount = 0;
	private PdfContext pdf_conext;
	private CodePage vuPage;
	private RectF rf;
	private Bitmap bitmap;
	private float screen_width = 0;
	private float screen_height = 0;
	private CodeDocument d;
	private String TAG = "com.printer.demo.ui";
	private int paperWidth = 58;
	private boolean is_58mm ;
	private int type;
	private String filePath;
	private boolean isGetFile = false;
	private static final int GET_PDF =1;
	private static final int DEFAULT_PDF =2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_print);
		Intent intent = getIntent();
		type = intent.getIntExtra("type",1);
		wiFiPrinter = PrinterSDKDemo_Plus_USB.wiFiPrinter;
		usbPrinter = PrinterSDKDemo_Plus_USB.mPrinter;
		bPrinter = PrinterSDKDemo_Plus_USB.bPrinter;

		ivShowPdf =  findViewById(R.id.iv_showpdf);
		tvShowPage =  findViewById(R.id.tv_showpage);

		btnPrintPdf =  findViewById(R.id.btn_printpdf);
		btnGetPdf =  findViewById(R.id.btn_getpdf);
		btnDefaultPdf =  findViewById(R.id.btn_default);
		btnPrevious =  findViewById(R.id.btn_pre);
		btnNext =  findViewById(R.id.btn_next);
		btnPrintthis =  findViewById(R.id.btn_printthis);
		setBtnState();

		btnPrintPdf.setOnClickListener(this);
		btnGetPdf.setOnClickListener(this);
		btnDefaultPdf.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrintthis.setOnClickListener(this);

		//header =  findViewById(R.id.ll_headerview_Pdf_Printactivity);
		paperWidth = PrefUtils.getInt(PdfPrintActivity.this,
				GlobalContants.PAPERWIDTH, 80);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		is_58mm=PrinterSDKDemo_Plus_USB.is_58mm;
		if (is_58mm){
			paperWidth =58;
		}else{
			paperWidth =80;
		}
		switch (paperWidth) {
		case 58:
			screen_width = 386;
			screen_height = 500;
			break;
		case 80:
			screen_width = 576;
			screen_height = 820;
			break;
		case 100:
			screen_width = 724;
			screen_height = 1000;
			break;
		default:
			break;
		}
	}

	private void setBtnState(){
		btnNext.setEnabled(isGetFile);
		btnPrintPdf.setEnabled(isGetFile);
		btnPrevious.setEnabled(isGetFile);
		btnPrintthis.setEnabled(isGetFile);
	}

	/**
	 * 从assets目录中复制整个文件夹内容
	 *
	 * @param context
	 *            Context 使用CopyFiles类的Activity
	 * @param oldPath
	 *            String 原文件路径 如：/aa
	 * @param newPath
	 *            String 复制后路径 如：xx:/bb/cc
	 */
	private void copyFilesFromassets(Context context, String oldPath,
									 String newPath) {
		try {
			String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
			if (fileNames.length > 0) {// 如果是目录
				File file = new File(newPath);
				file.mkdirs();// 如果文件夹不存在，则递归
				for (String fileName : fileNames) {
					copyFilesFromassets(context, oldPath + "/" + fileName,
							newPath + "/" + fileName);
				}
			} else {// 如果是文件
				InputStream is = context.getAssets().open(oldPath);
				File file = new File (newPath);
				if(!file.exists())
				{
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
																// buffer字节
					fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
				}
				fos.flush();// 刷新缓冲区
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 如果捕捉到错误则通知UI线程
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if (GlobalContants.ISCONNECTED) {
//			if ("".equals(GlobalContants.DEVICENAME)
//					|| GlobalContants.DEVICENAME == null) {
//				headerConnecedState.setText(R.string.unknown_device);
//
//			} else {
//
//				headerConnecedState.setText(GlobalContants.DEVICENAME);
//			}
//
//		}
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_printpdf:
				// // 打印整份pdf
				if (!PrinterSDKDemo_Plus_USB.isConnected) {
					Toast.makeText(PdfPrintActivity.this, "打印机未连接",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("fdh", "pdf文件共有 " + d.getPageCount() + "页");
					for (int i = 0; i < d.getPageCount(); i++) {
						vuPage = d.getPage(i);
						rf = new RectF();
						rf.top = 0;
						rf.left = 0;
						rf.bottom = (float) 1.0;
						rf.right = (float) 1.0;

						// 参数一二为生成图片的宽高,参数三Rect的top,left,bottom,right为截取部分在该页中的百分比位置
						// 例如top=0.5,left=0.5,right=1.0,1.0则截取右下角四分之一的部分
						bitmap = vuPage.renderBitmap((int) screen_width,
								(int) screen_height, rf);
						// Log.i("fdh",
						// "图片宽： " + bitmap.getWidth() + "图片的高： "
						// + bitmap.getHeight());
						// PrinterInstance.mPrinter.printImage(bitmap);
						new InnerAsyncTask().execute();
					}
				}
				break;
			case R.id.btn_printthis:
				// 打印当前页码的pdf
				if (bPrinter == null && !PrinterSDKDemo_Plus_USB.isConnected) {
					Toast.makeText(PdfPrintActivity.this, "打印机未连接",
							Toast.LENGTH_SHORT).show();
				} else {
					if (pageCount >= d.getPageCount()) {
						pageCount = d.getPageCount() - 1;
					}
					if (pageCount <= 0) {
						pageCount = 0;
					}
					vuPage = d.getPage(pageCount);
					rf = new RectF();
					rf.top = 0;
					rf.left = 0;
					rf.bottom = (float) 1.0;
					rf.right = (float) 1.0;
					Log.v("TAG:screen_width", "" + screen_width);
					Log.v("TAG:screen_height", "" + screen_height);
					bitmap = vuPage.renderBitmap((int) screen_width,
							(int) screen_height, rf);
					new InnerAsyncTask().execute();
				}
				break;
			case R.id.btn_pre:
				// 显示上一页pdf
				pageCount--;
				if (pageCount <= 0) {
					pageCount = 0;
					showImage(pageCount);
					tvShowPage.setText("1/" + d.getPageCount());

				} else {
					showImage(pageCount);
					tvShowPage.setText(pageCount + 1 + "/" + d.getPageCount());
				}
				break;
			case R.id.btn_next:
				// 显示下页pdf
				pageCount++;
				Log.e(TAG, "当前的count值" + pageCount);
				if (pageCount >= d.getPageCount()) {
					Log.e(TAG, "count（" + pageCount + "）值大于或者等于" + d.getPageCount());
					pageCount = d.getPageCount();
					showImage(pageCount - 1);
					tvShowPage.setText(d.getPageCount() + "/" + d.getPageCount());
				} else {
					showImage(pageCount);
					tvShowPage.setText(pageCount + 1 + "/" + d.getPageCount());
				}
				break;
			case R.id.btn_getpdf:
				showContacts();
				break;
			case R.id.btn_default:
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED
						|| ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getApplicationContext(),"没有权限,请手动开启读写权限",Toast.LENGTH_SHORT).show();
					// 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
					ActivityCompat.requestPermissions(PdfPrintActivity.this,new String[]{
							Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
					}, DEFAULT_PDF);
				}else{
					copyFilesFromassets(this, "test2.pdf", Environment.getExternalStorageDirectory().getPath()+"/Download/test3.pdf");
					pdf_conext = new PdfContext();
					d = pdf_conext.openDocument(Environment.getExternalStorageDirectory().getPath()+"/Download/test3.pdf");
					// 默认显示第一页
					showImage(pageCount);
					tvShowPage.setText(pageCount + 1 + "/" + d.getPageCount());
					isGetFile = true;
					setBtnState();
				}
				break;
		}
	}

	private void showImage(int count) {
		RectF rf = new RectF();
		rf.top = 0;
		rf.left = 0;
		rf.bottom = (float) 1.0;
		rf.right = (float) 1.0;
		pdf_conext = new PdfContext();
		vuPage = d.getPage(count);
		Bitmap bitmap = vuPage.renderBitmap( 576,  820, rf);
		ivShowPdf.setImageBitmap(bitmap);
		switch (0){
			case 1:
			break;
		}
	}

	private class InnerAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG, "宽："+bitmap.getWidth()+" 高："+bitmap.getHeight());
			if (type==1){
				usbPrinter.printImage(bitmap);
				usbPrinter.cutPaper();
			}else if (type==2){
				bPrinter.printImage(bitmap);
				bPrinter.cutPaper();
			}else if (type==3){
				wiFiPrinter.printImage(bitmap);
				wiFiPrinter.cutPaper();
			}
			return null;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bundle bundle = null;

		if(1 == requestCode) {
			if (data != null && (bundle = data.getExtras()) != null) {
				 filePath = bundle.getString("file");
				Log.d(TAG, "onCreate: filePath:"+filePath);
				if (filePath!=null&&!filePath.equals("")){
					pdf_conext = new PdfContext();
					d = pdf_conext.openDocument(filePath);
					isGetFile = true;
					setBtnState();
				}
				// 默认显示第一页
				showImage(pageCount);
				tvShowPage.setText(pageCount + 1 + "/" + d.getPageCount());
			}
		}
	}

	private void getSdcardFile(){
		//打开文件夹
		Intent fileintent = new Intent(PdfPrintActivity.this,PrinterFileManager.class);
		startActivityForResult(fileintent, 1);
	}
	//请求权限
	public void showContacts(){
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(getApplicationContext(),"没有权限,请手动开启读写权限",Toast.LENGTH_SHORT).show();
			// 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
			ActivityCompat.requestPermissions(PdfPrintActivity.this,new String[]{
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
			}, GET_PDF);
		}else{
			getSdcardFile();
		}
	}

	//Android6.0申请权限的回调方法
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			// requestCode即所声明的权限获取码，在checkSelfPermission时传入
			case GET_PDF:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
					getSdcardFile();
				} else {
					isGetFile = false;
					// 没有获取到权限，做特殊处理
					Toast.makeText(getApplicationContext(), "获取读写权限失败，请手动开启", Toast.LENGTH_SHORT).show();
				}
				break;
			case DEFAULT_PDF:
				copyFilesFromassets(this, "test2.pdf", Environment.getExternalStorageDirectory().getPath()+"/Download/test3.pdf");
				pdf_conext = new PdfContext();
				d = pdf_conext.openDocument(Environment.getExternalStorageDirectory().getPath()+"/Download/test3.pdf");
				// 默认显示第一页
				showImage(pageCount);
				tvShowPage.setText(pageCount + 1 + "/" + d.getPageCount());
				isGetFile = true;
				setBtnState();
				break;
			default:
				break;
		}
	}
}
