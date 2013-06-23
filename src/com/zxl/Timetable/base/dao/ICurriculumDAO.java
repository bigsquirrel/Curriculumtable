
package com.zxl.Timetable.base.dao;

import com.zxl.Timetable.entity.Curriculum;

import android.database.Cursor;

/**
 * @author IVANCHOU
 */
public interface ICurriculumDAO {
	
	/**
	 * 查询所有的课程
	 * @return 返回
	 */
	public Cursor getAllCurriculums();
	
	/**
	 * 查询某一天的课表。
	 * 
	 * @param day
	 *            指定的某一天，值为0-6，分别表示周日至周六。
	 * @return 返回当天所有课程的id, 科目名，教学楼名，课室 号。
	 */
	public Cursor getCurriculumsByDay(int day);

	/**
	 * 得到在某一天某一节课上课的课程的上课周数信息。
	 * 
	 * @param day
	 *            表示星期几的一个整数。
	 * @param onClass
	 *            表示在第几节。
	 * @return Cursor, 指定星期几和第几节上课的所有课程的上课周数。
	 */
	public Cursor getCurriculumsWeeks(int day, int onClass);

	/**
	 * 得到在某一天某一节课上课的课程的上课周数信息。
	 * 
	 * @param day
	 *            表示星期几的一个整数。
	 * @param onClass
	 *            表示在第几节。
	 * @param id
	 *            当前课程的id
	 * @return Cursor, 指定星期几和第几节上课的所有课程的上课周数。
	 */
	public Cursor getCurriculumsWeeks(int day, int onClass, int id);
	
	/**
	 * 得到在某一天的某一节课是否有课。
	 * 
	 * @param day
	 *            表示星期几的一个整数。
	 * @param classOnDay
	 *            表示在第几节。
	 */
	public Cursor getCurriculumByOnClass(int day, int classOnDay);

	/**
	 * 获取指定某一节课的信息细节信息。（包括具体的科目名，老师名，教学楼……）
	 * 
	 * @param id
	 *            指定某一节课的ID.
	 * @return Cursor 此Cursor对象已经指向第一个。
	 */
	public Cursor getCurriculumDetail(int id);

	/**
	 * 获取指定某一节课信息（只查询课程表）。
	 * 
	 * @param id
	 *            指定某一节课的ID.
	 * @return Cursor 此Cursor对象已经指向第一个。
	 */
	public Cursor getCurriculum(int id);

	public Cursor getCurriculumCount(int dayOfWeek);
	
	/**
	 * 获取指定某一科目被引用的所有课程信息（只查询课程表）。
	 * 
	 * @param id
	 *            指定某一科目的ID
	 */
	public Cursor getAllCourseIDCurriculum(int id);
	
	
	/**
	 * 删除指定的某一节课。
	 * 
	 * @param id
	 *            指定某一节课的ID.
	 * @return 当且仅当删除成功时返回true, 否则返回false。
	 */
	public boolean deleteCurriculum(int id);

	
	/**
	 * 撤销删除某一节课。
	 */
	public boolean undoDelCurriculum(int id);
	
	/**
	 * 清空所有课程。
	 * 
	 * @return 当且仅当删除课程成功时返回true，否则返回false。
	 */
	public boolean deleteAllCurriculums();

	/**
	 * 查询课程表是否是空的。
	 * 
	 * @return 当且仅当课程表中的数据是空的时候返回true，否则返回false。
	 */
	boolean isCurriculumsEmpty();

	/**
	 * 添加课程。
	 * 
	 * @param curriculum
	 *            要插入的课程。
	 * @return 当且仅当插入成功时返回true, 否则返回false。
	 */
	public boolean insertCurriculum(Curriculum curriculum);
	/**
	 * 修改课程。
	 * 
	 * @param curriculum
	 *            要修改的课程。
	 * @return 当且仅当修改成功时返回true, 否则返回false。
	 */
	public boolean updateCurriculum(Curriculum curriculum) ;
	
}
