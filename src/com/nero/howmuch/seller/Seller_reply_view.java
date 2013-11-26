package com.nero.howmuch.seller;

import java.net.MalformedURLException;
import java.net.URL;

import com.nero.howmuch.ImageAttachActivity;
import com.nero.howmuch.R;
import com.nero.howmuch.Reply;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.Utils;
import com.nero.howmuch.R.layout;
import com.nero.howmuch.R.menu;
import com.nero.howmuch.consumer.Consumer_detail;
import com.nero.howmuch.thread.ImageItem;
import com.nero.howmuch.thread.ImageThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Seller_reply_view extends Activity {
	
	private TextView tv_seller_reply_view_name;
	private TextView tv_seller_reply_view_contents;
	private ImageView iv_seller_reply_view_image;
	
	private Button btn_seller_reply_view_reply;
	private Reply reply;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seller_reply_view);
		initWidget();
		
		Bundle bundle = getIntent().getExtras();
		reply = bundle.getParcelable("reply");
		
		fillData();
	}
	private void initWidget(){
		tv_seller_reply_view_name = (TextView)findViewById(R.id.tv_seller_reply_view_seller_name);
		tv_seller_reply_view_contents = (TextView)findViewById(R.id.tv_seller_reply_view_contents);
		
		iv_seller_reply_view_image = (ImageView)findViewById(R.id.iv_seller_reply_view_image);
		
		btn_seller_reply_view_reply = (Button)findViewById(R.id.btn_seller_reply_view_reply);
		btn_seller_reply_view_reply.setOnClickListener(btnClickListener);
	}
	private void fillData(){
		tv_seller_reply_view_contents.setText(reply.getContents());
		if(reply.getSend_flag()==0){
			tv_seller_reply_view_name.setText(reply.getConsumer_name());
			btn_seller_reply_view_reply.setVisibility(View.VISIBLE);
		}else if(reply.getSend_flag()==1){
			tv_seller_reply_view_name.setText(reply.getSeller_name());
			btn_seller_reply_view_reply.setVisibility(View.INVISIBLE);
		}
		if(reply.getReply_images_name().size()>0){
			if (!reply.getReply_images_name().get(0).equals("null")) {
				//사진이 있을경우 사진 아이콘 설정.
				try {
					URL url =new URL(Utils.imageUrl + "/"
							+ reply.getReply_images_name().get(0));
					ImageThread imageThread = new ImageThread("load_image",url ,0, handler);
					imageThread.start();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else{
				iv_seller_reply_view_image
				.setImageResource(R.drawable.icon_no_image);
			}
		}else{
			iv_seller_reply_view_image
			.setImageResource(R.drawable.icon_no_image);
		}
	}
	
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==btn_seller_reply_view_reply.getId()){
				if(SharedPreferenceUtil.getSellerPlane(Seller_reply_view.this)<=0){
					//alert dialog.
					showAlertDialog("답글쓰기", "종이비행기가 부족합니다. 충전하시겠습니까?", Seller_reply_view.this);
				}else{
					Intent intent = new Intent(Seller_reply_view.this, Seller_reply.class);
					intent.putExtra("reply", reply);
					intent.putExtra("is_reply", true);
					startActivity(intent);
				}
			}else if(v.getId() == iv_seller_reply_view_image.getId()){
				Intent intent = new Intent(Seller_reply_view.this, ImageAttachActivity.class);
				intent.putExtra("type", ImageAttachActivity.URL);
				intent.putExtra("path", reply.getReply_images_name().get(0));
				startActivity(intent);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seller_reply_view, menu);
		return true;
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			ImageItem imageItem = (ImageItem)msg.obj;
			iv_seller_reply_view_image.setImageBitmap(imageItem.getImgBitmap());
			iv_seller_reply_view_image.setOnClickListener(btnClickListener);
			
		}
	};
}
