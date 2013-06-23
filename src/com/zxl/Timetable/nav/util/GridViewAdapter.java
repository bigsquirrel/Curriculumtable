package com.zxl.Timetable.nav.util;

import com.zxl.Timetable.R;
import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.util.ItemLoadAsyncTask;
import com.zxl.Timetable.util.MyUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author ivanchou
 *
 */
public class GridViewAdapter extends BaseAdapter {
	
	private int count;
	private Context context;
	private int columnHeight;
	
	static class ViewHolder {
		TextView courseText;
		TextView placeText;
		TextView roomText;
	}
	
	public void resetHolder(ViewHolder holder) {
		holder.courseText.setText("");
		holder.placeText.setText("");
		holder.roomText.setText("");
	}
	
	public GridViewAdapter(Context context, int height){
		this.context = context;
		this.columnHeight = height;
	}
	
	@Override
	public int getCount() {
		count = MyUtils.getDayClass(context) * 8;
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemLoadAsyncTask.Item item = ItemLoadAsyncTask.itemCache.get(position);
		
		View gridItemView = convertView;
		if (gridItemView == null) {
			gridItemView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.courseText = (TextView) gridItemView.findViewById(R.id.grid_item_nick);
			viewHolder.placeText = (TextView) gridItemView.findViewById(R.id.grid_item_palce);
			viewHolder.roomText = (TextView) gridItemView.findViewById(R.id.grid_item_room);
			gridItemView.setTag(viewHolder);
		} 
		ViewHolder holder = (ViewHolder) gridItemView.getTag();
		
		
		GridView.LayoutParams params = new GridView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, columnHeight);
		gridItemView.setLayoutParams(params);
		
		int dayOfWeek = position % 8 - 1;
		int onClass = position / 8 + 1;  //第几节
		
		if (position % 8 == 0) {
			resetHolder(holder);
			gridItemView.setBackgroundColor(context.getResources().getColor(R.color.holo_blue_light));
			holder.courseText.setText("第" + onClass + "节");
			return gridItemView;
		}
		
		if (item != null) {
			holder.courseText.setText(item.getNickName());
			holder.courseText.setGravity(Gravity.CENTER);
			holder.placeText.setText(item.getPlace());
			holder.placeText.setGravity(Gravity.CENTER);
			holder.roomText.setText(item.getRoomNum());
			holder.roomText.setGravity(Gravity.CENTER);
		} else {
			resetHolder(holder);
		}
		if (position % 8 != 0 && TimeUtil.judgeTime(context, dayOfWeek, onClass)) {
			if (item == null) {
				resetHolder(holder);
			}
			gridItemView.setBackgroundColor(Color.WHITE);
		} else {
			if (item == null) {
				resetHolder(holder);
			}
			gridItemView.setBackground(null);
		}
		
		return gridItemView;
	}

}
