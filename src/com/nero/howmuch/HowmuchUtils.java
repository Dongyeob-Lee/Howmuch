package com.nero.howmuch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class HowmuchUtils {

	public static String randomChar(){
		String random = new String();
		for(int i=0; i<5; i++){
			char ch = (char)((Math.random()*26)+97);
			random = random + ch;
		}
		return random;
	}
	public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

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
