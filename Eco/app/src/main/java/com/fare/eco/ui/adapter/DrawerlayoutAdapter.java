package com.fare.eco.ui.adapter;

import java.util.List;

import com.fare.eco.model.DrawerLayoutInfo;
import com.fare.eco.ui.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrawerlayoutAdapter extends BaseAdapter{

	private List<DrawerLayoutInfo> mList;
	private LayoutInflater mInflater;

	public DrawerlayoutAdapter(Context context, List<DrawerLayoutInfo> list) {
		mList = list;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = (LinearLayout) mInflater.inflate(R.layout.drawer_item, null);
			viewHolder.mItemName = (TextView) convertView.findViewById(R.id.text_right);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image_left);
			convertView.setBackgroundResource(R.drawable.listitem_selector);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mItemName.setText(mList.get(position).getText());
		viewHolder.mImageView.setImageDrawable(mList.get(position).getDrawable());

		return convertView;
	}

	class ViewHolder {
		TextView mItemName;
		ImageView mImageView;
	}

}
