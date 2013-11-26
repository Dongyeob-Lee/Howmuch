package com.nero.howmuch.seller;

import java.util.List;
import java.util.Map;

import com.nero.howmuch.Category;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.SimpleListAdapter;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.R.layout;
import com.nero.howmuch.R.menu;
import com.nero.howmuch.thread.SellerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Seller_info_edit extends Activity {
	
	private EditText et_seller_edit_name;
	private EditText et_seller_edit_num;
	private EditText et_seller_edit_city;
	private EditText et_seller_edit_address;
	private EditText et_seller_edit_phone;
	private Button btn_seller_edit_select_city;
	private Button btn_seller_edit;
	private Button btn_seller_edit_cancel;
	
	private Seller seller;
	private SimpleListAdapter ad_city;
	private ListView lv_seller_select_city;
	private Dialog city_Dialog;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_info_edit);
		seller = Xml_controler.getSellerDateFromXml(this);
		initWidget();
		fillData();
	}
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == btn_seller_edit_select_city.getId()){
				showCityDialog();
			}else if(v.getId() == btn_seller_edit.getId()){
				update();
			}else if(v.getId() == btn_seller_edit_cancel.getId()){
				finish();
			}
		}
	};
	private void update(){
		if(et_seller_edit_address.getText().toString().equals("")){
			Toast.makeText(Seller_info_edit.this, "�ּҸ� �Է��Ͽ��ּ���",Toast.LENGTH_SHORT).show();
		}else if(et_seller_edit_phone.getText().toString().equals("")){
			Toast.makeText(Seller_info_edit.this, "����ó�� �Է��Ͽ��ּ���",Toast.LENGTH_SHORT).show();
		}else{
			progressDialog = ProgressDialog.show(Seller_info_edit.this,"������Ʈ..","��ø� ��ٷ��ּ���..",true);
			SellerThread sellerThread = new SellerThread(seller, Seller.SELLER_UPDATE, handler);
			sellerThread.start();
		}
	}
	private void showCityDialog(){
		ad_city = new SimpleListAdapter(Seller_info_edit.this, Seller_register.CITY);
		city_Dialog = new Dialog(Seller_info_edit.this);
		city_Dialog.setContentView(R.layout.seller_select_city_dialog);
		city_Dialog.setTitle("���� ����");
		city_Dialog.setCancelable(true);
		lv_seller_select_city = (ListView)city_Dialog.findViewById(R.id.lv_seller_select_city);
		lv_seller_select_city.setAdapter(ad_city);
		
		lv_seller_select_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				Toast.makeText(Seller_info_edit.this, (String)lv_seller_select_city.getItemAtPosition(pos), Toast.LENGTH_LONG).show();
				et_seller_edit_city.setText((CharSequence) lv_seller_select_city.getItemAtPosition(pos));
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
	}
	private void fillData(){
		et_seller_edit_name.setText(seller.getSeller_name());
		et_seller_edit_num.setText(seller.getSeller_num());
		et_seller_edit_city.setText(seller.getSeller_city());
		et_seller_edit_address.setText(seller.getSeller_address());
		et_seller_edit_phone.setText(seller.getSeller_phone());
		
	}
	private void initWidget(){
		et_seller_edit_name = (EditText)findViewById(R.id.et_seller_edit_name);
		et_seller_edit_num = (EditText)findViewById(R.id.et_seller_edit_num);
		et_seller_edit_city = (EditText)findViewById(R.id.et_seller_edit_city);
		et_seller_edit_address = (EditText)findViewById(R.id.et_seller_edit_address);
		et_seller_edit_phone = (EditText)findViewById(R.id.et_seller_edit_phone);
		//�̸��� ��ȣ�� �����Ұ� 
		et_seller_edit_name.setEnabled(false);
		et_seller_edit_num.setEnabled(false);
		et_seller_edit_city.setEnabled(false);
		btn_seller_edit_select_city = (Button)findViewById(R.id.btn_seller_edit_select_city);
		btn_seller_edit = (Button)findViewById(R.id.btn_seller_edit);
		btn_seller_edit_cancel = (Button)findViewById(R.id.btn_seller_edit_cancel);
		btn_seller_edit_select_city.setOnClickListener(btnClickListener);
		btn_seller_edit_cancel.setOnClickListener(btnClickListener);
		btn_seller_edit.setOnClickListener(btnClickListener);
	}
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				progressDialog.dismiss();
				showAlertDialog("����","�����κ��� ������ �����ϴ�.����Ŀ� �ٽýõ��� �ֽʽÿ�", Seller_info_edit.this,false);
				return;
			}
			List<Map<String, String>> items = (List<Map<String, String>>) msg.obj;
			if(items.size()!=0){
				progressDialog.dismiss();
				if(items.get(0).get("title").equals(Seller.SELLER_REGISTER))
				{
					if(items.get(0).get("result").equals("true")){
						//����� �����̶� ��й�ȣ �������� ����!!
						Xml_controler.makeSellerFile(seller, Seller_info_edit.this);
						Toast.makeText(Seller_info_edit.this, "���� �Ϸ�", Toast.LENGTH_LONG).show();
						finish();
					}else{
						showAlertDialog("���","������ ����ڵ���� �����Ͽ����ϴ�.", Seller_info_edit.this, false);
					}
				}
			}else{
				progressDialog.dismiss();
				showAlertDialog("����","�����κ��� ������ �����ϴ�.", Seller_info_edit.this,false);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_info_edit, menu);
		return true;
	}

}
