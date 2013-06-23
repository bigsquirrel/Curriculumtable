/*
 * @(#)TableWidgetProvider.java		       Project:UniversityTimetable
 * Date:2013-2-11
 *
 * Copyright (c) 2013 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zxl.Timetable.appwidget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.lurencun.cfuture09.androidkit.db.KV;
import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.SimpleDate;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.base.utils.DateUtil;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.dto.CurriculumDTO;
import com.zxl.Timetable.ui.MainActivity;
import com.zxl.Timetable.util.CursorUtil;
import com.zxl.Timetable.R;

/**
 * @Author ivanchou
 * @Function
 */
public class TableSmallWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "TableSmallWidgetProvider";
	public static final String ACTION_UPDATE = "cfuture09.universityTimetable.action.TIMETABLE.APPWIDGET_SMALL_UPDATE";

	private ICurriculumDAO mCurriculumDAO;
	private KV mKV;
	private Context con;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(TAG, "onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		DBUtil.closeDB((DBCloseable) mCurriculumDAO);
		Log.d(TAG, "onDisable");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.d(TAG, "onEnabled");
		init(context);
	}

	private void init(Context context) {
		con = context;
		mKV = new KV(context, AppConstant.Preferences.PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		if (ACTION_UPDATE.equals(intent.getAction())) {
			if (mKV == null) {
				init(context);
			}
			int currentDay = DateUtil.getCurrentDayOfWeek();
			String[] arrayWeeks = context.getResources().getStringArray(
					R.array.array_week);

			CurriculumDTO dto = getWidgetData(currentDay);
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_small);
			// 设置星期几
			remoteViews.setTextViewText(R.id.widget_small_day,
					arrayWeeks[currentDay]);
			// 更新节课
			updateWidgetViews(remoteViews, dto);
			PendingIntent startIntent = PendingIntent.getActivity(context, 0,
					new Intent(context, MainActivity.class), 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_small_content,
					startIntent);

			ComponentName componentName = new ComponentName(context,
					TableSmallWidgetProvider.class);
			AppWidgetManager.getInstance(context).updateAppWidget(
					componentName, remoteViews);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "onUpdate");
		if (mKV == null) {
			init(context);
		}
		int currentDay = DateUtil.getCurrentDayOfWeek();
		String[] arrayWeeks = context.getResources().getStringArray(
				R.array.array_week);
		CurriculumDTO dto = getWidgetData(currentDay);

		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_small);
			// 设置星期几
			remoteViews.setTextViewText(R.id.widget_small_day,
					arrayWeeks[currentDay]);
			// 更新节课
			updateWidgetViews(remoteViews, dto);
			PendingIntent startIntent = PendingIntent.getActivity(context, 0,
					new Intent(context, MainActivity.class), 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_small_content,
					startIntent);

			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE);
			// 以发送广播消息的方式创建PendingIntent.
			PendingIntent pending_intent = PendingIntent.getBroadcast(context,
					0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.widget_small_refresh,
					pending_intent);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
	}

	/**
	 * 返回当前应该显示哪一节课。以上课时间判断当前还没上的下一节课。如果下一节已经没课，则返回最后一节。
	 * 
	 * @return 返回下一节课为第几节，如果下一节没课，则返回最后一节。
	 */
	private int getCurrentClassTime() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int classCount = mKV.getInt(AppConstant.Preferences.CLASSES_ONE_DAY, 0);
		int startH = 0;
		int startM = 0;

		for (int i = 1; i <= classCount; i++) {
			startH = mKV.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + i, 0);
			startM = mKV.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE + i,
					0);
			if (hour * 60 + minute <= startH * 60 + startM) {
				return i;
			}
		}
		return classCount + 1;
	}

	/**
	 * 更新appwidget
	 * 
	 * @param views
	 * @param dto
	 */
	private void updateWidgetViews(RemoteViews views, CurriculumDTO dto) {
		if (dto == null) {
			views.setTextViewText(R.id.list_item_class, "");
			views.setTextViewText(R.id.list_item_time, "");
			views.setTextViewText(R.id.list_item_course, "貌似没课");
			views.setTextViewText(R.id.list_item_teacher, "点此打开");
			views.setTextViewText(R.id.list_item_week, "");
			views.setTextViewText(R.id.list_item_room,"");
			return;
		}
		views.setTextViewText(R.id.list_item_class, dto.getOnClass());
		views.setTextViewText(R.id.list_item_time, dto.getOnClassTime());
		views.setTextViewText(R.id.list_item_course, dto.getCourse());
		views.setTextViewText(R.id.list_item_teacher, dto.getTeacher());
		views.setTextViewText(R.id.list_item_week, dto.getWeeksModel());
		views.setTextViewText(R.id.list_item_room,
				dto.getBuilding() + dto.getRoom());
	}

	/**
	 * 更新appwidget的数据。
	 * 
	 * @param day
	 *            星期几
	 * @param classTime
	 *            第几节
	 * @return
	 */
	private CurriculumDTO getWidgetData(int day) {
		// 显示第几节
		int classTime = getCurrentClassTime();
		Cursor cursor = mCurriculumDAO.getCurriculumsByDay(day);
		while (cursor.moveToNext()) {
			if (CursorUtil.getInt(cursor, "onClass") >= classTime) {
				break;
			}
		}
		if (cursor.isAfterLast()) {
			return null;
		}
		final SimpleDate date = DateUtil.getDefaultFirstDay();
		final SimpleDate simpleDate = new SimpleDate(mKV.getInt(
				AppConstant.Preferences.FIRST_DAY_YEAR, date.getYear()),
				mKV.getInt(AppConstant.Preferences.FIRST_DAY_MONTH,
						date.getMonth()), mKV.getInt(
						AppConstant.Preferences.FIRST_DAY_DAY,
						date.getDayOfMonth()));
		int currentWeek = DateUtil.getWeeksOfTerm(simpleDate);
		CurriculumDTO dto = new CurriculumDTO(con, cursor, currentWeek);
		return dto;
	}
}
