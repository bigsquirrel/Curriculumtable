
package com.zxl.Timetable.base.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.entity.Curriculum;

/**
 * @author ivanchou
 * @功能 实现了ICrriculumDAO 接口，实现了查询表的功能
 */
public class CurriculumDAOImpl implements ICurriculumDAO {
	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;

	public CurriculumDAOImpl(SQLiteDatabase dbRead, SQLiteDatabase dbWrite) {
		this.dbRead = dbRead;
		this.dbWrite = dbWrite;
	}
	
	@Override
	public Cursor getAllCurriculums() {
		return dbRead.rawQuery("select t_curriculums._id, weeks, sign, t_courses.nick, teacher, onClass, dayOfWeek from t_buildings, t_courses, t_curriculums " +
								"where buildingId=t_buildings._id and courseId=t_courses._id and deleted<>'true' " +
								"AND t_curriculums.courseId not in (select courseId from t_exams) order by courseId,dayOfWeek,onClass", null);
	}

	@Override
	public Cursor getCurriculumsByDay(int day) {
		return dbRead
				.rawQuery(
						"select t_curriculums._id, dayOfWeek, onClass, course, teacher, building, roomNum, weeks, sign, color " +
						"from t_buildings, t_courses, t_curriculums where buildingId=t_buildings._id and courseId=t_courses._id " +
						"and deleted<>'true' and dayOfWeek = " + day + " order by onClass", null);
	}

	@Override
	public Cursor getCurriculumsWeeks(int day, int onClass) {
		return dbRead.rawQuery(
				"select weeks from t_curriculums where dayOfWeek=" + day
						+ " and onClass=" + onClass + " and deleted<>'true'", null);
	}

	
	
	@Override
	public Cursor getCurriculumsWeeks(int day, int onClass, int id) {
		return dbRead
				.rawQuery(
						"select weeks from t_curriculums where dayOfWeek="
								+ day + " and onClass=" + onClass
								+ " and _id <> " + id, null);
	}

	@Override
	public Cursor getCurriculumByOnClass(int day, int classOnDay) {
		Cursor c = dbRead.rawQuery("select _id, dayOfWeek, onClass, courseId, weeks, buildingId, roomNum, remark, sign, color, remind " +
				"from t_curriculums where dayOfWeek=" + day + " and onClass=" + classOnDay + " and remind!=0 and deleted<>'true'", null);
		c.moveToFirst();
		return c;
	}
	
	
	@Override
	public Cursor getCurriculumDetail(int id) {
		Cursor c = dbRead
				.rawQuery(
						"select t_curriculums._id, dayOfWeek, onClass, course, teacher, building, roomNum, weeks, remark, sign, color, remind " +
						"from t_buildings, t_courses, t_curriculums where buildingId=t_buildings._id and courseId=t_courses._id and t_curriculums._id="
								+ id, null);
		c.moveToFirst();
		return c;
	}

	@Override
	public Cursor getCurriculum(int id) {
		Cursor c = dbRead
				.rawQuery(
						"select _id, dayOfWeek, onClass, courseId, buildingId, roomNum, weeks, remark, sign, color, remind from t_curriculums where _id="
								+ id, null);
		c.moveToFirst();
		return c;
	}

	@Override
	public Cursor getCurriculumCount(int dayOfWeek) {
		Cursor c = dbRead.rawQuery("select _id from t_curriculums where dayOfWeek = " + dayOfWeek, null);
		c.moveToFirst();
		return c;
	}
	
	@Override
	public Cursor getAllCourseIDCurriculum(int id) {
		Cursor c = dbRead.rawQuery("select _id, dayOfWeek, onClass, weeks, sign, color from t_curriculums where courseId="
									+ id + " and deleted<>'true'", null);
		c.moveToFirst();
		return c;
	}
	
	@Override
	public boolean deleteCurriculum(int id) {
		ContentValues v = new ContentValues();
		v.put("deleted", "true");
		//只是将delete标记改为true
		return dbWrite.update("t_curriculums", v, "_id=" + id, null) >= 1;
//		return dbWrite.delete("t_curriculums", "_id=" + id, null) >= 1;
	}
	
	@Override
	public boolean undoDelCurriculum(int id) {
		ContentValues v = new ContentValues();
		v.put("deleted", "false");
		return dbWrite.update("t_curriculums", v, "_id=" + id, null) >= 1;
	}
	
	@Override
	public boolean deleteAllCurriculums() {
		ContentValues v = new ContentValues();
		v.put("deleted", "true");
		dbWrite.update("t_curriculums", v, null, null);
//		dbWrite.delete("t_curriculums", "1", null);
		return isCurriculumsEmpty();
	}

	@Override
	public boolean isCurriculumsEmpty() {
		Cursor c = dbRead.rawQuery("select count(*) as sum from t_curriculums where deleted<>'ture'", null);
		c.moveToFirst();
		final int sum = c.getInt(c.getColumnIndex("sum"));
		c.close();
		return sum == 0;
	}

	@Override
	public boolean insertCurriculum(Curriculum curriculum) {
		ContentValues v = new ContentValues();
		v.put("dayOfWeek", curriculum.getDay());
		v.put("onClass", curriculum.getOnClass());
		v.put("courseId", curriculum.getCourseId());
		v.put("weeks", curriculum.getWeeks());
		v.put("buildingId", curriculum.getBuildingId());
		v.put("roomNum", curriculum.getRoomNum());
		v.put("remark", curriculum.getRemark());
		v.put("sign", curriculum.getSign());
		v.put("color", curriculum.getColor());
		v.put("deleted", curriculum.getDeleted());
		v.put("remind", curriculum.getRemind());
		v.put("json", curriculum.getJsonStr());
		return dbWrite.insert("t_curriculums", null, v) != -1;
	}

	@Override
	public boolean updateCurriculum(Curriculum curriculum) {
		ContentValues v = new ContentValues();
		v.put("dayOfWeek", curriculum.getDay());
		v.put("onClass", curriculum.getOnClass());
		v.put("courseId", curriculum.getCourseId());
		v.put("weeks", curriculum.getWeeks());
		v.put("buildingId", curriculum.getBuildingId());
		v.put("roomNum", curriculum.getRoomNum());
		v.put("remark", curriculum.getRemark());
		v.put("sign", curriculum.getSign());
		v.put("color", curriculum.getColor());
		v.put("remind", curriculum.getRemind());
		v.put("json", curriculum.getJsonStr());
		System.out.println("upadate curriculum get id" + curriculum.get_id());
		return dbWrite.update("t_curriculums", v, "_id=" + curriculum.get_id(), null) >= 1;
	}

}
