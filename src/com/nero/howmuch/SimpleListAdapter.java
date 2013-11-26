package com.nero.howmuch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SimpleListAdapter extends ArrayAdapter<String>{
	private LayoutInflater mInflater;
	private Context context;
	public SimpleListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public SimpleListAdapter(Context context,String[] objects) {
		super(context, 0, objects);
		this.context = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public SimpleListAdapter(Context context,List<String> objects) {
		super(context, 0, objects);
		this.context = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;
		
		if(v==null){
			view = mInflater.inflate(R.layout.simple_dialog_row, null);
		}else{
			view = v;
		}
		
		final String item = this.getItem(position);
		
		if(item != null){
			TextView tv_simple_item = (TextView)view.findViewById(R.id.tv_simple_dialog_row);
			tv_simple_item.setText(item);
		}
		return view;
	}
}
