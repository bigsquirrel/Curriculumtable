
package com.zxl.Timetable.dto;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.util.ClassTimeSetUtil;
import com.zxl.Timetable.util.CursorUtil;
import com.zxl.Timetable.util.MyUtils;

/**
 * @author ivanchou
 */
public class CurriculumDTO {

	private static final String TAG = "CurriculumDTO";
	private int day;
	private int onClassInt;
	private String onClassStr;
	private String onClassTime;
	private String weeksModel;
	private String course;
	private String teacher;
	private String building;
	private String room;
	private int id;
	private int sign;
	private int weeks;
	private int ahead;
	private boolean isNeedAttendClass;
	private boolean isClassSigned;

	public CurriculumDTO(Context context, Cursor cursor, int currentWeek) {
		day = CursorUtil.getInt(cursor, "dayOfWeek");

		onClassInt = CursorUtil.getInt(cursor, "onClass");
		onClassStr = onClassInt + "";// 这里为1 2 3 ....
		ClassTimeSetUtil.init(context, onClassInt);
		onClassTime = ClassTimeSetUtil.getClassTimeString() + "-" + ClassTimeSetUtil.getBreakTimeString();
//		onClassTime = String
//				.format("%02d:%02d—%02d:%02d",
//						kv.getInt(AppConstant.Preferences.CLASS_TIME_HOUR
//								+ onClass, 0),
//						kv.getInt(AppConstant.Preferences.CLASS_TIME_MINUTE
//								+ onClass, 0),
//						kv.getInt(AppConstant.Preferences.BREAK_TIME_HOUR
//								+ onClass, 0),
//						kv.getInt(AppConstant.Preferences.BREAK_TIME_MINUTE
//								+ onClass, 0));

//		Log.d(TAG,
//				kv.getInt(AppConstant.Preferences.CLASS_TIME_HOUR + onClass, 0)
//						+ "");
		onClassStr = "第" + onClassStr + "节";
		isNeedAttendClass = WeeksModel.isNeedAttendClass(CursorUtil.getInt(cursor, "weeks"), currentWeek);
		weeksModel = WeeksModel.getWeeksModel(CursorUtil.getInt(cursor, "weeks")).toString();
		weeks = CursorUtil.getInt(cursor, "weeks");
		course = CursorUtil.getString(cursor, "course");
		teacher = CursorUtil.getString(cursor, "teacher");
		building = CursorUtil.getString(cursor, "building");
		room = CursorUtil.getString(cursor, "roomNum");
		sign = CursorUtil.getInt(cursor, "sign");
		id = CursorUtil.getInt(cursor, "_id");
		isClassSigned = MyUtils.isClassSigned(weeks, currentWeek ,sign);
	}
	

	public CurriculumDTO(Cursor cursor, int week) {
		day = CursorUtil.getInt(cursor, "dayOfWeek");
		onClassInt = CursorUtil.getInt(cursor, "onClass");
		onClassStr = "第" + onClassInt + "节";
		isNeedAttendClass = WeeksModel.isNeedAttendClass(CursorUtil.getInt(cursor, "weeks"), week);
		weeks = CursorUtil.getInt(cursor, "weeks");
		sign = CursorUtil.getInt(cursor, "sign");
		course = CursorUtil.getString(cursor, "nick");
		teacher = CursorUtil.getString(cursor, "teacher");
		isClassSigned = MyUtils.isClassSigned(weeks, week ,sign);
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getOnClass() {
		return onClassStr;
	}

	public String getOnClassTime() {
		return onClassTime;
	}

	public void setOnClassTime(String onClassTime) {
		this.onClassTime = onClassTime;
	}

	public String getWeeksModel() {
		return weeksModel;
	}

	public void setWeeksModel(String weeksModel) {
		this.weeksModel = weeksModel;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	
	public int getSign() {
		return sign;
	}
	
	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNeedAttendClass() {
		return isNeedAttendClass;
	}

	public void setNeedAttendClass(boolean isNeedAttendClass) {
		this.isNeedAttendClass = isNeedAttendClass;
	}
	
	public boolean isClassSigned() {
		return isClassSigned;
	}

}
