
package com.zxl.Timetable.entity;

import android.content.Context;
import android.database.Cursor;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.common.AppConstant;

/**
 * 该类封装了课程，与数据库中的课程表相对应。
 * 
 * @author ivanchou 
 */
public class Curriculum /*implements Parcelable*/{

	private int _id; // ID
	private int day; // 星期几
	private int onClass; // 第几节

	/*
	 * 表示在哪些周需要上课。这里以一个int型来表示，第0位至第21位分别对应1至22周，需要上课为1，否则为0。
	 * 第29至31位则表示单周、双周或全部周都要上课 ,即AppConstant.Weeks中的FLAG_ODD, FLAG_EVEN,
	 * FLAG_ALL三个常量。
	 */
	private int weeks; // 周数.
	private int buildingId; // 教学楼
	private int courseId; // 科目
	private String roomNum; // 课室号
	private String remark; // 备注
	private int sign;
	private int color; //给课程设置的标记颜色
	private String deleted; //课程的删除与否的标记
	private int remind; //remind = 0 ，代表无需设置提醒， remind = （int）i 设置提醒为提前i分钟
	private String jsonStr; //将课程信息编码为json数据，用于同步。。。
	private boolean isSetReminder; //判断课程是否设置了提醒
	

	private static Curriculum curriculum;
	private ICurriculumDAO mCurriculumDAO;
	private Cursor cursor;
	

	public Curriculum() {
		super();
	}

	public Curriculum(int day, int onClass, int weeks, int buildingId,
			int courseId, String roomNum, String remark, int sign) {
		super();
		this.day = day;
		this.onClass = onClass;
		this.weeks = weeks;
		this.buildingId = buildingId;
		this.courseId = courseId;
		this.roomNum = roomNum;
		this.remark = remark;
		this.sign = sign;
	}

	

	@Override
	public String toString() {
		return "Curriculum [_id=" + _id + ", day=" + day + ", onClass="
				+ onClass + ", weeks=" + Integer.toBinaryString(weeks)
				+ ", buildingId=" + buildingId + ", courseId=" + courseId
				+ ", roomNum=" + roomNum + ", remark=" + remark + ", sign=" + sign + "]";
	}

	public static Curriculum getCurriculum(Context context, int id) {
		if (curriculum == null) {
			curriculum = new Curriculum();
		}
		curriculum._id = id;
		ICurriculumDAO mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor cursor = mCurriculumDAO.getCurriculum(id);
		
		int day = cursor.getInt(cursor.getColumnIndex("dayOfWeek"));
		int onClass = cursor.getInt(cursor.getColumnIndex("onClass"));
		int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
		int weeks = cursor.getInt(cursor.getColumnIndex("weeks"));
		
		int buildingId = cursor.getInt(cursor.getColumnIndex("buildingId"));
		String roomNum = cursor.getString(cursor.getColumnIndex("roomNum"));
		String remark = cursor.getString(cursor.getColumnIndex("remark"));
		int sign = cursor.getInt(cursor.getColumnIndex("sign"));
		int remind = cursor.getInt(cursor.getColumnIndex("remind"));
		int color = cursor.getInt(cursor.getColumnIndex("color"));
		
		curriculum.setDay(day);
		curriculum.setOnClass(onClass);
		curriculum.setCourseId(courseId);
		curriculum.setWeeks(weeks);
		curriculum.setBuildingId(buildingId);
		curriculum.setRoomNum(roomNum);
		curriculum.setRemark(remark);
		curriculum.setSign(sign);
		curriculum.setRemind(remind);
		curriculum.setColor(color);
		cursor.close();
		return curriculum;
		
	}
	
	/*
	 * dayOfWeek是星期n， classOnDay是第几节课
	 */
	public static Curriculum getCurriculum(Context context, int dayOfWeek, int classOnDay) {
//		if (curriculum == null) {
//			curriculum = new Curriculum();
//		}
		curriculum = new Curriculum();
		ICurriculumDAO mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor cursor = mCurriculumDAO.getCurriculumByOnClass(dayOfWeek, classOnDay);
		
		
		curriculum._id = cursor.getInt(cursor.getColumnIndex("_id"));
		int day = cursor.getInt(cursor.getColumnIndex("dayOfWeek"));
		int onClass = cursor.getInt(cursor.getColumnIndex("onClass"));
		int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
		int weeks = cursor.getInt(cursor.getColumnIndex("weeks"));
		int buildingId = cursor.getInt(cursor.getColumnIndex("buildingId"));
		String roomNum = cursor.getString(cursor.getColumnIndex("roomNum"));
		String remark = cursor.getString(cursor.getColumnIndex("remark"));
		int sign = cursor.getInt(cursor.getColumnIndex("sign"));
		int remind = cursor.getInt(cursor.getColumnIndex("remind"));
		int color = cursor.getInt(cursor.getColumnIndex("color"));
		
		curriculum.setDay(day);
		curriculum.setOnClass(onClass);
		curriculum.setCourseId(courseId);
		curriculum.setWeeks(weeks);
		curriculum.setBuildingId(buildingId);
		curriculum.setRoomNum(roomNum);
		curriculum.setRemark(remark);
		curriculum.setSign(sign);
		curriculum.setRemind(remind);
		curriculum.setColor(color);
		cursor.close();
		return curriculum;
	}

