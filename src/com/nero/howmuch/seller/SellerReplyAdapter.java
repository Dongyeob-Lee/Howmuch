package com.nero.howmuch.seller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.nero.howmuch.Category;
import com.nero.howmuch.R;
import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;
import com.nero.howmuch.thread.ImageItem;
import com.nero.howmuch.thread.ImageThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SellerReplyAdapter extends ArrayAdapter<Reply> {
	private LayoutInflater mInflater;
	private Context context;
	public SellerReplyAdapter(Context context, ArrayList<Reply> objects) {
		super(context, 0, objects);
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;

		// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

		if (v == null) {

			// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
			view = mInflater.inflate(R.layout.detail_reply_row, null);
		} else {

			view = v;
		}

		// 자료를 받는다.
		final Reply reply = this.getItem(position);

		if (reply != null) {
			// imageview
			final ImageView iv_seller_detail_reply_image = (ImageView) view
					.findViewById(R.id.iv_detail_reply_image);
			Handler handler = new Handler() {

				@SuppressWarnings("unchecked")
				@Override
				public void handleMessage(Message msg) {
					
					ImageItem imageItem = (ImageItem)msg.obj;
					iv_seller_detail_reply_image.setImageBitmap(imageItem.getImgBitmap());
					
				}
			};
			// textview setting
			TextView tv_seller_detail_reply_contents = (TextView) view
					.findViewById(R.id.tv_detail_reply_contents);
			TextView tv_seller_detail_reply_date = (TextView) view
					.findViewById(R.id.tv_detail_reply_date);
			TextView tv_seller_detail_reply_name = (TextView) view
					.findViewById(R.id.tv_detail_reply_name);

			LinearLayout ll_consumer_detail_reply = (LinearLayout)view.findViewById(R.id.ll_detail_reply);
			
			tv_seller_detail_reply_contents.setText(reply.getContents());
			tv_seller_detail_reply_date.setText(reply.getDate());
			if (reply.getSend_flag() == 0) {
				// seller가 받은, 작성자 이름을 구매자로.
				tv_seller_detail_reply_name.setText(reply.getConsumer_name());
			} else if (reply.getSend_flag() == 1) {
				// seller가 보낸 작성자 이름을 판매자로.
				tv_seller_detail_reply_name.setText(reply.getSeller_name());
			}
			if (reply.getReply_images_name().size() > 0) {
				if (!reply.getReply_images_name().get(0).equals("null")) {
					iv_seller_detail_reply_image.setImageResource(R.drawable.icon_isimage);
//					try {
//						URL url =new URL(Utils.imageUrl + "/"
//								+ reply.getReply_images_name().get(0));
//						ImageThread imageThread = new ImageThread("load_image",url ,0, handler);
//						imageThread.start();
//					} catch (MalformedURLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
			if(reply.getPflag()==Reply.PARENT){
				LayoutParams layoutParams  = new LayoutParams(0, 0, 0);
				ll_consumer_detail_reply.setLayoutParams(layoutParams);
			}
//			view.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(context, Seller_reply_view.class);
//					intent.putExtra("reply", reply);
//					context.startActivity(intent);
//				}
//			});
		}

		return view;

	}
}
