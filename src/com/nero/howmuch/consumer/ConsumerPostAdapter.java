package com.nero.howmuch.consumer;

import java.util.ArrayList;
import java.util.List;

import com.nero.howmuch.Category;
import com.nero.howmuch.R;
import com.nero.howmuch.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConsumerPostAdapter extends ArrayAdapter<ConsumerPost> {
	private LayoutInflater mInflater;

	public ConsumerPostAdapter(Context context, ArrayList<ConsumerPost> objects) {
		super(context, 0, objects);

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
			view = mInflater.inflate(R.layout.consumer_post_list_row, null);
		} else {

			view = v;
		}

		// �ڷḦ �޴´�.
		final ConsumerPost post = this.getItem(position);

		if (post != null) {
			// textview setting
			TextView tv_post_row_year = (TextView)view.findViewById(R.id.tv_post_row_year);
			TextView tv_post_row_date = (TextView) view.findViewById(R.id.tv_post_row_date);
			TextView tv_post_row_category = (TextView) view.findViewById(R.id.tv_post_row_category);
			TextView tv_post_row_title = (TextView)view.findViewById(R.id.tv_post_row_title);
			TextView tv_post_row_reply = (TextView)view.findViewById(R.id.tv_post_row_reply);
			List<String> dateToken = Utils.getPostDateFromDate(post.getDate());
			tv_post_row_year.setText(dateToken.get(0));
			tv_post_row_date.setText(dateToken.get(1)+"�� "+dateToken.get(2)+"��");
			tv_post_row_category.setText(Category.CATEGORIES[Integer.parseInt(post.getCategory())]);
			tv_post_row_title.setText(post.getTitle());
			tv_post_row_reply.setText("["+post.getReplyCnt()+"]");
		}
		return view;

	}
}
