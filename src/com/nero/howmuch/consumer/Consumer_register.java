package com.nero.howmuch.consumer;

import java.util.List;
import java.util.Map;
import com.google.android.gcm.GCMRegistrar;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.seller.Seller;
import com.nero.howmuch.thread.ConsumerThread;
import com.nero.howmuch.thread.SellerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Consumer_register extends Activity {
	
	public static final String PROJECT_ID = "57467571132";
	private EditText et_consumer_name;
	private TextView tv_consumer_name_isvalid;
	private Button btn_check_consumer_name;
	private Button btn_consumer_register;
	private boolean isvalid_name=false;
	
	private Consumer consumer;
	
	private String regID;
	List<Map<String, String>> items = null;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumer_register);
		
		et_consumer_name = (EditText)findViewById(R.id.et_consumer_name);
		tv_consumer_name_isvalid = (TextView)findViewById(R.id.tv_consumer_name_isvalid);
		btn_check_consumer_name = (Button)findViewById(R.id.btn_check_consumer_name);
		btn_consumer_register = (Button)findViewById(R.id.btn_consumer_register);
		btn_check_consumer_name.setOnClickListener(btnClickListener);
		btn_consumer_register.setOnClickListener(btnClickListener);
		consumer = new Consumer();
		registerGcm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_register, menu);
		return true;
	}
	private OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==btn_check_consumer_name.getId()){
				consumer.setConsumer_name(et_consumer_name.getText().toString());
				ConsumerThread consumerThread = new ConsumerThread(consumer.getConsumer_name(), Consumer.CONSUMER_CHECKNAME, handler);
				consumerThread.start();
			}else if(v.getId()==btn_consumer_register.getId()){
				if(isvalid_name){
					while(true){
						regID = GCMRegistrar.getRegistrationId(Consumer_register.this);
						if(!regID.equals("")){
							break;
						}
					}
					showRegisterAlertDialog(Consumer_register.this);
				}else{
					Toast.makeText(Consumer_register.this, "사용자 이름 중복환인을 해주시기 바랍니다.", Toast.LENGTH_LONG).show();
				}
			}
		}
	};
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			if(msg.what==100){
				showAlertDialog("오류","서버로 부터 응답이 없습니다.", Consumer_register.this,false);
				return;
			}
			items = (List<Map<String, String>>) msg.obj;
			for(int i=0; i<items.size(); i++){
				
				System.out.println(items.get(i).get("title"));
				System.out.println(items.get(i));
			}
			if(items.size()!=0){
				if(items.get(0).get("title").equals(Consumer.CONSUMER_REGISTER))
				{
					if(items.get(0).get("result").equals("true")){
						intent = new Intent(Consumer_register.this,Consumer_main.class);
						SharedPreferenceUtil.putConsumerSharedPreference(Consumer_register.this, consumer.getConsumer_name());
						SharedPreferenceUtil.putConsumerGCM(Consumer_register.this, regID);
						Xml_controler.makeConsumerFile(consumer, Consumer_register.this);
						Toast.makeText(Consumer_register.this, "구매자 등록 완료", Toast.LENGTH_LONG).show();
						startActivity(intent);
						finish();
					}else{
						showAlertDialog("오류","구매자 등록을 실패 하였습니다.", Consumer_register.this, false);
					}
				}else if(items.get(0).get("title").equals(Consumer.CONSUMER_CHECKNAME)){
					if(items.get(0).get("result").equals("true")){
						tv_consumer_name_isvalid.setText(consumer.getConsumer_name()+"은(는) 사용가능한 이름입니다.");
						isvalid_name = true;
					}else{
						tv_consumer_name_isvalid.setText(consumer.getConsumer_name()+"은(는) 사용할 수 없습니다.");
						isvalid_name = false;
					}
				}
			}else{
				showAlertDialog("오류","서버로 부터 응답이 없습니다.", Consumer_register.this,false);
			}
			
			
		}
	};
	protected void showRegisterAlertDialog(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage("해당 어플리케이션은 사용자간 일어나는 금전적 거래에 대하여 아무런 책임을 지지 않습니다.")
				.setTitle("경고")
				.setCancelable(false)
				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								consumer.setConsumer_name(et_consumer_name.getText().toString());
								consumer.setGcm_id(regID);
								Log.d("gcmId", regID);
								ConsumerThread consumerThread = new ConsumerThread(consumer, Consumer.CONSUMER_REGISTER, handler);
								consumerThread.start();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
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
									Intent intent = new Intent(context, Consumer_main.class);
									dialog.dismiss();
									context.startActivity(intent);
								}else{
									dialog.cancel();
								}
								
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
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
