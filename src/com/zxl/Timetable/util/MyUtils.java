package com.zxl.Timetable.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.zxl.Timetable.base.entity.SimpleDate;
import com.zxl.Timetable.base.utils.DateUtil;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;

public class MyUtils {

	private static int currentWeek = 0; // 当前周数。
	private static SharedPreferences settingsPreferences;
	
	
	public static void setWeekIcon(LayerDrawable icon, Context context) {
		WeekOfTermDrawable currentWeekDrawable;
		
		Drawable currentDrawable = icon.findDrawableByLayerId(R.id.current_week_icon_week);
		if (currentDrawable != null && currentDrawable instanceof WeekOfTermDrawable) {
			currentWeekDrawable = (WeekOfTermDrawable)currentDrawable;
        } else {
        	currentWeekDrawable = new WeekOfTermDrawable(context);
        }
		
		// Set the day and update the icon
		getCurrentWeek(context);
		currentWeekDrawable.setWeekOfTerm(currentWeek);
		icon.mutate();
		icon.setDrawableByLayerId(R.id.current_week_icon_week, currentWeekDrawable);
	}
	
	public static int getCurrentWeek(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
				
		final SimpleDate date = DateUtil.getDefaultFirstDay();
		final SimpleDate simpleDate = new SimpleDate(
				settingsPreferences.getInt(
						AppConstant.Preferences.FIRST_DAY_YEAR, date.getYear()),
				settingsPreferences.getInt(
						AppConstant.Preferences.FIRST_DAY_MONTH,
						date.getMonth()), settingsPreferences.getInt(
						AppConstant.Preferences.FIRST_DAY_DAY,
						date.getDayOfMonth()));
		return currentWeek = DateUtil.getWeeksOfTerm(simpleDate);
		
	}
	
	public static int getAllWeeks(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final int weeks = settingsPreferences.getInt(AppConstant.Preferences.ALL_WEEK, 20);
		return weeks;
	} 
	
	public static int getDayClass(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final int cls = settingsPreferences.getInt(AppConstant.Preferences.CLASSES_ONE_DAY, 5);
		return cls;
	}
	
	public static int getClassLastMinute(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final int min = settingsPreferences.getInt(AppConstant.Preferences.CLASS_LAST_MINUTE, 45);
		return min;
	}
	
	public static int getBreakMinute(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final int min = settingsPreferences.getInt(AppConstant.Preferences.CLASS_BREAK_MINUTE, 5);
		return min;
	}
	
	public static int getClassTime(Context context, int onClass) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final int min = settingsPreferences.getInt(AppConstant.Preferences.CLASS_TIME + onClass, 0);
		return min;
	}
	
	public static boolean getMuteValue(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final boolean muteValue = settingsPreferences.getBoolean(AppConstant.Preferences.AUTOMUTE, true);
		return muteValue;
	}
	
	public static boolean getNotifiValue(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final boolean notifValue = settingsPreferences.getBoolean(AppConstant.Preferences.NOTIFICATION, true);
		return notifValue;
	}
	
	public static String getMuteMode(Context context) {
		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		final String muteMode = settingsPreferences.getString(AppConstant.Preferences.MUTEMODE, "mute");
		return muteMode;
	}
	
//	public static Time getClassTime(Context context, int onClass) {
//		settingsPreferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Activity.MODE_APPEND);
//		final int classTimeHour = settingsPreferences.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + onClass, 0);
//		final int classTimeMinute = settingsPreferences.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE + onClass, 0);
//		Time classTime = new Time(classTimeHour, classTimeMinute, 0);
//		return classTime;
//	}
	
	public static boolean isEmpty(String str) {
		if (str.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isClassSigned(int weeks, int curweek, int sign) {
		return ((weeks & 1 << (curweek - 1)) & sign) != 0;
	}
	
	public static int signClass(int weeks, int curweek, int sign) {
		return (weeks & 1 << (curweek - 1)) | sign;
		
	}
	
	public static String convertMillisToTime(long millis) {
		Date date = new Date(millis);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date);
	}
	
	public static String formatCalenderDate(Calendar c){
		String dayOfWeek = AppConstant.week[(c.get(Calendar.DAY_OF_WEEK) + 1) / 7];
		Date date = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date) + " " +dayOfWeek;
	}
	
	public static String formatCalenderTime(Calendar c){
		Date date = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(date);
	}
	
	//id is the curriculum id, then get the current time and the class time to judge the class can be sign
	public static boolean isCouldSign(Context context, int dayOfWeek, int onClass) {
		int classTime = getClassTime(context, onClass);
		int classMin = getClassLastMinute(context);
		Calendar c = Calendar.getInstance();
		int nowTime = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
		if (c.get(Calendar.DAY_OF_WEEK) != dayOfWeek + 1) {
			return false;
		} else if (nowTime < classTime || nowTime > (classTime + classMin)) {
			return false;
		}
		return true;
	}
	
	public static boolean isCouldShare(Context context, int dayOfWeek, int onClass) {
		return isCouldSign(context, dayOfWeek, onClass);
	}
	
	
	public static int convertIntToRes(int id) {
		switch (id) {
		case 1:
			return R.id.checked1;
		case 2:
			return R.id.checked2;
		case 3:
			return R.id.checked3;
		case 4:
			return R.id.checked4;
		case 5:
			return R.id.checked5;
		case 6:
			return R.id.checked6;
		case 7:
			return R.id.checked7;
		case 8:
			return R.id.checked8;
		case 9:
			return R.id.checked9;
		case 10:
			return R.id.checked10;
		case 11:
			return R.id.checked11;
		case 12:
			return R.id.checked12;
		case 13:
			return R.id.checked13;
		case 14:
			return R.id.checked14;
		case 15:
			return R.id.checked15;
		case 16:
			return R.id.checked16;
		case 17:
			return R.id.checked17;
		case 18:
			return R.id.checked18;
		case 19:
			return R.id.checked19;
		case 20:
			return R.id.checked20;
		case 21:
			return R.id.checked21;
		case 22:
			return R.id.checked22;
		case 23:
			return R.id.checked23;
		case 24:
			return R.id.checked24;
		default:
			break;
		}
		return 0;
	}
	
	public static int getRemindTime(int i) {
		switch (i) {
		case 0:
			return 0;
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			return 15;
		case 4:
			return 20;
		case 5:
			return 25;
		case 6:
			return 30;
		case 7:
			return 45;

		default:
			break;
		}
		return 0;
	}
}
