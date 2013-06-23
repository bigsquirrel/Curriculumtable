package com.zxl.Timetable.alert;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.zxl.Timetable.R;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.entity.Building;
import com.zxl.Timetable.entity.Course;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.ui.MainActivity;

public class ClassAlertService extends Service {
	private static String TAG="ClassAlertService";
	
	String action;
	NotificationManager nm;

	int curriculumId;
	int courseId;
	int buildId;
	int classId;
	int dayOfWeek;
	String courseName;
	String teacherName;
	String nickName;
	String buildName;
	String onClassName;
	String onWeekName;
	String dayOfWeekName;
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		showNotification();
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle i = intent.getExtras();
		curriculumId = i.getInt("id");
		
		getCurriculumInfo(curriculumId);
		getCourseInfo(courseId);
		getBuildingInfo(buildId);
		
		return super.onStartCommand(intent, flags, startId);
	}

	public void showNotification() {
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent locateIntent = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notification = new Notification.Builder(this)
										.setTicker("下节课:" + courseName)
										.setDefaults(Notification.DEFAULT_ALL)
										.setSmallIcon(R.drawable.current_week)
										.setContentTitle("下节课:" + courseName)
										.setContentText(onClassName + ". 地点 :" + buildName)
										.setContentIntent(locateIntent)
										.build();
		
		
		nm.notify(0, notification);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	public void getCurriculumInfo(int id) {
		Curriculum curriculum = Curriculum.getCurriculum(this, id);
		courseId = curriculum.getCourseId();
		buildId = curriculum.getBuildingId();
		classId = curriculum.getOnClass();
		dayOfWeek = curriculum.getDay();
		onClassName = AppConstant.CLASSES_ONE_DAY[classId - 1];
		dayOfWeekName = AppConstant.week[dayOfWeek];
	}
	
	
	public void getCourseInfo(int id) {
		Course course = Course.getCourse(this, courseId);
		courseName = course.getCourse();
		nickName = course.getNickName();
		teacherName = course.getTeacher();
		
	}
	
	public void getBuildingInfo(int id) {
		Building building = Building.getBuilding(this, buildId);
		buildName = building.getBuildStr();
	}
	
}
