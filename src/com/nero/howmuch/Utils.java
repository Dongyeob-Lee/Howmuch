package com.nero.howmuch;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class Utils {
	public static final String userUrl = "http://ua32645.cafe24.com/userservlet";
	public static final String postUrl = "http://ua32645.cafe24.com/postservlet";
	public static final String replyUrl = "http://ua32645.cafe24.com/replyservlet";
	public static final String imageUrl = "http://ua32645.cafe24.com/image"; 
	
	public static final String PROJECT_ID = "57467571132";
	//데이터베이스에 있는 이미지파일명 가져와서 자르기.
	public static ArrayList<String> getImgNameToken(String str){
		ArrayList<String> images = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(str, "/");
		while(tokenizer.hasMoreTokens()){
			String temp = tokenizer.nextToken();
			if(tokenizer.equals("/")){
				
			}else{
				images.add(temp);
			}
		}
		return images;
	}
	public static String sumImgName(ArrayList<String> images){
		String str_image = "";
		if(images.size()>0){
			str_image = images.get(0);
		}
		for(int i=1; i<images.size(); i++){
			str_image = str_image+"/"+images.get(i);
		}
		return str_image;
	}
	public String registerGcm(Context context){
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		String regID="";
		final String Id = GCMRegistrar.getRegistrationId(context);
		
		if (Id.equals("")) {
			GCMRegistrar.register(context, PROJECT_ID);
			regID=GCMRegistrar.getRegistrationId(context);;
			System.out.println("unregister:"+regID);
		}else {
			regID=Id;
			System.out.println("register:"+regID);
			Log.e("reg_id", Id);
		}
		return regID;
	}
	public static List<String> getPostDateFromDate(String date){
		//date = 2013-07-07
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(date, "-");
		result.add(tokenizer.nextToken());
		result.add(String.valueOf(Integer.parseInt(tokenizer.nextToken())));
		result.add(String.valueOf(Integer.parseInt(tokenizer.nextToken())));
		return result;
	}
}
