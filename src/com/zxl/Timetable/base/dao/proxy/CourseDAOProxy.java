
package com.zxl.Timetable.base.dao.proxy;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.impl.CourseDAOImpl;
import com.zxl.Timetable.base.dbc.DBConnection;
import com.zxl.Timetable.entity.Course;

/**
 * @author ivanchou
 */
public class CourseDAOProxy implements ICourseDAO, DBCloseable {
	private ICourseDAO mCourseDAO;
	private DBConnection mConnection;

	public CourseDAOProxy(Context context) {
		mConnection = new DBConnection(context);
		mCourseDAO = new CourseDAOImpl(mConnection.getReadableDatabase(),
				mConnection.getWritableDatabase());
	}

	@Override
	public boolean insertCourse(Course course) {
		return mCourseDAO.insertCourse(course);
	}

	@Override
	public boolean deleteCourse(int id) {
		return mCourseDAO.deleteCourse(id);
	}

	@Override
	public boolean updateCourse(int id, Course course) {
		return mCourseDAO.updateCourse(id, course);
	}

	@Override
	public Cursor getAllCourses() {
		return mCourseDAO.getAllCourses();
	}

	@Override
	public boolean hasCourseBeReferences(int courseId) {
		return mCourseDAO.hasCourseBeReferences(courseId);
	}

	@Override
	public void deleteAllCourses() {
		mCourseDAO.deleteAllCourses();
	}

	@Override
	public void closeDB() {
		if (mConnection != null) {
			mConnection.close();
		}
	}

	@Override
	public Cursor getCourse(int id) {
		Cursor cursor = mCourseDAO.getCourse(id);
		return cursor;
	}

}
