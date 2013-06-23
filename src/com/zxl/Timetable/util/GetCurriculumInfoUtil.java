package com.zxl.Timetable.util;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.common.AppConstant;

public class GetCurriculumInfoUtil {
	
	
	private static String string = null;
	private static ICurriculumDAO mCurriculumDAO;
	private static ICourseDAO mCourseDAO;
	private static IBuildingDAO mBuildingDAO;
	private static Cursor cursor;
	private static WeeksModel model;
	private static int sign;
	private static int weeks;
	private static int currentWeek;
	private static Context con;
	
	
	public static void initGetInfo(Context context, int id) {
		
		con = context;
		mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		cursor = mCurriculumDAO.getCurriculumDetail(id);
		model = WeeksModel.getWeeksModel(cursor.getInt(cursor.getColumnIndex("weeks")));
		currentWeek = MyUtils.getCurrentWeek(context);
	}
	
	public static void initEditBuild(Context context, int id) {
		mBuildingDAO = DAOFactory.getBuildingDAOInstance(context);
		cursor = mBuildingDAO.getBuilding(id);
	}
	
	public static void initEditCourse(Context context, int id) {
		mCourseDAO = DAOFactory.getCourseDAOInstance(context);
		cursor = mCourseDAO.getCourse(id);
	}
	
	public static void endGetWork() {
		DBUtil.closeDB((DBCloseable) mCurriculumDAO);
	}
	
	public static void endEditWork(){
		DBUtil.closeDB((DBCloseable) mBuildingDAO);
		
	}
	
	public static void endEditCourse(){
		DBUtil.closeDB((DBCloseable) mCourseDAO);
	}
	/*
	 * 获得课程+教师
	 */
	public static String getTitle() {
		
		string = cursor.getString(cursor.getColumnIndex("course")) + ": " + cursor.getString(cursor.getColumnIndex("teacher"));
		return string;
	}
	
	/*
	 * 获取上课节数时间
	 */
	public static String getTime() {
		string = AppConstant.week[cursor.getInt(cursor.getColumnIndex("dayOfWeek"))] + 
				" " + AppConstant.CLASSES_ONE_DAY[cursor.getInt(cursor.getColumnIndex("onClass")) - 1];
		return string;
	}
	
	/*
	 * 获取上课地点
	 */
	public static String getPlace() {
		string = cursor.getString(cursor.getColumnIndex("building")) + " " +cursor.getString(cursor.getColumnIndex("roomNum"));
		return string;
	}
	
	/*
	 * 获取上课周数
	 */
	
	public static String getWeeks() {
		string = model.toString();
		return string;
	}
	
	/*
	 * 获取备注信息
	 */
	public static String getMark(){
		string = cursor.getString(cursor.getColumnIndex("remark"));
		return string;
	}
	
	/*
	 * 获取所需编辑的教学楼名字，用于dialog中
	 */
	public static String getBulid() {
		string = cursor.getString(cursor.getColumnIndex("building"));
		return string;
	}
	
	/*
	 * 获取所需编辑的科目名
	 */
	public static String getCourse() {
		string = cursor.getString(cursor.getColumnIndex("course"));
		return string;
	}
	
	/*
	 * 获取所需编辑的教师名
	 */
	public static String getTeacher() {
		string = cursor.getString(cursor.getColumnIndex("teacher"));
		return string;
	}
	
	public static String getNick() {
		string = cursor.getString(cursor.getColumnIndex("nick"));
		return string;
	}
	
	/*
	 * 
	 */
	public static void getClassTime() {
		
		
	}
	
	/*
	 * 判断当前课程是否能签到，
	 * 条件：
	 *      1、当前课程需要上课
	 *      2、当前课程是否以前签到
	 *      3、系统时间是否在当前课程上课时间范围中 
	 */
	
//	public static boolean isCurrentClassCanbeSign() {
//		Time classTime = MyUtils.getClassTime(con, cursor.getInt(cursor.getColumnIndex("onClass")));
//		Time curTime = 
//		return false;
//	}
//	
	/*
	 * 获取某课程已经签到周数的信息
	 */
	public static String getClassSignedWeek() {
		sign = cursor.getInt(cursor.getColumnIndex("sign"));
		weeks = cursor.getInt(cursor.getColumnIndex("weeks"));
		string = "";
		for (int i = 1; i <= currentWeek; i++) {
			if(MyUtils.isClassSigned(weeks, i, sign)){
				string = string + i + ",";
			}
		}
		if (string.length() != 0) {
			string = string.substring(0, string.length() - 1);
			return string = "第 " + string + " 周";
		} else {
			return string = "暂无签到";
		}
		
	}
	
}
