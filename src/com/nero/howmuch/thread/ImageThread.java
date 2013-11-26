package com.nero.howmuch.thread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.nero.howmuch.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class ImageThread extends Thread{
	public static final String LOAD_IMAGE = "load_image";
	public static final String LOAD_IMAGES = "load_images";
	String action;
	URL imageURL;
	ArrayList<URL> imagesURL = new ArrayList<URL>();
	Handler handler;
	int count;
	
	public ImageThread(String _action, URL _imageURL, int cnt,Handler _handler){
		this.action = _action;
		this.imageURL = _imageURL;
		this.handler = _handler;
		this.count = cnt;
	}
	public ImageThread(String _action, ArrayList<String> urls, int cnt,Handler _handler){
		this.action = _action;
		try {
			for (int i = 0; i < urls.size(); i++) {
				this.imagesURL.add(new URL(Utils.imageUrl + "/" + urls.get(i)));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.handler = _handler;
		this.count = cnt;
	}
	@Override
	public void run() {
		System.out.println("action:" + action);
		if (action.equals(LOAD_IMAGE)) {
			try {

				HttpURLConnection httpConn = (HttpURLConnection) imageURL
						.openConnection();
				BufferedInputStream bis = new BufferedInputStream(
						httpConn.getInputStream(), 10240);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				
				//bitmap return...
				Message message = handler.obtainMessage(1);
				ImageItem imageItem = new ImageItem();
				imageItem.setImgBitmap(bm);
				imageItem.setCount(count);
				message.obj = imageItem;
				handler.sendMessage(message);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals(LOAD_IMAGES)){
//			try {
//				ArrayList<ImageItem> images = new ArrayList<ImageItem>();
//				for(int i=0; i<imagesURL.size(); i++){
//					HttpURLConnection httpConn = (HttpURLConnection) imagesURL.get(i)
//							.openConnection();
//					BufferedInputStream bis = new BufferedInputStream(
//							httpConn.getInputStream(), 10240);
//					Bitmap bm = BitmapFactory.decodeStream(bis);
//					
//					bis.close();
//				}
//			
//				
//				//bitmap return...
//				Message message = handler.obtainMessage(1);
//				ImageItem imageItem = new ImageItem();
//				imageItem.setImgBitmap(bm);
//				imageItem.setCount(count);
//				message.obj = imageItem;
//				handler.sendMessage(message);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
