package com.nero.howmuch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
	public static void putConsumerSharedPreference(Context context,String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("consumer_id", value);
		editor.commit();
	}
	public static void putSellerNameSharedPreference(Context context,String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("seller_name", value);
		editor.commit();
	}
	public static void putSellerNumSharedPreference(Context context,String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("seller_num", value);
		editor.commit();
	}
	public static void putSellerGCM(Context context,String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("seller_gcm", value);
		editor.commit();
	}
	public static void putConsumerGCM(Context context,String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("consumer_gcm", value);
		editor.commit();
	}
	public static String getConsumerSharedPreference(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("consumer_id", null);
	}
	public static String getsellerNameSharedPreference(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("seller_name", null);
	}
	public static String getsellerNumSharedPreference(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("seller_num", null);
	}
	public static String getSellerGCM(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("seller_gcm", null);
	}
	public static String getConsumerGCM(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("consumer_gcm", null);
	}
	public static void putSellerPlane(Context context,int value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("seller_plane", value);
		editor.commit();
	}
	public static int getSellerPlane(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt("seller_plane", 0);
	}
}
