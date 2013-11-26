package com.nero.howmuch;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.nero.howmuch.consumer.Consumer;
import com.nero.howmuch.consumer.ConsumerPost;
import com.nero.howmuch.consumer.Consumer_main;
import com.nero.howmuch.seller.Seller;
import com.nero.howmuch.seller.Seller_main;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.sqlite.ConsumerReplyDbAdapter;
import com.nero.howmuch.sqlite.SellerPostDbAdapter;
import com.nero.howmuch.sqlite.SellerReplyDbAdapter;
import com.nero.howmuch.thread.ConsumerThread;
import com.nero.howmuch.thread.SellerThread;

/********************************/
/* 푸쉬 알람을 받기위한 클래스         */
/********************************/
public class GCMIntentService extends GCMBaseIntentService{
	
	
	//새로운 글 Notification(알람) 을 띄어주는 메소드
	private static void newPostNotification(Context context, ConsumerPost post) {
		int icon = com.nero.howmuch.R.drawable.ic_plane2;
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder notification = new Notification.Builder(context);
		
		//메세지 설
		String ticker = context.getString(com.nero.howmuch.R.string.app_name);
		String title = post.getConsumer_name()+"님의 메세지";
		String message = post.getTitle();
		Intent intent = new Intent(context, Seller_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		notification.setTicker(ticker);
		notification.setAutoCancel(true);
		notification.setSmallIcon(icon);
		notification.setContentTitle(title);
		notification.setContentText(message);
		notification.setContentIntent(pendingIntent);
		notificationManager.notify(0, notification.getNotification());
	}
	//새로운 판매자의 답글 Notification(알람) 을 띄어주는 메소드
	private static void newReplyOfSellerNotification(Context context, Reply reply) {
		int icon = com.nero.howmuch.R.drawable.ic_plane2;
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder notification = new Notification.Builder(context);
		
		String ticker = context.getString(com.nero.howmuch.R.string.app_name);
		String title = reply.getSeller_name()+"님의 답글";
		String message = reply.getContents();
		Intent intent = new Intent(context, Consumer_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		notification.setTicker(ticker);
		notification.setAutoCancel(true);
		notification.setSmallIcon(icon);
		notification.setContentTitle(title);
		notification.setContentText(message);
		notification.setContentIntent(pendingIntent);
		notificationManager.notify(0, notification.getNotification());
	}
	//새로운 구매자의 답글 Notification(알람) 을 띄어주는 메소드
	private static void newReplyOfConsumerNotification(Context context, Reply reply) {
		int icon = com.nero.howmuch.R.drawable.ic_plane2;
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder notification = new Notification.Builder(context);
		
		String ticker = context.getString(com.nero.howmuch.R.string.app_name);
		String title = reply.getConsumer_name()+"님의 답글";
		String message = reply.getContents();
		Intent intent = new Intent(context, Seller_main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		notification.setTicker(ticker);
		notification.setAutoCancel(true);
		notification.setSmallIcon(icon);
		notification.setContentTitle(title);
		notification.setContentText(message);
		notification.setContentIntent(pendingIntent);
		notificationManager.notify(0, notification.getNotification());
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	//GCM push를 받는 메소드
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String msg = null;
		String action = intent.getStringExtra("action");
		Log.d("gcm onmessage", action);
		if(action.equals("seller_receive_post")){
			//판매자가 새로운 글을 받은 경우
			String post_id = intent.getStringExtra("post_id");
			System.out.println("seller receive post id:"+post_id);
			SellerThread sellerThread = new SellerThread(post_id, Seller.SELLER_REQUEST_POST, handler);
			sellerThread.start();
		}else if(action.equals(Seller.SELLER_SEND_REPLY)){
			//판매자로 부터 답장을 수신했을 때
			ConsumerPostDbAdapter adapter = new ConsumerPostDbAdapter(context);
			ConsumerPost consumerPost = null;
			adapter.open();
			String post_id = intent.getStringExtra("post_id");
			consumerPost = adapter.fetchPost(Integer.parseInt(post_id));
			adapter.close();
			if(consumerPost!=null){
				String id = intent.getStringExtra("reply_id");
				if(id.equals("null")){
					//수신 에러..
					Log.d("Reply Receive Error", "receive error");
				}else{
					ConsumerThread consumerThread = new ConsumerThread(Integer.parseInt(id), Consumer.CONSUMER_REQUEST_REPLY, handler);
					consumerThread.start();
				}
			}
			
			
		}else if(action.equals(Consumer.CONSUMER_SEND_REPLY)){
			//구매자로 부터 답장을 수신했을 때
			SellerPostDbAdapter adapter = new SellerPostDbAdapter(context);
			ConsumerPost consumerPost = null;
			adapter.open();
			String post_id = intent.getStringExtra("post_id");
			consumerPost = adapter.fetchPost(Integer.parseInt(post_id));
			adapter.close();
			if(consumerPost!=null){
				String id = intent.getStringExtra("reply_id");
				if(id.equals("null")){
					//수신 에러..
					Log.d("Reply Receive Error", "receive error");
				}else{
					SellerThread sellerThread = new SellerThread(Integer.parseInt(id), Seller.SELLER_REQUEST_REPLY, handler);
					sellerThread.start();
				}
			}
		}
	}

	@Override
	protected void onRegistered(Context context, String reg_id) {
		// TODO Auto-generated method stub
		Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	//스레드의 액션에 따른 결과값처리를 위한 핸들러
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what==1){
				//판매자가 구매자의 post수신.
				ConsumerPost items = null;
				items = (ConsumerPost) msg.obj;
				savePostDb(items, getApplicationContext());
				newPostNotification(getApplicationContext(),items);
			}else if(what==2){
				//구매자가 판매자 댓글수신
				Reply items = null;
				items = (Reply) msg.obj;
				saveReplyOfSeller(items, getApplicationContext());
				newReplyOfSellerNotification(getApplicationContext(), items);
			}else if(what==3){
				//판매자가 구매자 댓글 수신
				Reply items = null;
				items = (Reply)msg.obj;
				saveReplyOfConsumer(items, getApplicationContext());
				newReplyOfConsumerNotification(getApplicationContext(), items);
			}
		}
	};
	private boolean saveReplyOfSeller(Reply reply, Context context){
		boolean result = false;
		ConsumerReplyDbAdapter dbAdapter = new ConsumerReplyDbAdapter(context);
		reply.setSend_flag(0);
		dbAdapter.open();
		if(dbAdapter.addReply(reply)>0){
			result =true;
		}else{
			result = false;
		}
		dbAdapter.close();
		return result;
	}
	private boolean saveReplyOfConsumer(Reply reply, Context context){
		boolean result = false;
		SellerReplyDbAdapter dbAdapter = new SellerReplyDbAdapter(context);
		reply.setSend_flag(0); //0이면 consumer로부터 받은거.
		dbAdapter.open();
		if(dbAdapter.addReply(reply)>0){
			result = true;
		}else{
			result = false;
		}
		dbAdapter.close();
		return result;
	}
	private boolean savePostDb(ConsumerPost consumerPost, Context context){
		boolean result=false;
		SellerPostDbAdapter sellerPostDbHelper = new SellerPostDbAdapter(context);
		sellerPostDbHelper.open();
       if(sellerPostDbHelper.addPost(consumerPost)>0){
    	   result = true;
       }else{
    	  result =  false;
       }
		sellerPostDbHelper.close();
		return result;
	}
}
