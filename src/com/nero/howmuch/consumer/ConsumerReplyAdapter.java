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

	// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;
		
		// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���

		if (v == null) {

			// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
			view = mInflater.inflate(R.layout.detail_reply_row, null);
		} else {

			view = v;
		}

		// �ڷḦ �޴´�.
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
				//consumer�� ����, �ۼ��� �̸��� �Ǹ��ڷ�.
				tv_consumer_detail_reply_name.setText(reply.getSeller_name());
			}else if(reply.getSend_flag()==1){
				//consumer�� ���� �ۼ��� �̸��� ������ -> �Ǹ��ڷ�.
				tv_consumer_detail_reply_name.setText(reply.getConsumer_name()+" -> "+reply.getSeller_name());
			}
			if(reply.getReply_images_name().size()>0){
				if (!reply.getReply_images_name().get(0).equals("null")) {
					//������ ������� ���� ������ ����.
					iv_consumer_detail_reply_image.setImageResource(R.drawable.icon_isimage);
				}
			}
			if(reply.getPflag()==Reply.PARENT){
				LayoutParams layoutParams  = new LayoutParams(0, 0, 0);
				ll_consumer_detail_reply.setLayoutParams(layoutParams);
			}
			//��� �ٽ� �����°Ŵ�.. ��� ȭ�� ���� ��ư ����.
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
