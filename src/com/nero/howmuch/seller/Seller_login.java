package com.nero.howmuch.seller;

import java.util.List;
import java.util.Map;

import com.google.android.gcm.GCMRegistrar;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.R.layout;
import com.nero.howmuch.R.menu;
import com.nero.howmuch.consumer.Consumer_post;
import com.nero.howmuch.thread.SellerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Seller_login extends Activity {
	public static final String PROJECT_ID = "57467571132";
	private String regID;
	private EditText et_seller_login_num1;
	private EditText et_seller_login_num2;
	private EditText et_seller_login_num3;
	
	private EditText et_seller_login_pw;
	
	private Button btn_seller_login;
	
	private ProgressDialog progressDialog;
	
	Seller seller = new Seller();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_login);
		init();
		registerGcm();
	}

	private void init(){
		et_seller_login_num1 = (EditText)findViewById(R.id.et_seller_login_num1);
		et_seller_login_num2 = (EditText)findViewById(R.id.et_seller_login_num2);
		et_seller_login_num3 = (EditText)findViewById(R.id.et_seller_login_num3);
		et_seller_login_pw = (EditText)findViewById(R.id.et_seller_login_pw);
		
		btn_seller_login = (Button)findViewById(R.id.btn_seller_login);
		btn_seller_login.setOnClickListener(btnClickListener);
	}
	
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == btn_seller_login.getId()){
				while(true){
					regID = GCMRegistrar.getRegistrationId(Seller_login.this);
					if(!regID.equals("")){
						break;
					}
				}
				String seller_num = et_seller_login_num1.getText().toString()+"-"+et_seller_login_num2.getText().toString()+"-"+et_seller_login_num3.getText().toString();
				String seller_pw = et_seller_login_pw.getText().toString();
				seller.setSeller_num(seller_num);
				seller.setSeller_pw(seller_pw);
				seller.setGcm_id(regID);
				progressDialog = ProgressDialog.show(Seller_login.this,"login","wait ...",true);
				SellerThread sellerThread = new SellerThread(seller, Seller.SELLER_LOGIN, handler);
				sellerThread.start();
			}
		}
	};
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			List<Map<String, String>> items = null;
			if(msg.what==100){
				progressDialog.dismiss();
				Toast.makeText(Seller_login.this, "서버 응답이 없읍니다.", Toast.LENGTH_SHORT).show();
			}else{
				items = (List<Map<String,String>>)msg.obj;
				if(items.size()!=0){
					if(items.get(0).get("title").equals(Seller.SELLER_LOGIN))
					{
						if(items.get(0).get("result").equals("true")){
							progressDialog.dismiss();
							Intent intent = new Intent(Seller_login.this,Seller_main.class);
							//사용자 계정이랑 비밀번호 저장파일 생성!!
							seller.setSeller_name(items.get(0).get("seller_name"));
							seller.setSeller_city(items.get(0).get("seller_city"));
							seller.setSeller_address(items.get(0).get("seller_address"));
							seller.setSeller_phone(items.get(0).get("seller_phone"));
							seller.setPlane(Integer.parseInt(items.get(0).get("plane")));
							Xml_controler.makeSellerFile(seller, Seller_login.this);
							SharedPreferenceUtil.putSellerNameSharedPreference(Seller_login.this, seller.getSeller_name());
							SharedPreferenceUtil.putSellerNumSharedPreference(Seller_login.this, seller.getSeller_num());
							SharedPreferenceUtil.putSellerGCM(Seller_login.this, regID);	
							SharedPreferenceUtil.putSellerPlane(Seller_login.this, seller.getPlane());
							Toast.makeText(Seller_login.this, "로그인 성공", Toast.LENGTH_LONG).show();
							startActivity(intent);
							finish();
						}else{
							progressDialog.dismiss();
							Toast.makeText(Seller_login.this, "서버 응답이 없읍니다.", Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					progressDialog.dismiss();
					Toast.makeText(Seller_login.this, "서버 응답이 없읍니다.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_login, menu);
		return true;
	}
	public void registerGcm(){
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		final String Id = GCMRegistrar.getRegistrationId(this);
		
		if (Id.equals("")) {
			GCMRegistrar.register(this, PROJECT_ID);
			regID=GCMRegistrar.getRegistrationId(this);;
			System.out.println("unregister:"+regID);
		}else {
			regID=Id;
			System.out.println("register:"+regID);
			Log.e("reg_id", Id);
		}
	}
}
