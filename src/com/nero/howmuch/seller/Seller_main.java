package com.nero.howmuch.seller;

import java.util.ArrayList;

import com.nero.howmuch.MainActivity;
import com.nero.howmuch.R;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.consumer.ConsumerPost;
import com.nero.howmuch.consumer.ConsumerPostAdapter;
import com.nero.howmuch.consumer.Consumer_main;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.SellerPostDbAdapter;

import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Seller_main extends Activity {

	private Button btn_seller_setting;
	
	// ListView
	private ListView lv_seller_request;
	private SellerPostAdapter ad_seller_post;
	private ArrayList<ConsumerPost> postList;
	
	private SellerPostDbAdapter postDbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_main);
		btn_seller_setting= (Button)findViewById(R.id.btn_seller_setting);
		btn_seller_setting.setOnClickListener(btnClickListener);
		
		
		// listview setting
		lv_seller_request = (ListView) findViewById(R.id.lv_seller_request);
		lv_seller_request.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				Intent intent = new Intent(Seller_main.this,
						Seller_detail.class);
				System.out.println("seller main select id:"+postList.get(pos).getId());
				intent.putExtra("rowid", postList.get(pos).getId());
				startActivity(intent);
			}
		});
		lv_seller_request.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				showPostRemoveAlertDialog("삭제", "해당 글을 삭제하시겠습니까?",postList.get(position).getId(), Seller_main.this);
				return false;
			}
		});
		filldata();
	}
	@Override
	protected void onResume() {
		if (Xml_controler.getSellerDateFromXml(Seller_main.this)
				.getSeller_name().equals("empty")) {
			finish();
		}
		filldata();
		super.onResume();
	}

	private void filldata() {
		// post list setting
		// database open
		postDbHelper = new SellerPostDbAdapter(Seller_main.this);
		postDbHelper.open();
		// database 에서 모든 post가져오기
		postList = new ArrayList<ConsumerPost>();
				
		// post를 받기위한 post어댑터 객체 생성
		ad_seller_post = new SellerPostAdapter(this, postList);
		// connect adapter to listView
		lv_seller_request.setAdapter(ad_seller_post);
		
		postList = postDbHelper.fetchAllPosts();
		Log.d("size", String.valueOf(postList.size()));
		for(int i=0; i<postList.size(); i++){
			ad_seller_post.add(postList.get(i));
		}
		postDbHelper.close();
	}
	private OnClickListener btnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId()==btn_seller_setting.getId()){
				Intent intent = new Intent(Seller_main.this, Seller_setting.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_main, menu);
		return true;
	}
	private void showPostRemoveAlertDialog(String title, String message,final int post_id,final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								SellerPostDbAdapter adapter = new SellerPostDbAdapter(context);
								adapter.open();
								adapter.deletePost(post_id);
								adapter.close();
								filldata();
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
}
