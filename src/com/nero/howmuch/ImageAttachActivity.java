package com.nero.howmuch;

import java.io.File;
import java.net.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

public class ImageAttachActivity extends Activity {
	public static final int URL = 100;
	public static final int SDCARD = 200;
	private ImageViewTouch iv_image_attach;
	private TextView tv_image_attach;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_attach);
		String sdcardpath = getFilesDir().getAbsolutePath();
		initWidget();
		int type = getIntent().getIntExtra("type", 0);
		String path = getIntent().getStringExtra("path");
		if(type==URL){
			try {
				URL url = new URL(Utils.imageUrl+"/"+path);
				ImageThread imageThread = new ImageThread(ImageThread.LOAD_IMAGE, url, handler);
				imageThread.start();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(type==SDCARD){
			File imgFile = new File(sdcardpath+"/"+path);
			if(imgFile.exists()){
				iv_image_attach.setImageBitmap(BitmapFactory.decodeFile(imgFile.getPath()));
			}else{
				tv_image_attach.setText("파일이 존재하지 않습니다.");
			}
		}
//		boolean isUrl = getIntent().getBooleanExtra("isUrl", false);
//		
//		if(isUrl){
//			String url = getIntent().getStringExtra("url");
//			//DownLoad image From Server
//		}else{
//			String path = getIntent().getStringExtra("path");
//			//Get Bitmap From interner Storage
//			String filepath = Environment.getExternalStorageDirectory().getPath()+"/"+MinuteUtils.DIRECTORY_NAME+"/"+MinuteUtils.IMAGE;
//			File dirFile =new File(filepath);
//			if(!dirFile.exists()){
//				dirFile.mkdir();
//			}
//			File imgFile = new File(filepath+"/"+path);
//			if(imgFile.exists()){
//				iv_image_attach.setImageBitmap(BitmapFactory.decodeFile(filepath+"/"+path));
//			}else{
//				tv_image_attach.setText("��대�� �����쇱�� 議댁�ы��吏� �����듬�����.");
//			}
//		}
//		iv_image_attach.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_mic));
//		iv_image_attach.refreshDrawableState();
//		iv_image_attach.bringToFront();
//		iv_image_attach.buildDrawingCache();
//		iv_image_attach.dispatchTouchEvent(MotionEvent.obtain((long)1, (long)1, MotionEvent.ACTION_DOWN, 100f, 100f, 1f, 1f, 0, 100f, 100f, 0, 0));
	}
	private void initWidget(){
		iv_image_attach = (ImageViewTouch)findViewById(R.id.iv_image_attach);
		tv_image_attach = (TextView)findViewById(R.id.tv_image_attach);
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==ImageThread.LOAD_URL_IMAGE){
				System.out.println("handler image");
				Bitmap bm = (Bitmap)msg.obj;
				tv_image_attach.setText("");
				iv_image_attach.setImageBitmap(bm);
				iv_image_attach.refreshDrawableState();
				iv_image_attach.bringToFront();
				iv_image_attach.buildDrawingCache();
			}
		}
	};
}
