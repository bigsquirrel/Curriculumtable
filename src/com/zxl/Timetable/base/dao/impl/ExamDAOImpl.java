package com.zxl.Timetable.base.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.entity.Exam;

/**
 * @author ivanchou
 *
 */
public class ExamDAOImpl implements IExamDAO{

	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;
	
	public ExamDAOImpl(SQLiteDatabase dbRead, SQLiteDatabase dbWrite) {
		this.dbRead = dbRead;
		this.dbWrite = dbWrite;
	}
	
	@Override
	public Cursor getExam(int id) {
		Cursor cursor = dbRead.rawQuery("select courseId, date, buildingId, roomNum, scores, totalScores,sign, color, remind, json from t_exams where _id=" + id + " and deleted<>'true'", null); 
		cursor.moveToFirst();
		return cursor;
	}

	@Override
	public Cursor getFutureExams() {
		
		return dbRead.rawQuery("select t_exams._id, date, color, remind, building, roomNum, course, nick, teacher from t_exams, t_courses, t_buildings " +
				"where t_exams.buildingId=t_buildings._id AND courseId=t_courses._id AND t_exams.sign='false' AND t_exams.deleted<>'true'", null);
	}

	@Override
	public Cursor getCompleteExams() {
		return dbRead.rawQuery("SELECT t_exams._id, t_exams.color, course, nick, teacher, scores, totalScores FROM t_exams, t_courses  WHERE t_exams.courseId = t_courses._id AND t_exams.sign <> 'false' AND t_exams.deleted <> 'true' ;", null);
	}
	
//	@Override
//	public Cursor getCompleteExams() {
//		return dbRead.rawQuery("select distinct (t_exams._id), t_exams.color, course, nick, teacher, weeks, t_curriculums.sign, scores, totalScores from t_exams, t_courses, t_buildings, t_curriculums " +
//				"where t_exams.buildingId=t_buildings._id AND t_exams.courseId=t_courses._id AND t_exams.sign<>'false' AND t_exams.deleted<>'true'", null);
//	}

	@Override
	public boolean insertExam(Exam exam) {
		ContentValues v = new ContentValues();
		v.put("courseId", exam.getCourseId());
		v.put("date", exam.getDate());
		v.put("buildingId", exam.getBuildingId());
		v.put("roomNum", exam.getRoomNum());
		v.put("sign", exam.getSign());
		v.put("color", exam.getColor());
		v.put("remind", exam.getRemind());
		v.put("deleted", exam.getDeleted());
		
		
		return dbWrite.insert("t_exams", null, v) != -1;
	}

	@Override
	public boolean updateExam(Exam exam) {
		ContentValues v = new ContentValues();
		v.put("courseId", exam.getCourseId());
		v.put("data", exam.getDate());
		v.put("buildingId", exam.getBuildingId());
		v.put("roomNum", exam.getRoomNum());
		v.put("sign", exam.getSign());
		v.put("color", exam.getColor());
		v.put("remind", exam.getRemind());
		
		return dbWrite.update("t_exams", v, "_id=" + exam.get_id(), null) >= 1;
	}
	
	@Override
	public boolean updateScores(int id, int s, int ts) {
		ContentValues v = new ContentValues();
		v.put("scores", s);
		v.put("totalScores", ts);
		v.put("sign", "true");
		return dbWrite.update("t_exams", v, "_id=" + id, null) >= 1;
	}

	@Override
	public boolean deletedExam(int id) {
		ContentValues v = new ContentValues();
		v.put("deleted", "true");
		return dbWrite.update("t_exams", v, "_id=" + id, null) >= 1;
	}

	@Override
	public boolean deletedAllExams() {
		ContentValues v = new ContentValues();
		v.put("deleted", "true");
		dbWrite.update("t_exams", v, null, null);
		return isExamsEmpty();
	}

	@Override
	public boolean isExamsEmpty() {
		Cursor c = dbRead.rawQuery("select count(*) as sum from t_exams where deleted<>ture", null);
		c.moveToFirst();
		final int sum = c.getInt(c.getColumnIndex("sum"));
		c.close();
		return sum == 0;
	}
	
}
