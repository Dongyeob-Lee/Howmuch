package com.nero.howmuch.consumer;

import java.util.ArrayList;

import com.nero.howmuch.R;
import com.nero.howmuch.Reply;
import com.nero.howmuch.thread.ImageItem;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ConsumerReplyAdapter extends ArrayAdapter<Reply> {
	private LayoutInflater mInflater;
	private Context context;
	public ConsumerReplyAdapter(Context context, ArrayList<Reply> objects) {
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
			//imageview
			ImageView iv_consumer_detail_reply_image = (ImageView)view.findViewById(R.id.iv_detail_reply_image);
			// textview setting
			TextView tv_consumer_detail_reply_contents = (TextView) view.findViewById(R.id.tv_detail_reply_contents);
			TextView tv_consumer_detail_reply_date = (TextView) view.findViewById(R.id.tv_detail_reply_date);
			TextView tv_consumer_detail_reply_name = (TextView)view.findViewById(R.id.tv_detail_reply_name);
			
			LinearLayout ll_consumer_detail_reply = (LinearLayout)view.findViewById(R.id.ll_detail_reply);
			
			tv_consumer_detail_reply_contents.setText(reply.getContents());
			tv_consumer_detail_reply_date.setText(reply.getDate());
			if(reply.getSend_flag()==0){
				//consumer가 받은, 작성자 이름을 판매자로.
				tv_consumer_detail_reply_name.setText(reply.getSeller_name());
			}else if(reply.getSend_flag()==1){
				//consumer가 보낸 작성자 이름을 구매자 -> 판매자로.
				tv_consumer_detail_reply_name.setText(reply.getConsumer_name()+" -> "+reply.getSeller_name());
			}
			if(reply.getReply_images_name().size()>0){
				if (!reply.getReply_images_name().get(0).equals("null")) {
					//사진이 있을경우 사진 아이콘 설정.
					iv_consumer_detail_reply_image.setImageResource(R.drawable.icon_isimage);
				}
			}
			if(reply.getPflag()==Reply.PARENT){
				LayoutParams layoutParams  = new LayoutParams(0, 0, 0);
				ll_consumer_detail_reply.setLayoutParams(layoutParams);
			}
			//댓글 다시 보내는거는.. 댓글 화면 들어가서 버튼 생성.
//			view.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent  = new Intent(context, Consumer_reply_view.class);
//					intent.putExtra("reply", reply);
//					context.startActivity(intent);
//				}
//			});
//			view.setOnTouchListener(new View.OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//			});
		}

		return view;

	}
}
