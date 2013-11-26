package com.nero.howmuch.consumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nero.howmuch.Category;
import com.nero.howmuch.HowmuchUtils;
import com.nero.howmuch.R;
import com.nero.howmuch.SharedPreferenceUtil;
import com.nero.howmuch.SimpleListAdapter;
import com.nero.howmuch.image.ImageEditActivity;
import com.nero.howmuch.seller.Seller_register;
import com.nero.howmuch.seller.Seller_setting;
import com.nero.howmuch.sqlite.ConsumerPostDbAdapter;
import com.nero.howmuch.thread.ConsumerThread;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Consumer_post extends Activity {
	public static final int EDIT_FROM_EDITOR = 100;
	public static final int PICK_FROM_CAMERA = 101;
	public static final int PICK_FROM_ALBUM = 102;
	public static final String CONSUMER_POST = "consumer_post";
	List<Map<String, String>> items = null;
	// Button
	private Button btn_consumer_post_category;
	private Button btn_consumer_post_img;
	private Button btn_consumer_post_city;
	private Button btn_consumer_post_post;

	// EditText
	private EditText et_consumer_post_title;
	private EditText et_consumer_post_contents;

	// ImageView
	private ImageView iv_consumer_post_img1;
//	private ImageView iv_consumer_post_img2;
//	private ImageView iv_consumer_post_img3;
	private int image_cnt=0;
	// category select var
	private SimpleListAdapter simpleListAdapter;
	private Dialog category_dialog;
	private ListView lv_consumer_post_category;
	private boolean isCategory = false;
	private int categoryNum;

	//consumer post
	ConsumerPost consumerPost;
	ProgressDialog progressDialog;
	//database
	ConsumerPostDbAdapter postDbHelper;
	
	//date
	Date date = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	//Context
	static Context mContext;
	private String path;
	private static final String TEMP_IMAGE = "tmp_img.jpeg";
	private ArrayList<Bitmap> cur_bitmap_list = new ArrayList<Bitmap>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumer_post);
		mContext = this;
		
		//database open
		postDbHelper = new ConsumerPostDbAdapter(this);
		postDbHelper.open();
		path = this.getFilesDir().getAbsolutePath();
		// Button Setting
		btn_consumer_post_category = (Button) findViewById(R.id.btn_consumer_post_category);
		btn_consumer_post_post = (Button) findViewById(R.id.btn_consumer_post_post);
		btn_consumer_post_city = (Button)findViewById(R.id.btn_consumer_post_city);
		btn_consumer_post_img = (Button) findViewById(R.id.btn_consumer_post_img);
		btn_consumer_post_category.setOnClickListener(btnClickListener);
		btn_consumer_post_post.setOnClickListener(btnClickListener);
		btn_consumer_post_city.setOnClickListener(btnClickListener);
		btn_consumer_post_img.setOnClickListener(btnClickListener);

		// EditText Setting
		et_consumer_post_title = (EditText) findViewById(R.id.et_consumer_post_title);
		et_consumer_post_contents = (EditText) findViewById(R.id.et_consumer_post_contents);

		// ImageView Setting
		iv_consumer_post_img1 = (ImageView) findViewById(R.id.iv_consumer_post_img1);
//		iv_consumer_post_img2 = (ImageView)findViewById(R.id.iv_consumer_post_img2);
//		iv_consumer_post_img3 = (ImageView)findViewById(R.id.iv_consumer_post_img3);
		iv_consumer_post_img1.setOnLongClickListener(imageClickListener);
//		iv_consumer_post_img2.setOnLongClickListener(imageClickListener);
//		iv_consumer_post_img3.setOnLongClickListener(imageClickListener);
		
	}

	private OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int viewId = v.getId();

			if (viewId == btn_consumer_post_category.getId()) {
				//select big Category
				showCategoryDialog();
			} else if (viewId == btn_consumer_post_post.getId()) {
				//post new post
				if(!et_consumer_post_title.getText().toString().equals("")){
					if(!et_consumer_post_contents.getText().toString().equals("")){
						sendPost();
					}
				}else{
					showAlertDialog("오류","서식을 다 채워주세요.", Consumer_post.this, false);
				}
			} else if (viewId == btn_consumer_post_img.getId()) {
				//choose image to post
				if(cur_bitmap_list.size()>2){
					showAlertDialog("이미지 첨부","하나이상 첨부안됨", Consumer_post.this, false);
				}else{
					doTakeAlbumAction();
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_consumer_post, menu);
		return true;
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
//				refreshImageView();
//				break;
			case PICK_FROM_ALBUM :
//				Intent intent = new Intent(Consumer_post.this, ImageEditActivity.class);
//				intent.putExtra("output", data.getData());
//				intent.putExtra("flag", "album");
//				startActivityForResult(intent, EDIT_FROM_EDITOR);
				if (!addImage(data)) {
					Toast.makeText(Consumer_post.this, "사진 첨부 실패",
							Toast.LENGTH_LONG).show();
				}
				break;
//			case PICK_FROM_CAMERA :
//				Intent intent2 = new Intent(Consumer_post.this, ImageEditActivity.class);
//				intent2.putExtra("output", path+"/"+TEMP_IMAGE);
//				intent2.putExtra("flag", "camera");
//				startActivityForResult(intent2, EDIT_FROM_EDITOR);
//				break;
			}
		}
	}
	private void removeAllImageView(){
		iv_consumer_post_img1.setImageBitmap(null);
//		iv_consumer_post_img2.setImageBitmap(null);
//		iv_consumer_post_img3.setImageBitmap(null);
	}
	private void refreshImageView(){
		removeAllImageView();
		for(int i=0; i<cur_bitmap_list.size(); i++){
			if(i==0){
				iv_consumer_post_img1.setImageBitmap(cur_bitmap_list.get(i));
			}
//			else if(i==1){
//				iv_consumer_post_img2.setImageBitmap(cur_bitmap_list.get(i));
//			}else if(i==2){
//				iv_consumer_post_img3.setImageBitmap(cur_bitmap_list.get(i));
//			}
		}
	}
	private OnLongClickListener imageClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			if(v.getId() == iv_consumer_post_img1.getId()){
				if(cur_bitmap_list.size()>=0){
					showRemoveImageDialog("사진 삭제", "해당 사진을 삭제 하시겠습니까?", Consumer_post.this, 0);
				}
			}
