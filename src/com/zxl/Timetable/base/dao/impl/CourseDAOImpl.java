
package com.zxl.Timetable.base.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.entity.Course;

/**
 * @author ivanchou
 */
public class CourseDAOImpl implements ICourseDAO {
	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;

	public CourseDAOImpl(SQLiteDatabase dbRead, SQLiteDatabase dbWrite) {
		this.dbRead = dbRead;
		this.dbWrite = dbWrite;
	}

	@Override
	public boolean insertCourse(Course course) {
		ContentValues v = new ContentValues();
		v.put("course", course.getCourse());
		v.put("nick", course.getNickName());
		v.put("teacher", course.getTeacher());
		return dbWrite.insert("t_courses", null, v) != -1;
	}

	@Override
	public boolean deleteCourse(int id) {
		return dbWrite.delete("t_courses", "_id=" + id, null) >= 1;
	}

	@Override
	public boolean updateCourse(int id, Course course) {
		ContentValues v = new ContentValues();
		v.put("course", course.getCourse());
		v.put("nick", course.getNickName());
		v.put("teacher", course.getTeacher());
		return dbWrite.update("t_courses", v, "_id=" + id, null) >= 1;
	}

	@Override
	public Cursor getAllCourses() {
		return dbRead.rawQuery("select * from t_courses", null);
	}

	@Override
	public boolean hasCourseBeReferences(int courseId) {
		Cursor c = dbRead.rawQuery(
				"select count(*) as num from t_curriculums where courseId = ?",
				new String[] { Integer.toString(courseId) });
		c.moveToFirst();
		final int num = c.getInt(c.getColumnIndex("num"));
		c.close();
		return num > 0;
	}

	@Override
	public void deleteAllCourses() {
		dbWrite.delete("t_courses", "1", null);
	}

	@Override
	public Cursor getCourse(int id) {
		Cursor cursor = dbRead.rawQuery("select course, nick, teacher from t_courses where _id=" + id, null);
		cursor.moveToFirst();
		return cursor;
	}

}
