package com.zxl.Timetable.alert;

import java.util.ArrayList;
import java.util.Calendar;

import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.ui.MainActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CheckService extends Service {
	private static String TAG = "ChechService";
	private static MainActivity ma;
	public static ArrayList<Curriculum> reminderList = new ArrayList<Curriculum>();
	
	public static void startServie(Context context) {
		ma = (MainActivity)context;
		Intent i = new Intent(context, CheckService.class);
		context.startService(i);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		checkAndSet();
		stopSelf();
	}

	/*
	 * 检查今天所有的课程并且给课程设置提醒
	 */
	public void checkAndSet() {
		int classes = checkClassOnDay(this, TimeUtil.classesOneDay(this));
		Log.v(TAG, "Today have " + classes + " class");
		
		Curriculum curriculum;
		for (int i = 0; i < reminderList.size(); ) {
			curriculum = reminderList.get(i);
			if (!curriculum.isSetReminder()) {
				boolean isSet = setReminder(curriculum);
				curriculum.setSetReminder(isSet);
				reminderList.remove(curriculum);
			}
		}
	}
	
	
	/*
	 * 设置课程提醒的时间
	 */
	public boolean setReminder(Curriculum curriculum) {
		//上课的时间
		Calendar classTime = TimeUtil.getTimeOfClass(this, curriculum.getOnClass());
		int aheadMin = curriculum.getRemind();
		
		//当前时间
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(System.currentTimeMillis());
		
		long setModeTime = classTime.getTimeInMillis() - currentTime.getTimeInMillis();
		//离发送提醒通知还有的毫秒数
		long remainTime = setModeTime - ((long)aheadMin)*60*1000;
		int id = curriculum.get_id();
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		if (setModeTime >= 0) {
			Intent audioIntent = new Intent(this, AudioModeService.class);
			audioIntent.putExtra("mode", "class");
			PendingIntent pi2 = PendingIntent.getService(this, 0, audioIntent, PendingIntent.FLAG_ONE_SHOT);
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + setModeTime, pi2);
		}
		if (remainTime >= 0) {
			Intent alertIntent = new Intent(this, ClassAlertService.class);
			alertIntent.putExtra("id", id);
			PendingIntent pi = PendingIntent.getService(this, 0, alertIntent, PendingIntent.FLAG_ONE_SHOT);
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + remainTime, pi);
			return true;
		}
		return false;
	}

	
	/*
	 * 首先遍历一遍今天所有的课程，有课的节数add提醒
	 * classOneDay 指设定的一天有几节课
	 */
	public static int checkClassOnDay(Context context, int classOneDay) {
		//首先是初始化提醒的数组  reminderList
		
		int count = 0;
		
		for (int i = 1; i <= classOneDay; i++) {
			if (TimeUtil.hasSchool(context, i)) {
				count ++;
				Curriculum curriculum = TimeUtil.hasWhatCurriculum(context, i);
				addReminder(curriculum);
			} 
			
		}
		return count;
	}
	
	/*
	 * add 提醒
	 */
	public static void addReminder(Curriculum curriculum) {
		reminderList.add(curriculum);
	}
	/*
	 * remove 提醒
	 */
	public static void removeReminder(Curriculum curriculum) {
		reminderList.remove(curriculum);
	}
	
	/*
	 * 轮询提醒的数组是否为空
	 */
	public static void isReminderCountNull() {
		
	}
}
