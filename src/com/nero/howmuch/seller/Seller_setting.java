package com.nero.howmuch.seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.mime.MinimalField;

import com.nero.howmuch.Category;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.SimpleListAdapter;
import com.nero.howmuch.Utils;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.ConsumerReplyDbAdapter;
import com.nero.howmuch.sqlite.SellerPostDbAdapter;
import com.nero.howmuch.sqlite.SellerReplyDbAdapter;
import com.nero.howmuch.thread.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.*;
public class Seller_setting extends Activity {
	private EditText et_seller_category;
	
	private Button btn_seller_category;
	private Button btn_seller_add_category;
	private Button btn_seller_setting_charge_plane;
	private Button btn_seller_setting_info_edit;
	private Button btn_seller_setting_logout;
	private Button btn_seller_setting_delete;
	private TextView tv_seller_setting_name;
	private TextView tv_seller_setting_plane_cnt;
	
	private ListView lv_seller_categories;
	private ListView lv_seller_select_category;
	private Seller seller;
	
	ProgressDialog progressDialog;
	Dialog category_dialog;
	SimpleListAdapter simpleListAdapter;
	ArrayAdapter<String> ad_list;
	
	private boolean isCategory=false;
	private int categoryNum;
	private int removeCategoryNum;
	String seller_name;
	
