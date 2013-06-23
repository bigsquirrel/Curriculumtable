package com.zxl.Timetable.util;


import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.entity.Building;
import com.zxl.Timetable.entity.Course;
import com.zxl.Timetable.entity.Curriculum;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

/*
 * Params 向后台任务的执行方法传递参数的类型
 * Progress 在后台任务执行过程中，要求主UI线程处理中间状态，通常是一些UI处理中传递的参数类型
 * Result 后台任务执行完返回时的参数类型
 */
public class ItemLoadAsyncTask extends AsyncTask<Integer, String, Void> {

	public static final int HAS_COURSE = 2;
	
	private Context context;
	private int courseId;
	private int buildId;
	private String roomNum;
	public static SparseArray<Item> itemCache;
	
	public ItemLoadAsyncTask(Context c, int count) {
		context = c;
		itemCache = new SparseArray<Item>(count);
	}
	
	@Override 
	protected Void doInBackground(Integer... position) {

		for (int i = 0; i < position[0]; i++) {
			int dayOfWeek = i % 8 - 1;
			int onClass = i / 8 + 1;
			Item item = null;
//			
			if (i % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
				Curriculum curriculum = Curriculum.getCurriculum(context, dayOfWeek, onClass);
				courseId = curriculum.getCourseId();
				buildId = curriculum.getBuildingId();
				roomNum = curriculum.getRoomNum();
				
				String nickName = getCourseInfo(context, courseId);
				String buildName = getBuildInfo(context, buildId);
				
				item = new Item();
				item.setNickName(nickName);
				item.setPlace(buildName);
				item.setRoomNum(roomNum);
				
			} 
			itemCache.append(i, item);
		}
		
		return null;
	}
	
	public static String getCourseInfo(Context context, int id) {
		Course course = Course.getCourse(context, id);
		return course.getNickName();
	}
	
	public static String getBuildInfo(Context context, int id){
		Building building = Building.getBuilding(context, id);
		return building.getBuildStr();
	}
	
	//内部类，构造一个Item项
	public class Item {
		
		private String nickName;
		private String place;
		private String roomNum;
		
		public Item() {
			super();
		}

		public Item(String nickName, String place, String roomNum) {
			super();
			this.nickName = nickName;
			this.place = place;
			this.roomNum = roomNum;
		}

		@Override
		public String toString() {
			return "Item [nickName=" + nickName + ", place=" + place
					+ ", roomNum=" + roomNum + "]";
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getRoomNum() {
			return roomNum;
		}

		public void setRoomNum(String roomNum) {
			this.roomNum = roomNum;
		}
		
	}
}
