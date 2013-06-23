package com.zxl.Timetable.ui;


import com.zxl.Timetable.R;
import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.entity.GridViewItem;
import com.zxl.Timetable.util.AsycItemLoader;
import com.zxl.Timetable.util.ItemLoadAsyncTask;
import com.zxl.Timetable.util.MyUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class TotalCurriculumActivity extends Activity {

	private GridView totalGridView;
	int screenWidth;
	int screenHeight;
	int columnWidth;
	int columnHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.total_curriculum_gridview);
		setTitle(R.string.total_curriculum);
		findView();
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		columnWidth = screenWidth / 9;
		columnHeight = screenHeight / 8;
		totalGridView.setColumnWidth(columnWidth);
		totalGridView.setAdapter(new GridViewAdapter(this));
		
	}
	
	private void findView(){
		totalGridView = (GridView)findViewById(R.id.total_gridview);
		
	}
	
	private class GridViewAdapter extends BaseAdapter {

		private int count;
//		private int courseId;
//		private int buildId;
//		private String roomNo;
		private Context context;
		private AsycItemLoader itemLoader;
		
		TextView courseText;
		TextView placeText;
		TextView roomText;
		
		public GridViewAdapter(Context context){
			this.context = context;
			this.itemLoader = new AsycItemLoader();
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
			
			convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			
			courseText = (TextView) convertView.findViewById(R.id.grid_item_nick);
			placeText = (TextView) convertView.findViewById(R.id.grid_item_palce);
			roomText = (TextView) convertView.findViewById(R.id.grid_item_room);
			
			int dayOfWeek = position % 8 - 1;
			int onClass = position / 8 + 1;
			
			if (position % 8 == 0) {
				convertView.setBackgroundColor(getResources().getColor(R.color.gray));
				courseText.setText("第" + onClass + "节");
				return convertView;
			}
			if (position % 8 != 0 && TimeUtil.judgeTime(context, dayOfWeek, onClass)) {
				convertView.setBackgroundColor(Color.WHITE);
			}
//			if (position % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
//				new ItemLoadAsyncTask(context){
//					@Override
//					protected boolean handleMessage(Message msg) {
//						switch (msg.what) {
//						case 1:
//							setInfoToView((Item)msg.obj);
//							break;
//						}
//						return false;
//					}
//					
//				}.execute(position, dayOfWeek, onClass);
//				
//				return convertView;
//			}
			
			if (position % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
				GridViewItem item = itemLoader.loadItem(context, position, new AsycItemLoader.ItemCallBack() {
					
					@Override
					public void itemLoaded(GridViewItem item, int position) {
						
					}
				});
				if (item != null) {
					setInfoView(item);
				}
				return convertView;
			}
			
			return convertView;
			
//			GridView.LayoutParams params = new GridView.LayoutParams(LayoutParams.FILL_PARENT, columnHeight);
//			convertView.setLayoutParams(params);
//			
//			int dayOfWeek;  //星期几
//			int onClass;  //第几节
//			
//			dayOfWeek = position % 8 - 1;
//			onClass = position / 8 + 1;
//			
//			if (position % 8 == 0) {
//				convertView.setBackgroundColor(getResources().getColor(R.color.gray));
//				courseText.setText("第" + onClass + "节");
//				return convertView;
//			}
//			if (position % 8 != 0 && TimeUtil.judgeTime(context, dayOfWeek, onClass)) {
//				convertView.setBackgroundColor(Color.WHITE);
//			}
//			if (position % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
//				Curriculum curriculum = Curriculum.getCurriculum(context, dayOfWeek, onClass);
//				courseId = curriculum.getCourseId();
//				buildId = curriculum.getBuildingId();
//				roomNo = curriculum.getRoomNum();
//				
//				String nickName = getCourseInfo(courseId);
//				String place = getBuildInfo(buildId);
//				courseText.setText(nickName);
//				courseText.setGravity(Gravity.CENTER);
//				placeText.setText(place);
//				placeText.setGravity(Gravity.CENTER);
//				roomText.setText(roomNo);
//				roomText.setGravity(Gravity.CENTER);
//				return convertView;
//			} 
//			return convertView;
			
		}
		
//		private String getCourseInfo(int id) {
//			String nickName;
//			Course course = Course.getCourse(context, id);
//			nickName = course.getNickName();
//			return nickName;
//		}
//		
//		private String getBuildInfo(int id){
//			String buildName;
//			Building building = Building.getBuilding(context, id);
//			buildName = building.getBuildStr();
//			return buildName;
//		}
		
		
		public void setInfoToView(ItemLoadAsyncTask.Item item) {
			courseText.setText(item.getNickName());
			placeText.setText(item.getPlace());
			roomText.setText(item.getRoomNum());
		}
		
		public void setInfoView(GridViewItem item) {
			courseText.setText(item.getNickName());
			placeText.setText(item.getPlace());
			roomText.setText(item.getRoomNum());
		}
		
	}

}
