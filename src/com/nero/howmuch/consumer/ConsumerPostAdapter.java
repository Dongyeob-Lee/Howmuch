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

	// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;

		// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

		if (v == null) {

			// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
			view = mInflater.inflate(R.layout.consumer_post_list_row, null);
		} else {

			view = v;
		}

		// 자료를 받는다.
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
			tv_post_row_date.setText(dateToken.get(1)+"월 "+dateToken.get(2)+"일");
			tv_post_row_category.setText(Category.CATEGORIES[Integer.parseInt(post.getCategory())]);
			tv_post_row_title.setText(post.getTitle());
			tv_post_row_reply.setText("["+post.getReplyCnt()+"]");
		}
		return view;

	}
}
