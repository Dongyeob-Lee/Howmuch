package com.nero.howmuch.seller;

import java.util.List;
import java.util.Map;

import com.google.android.gcm.GCMRegistrar;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.SimpleListAdapter;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.consumer.Consumer_register;
import com.nero.howmuch.thread.SellerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Seller_register extends Activity {
	public static final String SELLER_REGISTER = "seller_register";
	public static final String SELLER_CHECKNAME = "seller_checkname";
	public static final String PROJECT_ID = "57467571132";
	public static final String[] CITY = {"서울","인천","대전","광주","대구","부산","울산","경기도","강원도","충청도","전라도","경상도","제주도"};
	SimpleListAdapter ad_city;
	Dialog city_Dialog;
	private String regID;
	
	private Seller seller;
	
	private ListView lv_seller_select_city;
	
	private EditText et_seller_name;
	private EditText et_seller_num1;
	private EditText et_seller_num2;
	private EditText et_seller_num3;
	private EditText et_seller_pw;
	private EditText et_seller_pw_confirm;
	private EditText et_seller_city;
	private EditText et_seller_address;
	private EditText et_seller_phone;
	
	private Button btn_check_seller_num;
	private Button btn_seller_select_city;
	private Button btn_seller_register;
	
	private TextView tv_seller_name_isvalid;
	
	List<Map<String, String>> items = null;
	Intent intent;
	boolean isvalid_name=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_register);
		
		et_seller_name = (EditText)findViewById(R.id.et_seller_name);
		et_seller_num1 = (EditText)findViewById(R.id.et_seller_num1);
		et_seller_num2 = (EditText)findViewById(R.id.et_seller_num2);
		et_seller_num3 = (EditText)findViewById(R.id.et_seller_num3);
		et_seller_pw = (EditText)findViewById(R.id.et_seller_pw);
		et_seller_pw_confirm = (EditText)findViewById(R.id.et_seller_pw_confirm);
		et_seller_num1.setOnTouchListener(numTouchlistener);
		et_seller_num2.setOnTouchListener(numTouchlistener);
		et_seller_num3.setOnTouchListener(numTouchlistener);
		et_seller_city = (EditText)findViewById(R.id.et_seller_city);
		et_seller_address = (EditText)findViewById(R.id.et_seller_address);
		et_seller_phone = (EditText)findViewById(R.id.et_seller_phone);
		
		btn_check_seller_num = (Button)findViewById(R.id.btn_check_seller_num);
		btn_seller_select_city = (Button)findViewById(R.id.btn_seller_select_city);
		btn_seller_register = (Button)findViewById(R.id.btn_seller_register);
		btn_check_seller_num.setOnClickListener(btnClickListener);
		btn_seller_select_city.setOnClickListener(btnClickListener);
		btn_seller_register.setOnClickListener(btnClickListener);
		tv_seller_name_isvalid = (TextView)findViewById(R.id.tv_seller_name_isvalid);
		
		et_seller_city.setEnabled(false);
		
		seller = new Seller();
		registerGcm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_register, menu);
		return true;
	}
	private OnTouchListener numTouchlistener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			isvalid_name = false;
			return false;
		}
	};
	private OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==btn_check_seller_num.getId()){
				String seller_num = et_seller_num1.getText().toString()+"-"+et_seller_num2.getText().toString()+"-"+et_seller_num3.getText().toString();
				Log.d("seller_name", seller_num);
				SellerThread sellerThread = new SellerThread(seller_num, Seller.SELLER_CHECKNAME, handler);
				sellerThread.start();
			}else if(v.getId()==btn_seller_select_city.getId()){
				ad_city = new SimpleListAdapter(Seller_register.this, CITY);
				city_Dialog = new Dialog(Seller_register.this);
				city_Dialog.setContentView(R.layout.seller_select_city_dialog);
				city_Dialog.setTitle("도시 선택");
				city_Dialog.setCancelable(true);
				lv_seller_select_city = (ListView)city_Dialog.findViewById(R.id.lv_seller_select_city);
				lv_seller_select_city.setAdapter(ad_city);
				
				lv_seller_select_city.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						Toast.makeText(Seller_register.this, (String)lv_seller_select_city.getItemAtPosition(pos), Toast.LENGTH_LONG).show();
						et_seller_city.setText((CharSequence) lv_seller_select_city.getItemAtPosition(pos));
						city_Dialog.dismiss();
					}
					
				});
				
				Button btn_select_city_close = (Button)city_Dialog.findViewById(R.id.btn_seller_select_city_dialog_close);
				btn_select_city_close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						city_Dialog.dismiss();
					}
				});
				
				city_Dialog.show();
			}else if(v.getId()==btn_seller_register.getId()){
				if(!checkEditTextAll()){
					Toast.makeText(Seller_register.this, "양식을 다 채워주세요", Toast.LENGTH_LONG).show();
				}else if(et_seller_pw.getText().toString().length()<6){
					Toast.makeText(Seller_register.this, "비밀번호는 6자리 이상이여야합니다", Toast.LENGTH_LONG).show();
				}else if(!et_seller_pw.getText().toString().equals(et_seller_pw_confirm.getText().toString())){
					Toast.makeText(Seller_register.this, "비밀번호를 다시 확인해 주세요", Toast.LENGTH_LONG).show();
				}
				else{
					if(isvalid_name){
						while(true){
							regID = GCMRegistrar.getRegistrationId(Seller_register.this);
							if(!regID.equals("")){
								break;
							}
						}
						showRegisterAlertDialog(Seller_register.this);
					}else{
						Toast.makeText(Seller_register.this, "사용자 이름을 다시확인해주세요", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	};
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				showAlertDialog("오류","서버로부터 응답이 없습니다.잠시후에 다시시도해 주십시오", Seller_register.this,false);
				return;
			}
			items = (List<Map<String, String>>) msg.obj;
			if(items.size()!=0){
				if(items.get(0).get("title").equals(Seller.SELLER_REGISTER))
				{
					if(items.get(0).get("result").equals("true")){
						intent = new Intent(Seller_register.this,Seller_main.class);
						//사용자 계정이랑 비밀번호 저장파일 생성!!
						Xml_controler.makeSellerFile(seller, Seller_register.this);
						SharedPreferenceUtil.putSellerNameSharedPreference(Seller_register.this, seller.getSeller_name());
						SharedPreferenceUtil.putSellerNumSharedPreference(Seller_register.this, seller.getSeller_num());
						SharedPreferenceUtil.putSellerGCM(Seller_register.this, regID);	
						SharedPreferenceUtil.putSellerPlane(Seller_register.this, 10);
						Toast.makeText(Seller_register.this, "가입 완료", Toast.LENGTH_LONG).show();
						startActivity(intent);
						finish();
					}else{
						showAlertDialog("등록","서버에 사용자등록을 실패하였습니다.", Seller_register.this, false);
					}
				}else if(items.get(0).get("title").equals(Seller.SELLER_CHECKNAME)){
				    String seller_num=	et_seller_num1.getText().toString()+"-"+et_seller_num2.getText().toString()+"-"+et_seller_num3.getText().toString();
					if(items.get(0).get("result").equals("true")){
						tv_seller_name_isvalid.setText(seller_num+"은(는) 사용가능합니다.");
						isvalid_name = true;
					}else{
						tv_seller_name_isvalid.setText(seller_num+"은(는) 사용할 수 없습니다.");
						isvalid_name = false;
					}
				}
			}else{
				showAlertDialog("오류","서버로부터 응답이 없습니다.", Seller_register.this,false);
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
									
								}else{
									dialog.cancel();
								}
								
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	protected void showRegisterAlertDialog(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage("해당 어플리케이션은 사용자간 일어나는 금전적 거래에 대하여 아무런 책임을 지지 않습니다.")
				.setTitle("경고")
				.setCancelable(true)
				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								String seller_num = et_seller_num1.getText().toString()+"-"+et_seller_num2.getText().toString()+"-"+et_seller_num3.getText().toString();
								seller.setSeller_num(seller_num);
								seller.setSeller_pw(et_seller_pw.getText().toString());
								seller.setSeller_name(et_seller_name.getText().toString());
								seller.setSeller_city(et_seller_city.getText().toString());
								seller.setSeller_address(et_seller_address.getText().toString());
								seller.setSeller_phone(et_seller_phone.getText().toString());
								seller.setGcm_id(regID);
								SellerThread sellerThread = new SellerThread(seller, Seller.SELLER_REGISTER, handler);
								sellerThread.start();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	private boolean checkEditTextAll(){
		if(et_seller_name.getText().toString().equals("")){
			return false;
		}
		if(et_seller_num1.getText().toString().equals("")||et_seller_num2.getText().toString().equals("")||et_seller_num3.getText().toString().equals("")){
			return false;
		}
		if(et_seller_city.getText().toString().equals("")){
			return false;
		}
		if(et_seller_address.getText().toString().equals("")){
			return false;
		}
		if(et_seller_phone.getText().toString().equals("")){
			return false;
		}
		
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
