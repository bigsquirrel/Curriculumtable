package com.zxl.Timetable.base.entity;

import java.util.Calendar;

/**
 * 这里简单地封装了表示日期的年、月、日。
 * @author IVANCHOU
 *
 */
public class SimpleDate {

	private int year;
	private int month;
	private int dayOfMonth;

	public SimpleDate(Calendar calendar) {
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public SimpleDate(int year, int month, int dayOfMonth) {
		super();
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
}
