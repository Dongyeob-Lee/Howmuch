package com.nero.howmuch;

import java.util.List;
import java.util.Map;

import com.google.android.gcm.GCMRegistrar;
import com.nero.howmuch.consumer.Consumer;
import com.nero.howmuch.consumer.Consumer_main;
import com.nero.howmuch.consumer.Consumer_register;
import com.nero.howmuch.seller.Seller;
import com.nero.howmuch.seller.Seller_login;
import com.nero.howmuch.seller.Seller_main;
import com.nero.howmuch.seller.Seller_register;
import com.nero.howmuch.seller.Seller_setting;
import com.nero.howmuch.thread.ConsumerThread;
import com.nero.howmuch.thread.SellerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final int CONSUMER = 0;
	static final int SELLER = 1;
	
	private Button btn_consumer;
	private Button btn_seller;
	private Consumer consumer = new Consumer();
	private Seller seller;
	ProgressDialog progressDialog1;
	ProgressDialog progressDialog2;
	
	String gcm_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//initialize button
		btn_consumer = (Button)findViewById(R.id.btn_consumer);
		btn_seller = (Button)findViewById(R.id.btn_seller);
		btn_consumer.setOnClickListener(btnClickListener);
		btn_seller.setOnClickListener(btnClickListener);
		
		consumer = Xml_controler.getConsumerDateFromXml(this);
		seller = Xml_controler.getSellerDateFromXml(this);
		
		checkGCM();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//button click listener to go to each main activity or register activity
	private OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==btn_consumer.getId()){
				consumer = Xml_controler.getConsumerDateFromXml(MainActivity.this);
				if(consumer.getConsumer_name().equals("empty")){
					showRegisterDialog(CONSUMER);
				}else{
					SharedPreferenceUtil.putConsumerSharedPreference(MainActivity.this, consumer.getConsumer_name());
					Intent intent = new Intent(MainActivity.this, Consumer_main.class);
					startActivity(intent);
				}
			}else if(v.getId()==btn_seller.getId()){
				seller = Xml_controler.getSellerDateFromXml(MainActivity.this);
				if(seller.getSeller_name().equals("empty")){
					selectLoginOrRegister(MainActivity.this);
				}else{
					SharedPreferenceUtil.putSellerNameSharedPreference(MainActivity.this, seller.getSeller_name());
					SharedPreferenceUtil.putSellerNumSharedPreference(MainActivity.this, seller.getSeller_num());
					Intent intent = new Intent(MainActivity.this, Seller_main.class);
					startActivity(intent);
				}
			}
			
		}
	};
	private void showRegisterDialog(final int what){
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.this);

		if(what==CONSUMER){
			builder.setTitle("구매자 등록");
			builder.setMessage("새로운 구매자를 등록하시겠습니까?");
		}else if(what==SELLER){
			builder.setTitle("판매자 등록");
			builder.setMessage("새로운 판매자를 등록하시겠습니까?");
		}
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent;
						if(what==CONSUMER){
							intent = new Intent(MainActivity.this,
									Consumer_register.class);
							startActivity(intent);
							finish();
						}else if(what==SELLER){
							intent = new Intent(MainActivity.this,
									Seller_register.class);
							startActivity(intent);
							finish();
						}
					}
				});
		builder.setNegativeButton("No", null);
		builder.show();
	}
	
	private void checkGCM(){
		if(SharedPreferenceUtil.getConsumerSharedPreference(this)!=null){
			while(true){
				gcm_id = GCMRegistrar.getRegistrationId(this);
				if(!gcm_id.equals("")){
					break;
				}
			}
			if(SharedPreferenceUtil.getConsumerGCM(this)==null){
				Consumer consumer = new Consumer();
				consumer.setConsumer_name(SharedPreferenceUtil.getConsumerSharedPreference(this));
				consumer.setGcm_id(gcm_id);
				ConsumerThread consumerThread = new ConsumerThread(consumer, Consumer.CONSUMER_UPDATE_GCM, handler);
				progressDialog1 = ProgressDialog.show(MainActivity.this,"user data update","wait please..",true);
				consumerThread.start();
			} else if(!SharedPreferenceUtil.getConsumerGCM(this).equals(gcm_id)){
				//같지 않다.. 새로 갱신한다.!!
				Consumer consumer = new Consumer();
				consumer.setConsumer_name(SharedPreferenceUtil.getConsumerSharedPreference(this));
				consumer.setGcm_id(gcm_id);
				ConsumerThread consumerThread = new ConsumerThread(consumer, Consumer.CONSUMER_UPDATE_GCM, handler);
				progressDialog1 = ProgressDialog.show(MainActivity.this,"user data update","wait please..",true);
				consumerThread.start();
			}
		}
		if(SharedPreferenceUtil.getsellerNumSharedPreference(this)!=null){
			String gcm_id ="";
			while(true){
				gcm_id = GCMRegistrar.getRegistrationId(this);
				if(!gcm_id.equals("")){
					break;
				}
			}
			if(SharedPreferenceUtil.getSellerGCM(this)==null){
				Seller seller = new Seller();
				seller.setSeller_name(SharedPreferenceUtil.getsellerNumSharedPreference(this));
				seller.setGcm_id(gcm_id);
				SellerThread sllerThread = new SellerThread(seller, Seller.SELLER_UPDATE_GCM, handler);
				progressDialog2 = ProgressDialog.show(MainActivity.this,"user data update","wait please..",true);
				sllerThread.start();
			}else if(!SharedPreferenceUtil.getSellerGCM(this).equals(gcm_id)){
				//같지 않다.. 새로 갱신한다.!!
				Seller seller = new Seller();
				seller.setSeller_num(SharedPreferenceUtil.getsellerNumSharedPreference(this));
				seller.setGcm_id(gcm_id);
				SellerThread sllerThread = new SellerThread(seller, Seller.SELLER_UPDATE_GCM, handler);
				progressDialog2 = ProgressDialog.show(MainActivity.this,"user data update","wait please..",true);
				sllerThread.start();
			}
		}
	}
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			List<Map<String, String>> items;
			items = (List<Map<String, String>>) msg.obj;
			if(items.size()!=0){
				if(items.get(0).get("title").equals(Consumer.CONSUMER_UPDATE_GCM))
				{
					if(items.get(0).get("result").equals("true")){
						SharedPreferenceUtil.putConsumerGCM(MainActivity.this, gcm_id);
						progressDialog1.dismiss();
					}else{
						progressDialog1.dismiss();
						showAlertDialog("update","Failed to update user data.", MainActivity.this, false);
					}
				}else if(items.get(0).get("title").equals(Seller.SELLER_UPDATE_GCM)){
					if(items.get(0).get("result").equals("true")){
						SharedPreferenceUtil.putSellerGCM(MainActivity.this, gcm_id);
						progressDialog2.dismiss();
					}else{
						progressDialog2.dismiss();
						showAlertDialog("update","Failed to update user data.", MainActivity.this, false);
					}
				}
			}else{
				if(progressDialog1!=null){
					progressDialog1.dismiss();
				}
				if(progressDialog2!=null){
					progressDialog2.dismiss();
				}
				showAlertDialog("오류","서버로부터 응답이 없습니다.", MainActivity.this,false);
			}
		}
	};
	public static void showAlertDialog(String title, String message,
			final Context context, final boolean isCorrect) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								if(isCorrect){
									dialog.dismiss();
								}else{
									dialog.cancel();
								}
								
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	private void selectLoginOrRegister(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle("판매자");
		builder.setMessage("이미 가입되있으신 판매자는 로그인을, 처음이신 분은 회원가입을 눌러주세요");
		builder.setCancelable(true);
		builder.setPositiveButton("회원가입", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(context, Seller_register.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("로그인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(context, Seller_login.class);
				startActivity(intent);
			}
		});
		builder.show();
	}
}
