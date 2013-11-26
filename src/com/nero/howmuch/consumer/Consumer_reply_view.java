package com.nero.howmuch.consumer;

import java.net.MalformedURLException;
import java.net.URL;

import com.nero.howmuch.R;
import com.nero.howmuch.R.layout;
import com.nero.howmuch.R.menu;
import com.nero.howmuch.ImageAttachActivity;
import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;
import com.nero.howmuch.seller.Seller_reply_view;
import com.nero.howmuch.thread.ImageItem;
import com.nero.howmuch.thread.ImageThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Consumer_reply_view extends Activity {
	
	private TextView tv_consumer_reply_view_seller_name;
	private TextView tv_consumer_reply_view_seller_num;
	private TextView tv_consumer_reply_view_seller_phone;
	private TextView tv_consumer_reply_view_contents;
	private ImageView iv_consumer_reply_view_image;
	
	private Button btn_consumer_reply_view_reply;
	
	private Reply reply;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumer_reply_view);
		
		initWidget();
		
		Bundle bundle = getIntent().getExtras();
		reply = bundle.getParcelable("reply");
		
		fillData();
	}

	private void initWidget(){
		tv_consumer_reply_view_seller_name = (TextView)findViewById(R.id.tv_consumer_reply_view_seller_name);
		tv_consumer_reply_view_seller_num = (TextView)findViewById(R.id.tv_consumer_reply_view_seller_num);
		tv_consumer_reply_view_seller_phone = (TextView)findViewById(R.id.tv_consumer_reply_view_seller_phone);
		tv_consumer_reply_view_contents = (TextView)findViewById(R.id.tv_consumer_reply_view_contents);
		
		iv_consumer_reply_view_image = (ImageView)findViewById(R.id.iv_consumer_reply_view_image);
		
		btn_consumer_reply_view_reply = (Button)findViewById(R.id.btn_consumer_reply_view_reply);
		btn_consumer_reply_view_reply.setOnClickListener(btnClickListener);
	}
	private void fillData(){
		tv_consumer_reply_view_contents.setText(reply.getContents());
		if(reply.getSend_flag()==0){
			tv_consumer_reply_view_seller_name.setText("사업자명  : "+reply.getSeller_name());
			btn_consumer_reply_view_reply.setVisibility(View.VISIBLE);
			tv_consumer_reply_view_seller_num.setText("사업자번호 : "+reply.getSeller_num());
			tv_consumer_reply_view_seller_phone.setText("전화번호  :"+reply.getSeller_phone());
		}else if(reply.getSend_flag()==1){
			tv_consumer_reply_view_seller_name.setText(reply.getConsumer_name()+" -> "+reply.getSeller_name());
			btn_consumer_reply_view_reply.setVisibility(View.INVISIBLE);
			tv_consumer_reply_view_seller_num.setVisibility(View.INVISIBLE);
			tv_consumer_reply_view_seller_phone.setVisibility(View.INVISIBLE);
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
				iv_consumer_reply_view_image
				.setImageResource(R.drawable.icon_no_image);
			}
		}else{
			iv_consumer_reply_view_image
			.setImageResource(R.drawable.icon_no_image);
		}
	}
	
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==btn_consumer_reply_view_reply.getId()){
				Intent intent = new Intent(Consumer_reply_view.this, Consumer_reply.class);
				intent.putExtra("reply", reply);
				startActivity(intent);
			}else if(v.getId() == iv_consumer_reply_view_image.getId()){
				Intent intent = new Intent(Consumer_reply_view.this, ImageAttachActivity.class);
				intent.putExtra("type", ImageAttachActivity.URL);
				intent.putExtra("path", reply.getReply_images_name().get(0));
				startActivity(intent);
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_reply_view, menu);
		return true;
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			ImageItem imageItem = (ImageItem)msg.obj;
			iv_consumer_reply_view_image.setImageBitmap(imageItem.getImgBitmap());
			iv_consumer_reply_view_image.setOnClickListener(btnClickListener);
			
		}
	};
}
