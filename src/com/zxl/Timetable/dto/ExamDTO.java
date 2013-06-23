package com.zxl.Timetable.dto;

import android.database.Cursor;

import com.zxl.Timetable.util.CursorUtil;

/**
 * @author ivanchou
 *
 */
public class ExamDTO {

	private long date;
	private int color;
	private int remind;
	private String building;
	private String roomNum;
	private String course;
	private String nick;
	private String teacher;
	private int scores;
	private int tolScores;
	private int weeks;
	private int sign;
	
	public ExamDTO(Cursor cursor) {
		date = CursorUtil.getLong(cursor, "date");
		color = CursorUtil.getInt(cursor, "color");
		remind = CursorUtil.getInt(cursor, "remind");
		building = CursorUtil.getString(cursor, "building");
		roomNum = CursorUtil.getString(cursor, "roomNum");
		course = CursorUtil.getString(cursor, "course");
		nick = CursorUtil.getString(cursor, "nick");
		teacher = CursorUtil.getString(cursor, "teacher");
		
	}
	
	public ExamDTO(Cursor cursor, int id) {
		course = CursorUtil.getString(cursor, "course");
		nick = CursorUtil.getString(cursor, "nick");
		teacher = CursorUtil.getString(cursor, "teacher");
		scores = CursorUtil.getInt(cursor, "scores");
		tolScores = CursorUtil.getInt(cursor, "totalScores");
	}

	public long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getRemind() {
		return remind;
	}

	public void setRemind(int remind) {
		this.remind = remind;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public int getScores() {
		return scores;
	}

	public int getTolScores() {
		return tolScores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	public void setTolScores(int tolScores) {
		this.tolScores = tolScores;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getSign() {
		return sign;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}
}
