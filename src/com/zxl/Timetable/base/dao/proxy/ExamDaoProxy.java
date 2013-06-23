package com.zxl.Timetable.base.dao.proxy;

import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.base.dao.impl.ExamDAOImpl;
import com.zxl.Timetable.base.dbc.DBConnection;
import com.zxl.Timetable.entity.Exam;

import android.content.Context;
import android.database.Cursor;

/**
 * @author ivanchou
 *
 */
public class ExamDaoProxy implements IExamDAO, DBCloseable{

	private IExamDAO mExamDAO;
	private DBConnection mConnection;
	
	public ExamDaoProxy(Context context) {
		mConnection = new DBConnection(context);
		mExamDAO = new ExamDAOImpl(mConnection.getReadableDatabase(), mConnection.getWritableDatabase());
	}

	@Override
	public Cursor getExam(int id) {
		return mExamDAO.getExam(id);
	}

	@Override
	public Cursor getFutureExams() {
		return mExamDAO.getFutureExams();
	}
	

	@Override
	public Cursor getCompleteExams() {
		return mExamDAO.getCompleteExams();
	}
	
	@Override
	public boolean insertExam(Exam exam) {
		return mExamDAO.insertExam(exam);
	}


	@Override
	public boolean updateExam(Exam exam) {
		return mExamDAO.updateExam(exam);
	}
	
	@Override
	public boolean updateScores(int id, int s, int ts) {
		return mExamDAO.updateScores(id, s, ts);
	}

	@Override
	public boolean deletedExam(int id) {
		return mExamDAO.deletedExam(id);
	}


	@Override
	public boolean deletedAllExams() {
		return mExamDAO.deletedAllExams();
	}
	
	@Override
	public boolean isExamsEmpty() {
		return mExamDAO.isExamsEmpty();
	}
	
	@Override
	public void closeDB() {
		if (mConnection != null) {
			mConnection.close();
		}
	}
	

	
}
