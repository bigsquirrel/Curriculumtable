package com.zxl.Timetable.base.utils;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.dao.proxy.CurriculumDAOProxy;
import com.zxl.Timetable.base.entity.SimpleDate;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.util.ClassTimeSetUtil;

public class TimeUtil {
	
	
	protected static SharedPreferences preferences; 
	protected static ICurriculumDAO curriculumDAO;
	
	public static final void init(Context context) {
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 计算提醒上课的时间,并保存在文件中。
	 * 
	 * @param context
	 *            用于打开配置文件。
	 */
	public static final void calculateRemindTime(Context context) {
		SharedPreferences.Editor editor = preferences.edit();

		// 得到一天的上课的时间。
		final int classCount = preferences.getInt(
				AppConstant.Preferences.CLASSES_ONE_DAY, 0);

		// 得到需要提前多少分钟提醒。
		final int remindTime = preferences.getInt(
				AppConstant.Preferences.IN_ADVANCE, 0) * 5 + 5;

		// 计算具体的提醒时间。
		int h = 0;
		int m = 0;
		for (int i = 1; i <= classCount; i++) {
			h = preferences.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + i,
					0);
			m = preferences.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE
					+ i, 0);
			m -= remindTime;
			if (m < 0) {
				h--;
				m += 60;
			}
			if (h < 0) {
				h += 24;
			}
			editor.putInt(AppConstant.Preferences.REMIND_TIME_HOUR + i, h);
			editor.putInt(AppConstant.Preferences.REMIND_TIME_MINUTE + i, m);
		}
		editor.commit();
	}

	/**
	 * 判断当前是否正在上课   			 只是判断当前是否是上课时间
	 * 
	 * @return 当且仅当正在上课时返回true，否则返回false。
	 */
	public static final int hasSchoolNow(Context context) {
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

		// 得到一天的上课的时间。
		final int classCount = preferences.getInt(
				AppConstant.Preferences.CLASSES_ONE_DAY, 0);
		Calendar c = Calendar.getInstance();
		final int nowH = c.get(Calendar.HOUR_OF_DAY);
		final int nowM = c.get(Calendar.MINUTE);
		
		final int now = nowH * 60 + nowM;
		// 计算具体的提醒时间。
		int h1 = 0;
		int m1 = 0;
		int h2 = 0;
		int m2 = 0;
		for (int i = 1; i <= classCount; i++) {
			h1 = preferences.getInt(
					AppConstant.Preferences.CLASS_TIME_HOUR + i, 0);
			m1 = preferences.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE
					+ i, 0);
			h2 = preferences.getInt(
					AppConstant.Preferences.BREAK_TIME_HOUR + i, 0);
			m2 = preferences.getInt(AppConstant.Preferences.BREAK_TIME_MINUTE
					+ i, 0);
			if ((h1 * 60 + m1 <= now) && (now <= h2 * 60 + m2)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 指定节数是否有课。
	 * 
	 * @return 当且仅当有课时返回true，否则返回false。
	 */
	public static final boolean hasSchool(Context context, int classOnDay) {
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		SimpleDate simpleDate = new SimpleDate(preferences.getInt(
				AppConstant.Preferences.FIRST_DAY_YEAR, 0), preferences.getInt(
				AppConstant.Preferences.FIRST_DAY_MONTH, 0),
				preferences.getInt(AppConstant.Preferences.FIRST_DAY_DAY, 0));
		final int week = DateUtil.getWeeksOfTerm(simpleDate);
		if (week == -1) {
			return false;
		}
		curriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor c = curriculumDAO.getCurriculumsWeeks(DateUtil.getCurrentDayOfWeek(), classOnDay);
		while (c.moveToNext()) {
			if (Curriculum.isNeedClassAtWeek(week, c.getInt(c.getColumnIndex("weeks")))) {
				c.close();
				((CurriculumDAOProxy) curriculumDAO).closeDB();
				return true;
			}
		}
		c.close();
		((CurriculumDAOProxy) curriculumDAO).closeDB();
		return false;
	}
	/*
	 * 指定的某一天某一节课是否有课
	 */
	public static final boolean hasSchool(Context context, int dayOfWeek, int classOnDay) {
		
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		SimpleDate simpleDate = new SimpleDate(preferences.getInt(
				AppConstant.Preferences.FIRST_DAY_YEAR, 0), preferences.getInt(
				AppConstant.Preferences.FIRST_DAY_MONTH, 0),
				preferences.getInt(AppConstant.Preferences.FIRST_DAY_DAY, 0));
		final int week = DateUtil.getWeeksOfTerm(simpleDate);
		if (week == -1) {
			return false;
		}
		curriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor c = curriculumDAO.getCurriculumsWeeks(dayOfWeek, classOnDay);
		while (c.moveToNext()) {
			if (Curriculum.isNeedClassAtWeek(week, c.getInt(c.getColumnIndex("weeks")))) {
				c.close();
//				((CurriculumDAOProxy) curriculumDAO).closeDB();
				return true;
			}
		}
		c.close();
//		((CurriculumDAOProxy) curriculumDAO).closeDB();
		return false;
	}
	
	
	
	public static int classesOneDay(Context context) {
		preferences = context.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		int num = preferences.getInt(AppConstant.Preferences.CLASSES_ONE_DAY, 0);
		return num;
	}
	
	/*
	 * 计算当前时间处于第几节课 classOnDay
	 * 这里的计算方式，第一节课：第一节上课到第二节下课的时间，依次类推
	 * @return int类型数字，代表当前的时间是第几节课
	 */
	public static int inWhichClass(Context context) {
		SharedPreferences preferences = context
				.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME,
						Context.MODE_PRIVATE);

		// 得到一天的上课的时间。
		final int classCount = preferences.getInt(
				AppConstant.Preferences.CLASSES_ONE_DAY, 0);
		
		//获得当前时间
		
		Calendar c = Calendar.getInstance();
		final int nowH = c.get(Calendar.HOUR_OF_DAY);
		final int nowM = c.get(Calendar.MINUTE);
		final int now = nowH * 60 + nowM;
		
		int h1 = 0;
		int h2 = 0;
		int m1 = 0;
		int m2 = 0;
		int next = 0;
		for (int i = 1; i < classCount; i++) {
			next = i + 1;
			h1 = preferences.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + i, 0);
			m1 = preferences.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE + i, 0);
			h2 = preferences.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + next, 0);
			m2 = preferences.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE + next, 0);
			
			if ((h1 * 60 + m1) < now && (h2 * 60 + m2) > now) {
				return i;
			} /*else if ((h1 * 60 + m1) > now && (h2 * 60 +m2) > now) {
				return next;
			}*/
		}
		return 0;
	}
	
	
	/*
	 * 得到第n节课的上课时间
	 */
	public static Calendar getTimeOfClass(Context context, int classOnDay) {
		
		ClassTimeSetUtil.init(context, classOnDay);
		int classTime = ClassTimeSetUtil.getClassTime(classOnDay);
		int classTimeH = classTime / 60;
		int classTimeM = classTime % 60;
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, classTimeH);
		c.set(Calendar.MINUTE, classTimeM);
		c.set(Calendar.SECOND, 0);
		return c;
	}
	
	
	/*
	 * 获取当前上的是哪节课，作出相应的Notification通知
	 */
	public static Curriculum hasWhatCurriculum(Context context, int classOnDay) {
		Curriculum curriculum = Curriculum.getCurriculum(context, DateUtil.getCurrentDayOfWeek(), classOnDay);
		return curriculum;
	}
	
	public static boolean judgeTime(Context context, int dayOfWeek, int classOnDay) {
		Calendar classTime = getTimeOfClass(context, classOnDay);
		classTime.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTimeInMillis(System.currentTimeMillis());
		if (classTime.getTimeInMillis() - nowTime.getTimeInMillis() > 0) {
			return true;
		}
		
		return false;
	}
}
