package com.nero.howmuch.consumer;

import java.util.ArrayList;

import com.nero.howmuch.Category;
import com.nero.howmuch.ImageAttachActivity;
import com.nero.howmuch.R;
import com.nero.howmuch.Reply;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.ConsumerReplyDbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class Consumer_detail extends Activity {

	// textview
	private TextView tv_consumer_detail_title;
	private TextView tv_consumer_detail_category;
	private TextView tv_consumer_detail_city;
	private TextView tv_consumer_detail_contents;

	// imageview
	private ImageView iv_consumer_detail_image1;
	private ImageView iv_consumer_detail_image2;
	private ImageView iv_consumer_detail_image3;

	// ListView
//	private ListView lv_consumer_detail_reply;
	private ConsumerReplyAdapter ad_consumer_reply;
	private ArrayList<Reply> replyList;

	// database
	private ConsumerPostDbAdapter postDbHelper;
	private ConsumerReplyDbAdapter replyDbHelper;
	// post 객체
	private ConsumerPost consumerPost;

	// path
	private String path;

	// Sliding REPLY_LIST
	private LinearLayout ll_reply_list;
	private SlidingDrawer sd_reply_list;
	private boolean isReplyListOpen = false;
	private ListView lv_consumer_detail_reply;
	// SLIDING BUTTON
	private Button btn_reply_list_close;
	private Button btn_consumer_detail_reply;
	private Dialog image_view_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumer_detail);
		path = getFilesDir().getAbsolutePath();
		// textview setting
		tv_consumer_detail_title = (TextView) findViewById(R.id.tv_consumer_detail_title);
		tv_consumer_detail_category = (TextView) findViewById(R.id.tv_consumer_detail_category);
		tv_consumer_detail_city = (TextView) findViewById(R.id.tv_consumer_detail_city);
		tv_consumer_detail_contents = (TextView) findViewById(R.id.tv_consumer_detail_contents);

		// imageView
		iv_consumer_detail_image1 = (ImageView) findViewById(R.id.iv_consumer_detail_image1);
		iv_consumer_detail_image2 = (ImageView) findViewById(R.id.iv_consumer_detail_image2);
		iv_consumer_detail_image3 = (ImageView) findViewById(R.id.iv_consumer_detail_image3);

		btn_consumer_detail_reply = (Button)findViewById(R.id.btn_consumer_detail_reply);
		btn_consumer_detail_reply.setOnClickListener(btnClickListener);
		
		loadPost();
		fillPostdata();
		// imageView click listener
		initSlidingWidget();
		fillReplyData();
		lv_consumer_detail_reply
				.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int pos, long arg3) {
						Intent intent = new Intent(Consumer_detail.this,
								Consumer_reply_view.class);
						intent.putExtra("reply", ad_consumer_reply.getItem(pos));
						Consumer_detail.this.startActivity(intent);
					}
				});
	}

	
	private void initSlidingWidget(){
		Window window = getWindow();
		LayoutInflater slidinginflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ll_reply_list = (LinearLayout) slidinginflater.inflate(
				R.layout.sliding_reply_list, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		window.addContentView(ll_reply_list, params);
		sd_reply_list = (SlidingDrawer) ll_reply_list
				.findViewById(R.id.sd_reply_list);
		
		btn_reply_list_close = (Button)ll_reply_list.findViewById(R.id.btn_reply_list_close);
		btn_reply_list_close.setOnClickListener(btnClickListener);
		lv_consumer_detail_reply = (ListView)ll_reply_list.findViewById(R.id.lv_reply_list);
	}
	
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==btn_reply_list_close.getId()){
				if(isReplyListOpen){
					sd_reply_list.animateClose();
					isReplyListOpen = false;
				}
			}else if(v.getId()==btn_consumer_detail_reply.getId()){
				if(!isReplyListOpen){
					sd_reply_list.animateOpen();
					isReplyListOpen = true;
				}
			}
		}
	};
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (isReplyListOpen) {
			sd_reply_list.animateClose();
			isReplyListOpen = false;
		} else {
			super.onBackPressed();
		}
	}
	private void loadPost() {
		postDbHelper = new ConsumerPostDbAdapter(this);
		postDbHelper.open();
		consumerPost = postDbHelper.fetchPost(getIntent().getIntExtra("rowid",
				0));
		postDbHelper.close();
	}

	private void fillPostdata() {
		tv_consumer_detail_title.setText(consumerPost.getTitle());
		tv_consumer_detail_category.setText(Category.CATEGORIES[Integer
				.parseInt(consumerPost.getCategory())]);
		tv_consumer_detail_city.setText(consumerPost.getCity());
		tv_consumer_detail_contents.setText(consumerPost.getContents());
		btn_consumer_detail_reply.setText("답글("+consumerPost.getReplyCnt()+")");
		if (consumerPost.getPost_images_name().size() > 0) {
			for (int i = 0; i < consumerPost.getPost_images_name().size(); i++) {
				if (i == 0) {
					consumerPost.getPost_images().add(
							BitmapFactory
									.decodeFile(path
											+ "/"
											+ consumerPost
													.getPost_images_name().get(
															i)));
					iv_consumer_detail_image1.setImageBitmap(consumerPost
							.getPost_images().get(i));
					iv_consumer_detail_image1
							.setOnClickListener(ivClickListener);
				} else if (i == 1) {
					consumerPost.getPost_images().add(
							BitmapFactory
									.decodeFile(path
											+ "/"
											+ consumerPost
													.getPost_images_name().get(
															i)));
					iv_consumer_detail_image2.setImageBitmap(consumerPost
							.getPost_images().get(i));
					iv_consumer_detail_image2
							.setOnClickListener(ivClickListener);
				} else if (i == 2) {
					consumerPost.getPost_images().add(
							BitmapFactory
									.decodeFile(path
											+ "/"
											+ consumerPost
													.getPost_images_name().get(
															i)));
					iv_consumer_detail_image3.setImageBitmap(consumerPost
							.getPost_images().get(i));
					iv_consumer_detail_image3
							.setOnClickListener(ivClickListener);
				}
			}
			// iv_consumer_detail_image.setImageBitmap(BitmapFactory.decodeFile(path+"/"+consumerPost.getPost_images_name().get(0)));
		} else {
			iv_consumer_detail_image1
					.setImageResource(R.drawable.icon_no_image);
		}
	}

	private OnClickListener ivClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == iv_consumer_detail_image1.getId()) {
				Intent intent = new Intent(Consumer_detail.this, ImageAttachActivity.class);
				intent.putExtra("type", ImageAttachActivity.SDCARD);
				intent.putExtra("path", consumerPost.getPost_images_name().get(0));
				startActivity(intent);
//				showImageViewDialog(consumerPost.getPost_images().get(0));
			} else if (v.getId() == iv_consumer_detail_image2.getId()) {
				showImageViewDialog(consumerPost.getPost_images().get(1));
			} else if (v.getId() == iv_consumer_detail_image3.getId()) {
				showImageViewDialog(consumerPost.getPost_images().get(2));
			}
		}
	};

	private void showImageViewDialog(Bitmap bitmap) {
		image_view_dialog = new Dialog(Consumer_detail.this);
		image_view_dialog.setContentView(R.layout.image_view_dialog);
		image_view_dialog.setTitle("사진");
		image_view_dialog.setCancelable(true);
		ImageView iv = (ImageView) image_view_dialog
				.findViewById(R.id.iv_image_view_dialog);
		iv.setImageBitmap(bitmap);
		Button btn = (Button) image_view_dialog
				.findViewById(R.id.btn_image_view_close);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				image_view_dialog.dismiss();
			}
		});
		image_view_dialog.show();
	}

	private void loadReply() {
		replyDbHelper = new ConsumerReplyDbAdapter(this);
		replyDbHelper.open();
		replyList = replyDbHelper.fetchAllreply(consumerPost.getId());
		replyDbHelper.close();
	}

	private void fillReplyData() {
		replyList = new ArrayList<Reply>();

		ad_consumer_reply = new ConsumerReplyAdapter(Consumer_detail.this,
				replyList);
		lv_consumer_detail_reply.setAdapter(ad_consumer_reply);
		// reply info load
		loadReply();

		ArrayList<Reply> parentList = new ArrayList<Reply>();
		for(int i=0; i<replyList.size();i++){
			if(replyList.get(i).getPflag()==Reply.PARENT){
				parentList.add(replyList.get(i));
			}
		}
		
		
		for(int i=0; i<parentList.size();i++){
			ad_consumer_reply.add(parentList.get(i));
			for(int j=0; j<replyList.size(); j++){
				if((replyList.get(j).getPflag()==Reply.CHILD) && (parentList.get(i).getId()==replyList.get(j).getParent())){
					ad_consumer_reply.add(replyList.get(j));
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_detail, menu);
		return true;
	}

	@Override
	protected void onResume() {
		fillReplyData();
		super.onResume();
	}
}
