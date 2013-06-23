package com.zxl.Timetable.util;

import com.zxl.Timetable.common.AppConstant;

import android.content.Context;
import android.content.SharedPreferences;

public class ClassTimeSetUtil {

	public static int id;
	public static SharedPreferences preferences;
	
	public static void init(Context context, int id) {
		init(context);
		setId(id);
	}
	
	public static void init(Context context) {
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
	
	
	public static int getId() {
		return id;
	}



	public static void setId(int id) {
		ClassTimeSetUtil.id = id;
	}



	/*
	 * 生成上课时间的String
	 */
	public static String getClassTimeString() {
		String timeString;
		int classTime = getClassTime(id);
		int classTimeHour = classTime / 60;
		int classTimeMin = classTime % 60;
		
		timeString = String.format("%02d:%02d", classTimeHour, classTimeMin);
		
		return timeString;
		
	}
	
	/*
	 * 生成下课时间的String
	 */
	public static String getBreakTimeString() {
		String timeString;
		int classTime = getClassTime(id);
		int classLastMin = getClassLastMinute();
		int breakTime = classTime + classLastMin;
		int breakTimeHour = breakTime / 60;
		int breakTimeMin = breakTime % 60;
		
		timeString = String.format("%02d:%02d", breakTimeHour, breakTimeMin); 
		
		return timeString;
		
	}
	
	
	/*
	 * 从SP中取得上课时间
	 */
	public static int getClassTime(int id) {
		int classTime;
		classTime = preferences.getInt(AppConstant.Preferences.CLASS_TIME + id, 0);
		return classTime;
	}
	
	/*
	 * 推算出下课时间, 当节课的上课时间加上该节课持续的时间
	 */
	public static int getClassBreakTime(int id) {
		if (id == 0) {
			return 475;
		} else {
			int classBreakTime = getClassTime(id) + getClassLastMinute();
			return classBreakTime;
		}
	}
	
	/*
	 * 从SP中取得每节课的时间
	 */
	public static int getClassLastMinute() {
		int classLastMin;
		classLastMin = preferences.getInt(AppConstant.Preferences.CLASS_LAST_MINUTE, 45);
		return classLastMin;
	}
	
	/*
	 * 从SP中取得课间休息时间
	 */
	public static int getClassBreakMinute() {
		int classBreakMin;
		classBreakMin = preferences.getInt(AppConstant.Preferences.CLASS_BREAK_MINUTE, 5);
		return classBreakMin;
	}
	
	
	/*
	 * 根据第一节课上课时间和每节课的持续时间已经课间休息时间设置其他所有的数据
	 * clickPostion 是用户点击的课程列表的ID，若设置此id的上课时间，则自动设置其他课程
	 */
	public static void autoSetAllClassTime(int clickPosition, int classOneDay) {
		for (id = clickPosition + 1; id <= classOneDay; id ++) {
			//下节课的开始时间是上节课的 下课时间 + 课件休息时间 
			int nextClassTime = getClassBreakTime(id - 1) + getClassBreakMinute();
			int nextClassTimeHour = nextClassTime / 60;
			int nextClassTimeMin = nextClassTime % 60;
			setClassTime(nextClassTimeHour, nextClassTimeMin, id);
			System.out.println("class time hour :" + nextClassTimeHour + ", class time min :" + nextClassTimeMin);
		}
	}
	
	public static void autoSetAllClassTime(int classOneDay) {
		autoSetAllClassTime(id - 1, classOneDay);
	}
	
	/*
	 * 将TimePicker的时间存入SP, 设置上课时间
	 */
	public static void setClassTime(int hour, int minute, int clickId, int classOneDay) {
		preferences.edit().putInt(AppConstant.Preferences.CLASS_TIME + clickId , hour * 60 + minute).commit();
		for (id = clickId + 1; id <= classOneDay; id++) {
			int nextClassTime = getClassBreakTime(id - 1) + getClassBreakMinute();
			int nextClassTimeHour = nextClassTime / 60;
			int nextClassTimeMin = nextClassTime % 60;
			setClassTime(nextClassTimeHour, nextClassTimeMin, id);
		}
	}
	
	public static void setClassTime(int hour, int minute, int id) {
		preferences.edit().putInt(AppConstant.Preferences.CLASS_TIME + id , hour * 60 + minute).commit();
	}
	/*
	 * 设置每节课的时间
	 */
	public static void setClassLastMinute(int min) {
		preferences.edit().putInt(AppConstant.Preferences.CLASS_LAST_MINUTE, min).commit();
	}
	
	/*
	 * 设置课间时间
	 */
	public static void setClassBreakMinute(int min) {
		preferences.edit().putInt(AppConstant.Preferences.CLASS_BREAK_MINUTE, min).commit();
	}
}
