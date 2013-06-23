package com.zxl.Timetable.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.entity.Building;
import com.zxl.Timetable.entity.Course;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.entity.GridViewItem;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class AsycItemLoader {
	
	public static HashMap<Integer, SoftReference<GridViewItem>> itemCache;
	
	
	public AsycItemLoader() {
		itemCache = new HashMap<Integer, SoftReference<GridViewItem>>();
	}
	
	public GridViewItem loadItem (final Context context, final int position, final ItemCallBack itemCallBack) {
		if (itemCache.containsKey(position)) {
			SoftReference<GridViewItem> reference = itemCache.get(position);
			GridViewItem item = reference.get();
			if (item != null) {
				return item;
			}
		}
		
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				itemCallBack.itemLoaded((GridViewItem)msg.obj, position);
			}
			
		};
		
		new Thread() {

			@Override
			public void run() {
				GridViewItem item = loadItemFronDB(context, position);
				if (item != null) {
					itemCache.put(position, new SoftReference<GridViewItem>(item));
					Message message = handler.obtainMessage(0, item);
					handler.sendMessage(message);
				}
			}
			
		}.start();
		return null;
	}
	
	public static GridViewItem loadItemFronDB (Context context, int position) {
		int dayOfWeek;  //星期几
		int onClass;  //第几节
		int courseId;
		int buildId;
		String roomNo;
		
		
		dayOfWeek = position % 8 - 1;
		onClass = position / 8 + 1;
		
		if (position % 8 == 0) {
			return null;
		}
		if (position % 8 != 0 && TimeUtil.judgeTime(context, dayOfWeek, onClass)) {
		
			
		}
		if (position % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
			
			Curriculum curriculum = Curriculum.getCurriculum(context, dayOfWeek, onClass);
			courseId = curriculum.getCourseId();
			buildId = curriculum.getBuildingId();
			roomNo = curriculum.getRoomNum();
			
			String nickName = getCourseInfo(context, courseId);
			String buildName = getBuildInfo(context, buildId);
			
			AsycItemLoader itemLoader = new AsycItemLoader();
			GridViewItem item = new GridViewItem(nickName, buildName, roomNo);
			return item;
		} 
		return null;
	}
	
	public static String getCourseInfo(Context context, int id) {
		String nickName;
		Course course = Course.getCourse(context, id);
		nickName = course.getNickName();
		return nickName;
	}
	
	public static String getBuildInfo(Context context, int id){
		String buildName;
		Building building = Building.getBuilding(context, id);
		buildName = building.getBuildStr();
		return buildName;
	}
	
	public interface ItemCallBack {
		public void itemLoaded(GridViewItem item, int position);
	}
	
	
	
}