	public static int getCurriculumCount(Context context, int dayOfWeek) {
		ICurriculumDAO mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(context);
		Cursor cursor = mCurriculumDAO.getCurriculumCount(dayOfWeek);
		int count = cursor.getInt(cursor.getCount());
		return count;
	}
	
	public void setCurriculum(Curriculum curriculum) {
		Curriculum.curriculum = curriculum;
	}
	
	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getOnClass() {
		return onClass;
	}

	public void setOnClass(int onClass) {
		this.onClass = onClass;
	}

	public int getWeeks() {
		return weeks;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
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

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public boolean isSetReminder() {
		return isSetReminder;
	}

	public void setSetReminder(boolean isSetReminder) {
		this.isSetReminder = isSetReminder;
	}
	
	/**
	 * 判断上课周数是否与另一个课程冲突。 注：此方法假定两个课程是在一周中的同一天且同一节上课。
	 * 
	 * @param anotherWeeks
	 *            表示另一个课程的上课周数
	 * @return 当且仅当发生冲突时返回true, 否则返回false。
	 */
	public final boolean isConflictWith(int anotherWeeks) {
		return (weeks & anotherWeeks & 0xffffff) != 0;
	}

	/**
	 * 设置上课的周数。
	 * 
	 * @param start
	 *            开始上课的周数。
	 * @param end
	 *            结束上课的周数。
	 * @param field
	 *            表示在起止周数中是单周，双周，或全部周都要上课。值为AppConstant.Weeks类中以下三个值之一：ALL,
	 *            EVEN, ODD.
	 * @param flagField
	 *            表示在起止周数中是单周，双周，或全部周都要上课。值为AppConstant.Weeks类中以下三个值之一：FLAG_ALL,
	 *            FLAG_EVEN, FLAG_ODD.
	 */
	public final void setWeeks(int start, int end, int field, int flagField) {
		weeks = weekModelParseToInt(start, end, field, flagField);
	}

	/**
	 * 设置上课的周数。
	 * 
	 * @param start
	 *            开始上课的周数。
	 * @param end
	 *            结束上课的周数。
	 * @param field
	 *            表示在起止周数中是单周，双周，或全部周都要上课。值为AppConstant.Weeks类中以下三个值之一：ALL,
	 *            EVEN, ODD.
	 * @param flagField
	 *            表示在起止周数中是单周，双周，或全部周都要上课。值为AppConstant.Weeks类中以下三个值之一：FLAG_ALL,
	 *            FLAG_EVEN, FLAG_ODD.
	 */
	public static final int weekModelParseToInt(int start, int end, int field,
			int flagField) {
		int weeks = 0 | (field & ((0xffffffff << (start - 1)) & (0xffffffff >>> (32 - end))));
		weeks |= flagField;
		return weeks;
	}
	
	/**
	 *  查询某一周的某一天的某节课是否已经被标记 
	 */
	public final boolean isClassSignedAtDay(int week, int day, int id) {
		if (!isNeedClassAtWeek(week, weeks)) {
			
		} else {

		}
		return false;
	}

	/**
	 * 查询某一周是否需要上课。
	 * 
	 * @param week
	 *            表示某一周的参数。
	 * @return 当且仅当需要上课时返回true, 否则返回false.
	 */
	public final boolean isNeedClassAtWeek(int week) {
		return ((weeks >>> (week - 1)) & 0x1) != 0;
	}

	/**
	 * 查询某一周是否需要上课
	 * 
	 * @param week
	 *            表示某一周的参数
	 * @param weeks
	 *            表示某节课上课周数的参数
	 * @return 当且仅当需要上课时返回true, 否则返回false.
	 */
	public static final boolean isNeedClassAtWeek(int week, int weeks) {
		return ((weeks >>> (week - 1)) & 0x1) != 0;
	}

//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		// TODO Auto-generated method stub
//		
//	}
}