//			else if(v.getId() == iv_consumer_post_img2.getId()){
//				if(cur_bitmap_list.size()>=1){
//					showRemoveImageDialog("�ъ����", "�대� �ъ���������寃�����?", Consumer_post.this, 1);
//				}
//			}else if(v.getId() == iv_consumer_post_img3.getId()){
//				if(cur_bitmap_list.size()>=2){
//					showRemoveImageDialog("�ъ����", "�대� �ъ���������寃�����?", Consumer_post.this, 2);
//				}
//			}
			return false;
		}
	};
	private void sendPost(){
		
		consumerPost = new ConsumerPost();
		consumerPost.setDate(dateFormat.format(date));
		consumerPost.setConsumer_name(SharedPreferenceUtil.getConsumerSharedPreference(Consumer_post.this));
		consumerPost.setTitle(et_consumer_post_title.getText().toString());
		consumerPost.setCategory(String.valueOf(categoryNum));
		consumerPost.setCity("전국");
		consumerPost.setContents(et_consumer_post_contents.getText().toString());
		for(int i=0; i<cur_bitmap_list.size(); i++){
			String realImageName = HowmuchUtils.randomChar()+"_"+String.valueOf(System.currentTimeMillis())+".jpeg";
			System.out.println(i+":"+realImageName);
			HowmuchUtils.SaveBitmapToFileCache(cur_bitmap_list.get(i),path+"/"+realImageName);
			consumerPost.getPost_images_name().add(realImageName);
		}
		progressDialog = ProgressDialog.show(Consumer_post.this,"post","전송 ...",true);
		ConsumerThread consumerThread  = new ConsumerThread(consumerPost, CONSUMER_POST, path,handler);
		consumerThread.start();
	}

	private boolean saveDb(int rowid){
		boolean result=false;
		consumerPost.setId(rowid);
       if(postDbHelper.addPost(consumerPost)>0){
    	   result = true;
       }else{
    	  result =  false;
       }
		
		return result;
	}
	
	private void doTakeAlbumAction() {
		//choose from gallery
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
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
	private void showCategoryDialog() {
		simpleListAdapter = new SimpleListAdapter(Consumer_post.this, Category.CATEGORIES);
		category_dialog = new Dialog(Consumer_post.this);
		category_dialog.setContentView(R.layout.seller_select_category_dialog);
		category_dialog.setTitle("카테고리");
		category_dialog.setCancelable(true);
		lv_consumer_post_category = (ListView) category_dialog
				.findViewById(R.id.lv_seller_select_category);
		lv_consumer_post_category.setAdapter(simpleListAdapter);
		lv_consumer_post_category
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						btn_consumer_post_category
								.setText(lv_consumer_post_category
										.getItemAtPosition(position).toString());
						categoryNum = position;
						isCategory = true;
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
	}
	
	Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				progressDialog.dismiss();
				showAlertDialog("오류","서버 응답이 없습니다.", Consumer_post.this,false);
			}else{
				items = (List<Map<String,String>>)msg.obj;
				if(items.size()!=0){
					if(items.get(0).get("title").equals(CONSUMER_POST))
					{
						if(items.get(0).get("result").equals("true")){
							if(saveDb(Integer.parseInt(items.get(0).get("post_id")))){
								Log.d("database", "success insert");
							}else{
								Log.d("database", "fail insert");
							}
							progressDialog.dismiss();
							showAlertDialog("전송","정상적으로 전송되었습니다.", Consumer_post.this,true);
						}else if(items.get(0).get("result").equals("null")){
							progressDialog.dismiss();
							showAlertDialog("오류","전송 실패 (해당 카테고리에 판매자가 없습니다.)", Consumer_post.this, false);
						}else{
							progressDialog.dismiss();
							showAlertDialog("오류","전송 실패", Consumer_post.this, false);
						}
					}
				}else{
					progressDialog.dismiss();
					showAlertDialog("오류","전송 실패", Consumer_post.this,false);
				}
			}
		}
	};
	private void showRemoveImageDialog(String title, String message,
			final Context context, final int pos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
									cur_bitmap_list.remove(pos);
									refreshImageView();
									dialog.dismiss();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
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
								if(isCorrect){
									dialog.dismiss();
									((Activity) mContext).finish();
								}else{
									dialog.cancel();
								}
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
