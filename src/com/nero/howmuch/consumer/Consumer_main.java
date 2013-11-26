package com.nero.howmuch.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nero.howmuch.MainActivity;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.seller.Seller;
import com.nero.howmuch.seller.Seller_setting;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.ConsumerReplyDbAdapter;
import com.nero.howmuch.sqlite.SellerPostDbAdapter;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Consumer_main extends Activity {
	private static final String CONSUMER_LOGOUT = "consumer_logout";
	// Button
	private Button btn_consumer_new_request;
	private Button btn_consumer_main_logout;
	// ListView
	private ListView lv_consumer_request;
	private ConsumerPostAdapter ad_consumer_post;
	private ArrayList<ConsumerPost> postList;
	// database
	private ConsumerPostDbAdapter postDbHelper;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumer_main);

		// button setting
		btn_consumer_new_request = (Button) findViewById(R.id.btn_consumer_new_request);
		btn_consumer_new_request.setOnClickListener(btnClickListener);
		btn_consumer_main_logout = (Button) findViewById(R.id.btn_consumer_main_logout);
		btn_consumer_main_logout.setOnClickListener(btnClickListener);

		// listview setting
		lv_consumer_request = (ListView) findViewById(R.id.lv_consumer_request);
		lv_consumer_request.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				Intent intent = new Intent(Consumer_main.this,
						Consumer_detail.class);
				intent.putExtra("rowid", postList.get(pos).getId());
				startActivity(intent);
			}
		});
		lv_consumer_request
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						showPostRemoveAlertDialog("삭제", "해당 글을 삭제하시겠습니까?",
								postList.get(position).getId(),
								Consumer_main.this);
						return false;
					}
				});
		filldata();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		filldata();
		super.onResume();
	}

	private void filldata() {
		// post list setting
		postDbHelper = new ConsumerPostDbAdapter(Consumer_main.this);
		postDbHelper.open();
		// database 에서 모든 post가져오기
		postList = new ArrayList<ConsumerPost>();

		// post를 받기위한 post어댑터 객체 생성
		ad_consumer_post = new ConsumerPostAdapter(this, postList);
		// connect adapter to listView
		lv_consumer_request.setAdapter(ad_consumer_post);

		postList = postDbHelper.fetchAllPosts();
		for (int i = 0; i < postList.size(); i++) {
			ad_consumer_post.add(postList.get(i));
		}
		postDbHelper.close();
	}

	private OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == btn_consumer_new_request.getId()) {
				Intent intent = new Intent(Consumer_main.this,
						Consumer_post.class);
				startActivity(intent);
			} else if (v.getId() == btn_consumer_main_logout.getId()) {
				showConsumerLogoutAlertDialog("탈퇴", Consumer_main.this);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_main, menu);
		return true;
	}

	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				progressDialog.dismiss();
				showAlertDialog("오류", "서버 응답이 없습니다.", Consumer_main.this, false);
			} else {
				List<Map<String, String>> items = (List<Map<String, String>>) msg.obj;
				if (items.size() != 0) {
					if (items.get(0).get("title").equals(CONSUMER_LOGOUT)) {
						if (items.get(0).get("result").equals("true")) {
							progressDialog.dismiss();
							consumerLogout();
						} else {
							progressDialog.dismiss();
							showAlertDialog("오류", "전송 실패", Consumer_main.this,
									false);
						}
					}
				} else {
					progressDialog.dismiss();
					showAlertDialog("오류", "전송 실패", Consumer_main.this, false);
				}
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
								if (isCorrect) {
									Intent intent = new Intent(context,
											Consumer_main.class);
									dialog.dismiss();
									context.startActivity(intent);
								} else {
									dialog.cancel();
								}

							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showPostRemoveAlertDialog(String title, String message,
			final int post_id, final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								ConsumerPostDbAdapter adapter = new ConsumerPostDbAdapter(
										context);
								adapter.open();
								adapter.deletePost(post_id);
								adapter.close();
								filldata();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showConsumerLogoutAlertDialog(String title,
			final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String message = "탈퇴를 하시게 되시면 저장된 모든 데이터는 삭제 되며 복구 불가능 합니다. 탈퇴하시겠습니까?";
		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								progressDialog = ProgressDialog.show(Consumer_main.this,
										"로그아웃", "잠시만 기다려주세요");
								ConsumerThread consumerThread = new ConsumerThread(
										SharedPreferenceUtil
												.getConsumerSharedPreference(Consumer_main.this),
										Consumer.CONSUMER_LOGOUT, handler);
								consumerThread.start();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void consumerLogout() {
		Xml_controler.deleteConusmerDataXml(this);
		ConsumerPostDbAdapter consumerPostDbAdapter = new ConsumerPostDbAdapter(
				this);
		consumerPostDbAdapter.open();
		consumerPostDbAdapter.deleteAll();
		consumerPostDbAdapter.close();
		ConsumerReplyDbAdapter consumerReplyDbAdapter = new ConsumerReplyDbAdapter(
				this);
		consumerReplyDbAdapter.open();
		consumerReplyDbAdapter.deleteAll();
		consumerReplyDbAdapter.close();
		finish();
	}
}
