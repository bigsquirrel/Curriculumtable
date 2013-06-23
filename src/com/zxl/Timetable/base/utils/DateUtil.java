package com.zxl.Timetable.base.utils;

import java.util.Calendar;

import com.zxl.Timetable.base.entity.SimpleDate;

public class DateUtil {

	private static final int MILLS_OF_DAY = 86400000; // 一天的毫秒数。

	/**
	 * 得到当前日期是星期几。
	 * 
	 * @return 当为周日时，返回0，当为周一至周六时，则返回对应的1-6。
	 */
	public static final int getCurrentDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 得到开学第一天是几月几日。程序假定为第一周的周日。
	 * 
	 * @param nowWeek 现在是第几周
	 * @return
	 */
	public static final Calendar getFirstDayOfTerm(int nowWeek) {
		Calendar c = Calendar.getInstance();
		int amount = c.get(Calendar.DAY_OF_WEEK) - 1 + (nowWeek - 1) * 7;
		c.add(Calendar.DAY_OF_YEAR, -amount);
		return c;
	}

	/**
	 * 得到默认的开学日期（这里默认为新历9月份的第一个周日）。
	 * 
	 * @return
	 */
	public static final SimpleDate getDefaultFirstDay() {
		final Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
			calendar.set(calendar.get(Calendar.YEAR) - 1, Calendar.SEPTEMBER, 1);
		} else {
			calendar.set(calendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1);
		}
		final SimpleDate date = new SimpleDate(calendar);
		int tmp = 8 - calendar.get(Calendar.DAY_OF_WEEK);
		if (date.getDayOfMonth() > 7) {
			tmp -= 7;
		}
		date.setDayOfMonth(tmp);
		return date;
	}

	/**
	 * 判断当前是这个学期的第几周。
	 * 
	 * @param simpleDate
	 *            表示开学第一天的Calendar对象。
	 * @return 如果当前日期比第一天还要早，则返回-1。否则，返回具体的第几周。
	 */
	public static final int getWeeksOfTerm(SimpleDate simpleDate) {
		final Calendar current = Calendar.getInstance();
		final Calendar firstDay = Calendar.getInstance();
		firstDay.set(simpleDate.getYear(), simpleDate.getMonth() - 1,
				simpleDate.getDayOfMonth());
		if (current.compareTo(firstDay) <= -1) {
			return -1;
		}
		final long diff = (current.getTimeInMillis() - firstDay
				.getTimeInMillis()) / MILLS_OF_DAY;
		return (int) (diff / 7 + 1);
	}

}
