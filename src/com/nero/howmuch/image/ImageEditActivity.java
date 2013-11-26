package com.nero.howmuch.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.nero.howmuch.R;


import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class ImageEditActivity extends Activity {
	private LinearLayout ll_image_edit;
	private Button btn_image_edit_save;
	private static final String TEMP_IMAGE = "tmp_img.jpeg";

	//button
	private Button btn_image_edit_pen_black;
	private Button btn_image_edit_pen_blue;
	private Button btn_image_edit_pen_red;
	private Button btn_image_edit_pen_white;
	
	
	String output;
	Uri uri_output;
	String flag;
	String path;
	
	int ll_width;
	int ll_height;
	//
	Bitmap tmp_bitmap;
	ImageEditView imageEditView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_edit);
	    btn_image_edit_save = (Button)findViewById(R.id.btn_image_edit_save);
	    btn_image_edit_save.setOnClickListener(btnClickListener);
	    ll_image_edit =(LinearLayout)findViewById(R.id.ll_image_edit);
	    //button
	    btn_image_edit_pen_black = (Button)findViewById(R.id.btn_image_edit_pen_black);
	    btn_image_edit_pen_blue = (Button)findViewById(R.id.btn_image_edit_pen_blue);
	    btn_image_edit_pen_red = (Button)findViewById(R.id.btn_image_edit_pen_red);
	    btn_image_edit_pen_white = (Button)findViewById(R.id.btn_image_edit_pen_white);
	    btn_image_edit_pen_black.setOnClickListener(colorClickListener);
	    btn_image_edit_pen_blue.setOnClickListener(colorClickListener);
	    btn_image_edit_pen_red.setOnClickListener(colorClickListener);
	    btn_image_edit_pen_white.setOnClickListener(colorClickListener);
	    
	    path = this.getFilesDir().getAbsolutePath();
	   
	    imageEditView = new ImageEditView(this);
	    
	    flag = getIntent().getStringExtra("flag");
	    if(flag.equals("album")){
	    	uri_output = getIntent().getParcelableExtra("output");
	    	Log.d("uri", uri_output.toString());
	    	try {
				imageEditView.setTest_bitmap(Images.Media.getBitmap(getContentResolver(), uri_output));
				ll_image_edit.addView(imageEditView);
				ll_image_edit.invalidate();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }else if(flag.equals("camera")){
	    	 output = getIntent().getStringExtra("output");
//	    	iv_image_edit.setImageBitmap(BitmapFactory.decodeFile(output));
	    }
	    
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		ll_image_edit =(LinearLayout)findViewById(R.id.ll_image_edit);
		ll_width = ll_image_edit.getWidth();
		ll_height = ll_image_edit.getHeight();
		Rect rect = new Rect(0, 0, ll_width, ll_height);
		imageEditView.setTest_bitmap_rect(rect);
		Log.d("width , height", ll_width+","+ll_height);
	}
	private OnClickListener colorClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int viewId = v.getId();
			Paint paint  = new Paint();
			if(viewId == btn_image_edit_pen_black.getId()){
				paint.setColor(Color.BLACK);
				paint.setStrokeWidth(2);
				imageEditView.setCurPaint(paint);
				btn_image_edit_pen_black.setPressed(true);
				btn_image_edit_pen_blue.setPressed(false);
				btn_image_edit_pen_red.setPressed(false);
				btn_image_edit_pen_white.setPressed(false);
			}else if(viewId == btn_image_edit_pen_blue.getId()){
				paint.setColor(Color.BLUE);
				paint.setStrokeWidth(2);
				imageEditView.setCurPaint(paint);
				btn_image_edit_pen_black.setPressed(false);
				btn_image_edit_pen_blue.setPressed(true);
				btn_image_edit_pen_red.setPressed(false);
				btn_image_edit_pen_white.setPressed(false);
			}else if(viewId == btn_image_edit_pen_red.getId()){
				paint.setColor(Color.RED);
				paint.setStrokeWidth(2);
				imageEditView.setCurPaint(paint);
				btn_image_edit_pen_black.setPressed(false);
				btn_image_edit_pen_blue.setPressed(false);
				btn_image_edit_pen_red.setPressed(true);
				btn_image_edit_pen_white.setPressed(false);
			}else if(viewId == btn_image_edit_pen_white.getId()){
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(2);
				imageEditView.setCurPaint(paint);
				btn_image_edit_pen_black.setPressed(false);
				btn_image_edit_pen_blue.setPressed(false);
				btn_image_edit_pen_red.setPressed(false);
				btn_image_edit_pen_white.setPressed(true);
			}
		}
	};
	private OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(flag.equals("album")){
				imageEditView.setDrawingCacheEnabled(true);
				tmp_bitmap = imageEditView.getDrawingCache();
					
//				try {
//					tmp_bitmap = Images.Media.getBitmap(getContentResolver(), uri_output);
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(tmp_bitmap==null){
//					Log.d("bitmap", "∫Ò∆Æ∏  null");
//				}
//				SaveBitmapToFileCache(tmp_bitmap, path+"/"+TEMP_IMAGE);
//				Log.d("path", path+"/"+TEMP_IMAGE);
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			tmp_bitmap.compress(CompressFormat.JPEG, 50, stream);
			byte[] bb = stream.toByteArray();
			Intent intent = new Intent();
			intent.putExtra("bitmap", bb);
			ImageEditActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
	};

	private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

		File fileCacheItem = new File(strFilePath);
		OutputStream out = null;
		// if(fileCacheItem.exists()){
		// fileCacheItem.delete();
		// }
		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.JPEG, 70, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
