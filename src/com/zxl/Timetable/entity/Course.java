package com.zxl.Timetable.entity;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;

/**
 * 该类封装了科目。
 * 
 * @author ivanchou
 */
public class Course {

	private int id;
	private String courseStr;
	private String teacherStr;
	private String nickNameStr;
	private static Course course;
	
	
	
	public static Course getCourse(Context context, int id) {
		if (course == null) {
			course = new Course();
		}
		ICourseDAO courseDAO = DAOFactory.getCourseDAOInstance(context);
		Cursor cursor = courseDAO.getCourse(id);
		String courseStr = cursor.getString(cursor.getColumnIndex("course"));
		String nickNameStr = cursor.getString(cursor.getColumnIndex("nick"));
		String teacherStr = cursor.getString(cursor.getColumnIndex("teacher"));

		course.setCourse(courseStr);
		course.setTeacher(teacherStr);
		course.setNickNameStr(nickNameStr);
		
		cursor.close();
		return course;
	}
	
	public Course() {
		
	}

	public Course(String courseName, String nickName, String teacher) {
		super();
		this.courseStr = courseName;
		this.nickNameStr = nickName;
		this.teacherStr = teacher;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public String getCourse() {
		return courseStr;
	}

	public void setCourse(String course) {
		this.courseStr = course;
	}

	public String getTeacher() {
		return teacherStr;
	}

	public void setTeacher(String teacher) {
		this.teacherStr = teacher;
	}

	public String getNickName() {
		return nickNameStr;
	}

	public void setNickNameStr(String nickNameStr) {
		this.nickNameStr = nickNameStr;
	}

}
