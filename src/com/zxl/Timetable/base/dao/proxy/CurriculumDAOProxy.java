
package com.zxl.Timetable.base.dao.proxy;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.impl.CurriculumDAOImpl;
import com.zxl.Timetable.base.dbc.DBConnection;
import com.zxl.Timetable.entity.Curriculum;

/**
 * @author IVANCHOU
 */
public class CurriculumDAOProxy implements ICurriculumDAO, DBCloseable {
	private ICurriculumDAO mCurriculumDAO;
	private DBConnection mConnection;

	public CurriculumDAOProxy(Context context) {
		mConnection = new DBConnection(context);
		mCurriculumDAO = new CurriculumDAOImpl(mConnection.getReadableDatabase(), mConnection.getWritableDatabase());
	}

	@Override
	public Cursor getAllCurriculums() {
		return mCurriculumDAO.getAllCurriculums();
	}

	@Override
	public Cursor getCurriculumsByDay(int day) {
		return mCurriculumDAO.getCurriculumsByDay(day);
	}

	@Override
	public Cursor getCurriculumsWeeks(int day, int onClass) {
		return mCurriculumDAO.getCurriculumsWeeks(day, onClass);
	}

	@Override
	public Cursor getCurriculumsWeeks(int day, int onClass, int id) {
		return mCurriculumDAO.getCurriculumsWeeks(day, onClass, id);
	}
	

	@Override
	public Cursor getCurriculumByOnClass(int day, int classOnDay) {
		return mCurriculumDAO.getCurriculumByOnClass(day, classOnDay);
	}


	@Override
	public Cursor getCurriculumDetail(int id) {
		return mCurriculumDAO.getCurriculumDetail(id);
	}

	@Override
	public Cursor getCurriculum(int id) {
		return mCurriculumDAO.getCurriculum(id);
	}


	@Override
	public Cursor getCurriculumCount(int dayOfWeek) {
		return mCurriculumDAO.getCurriculumCount(dayOfWeek);
	}
	
	@Override
	public Cursor getAllCourseIDCurriculum(int id) {
		return mCurriculumDAO.getAllCourseIDCurriculum(id);
	}
	
	@Override
	public boolean deleteCurriculum(int id) {
		return mCurriculumDAO.deleteCurriculum(id);
	}
	
	@Override
	public boolean undoDelCurriculum(int id) {
		return mCurriculumDAO.undoDelCurriculum(id);
	}

	@Override
	public boolean deleteAllCurriculums() {
		return mCurriculumDAO.deleteAllCurriculums();
	}

	@Override
	public boolean isCurriculumsEmpty() {
		return mCurriculumDAO.isCurriculumsEmpty();
	}

	@Override
	public boolean insertCurriculum(Curriculum curriculum) {
		return mCurriculumDAO.insertCurriculum(curriculum);
	}

	@Override
	public boolean updateCurriculum(Curriculum curriculum) {
		return mCurriculumDAO.updateCurriculum(curriculum);
	}

	@Override
	public void closeDB() {
		if (mConnection != null) {
			mConnection.close();
		}
	}

}
