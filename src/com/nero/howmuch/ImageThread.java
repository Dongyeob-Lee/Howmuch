package com.nero.howmuch;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class ImageThread extends Thread{
	public static final String LOAD_IMAGE = "load_image";
	public static final int LOAD_URL_IMAGE =20;
	String action;
	URL imageURL;
	Handler handler;
	
	public ImageThread(String _action, URL _imageURL, Handler _handler){
		this.action = _action;
		this.imageURL = _imageURL;
		this.handler = _handler;
	}

	@Override
	public void run() {
		if(action.equals(LOAD_IMAGE)){
			try {
				HttpURLConnection httpConn = (HttpURLConnection)imageURL.openConnection();
				BufferedInputStream bis = new BufferedInputStream(httpConn.getInputStream(), 10240);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				
				Message message = handler.obtainMessage(LOAD_URL_IMAGE);
				message.obj = bm;
				handler.sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
