
package com.zxl.Timetable.base.entity;

import com.zxl.Timetable.common.AppConstant;

/**
 * 该类包含三个成员变量，分别表示上课的开始周数，结束周数，及单、双或全部周的标志。 表示单、双或全部周的变量请参考AppConstant.Week类。
 * 
 *  @author ivanchou
 */
public class WeeksModel {

	private int start;
	private int end;
	private int flag;

	public WeeksModel() {
		super();
	}

	/**
	 * 构造一个表示上课周数的对象。
	 * 
	 * @param start
	 *            上课的开始周数。
	 * @param end
	 *            上课的结束周数。
	 * @param flag
	 *            单周、双周或全部周。
	 */
	public WeeksModel(int start, int end, int flag) {
		super();
		this.start = start;
		this.end = end;
		this.flag = flag;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		if (start == end) {
			return "第" + start + "周";
		}
		String strFlag = "";
		if (flag == AppConstant.Weeks.FLAG_EVEN) {
			strFlag = " 双周";
		} else if (flag == AppConstant.Weeks.FLAG_ODD) {
			strFlag = " 单周";
		}

		return start + "至" + end + "周" + strFlag;
	}

	/**
	 * 判断某一周是否需要上课
	 * 
	 * @param weeks
	 *            需要上课的周数。
	 * @param whickWeek
	 *            哪一周
	 * @return
	 */
	public final static boolean isNeedAttendClass(int weeks, int whickWeek) {
		// 将1 左移 whickWeek - 1 位
		// 以11周为例, whichWeek = 11, whichWeek -1 = 10; 
		// 把1左移10位,    0000 0000 0000 0000 0000 0100 0000 0000
		// 如果1～18周有课,1000 0000 0000 0011 1111 1111 1111 1111
		
		return (weeks & (1 << (whickWeek - 1))) != 0;
	}

	/**
	 * 获取上课周数的起止周，及表示单双周的标志位。
	 * 
	 * @return 返回WeeksModel对象，包含起止周及单双周标志。
	 */
	public final static WeeksModel getWeeksModel(int weeks) {
		WeeksModel weeksModel = new WeeksModel();
		// 获取表示单双周的标志位。
		weeksModel.setFlag(weeks & 0xe0000000);

		int field = weeks & 0xfffffff;
		int flg = 1; // 此标志用于计算起止周数。
		// 获取开始周数。
		while ((field & 0x1) == 0) {
			flg++;
			field >>>= 1;
		}
		weeksModel.setStart(flg);
		// 获取结束周数。
		while ((field >>>= 1) != 0) {
			flg++;
		}
		weeksModel.setEnd(flg);
		return weeksModel;
	}
	
	
}