	List<Map<String, String>> items = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_setting);
		et_seller_category = (EditText)findViewById(R.id.et_seller_category);
		et_seller_category.setEnabled(false);
		et_seller_category.setFocusable(false);
		btn_seller_category = (Button)findViewById(R.id.btn_seller_category);
		btn_seller_add_category = (Button)findViewById(R.id.btn_seller_add_category);
		btn_seller_setting_charge_plane = (Button)findViewById(R.id.btn_seller_setting_charge_plane);
		btn_seller_setting_info_edit = (Button)findViewById(R.id.btn_seller_setting_info_edit);
		btn_seller_setting_logout = (Button)findViewById(R.id.btn_seller_setting_logout);
		btn_seller_setting_delete = (Button)findViewById(R.id.btn_seller_setting_delete);
		btn_seller_category.setOnClickListener(btnClickListener);
		btn_seller_add_category.setOnClickListener(btnClickListener);
		btn_seller_setting_charge_plane.setOnClickListener(btnClickListener);
		btn_seller_setting_info_edit.setOnClickListener(btnClickListener);
		btn_seller_setting_logout.setOnClickListener(btnClickListener);
		btn_seller_setting_delete.setOnClickListener(btnClickListener);
		
		seller_name = SharedPreferenceUtil.getsellerNameSharedPreference(this);
		
		tv_seller_setting_name = (TextView)findViewById(R.id.tv_seller_setting_name);
		tv_seller_setting_plane_cnt = (TextView)findViewById(R.id.tv_seller_setting_plane_cnt);
				
		
		fillPlaneData();
		
		lv_seller_categories = (ListView)findViewById(R.id.lv_seller_categories);
		lv_seller_categories.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String removeCategory = seller.getCategory().get(arg2);
				removeCategoryNum = Category.getSmallCategoryNumByName(removeCategory);
				showCategoryRemoveAlertDialog("ī�װ� ����", removeCategory+" ī�װ��� �����Ͻðڽ��ϱ�?", Seller_setting.this);
				return false;
			}
		});
		refreshListView();
		
	}
	private void fillPlaneData(){
		int planeCnt = SharedPreferenceUtil.getSellerPlane(this);
		tv_seller_setting_name.setText(seller_name);
		tv_seller_setting_plane_cnt.setText(String.valueOf(planeCnt));
	}
	private void refreshListView(){
		isCategory = false;
		et_seller_category.setText("ī�װ� ����");
		seller = Xml_controler.getSellerDateFromXml(Seller_setting.this);
		if(seller.getCategory().size()==0){
			ArrayList<String> tmplist = new ArrayList<String>();
			tmplist.add("��ϵ� ī�װ��� �����ϴ�.");
			ad_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tmplist);
			lv_seller_categories.setAdapter(ad_list);
		}else{
			ad_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,seller.getCategory());
			lv_seller_categories.setAdapter(ad_list);
		}
	}
	private OnClickListener btnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId()==btn_seller_category.getId()){
				simpleListAdapter = new SimpleListAdapter(Seller_setting.this, Category.CATEGORIES);
				category_dialog = new Dialog(Seller_setting.this);
				category_dialog.setContentView(R.layout.seller_select_category_dialog);
				category_dialog.setTitle("ī�װ� ����");
				category_dialog.setCancelable(true);
				lv_seller_select_category = (ListView)category_dialog.findViewById(R.id.lv_seller_select_category);
				lv_seller_select_category.setAdapter(simpleListAdapter);
				lv_seller_select_category.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						et_seller_category.setText(lv_seller_select_category.getItemAtPosition(position).toString());
						isCategory=true;
						categoryNum = position;
						category_dialog.dismiss();
					}
				});
				Button btn_seller_setting_dialog_cancel = (Button)category_dialog.findViewById(R.id.btn_seller_select_category_dialog_close);
				btn_seller_setting_dialog_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						category_dialog.dismiss();
					}
				});
				category_dialog.show();
			}else if(v.getId()==btn_seller_add_category.getId()){
				if(checkCategoryDuplication(Category.CATEGORIES[categoryNum])){
					Seller_register.showAlertDialog("ī�װ�","�ش�ī�װ��� �̹��ֽ��ϴ�.", Seller_setting.this, false);
				}else{
					if(isCategory){
						if(seller.getCategory().size()==5){
							Seller_register.showAlertDialog("ī�װ��߰�","ī�װ��� 5�� �̻� �߰� �Ұ��մϴ�.", Seller_setting.this, false);
						}else{
							// server ������ .. ������� �κп��� xml  
							progressDialog = ProgressDialog.show(Seller_setting.this,"ī�װ� �߰� ��","��ø� ��ٷ��ּ���..",true);
							SellerThread sellerThread = new SellerThread(seller.getSeller_num(),categoryNum,Seller.SELLER_ADD_CATEGORY,handler);
							sellerThread.start();
						}
					}else{
						Seller_register.showAlertDialog("ī�װ��߰�","ī�װ��� �������ּ���.", Seller_setting.this, false);
					}
				}
			}else if(v.getId()==btn_seller_setting_charge_plane.getId()){
				if(SharedPreferenceUtil.getSellerPlane(Seller_setting.this)==0){
					progressDialog = ProgressDialog.show(Seller_setting.this,"����� ������","��ø� ��ٷ��ּ���..",true);
					SellerThread sellerThread = new SellerThread(10, seller.getSeller_num(), Seller.SELLER_CHARGE_PLANE,planeHandler);
					sellerThread.start();
				}else{
					Toast.makeText(Seller_setting.this, "������ 0���϶��� ���� �����մϴ�.", Toast.LENGTH_LONG).show();
				}
				
			}else if(v.getId()==btn_seller_setting_info_edit.getId()){
				Intent intent = new Intent(Seller_setting.this,Seller_info_edit.class);
				startActivity(intent);
			}else if(v.getId() == btn_seller_setting_logout.getId()){
				showSellerLogoutAlertDialog(Seller_setting.this);
			}else if(v.getId() == btn_seller_setting_delete.getId()){
				showSellerDeleteAlertDialog(Seller_setting.this);
			}
		}
	};
	Handler planeHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				progressDialog.dismiss();
				Toast.makeText(Seller_setting.this, "�����κ��� ������ �����ϴ�.����Ŀ� �ٽýõ��� �ֽʽÿ�", Toast.LENGTH_LONG).show();
				return;
			}
			items = (List<Map<String, String>>) msg.obj;
			if(items.size()!=0){
				if(items.get(0).get("title").equals(Seller.SELLER_CHARGE_PLANE))
				{
					if(items.get(0).get("result").equals("true")){
						int plane = Integer.parseInt(items.get(0).get("plane"));
						SharedPreferenceUtil.putSellerPlane(Seller_setting.this, plane);
						progressDialog.dismiss();
						fillPlaneData();
						Toast.makeText(Seller_setting.this, "����� ���� �Ϸ�", Toast.LENGTH_LONG).show();
					}else{
						progressDialog.dismiss();
						Seller_register.showAlertDialog("�����","����� ������ �����Ͽ����ϴ�.", Seller_setting.this, false);
					}
				}
			}else{
				progressDialog.dismiss();
				Seller_register.showAlertDialog("����","�����κ��� ������ �����ϴ�.", Seller_setting.this,false);
			}
		}
	};
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				progressDialog.dismiss();
				Toast.makeText(Seller_setting.this, "�����κ��� ������ �����ϴ�.����Ŀ� �ٽýõ��� �ֽʽÿ�", Toast.LENGTH_LONG).show();
				return;
			}
			items = (List<Map<String, String>>) msg.obj;
			if(items.size()!=0){
				if(items.get(0).get("title").equals(Seller.SELLER_ADD_CATEGORY))
				{
					if(items.get(0).get("result").equals("true")){
						seller.getCategory().add(Category.CATEGORIES[categoryNum]);
						Xml_controler.makeSellerFile(seller,Seller_setting.this);
						//listview refresh;
						refreshListView();
						progressDialog.dismiss();
						Toast.makeText(Seller_setting.this, "ī�װ� �߰� �Ϸ�", Toast.LENGTH_LONG).show();
					}else{
						progressDialog.dismiss();
						Seller_register.showAlertDialog("ī�װ��߰�","������ ī�װ� �߰��� �����Ͽ����ϴ�.", Seller_setting.this, false);
					}
				}else if(items.get(0).get("title").equals(Seller.SELLER_REMOVE_CATEGORY)){
					if(items.get(0).get("result").equals("true")){
						seller.getCategory().remove(Category.CATEGORIES[removeCategoryNum]);
						Xml_controler.makeSellerFile(seller, Seller_setting.this);
						refreshListView();
						progressDialog.dismiss();
						Toast.makeText(Seller_setting.this, "ī�װ� ���� �Ϸ�", Toast.LENGTH_LONG).show();
					}else{
						progressDialog.dismiss();
						Seller_register.showAlertDialog("ī�װ��߰�","������ ī�װ� ������ �����Ͽ����ϴ�.", Seller_setting.this, false);
					}
				}else if(items.get(0).get("title").equals(Seller.SELLER_DELETE)){
					progressDialog.dismiss();
					if(items.get(0).get("result").equals("true")){
						//xml���� ����.
						Xml_controler.deleteSellerDataXml(Seller_setting.this);
						finish();
					}else{
						Seller_register.showAlertDialog("����","�����κ��� ������ �����ϴ�.", Seller_setting.this,false);
					}
				}else if(items.get(0).get("title").equals(Seller.SELLER_LOGOUT)){
					progressDialog.dismiss();
					if(items.get(0).get("result").equals("true")){
						//xml���� ����.
						Xml_controler.deleteSellerDataXml(Seller_setting.this);
						SellerPostDbAdapter sellerPostDbAdapter = new SellerPostDbAdapter(Seller_setting.this);
						sellerPostDbAdapter.open();
						sellerPostDbAdapter.deleteAll();
						sellerPostDbAdapter.close();
						SellerReplyDbAdapter sellerReplyDbAdapter = new SellerReplyDbAdapter(Seller_setting.this);
						sellerReplyDbAdapter.open();
						sellerReplyDbAdapter.deleteAll();
						sellerReplyDbAdapter.close();
						finish();
					}else{
						Seller_register.showAlertDialog("����","�����κ��� ������ �����ϴ�.", Seller_setting.this,false);
					}
				}
			}else{
				progressDialog.dismiss();
				Seller_register.showAlertDialog("����","�����κ��� ������ �����ϴ�.", Seller_setting.this,false);
			}
		}
	};
	
	//ī�װ� �ߺ� üũ
	public boolean checkCategoryDuplication(String small_name){
		boolean result=false;
		//���� ����� ���Ͽ��� ���� �˻�.
		for(int i=0; i<seller.getCategory().size(); i++){
			if(seller.getCategory().get(i).equals(small_name)){
				result = true;
			}
		}
		//�������� �ߺ��˻�.
		//�ϴ� �̰� ����.
		
		return result;
	}
	private void showCategoryRemoveAlertDialog(String title, String message,
			final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								progressDialog = ProgressDialog.show(Seller_setting.this,"ī�װ� ���� ��","��ø� ��ٷ��ּ���..",true);
								SellerThread sellerThread = new SellerThread(seller.getSeller_num(), removeCategoryNum, Seller.SELLER_REMOVE_CATEGORY, handler);
								sellerThread.start();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showSellerLogoutAlertDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String message = "�α׾ƿ� �Ͻø� ���� �������� ����⸸ �� �α��ν� ���� �Ǹ�, ���� ����̽��� ����� ��� �����ʹ� ���� �Ǹ� ���� �Ұ��� �մϴ�. �α׾ƿ��Ͻðڽ��ϱ�?";
		builder.setMessage(message)
				.setTitle("�α׾ƿ�")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								progressDialog = ProgressDialog.show(Seller_setting.this,"�α׾ƿ� ��","��ø� ��ٷ��ּ���..",true);
								SellerThread sellerThread = new SellerThread(seller.getSeller_num(), Seller.SELLER_LOGOUT, handler);
								sellerThread.start();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showSellerDeleteAlertDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String message = "Ż�� �Ͻð� �ǽø� ����� ��� �����ʹ� ���� �Ǹ� ���� �Ұ��� �մϴ�. Ż���Ͻðڽ��ϱ�?";
		builder.setMessage(message)
				.setTitle("Ż��")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								progressDialog = ProgressDialog.show(Seller_setting.this,"Ż�� ��","��ø� ��ٷ��ּ���..",true);
								SellerThread sellerThread = new SellerThread(seller.getSeller_num(), Seller.SELLER_DELETE, handler);
								sellerThread.start();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});;
		AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_setting, menu);
		return true;
	}
	public class CategoryListAdapter extends ArrayAdapter<String>{
		private LayoutInflater mInflater;
		private Context context;
		public CategoryListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}
		
		public CategoryListAdapter(Context context,String[] objects) {
			super(context, 0, objects);
			this.context = context;
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public CategoryListAdapter(Context context,List<String> objects) {
			super(context, 0, objects);
			this.context = context;
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			View view = null;
			
			if(v==null){
				view = mInflater.inflate(R.layout.category_list_row, null);
			}else{
				view = v;
			}
			
			final String item = this.getItem(position);
			
			if(item != null){
				TextView tv_simple_item = (TextView)view.findViewById(R.id.tv_category_list_row);
				tv_simple_item.setText(item);
			}
			Button btn_remove = (Button)view.findViewById(R.id.btn_category_list_row);
			btn_remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String removeCategory = seller.getCategory().get(position);
					removeCategoryNum = Category.getSmallCategoryNumByName(removeCategory);
					showCategoryRemoveAlertDialog("ī�װ� ����", removeCategory+" ī�װ��� �����Ͻðڽ��ϱ�?", context);
				}
			});
			return view;
		}
	}
}
