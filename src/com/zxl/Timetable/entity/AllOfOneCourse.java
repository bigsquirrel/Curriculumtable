package com.zxl.Timetable.entity;

import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.util.MyUtils;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;

/**
 * @author ivanchou
 * this class put all of the curriculum use the same course id together
 */
public class AllOfOneCourse {
	private int curriculumid;
	private int weeks;
	private int sign;
	private int color;
	private int dayOfWeek;
	private int onClass;
	
	private static AllOfOneCourse allOfOneCourse;
	public static SparseArray<AllOfOneCourse> allCurArray; 
	
	public AllOfOneCourse() {
		super();
	}

	public AllOfOneCourse(int curriculumid, int weeks, int sign, int color,
			int dayOfWeek, int onClass) {
		super();
		this.curriculumid = curriculumid;
		this.weeks = weeks;
		this.sign = sign;
		this.color = color;
		this.dayOfWeek = dayOfWeek;
		this.onClass = onClass;
	}
	
	/*
	 * 统计一节课的上课情况
	 * ---1、 某科目一周几节课 （weeks字段检查，courseid相同，建立一个对应的数组<第几周, 几节课>）
	 * 2、 获得签到信息 （weeks 字段与sign 字段检查本周是否有课，有课是否签到，若签到+1， 同时建立本周一共有多少节课 ps，第一条就不需要了）
	 * 3、
	 */
	public static void getAllOfOneCourse(Context context, int id) {
		//id is the courseid , and use the courseid get all curriculumid, and then get each curriculum's weeks and sign info
		
		ICurriculumDAO mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor cursor = mCurriculumDAO.getAllCourseIDCurriculum(id);
		allCurArray = new SparseArray<AllOfOneCourse>(cursor.getCount());
		int index = 0;
		
		do {
			allOfOneCourse = new AllOfOneCourse();
			int curriculumid = cursor.getInt(cursor.getColumnIndex("_id"));
			int dayOfWeek = cursor.getInt(cursor.getColumnIndex("dayOfWeek"));
			int onClass = cursor.getInt(cursor.getColumnIndex("onClass"));
			int weeks = cursor.getInt(cursor.getColumnIndex("weeks"));
			int sign = cursor.getInt(cursor.getColumnIndex("sign"));
			int color = cursor.getInt(cursor.getColumnIndex("color"));
			
			allOfOneCourse.setCurriculumid(curriculumid);
			allOfOneCourse.setDayOfWeek(dayOfWeek);
			allOfOneCourse.setOnClass(onClass);
			allOfOneCourse.setWeeks(weeks);
			allOfOneCourse.setSign(sign);
			allOfOneCourse.setColor(color);
			
			allCurArray.append(index, allOfOneCourse);
			System.out.println(index + " " +allOfOneCourse.toString());
			index ++ ;
		} while (cursor.moveToNext() != false);
		cursor.close();
		
	}
	
	public static int getTolSignClass(int week) {
		int tolSignClass = 0 ;
		int size = allCurArray.size();
		System.out.println(size);
		for (int i = 0; i < size; i++) {
			allOfOneCourse = allCurArray.get(i);
			int weeks = allOfOneCourse.getWeeks();
			int sign = allOfOneCourse.getSign();
			boolean b = MyUtils.isClassSigned(weeks, week, sign);
			System.out.println(b);
			if(MyUtils.isClassSigned(weeks, week, sign)) {
				tolSignClass ++ ;
			}
		}
		return tolSignClass;
	}
	
	public static int getTolClass(int week) {
		int tolClass;
		int size = allCurArray.size();
		tolClass = size;
		for (int i = 0; i < size; i++) {
			allOfOneCourse = allCurArray.get(i);
			if (!WeeksModel.isNeedAttendClass(allOfOneCourse.getWeeks(), week)) {
				tolClass --;
			}
		}
		return tolClass;
	}
	
	
	
	@Override
	public String toString() {
		return "AllOfOneCourse [curriculumid=" + curriculumid + ", weeks="
				+ weeks + ", sign=" + sign + ", color=" + color
				+ ", dayOfWeek=" + dayOfWeek + ", onClass=" + onClass + "]";
	}

	public int getCurriculumid() {
		return curriculumid;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getSign() {
		return sign;
	}

	public int getColor() {
		return color;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public int getOnClass() {
		return onClass;
	}

	public void setCurriculumid(int curriculumid) {
		this.curriculumid = curriculumid;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setOnClass(int onClass) {
		this.onClass = onClass;
	}
}
