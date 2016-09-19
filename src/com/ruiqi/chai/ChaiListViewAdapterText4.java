package com.ruiqi.chai;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiqi.works.R;

public class ChaiListViewAdapterText4 extends BaseAdapter {

	private ViewHolder holder;
	private List<ChaiTableInfo> list;
	private Context context;

	public ChaiListViewAdapterText4(Context context, List<ChaiTableInfo> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.chai_listview_item_text5, null);
			holder.text0 = (TextView) convertView.findViewById(R.id.text0);
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			holder.text3 = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.drawable.rectangle_white);
		holder.text0.setTextColor(Color.parseColor("#333333"));
		holder.text1.setTextColor(Color.parseColor("#333333"));
		holder.text2.setTextColor(Color.parseColor("#333333"));
		holder.text3.setTextColor(Color.parseColor("#333333"));
		
		holder.text0.setText(list.get(position).getTitle1());
		holder.text1.setText(list.get(position).getTitle2());
		holder.text2.setText(list.get(position).getTitle3());
		holder.text3.setText(list.get(position).getTitle4());
		return convertView;
	}

	private static class ViewHolder {
		TextView text0, text1, text2, text3;
	}

}