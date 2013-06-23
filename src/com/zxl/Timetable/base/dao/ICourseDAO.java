
package com.zxl.Timetable.base.dao;

import android.database.Cursor;

import com.zxl.Timetable.entity.Course;

/**
 * @author ivanchou
 */
public interface ICourseDAO {

	/**
	 * 添加科目。
	 * 
	 * @param course
	 *            科目对象，包含科目名称及授课老师名字。
	 * @return 当且仅当插入成功时返回true, 否则返回false。
	 */
	public boolean insertCourse(Course course);

	/**
	 * 删除指定的科目。
	 * 
	 * @param id
	 *            指定科目的ID.
	 * @return 当且仅当删除成功时返回true, 否则返回false。
	 */
	public boolean deleteCourse(int id);

	/**
	 * 修改指定的科目。
	 * 
	 * @param id
	 *            需要修改的科目的ID.
	 * @param course
	 *            修改之后的科目信息。
	 * @return 当且仅当修改成功时返回true, 否则返回false.
	 */
	public boolean updateCourse(int id, Course course);

	/**
	 * 获取所有的科目。
	 * 
	 * @return 所有的科目。注意它不是同步的。
	 */
	public Cursor getAllCourses();
	
	/*
	 * 获取某个科目
	 */
	
	public Cursor getCourse(int id);

	/**
	 * 某一科目是否被引用。
	 */
	public boolean hasCourseBeReferences(int courseId);
	/**
	 * 清空所有的科目。
	 */
	public void deleteAllCourses() ;
}
