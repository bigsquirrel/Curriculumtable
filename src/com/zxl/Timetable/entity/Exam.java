package com.zxl.Timetable.entity;

import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import android.content.Context;
import android.database.Cursor;

/**
 * @author ivanchou
 *
 */
public class Exam {
	private int _id;
	private int courseId; //这个id是从t_curriculums.id对应的courseId取到的，也就是说一个courseId只能有一个exam
	private long date;
	private int buildingId;
	private String roomNum;
	private int scores;
	private int totalScores;
	private String sign;
	private int color;
	private String deleted;
	private int remind;
	private String json;
	
	private static Exam exam;
	
	public Exam() {
		super();
	}

	public Exam(int courseId, long date, int buildingId, int scores,
			String sign, int color, String deleted, int remind, String json) {
		super();
		this.courseId = courseId;
		this.date = date;
		this.buildingId = buildingId;
		this.scores = scores;
		this.sign = sign;
		this.color = color;
		this.deleted = deleted;
		this.remind = remind;
		this.json = json;
	}
	
	public static Exam getExam(Context context, int id) {
		if (exam == null) {
			exam = new Exam();
		}
		exam._id = id;
		IExamDAO mExamDAO = DAOFactory.getExamDaoInstance(context);
		Cursor cursor = mExamDAO.getExam(id);
		
		int courseId = cursor.getInt(cursor.getColumnIndex("courseId")); 
		long date = cursor.getInt(cursor.getColumnIndex("date"));
		int buildingId = cursor.getInt(cursor.getColumnIndex("buildingId"));
		String roomNum = cursor.getString(cursor.getColumnIndex("roomNum"));
		int scores = cursor.getInt(cursor.getColumnIndex("scores"));
		int totalScores = cursor.getInt(cursor.getColumnIndex("totalScores"));
		String sign = cursor.getString(cursor.getColumnIndex("sign"));
		int color = cursor.getInt(cursor.getColumnIndex("color"));
		
		int remind = cursor.getInt(cursor.getColumnIndex("remind"));
		
		cursor.close();
		
		exam.setCourseId(courseId);
		exam.setDate(date);
		exam.setBuildingId(buildingId);
		exam.setRoomNum(roomNum);
		exam.setScores(scores);
		exam.setTotalScores(totalScores);
		exam.setSign(sign);
		exam.setColor(color);
		exam.setRemind(remind);
		
		return exam;
	}

	

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public int getScores() {
		return scores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public int getRemind() {
		return remind;
	}

	public void setRemind(int remind) {
		this.remind = remind;
	}

	public int getTotalScores() {
		return totalScores;
	}

	public void setTotalScores(int totalScores) {
		this.totalScores = totalScores;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
}
