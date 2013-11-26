package com.nero.howmuch.seller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nero.howmuch.Category;
import com.nero.howmuch.ImageAttachActivity;
import com.nero.howmuch.R;
import com.nero.howmuch.Reply;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.Utils;
import com.nero.howmuch.Xml_controler;
import com.nero.howmuch.consumer.ConsumerPost;
import com.nero.howmuch.consumer.ConsumerReplyAdapter;
import com.nero.howmuch.consumer.Consumer_detail;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.SellerPostDbAdapter;
import com.nero.howmuch.sqlite.SellerReplyDbAdapter;
import com.nero.howmuch.thread.ImageItem;
import com.nero.howmuch.thread.ImageThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Seller_detail extends Activity {
	
	//textview
	private TextView tv_seller_detail_title;
	private TextView tv_seller_detail_category;
	private TextView tv_seller_detail_city;
	private TextView tv_seller_detail_contents;
	
	//imageview
	private ImageView iv_seller_detail_image1;
	private ImageView iv_seller_detail_image2;
	private ImageView iv_seller_detail_image3;
	// ListView
	private SellerReplyAdapter ad_seller_reply;
	private ArrayList<Reply> replyList;
	
	//database
	private SellerPostDbAdapter postDbHelper;
	private SellerReplyDbAdapter replyDbHelper;
	//post 객체
	private ConsumerPost consumerPost;
	private ImageItem[] imageItems = new ImageItem[3];
	private Dialog image_view_dialog;
	//path
	private String path;

	// Sliding REPLY_LIST
	private LinearLayout ll_reply_list;
	private SlidingDrawer sd_reply_list;
	private boolean isReplyListOpen = false;
	private ListView lv_seller_detail_reply;
	// SLIDING BUTTON
	private Button btn_reply_list_close;
	private Button btn_seller_detail_reply_sd;
	//Button
	private Button btn_seller_detail_reply;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_detail);
		path = getFilesDir().getAbsolutePath();

		initWidget();
		initSlidingWidget();
		
		loadPost();
		
		fillPostData();
		fillReplyData();
		
	}
	private void initWidget(){
		tv_seller_detail_title = (TextView)findViewById(R.id.tv_seller_detail_title);
		tv_seller_detail_category = (TextView)findViewById(R.id.tv_seller_detail_category);
		tv_seller_detail_city = (TextView)findViewById(R.id.tv_seller_detail_city);
		tv_seller_detail_contents = (TextView)findViewById(R.id.tv_seller_detail_contents);
		
		//imageView
		iv_seller_detail_image1 = (ImageView)findViewById(R.id.iv_seller_detail_image1);
		iv_seller_detail_image2 = (ImageView)findViewById(R.id.iv_seller_detail_image2);
		iv_seller_detail_image3 = (ImageView)findViewById(R.id.iv_seller_detail_image3);
		
		
		//button setting
		btn_seller_detail_reply = (Button)findViewById(R.id.btn_seller_detail_reply);
		btn_seller_detail_reply.setOnClickListener(btnClickListener);
		btn_seller_detail_reply_sd = (Button)findViewById(R.id.btn_seller_detail_reply_sd);
		btn_seller_detail_reply_sd.setOnClickListener(btnClickListener);
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
		lv_seller_detail_reply = (ListView)ll_reply_list.findViewById(R.id.lv_reply_list);
		lv_seller_detail_reply.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent(Seller_detail.this, Seller_reply_view.class);
				intent.putExtra("reply", ad_seller_reply.getItem(pos));
				Seller_detail.this.startActivity(intent);
			}
		});
	}
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
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == btn_seller_detail_reply.getId()){
				//post id, consumer_name intent..
				if(SharedPreferenceUtil.getSellerPlane(Seller_detail.this)<=0){
					//alert dialog.
					showAlertDialog("답글쓰기", "종이비행기가 부족합니다. 충전하시겠습니까?", Seller_detail.this);
				}else{
					Intent intent = new Intent(Seller_detail.this, Seller_reply.class);
					intent.putExtra("post_id", consumerPost.getId());
					intent.putExtra("is_reply", false);
					intent.putExtra("consumer_name", consumerPost.getConsumer_name());
					intent.putExtra("contents", consumerPost.getContents());
					startActivity(intent);
				}
			}else if(v.getId()==btn_reply_list_close.getId()){
				if(isReplyListOpen){
					sd_reply_list.animateClose();
					isReplyListOpen = false;
				}
			}else if(v.getId()==btn_seller_detail_reply_sd.getId()){
				if(!isReplyListOpen){
					sd_reply_list.animateOpen();
					isReplyListOpen = true;
				}
			}
		}
	};
	public static void showAlertDialog(String title, String message,
			final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(context, Seller_setting.class);
								context.startActivity(intent);
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
	private void loadPost(){
		postDbHelper = new SellerPostDbAdapter(this);
		postDbHelper.open();
		consumerPost = postDbHelper.fetchPost(getIntent().getIntExtra("rowid", 0));
		postDbHelper.close();
	}
	private void loadReply(){
		replyDbHelper = new SellerReplyDbAdapter(this);
		replyDbHelper.open();
		replyList = replyDbHelper.fetchAllreply(consumerPost.getId());
		replyDbHelper.close();
	}
	private void fillPostData(){
		System.out.println(consumerPost.getTitle() +","+consumerPost.getCategory()+","+consumerPost.getCity()+","+consumerPost.getContents());
		tv_seller_detail_title.setText(consumerPost.getTitle());
		tv_seller_detail_category.setText(Category.CATEGORIES[Integer.parseInt(consumerPost.getCategory())]);
		tv_seller_detail_city.setText(consumerPost.getCity());
		tv_seller_detail_contents.setText(consumerPost.getContents());
		btn_seller_detail_reply_sd.setText("답글("+consumerPost.getReplyCnt()+")");
		if(consumerPost.getPost_images_name().size()>0){
			if (!consumerPost.getPost_images_name().get(0).equals("null")) {
				
				//image view 는 미리 생성. 그 뒤에 스레드로 이미지 불러오기.
				for(int i=0; i<consumerPost.getPost_images_name().size(); i++){
					try {
						URL url =new URL(Utils.imageUrl + "/"
								+ consumerPost.getPost_images_name().get(i));
						ImageThread imageThread = new ImageThread("load_image",url ,i, handler);
						imageThread.start();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			iv_seller_detail_image1.setImageResource(R.drawable.icon_no_image);
		}
	}
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			
			ImageItem imageItem = (ImageItem)msg.obj;
			if(imageItem.getCount()==0){
				imageItems[0] = imageItem;
				iv_seller_detail_image1.setImageBitmap(imageItem.getImgBitmap());
				iv_seller_detail_image1.setOnClickListener(imageClickListener);
			}else if(imageItem.getCount()==1){
				imageItems[1] = imageItem;
				iv_seller_detail_image2.setImageBitmap(imageItem.getImgBitmap());
				iv_seller_detail_image2.setOnClickListener(imageClickListener);
			}else if(imageItem.getCount()==2){
				imageItems[2] = imageItem;
				iv_seller_detail_image3.setImageBitmap(imageItem.getImgBitmap());
				iv_seller_detail_image3.setOnClickListener(imageClickListener);
			}
		}
	};
	private OnClickListener imageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == iv_seller_detail_image1.getId()){
//				showImageViewDialog(imageItems[0].getImgBitmap());
				Intent intent = new Intent(Seller_detail.this, ImageAttachActivity.class);
				intent.putExtra("type", ImageAttachActivity.URL);
				intent.putExtra("path", consumerPost.getPost_images_name().get(0));
				startActivity(intent);
			}else if(v.getId() == iv_seller_detail_image2.getId()){
				showImageViewDialog(imageItems[1].getImgBitmap());
			}else if(v.getId() == iv_seller_detail_image3.getId()){
				showImageViewDialog(imageItems[2].getImgBitmap());
			}
		}
	};
	private void showImageViewDialog(Bitmap bitmap){
		image_view_dialog = new Dialog(Seller_detail.this);
		image_view_dialog.setContentView(R.layout.image_view_dialog);
		image_view_dialog.setTitle("사진");
		image_view_dialog.setCancelable(true);
		ImageView iv = (ImageView)image_view_dialog.findViewById(R.id.iv_image_view_dialog);
		iv.setImageBitmap(bitmap);
		Button btn = (Button)image_view_dialog.findViewById(R.id.btn_image_view_close);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				image_view_dialog.dismiss();
			}
		});
		image_view_dialog.show();
	}
	private void fillReplyData(){
		replyList = new ArrayList<Reply>();
		
		ad_seller_reply = new SellerReplyAdapter(Seller_detail.this, replyList);
		lv_seller_detail_reply.setAdapter(ad_seller_reply);
		//reply info load
		loadReply();
		
		ArrayList<Reply> parentList = new ArrayList<Reply>();
		for(int i=0; i<replyList.size();i++){
			if(replyList.get(i).getPflag()==Reply.PARENT){
				parentList.add(replyList.get(i));
			}
		}
		
		
		for(int i=0; i<parentList.size();i++){
			ad_seller_reply.add(parentList.get(i));
			for(int j=0; j<replyList.size(); j++){
				if((replyList.get(j).getPflag()==Reply.CHILD) && (parentList.get(i).getId()==replyList.get(j).getParent())){
					ad_seller_reply.add(replyList.get(j));
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
