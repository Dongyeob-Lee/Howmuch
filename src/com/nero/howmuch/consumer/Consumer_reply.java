package com.nero.howmuch.consumer;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nero.howmuch.HowmuchUtils;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.image.ImageEditActivity;
import com.nero.howmuch.seller.Seller_reply;
import com.nero.howmuch.sqlite.ConsumerReplyDbAdapter;
import com.nero.howmuch.thread.ConsumerThread;
import com.nero.howmuch.Reply;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Consumer_reply extends Activity {
	public static final int EDIT_FROM_EDITOR = 100;
	public static final int PICK_FROM_CAMERA = 101;
	public static final int PICK_FROM_ALBUM = 102;
	// textView
	private TextView tv_consumer_reply_contents;
	private TextView tv_consumer_reply_seller_name;

	// EditText
	private EditText et_consumer_reply_contents;

	// imageView
	private ImageView iv_conusmer_reply_img;

	// Button
	private Button btn_consumer_reply_send;
	private Button btn_consumer_reply_img;

	// Reply object
	private Reply receiveReply;
	private Reply sendReply;

	// progress dialog
	private ProgressDialog progressDialog;

	static Context mContext;

	// date
	Date date = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	
	private ArrayList<Bitmap> cur_bitmap_list = new ArrayList<Bitmap>();
	private String path;
	private static final String TEMP_IMAGE = "tmp_img.jpeg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumer_reply);
		path = this.getFilesDir().getAbsolutePath();
		mContext = this;

		initWidget();

		Bundle bundle = getIntent().getExtras();
		receiveReply = bundle.getParcelable("reply");

		fillData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_reply, menu);
		return true;
	}

	private void fillData() {
		tv_consumer_reply_contents.setText(receiveReply.getContents());
		tv_consumer_reply_seller_name.setText(receiveReply.getSeller_name());

//		if (receiveReply.getReply_images_name().size() > 0) {
//			if (!receiveReply.getReply_images_name().get(0).equals("null")) {
//				URL imageURL;
//
//				try {
//					imageURL = new URL(Utils.imageUrl + "/"
//							+ receiveReply.getReply_images_name().get(0));
//
//					HttpURLConnection httpConn = (HttpURLConnection) imageURL
//							.openConnection();
//					BufferedInputStream bis = new BufferedInputStream(
//							httpConn.getInputStream(), 10240);
//					Bitmap bm = BitmapFactory.decodeStream(bis);
//					bis.close();
//					iv_conusmer_reply_img.setImageBitmap(bm);
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}

	private void initWidget() {
		tv_consumer_reply_contents = (TextView) findViewById(R.id.tv_consumer_reply_contents);
		tv_consumer_reply_seller_name = (TextView) findViewById(R.id.tv_consumer_reply_seller_name);

		et_consumer_reply_contents = (EditText) findViewById(R.id.et_consumer_reply_contents);

		iv_conusmer_reply_img = (ImageView) findViewById(R.id.iv_consumer_reply_img);

		btn_consumer_reply_send = (Button) findViewById(R.id.btn_consumer_reply_send);
		btn_consumer_reply_img = (Button) findViewById(R.id.btn_consumer_reply_img);
		btn_consumer_reply_send.setOnClickListener(btnClickListener);
		btn_consumer_reply_img.setOnClickListener(btnClickListener);
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
//			case EDIT_FROM_EDITOR:
//				byte[] bb = data.getByteArrayExtra("bitmap");
//				Bitmap tmp_bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.length);
//				cur_bitmap_list.add(tmp_bitmap);
//				iv_conusmer_reply_img.setImageBitmap(cur_bitmap_list.get(0));
//				break;
			case PICK_FROM_ALBUM :
//				Intent intent = new Intent(Consumer_post.this, ImageEditActivity.class);
//				intent.putExtra("output", data.getData());
//				intent.putExtra("flag", "album");
//				startActivityForResult(intent, EDIT_FROM_EDITOR);
				if (!addImage(data)) {
					Toast.makeText(Consumer_reply.this, "사진 첨부 실패",
							Toast.LENGTH_LONG).show();
				}
				break;
//			case PICK_FROM_CAMERA :
//				Intent intent2 = new Intent(Consumer_reply.this, ImageEditActivity.class);
//				intent2.putExtra("output", path+"/"+TEMP_IMAGE);
//				intent2.putExtra("flag", "camera");
//				startActivityForResult(intent2, EDIT_FROM_EDITOR);
//				break;
			}
		}
	}
	private boolean addImage(Intent data) {
		String imgFile = data.getData().getLastPathSegment() + ".jpeg";
		try {
			AssetFileDescriptor afd = getContentResolver()
					.openAssetFileDescriptor(data.getData(), "r");
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 4;

			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(
					afd.getFileDescriptor(), null, opt); // 臾몄��諛���� ��닿껐 肄����
			if (bitmap == null) {
				return false;
			} else {
				cur_bitmap_list.add(bitmap);
				refreshImageView();
				return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	private void removeAllImageView(){
		iv_conusmer_reply_img.setImageBitmap(null);
//		iv_consumer_post_img2.setImageBitmap(null);
//		iv_consumer_post_img3.setImageBitmap(null);
	}
	private void refreshImageView(){
		removeAllImageView();
		for(int i=0; i<cur_bitmap_list.size(); i++){
			if(i==0){
				iv_conusmer_reply_img.setImageBitmap(cur_bitmap_list.get(i));
			}
//			else if(i==1){
//				iv_consumer_post_img2.setImageBitmap(cur_bitmap_list.get(i));
//			}else if(i==2){
//				iv_consumer_post_img3.setImageBitmap(cur_bitmap_list.get(i));
//			}
		}
	}

	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// thread.. reply send to seller
			if (v.getId() == btn_consumer_reply_send.getId()) {
				if(et_consumer_reply_contents.getText().toString().equals("")){
					Toast.makeText(Consumer_reply.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT	).show();
				}else{
					sendReply();
				}
			}else if(v.getId()==btn_consumer_reply_img.getId()){
				doTakeAlbumAction();
			}
		}
	};
	private void sendReply(){
		String path = Consumer_reply.this.getFilesDir()
				.getAbsolutePath();
		sendReply = new Reply();
		
		if(receiveReply.getPflag()==Reply.PARENT){
			sendReply.setParent(receiveReply.getId());
		}else if(receiveReply.getPflag()==Reply.CHILD){
			sendReply.setParent(receiveReply.getParent());
		}
		sendReply.setPflag(Reply.CHILD);
		sendReply.setPost_id(receiveReply.getPost_id());
		sendReply.setSeller_name(receiveReply.getSeller_name());
		sendReply.setSeller_num(receiveReply.getSeller_num());
		sendReply.setSeller_phone(receiveReply.getSeller_phone());
		sendReply.setConsumer_name(SharedPreferenceUtil
				.getConsumerSharedPreference(mContext));
		sendReply.setDate(dateFormat.format(date));
		sendReply.setContents(et_consumer_reply_contents.getText()
				.toString());
		for(int i=0; i<cur_bitmap_list.size(); i++){
			String realImageName = HowmuchUtils.randomChar()+"_"+String.valueOf(System.currentTimeMillis())+".jpeg";
			HowmuchUtils.SaveBitmapToFileCache(cur_bitmap_list.get(i),path+"/"+realImageName);
			sendReply.getReply_images_name().add(realImageName);
		}
		ConsumerThread consumerThread = new ConsumerThread(sendReply,
				path, Consumer.CONSUMER_SEND_REPLY, handler);
		progressDialog = ProgressDialog.show(Consumer_reply.this,
				"메세지 전송 중", "잠시만 기다려주세요..", true);
		consumerThread.start();
	}
	private void doTakeAlbumAction() {
		//choose from gallery
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 100) {
				progressDialog.dismiss();
				showAlertDialog("오류", "서버로부터 응답이 없습니다.잠시후에 다시시도해 주십시오",
						Consumer_reply.this, false);
				return;
			}

			List<Map<String, String>> items = (List<Map<String, String>>) msg.obj;
			for (int i = 0; i < items.size(); i++) {

				System.out.println(items.get(i).get("title"));
				System.out.println(items.get(i));
			}
			if (items.size() != 0) {
				if (items.get(0).get("title")
						.equals(Consumer.CONSUMER_SEND_REPLY)) {
					if (items.get(0).get("result").equals("true")) {
						if (saveDb(Integer.parseInt(items.get(0)
								.get("reply_id")))) {
							Log.d("database", "success insert");
						} else {
							Log.d("database", "fail insert");
						}
						progressDialog.dismiss();
						showAlertDialog("전송", "정상적으로 전송되었습니다.",
								Consumer_reply.this, true);
					}else if(items.get(0).equals("null")){
						progressDialog.dismiss();
						showAlertDialog("전송","해당 사용자는 탈퇴하였습니다.", Consumer_reply.this, false);
					} else if(items.get(0).equals("logout")){
						progressDialog.dismiss();
						showAlertDialog("전송","해당 사용자는 비활성화 상태입니다.", Consumer_reply.this, false);
					} else {
						progressDialog.dismiss();
						showAlertDialog("전송", "전송을 실패하였습니다.",
								Consumer_reply.this, false);
					}
				}
			} else {
				progressDialog.dismiss();
				showAlertDialog("오류", "서버로부터 응답이 없습니다.", Consumer_reply.this,
						false);
			}

		}
	};

	private boolean saveDb(int rowid) {
		boolean result = false;
		ConsumerReplyDbAdapter replyDbHelper = new ConsumerReplyDbAdapter(
				Consumer_reply.this);
		sendReply.setSend_flag(1); // 1이면 보낸거.
		sendReply.setId(rowid);
		replyDbHelper.open();
		if (replyDbHelper.addReply(sendReply) > 0) {
			result = true;
		} else {
			result = false;
		}
		replyDbHelper.close();
		return result;
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
								if (isCorrect) {
									dialog.dismiss();
									((Activity) mContext).finish();
								} else {
									dialog.cancel();
								}

							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

}