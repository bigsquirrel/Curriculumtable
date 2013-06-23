package com.zxl.Timetable.common;

public class AppConstant {
	public static final String USER_EXP_PRE = "userexp.pref";
	public static final int DB_VERSION = 1; // 这里表示数据库版本。
	/**
	 * 一天的节数，如：第一节，第二节……
	 */
	public static final String[] CLASSES_ONE_DAY = new String[] { "第一节", "第二节",
			"第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节", "第十一节",
			"第十二节", "第十三节" };
	/**
	 * 星期几的汉字表示，如星期日，星期一……
	 */
	public static final String[] week = new String[] { "星期日", "星期一", "星期二",
			"星期三", "星期四", "星期五", "星期六" };
	/**
	 * 周数的表示，如第一周，第二周……
	 */
	public static final String[] WEEKS_ON_TERM = { "第一周", "第二周", "第三周", "第四周",
			"第五周", "第六周", "第七周", "第八周", "第九周", "第十周", "十一周", "十二周", "十三周",
			"十四周", "十五周", "十六周", "十七周", "十八周", "十九周", "二十周", "廿一周", "廿二周",
			"廿三周", "廿四周", "廿五周" };
	/**
	 * 提前的时间
	 */
	public static final String[] AHEAD_TIME = { "不提醒", "5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟", "45分钟", "1小时" };
	
	/**
	 * 水平标签
	 */
	public static final String[] HORIZONLAB = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", 
													"17", "18", "19", "20", "21", "22", "23", "24"};
	/**
	 * 垂直标签
	 */
	public static final String[] VERTICALLAB = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
	/**
	 * 
	 */
	public static final String CLASS_ON_DAY = "classOnDay"; // 第几节

	/**
	 * 与课程表设置相关的一些常量。
	 * 
	 * @author msdx
	 */
	public static class Preferences {
		/**
		 * 课表基本设置的配置文件名称。
		 */
		public static final String PREFERENCES_NAME = "com.zxl.Timetable_preferences";
		/**
		 * KEY, 一共有几周
		 */
		public static final String ALL_WEEK = "allWeek";
		/**
		 * KEY, 一天上几节课。
		 */
		public static final String CLASSES_ONE_DAY = "classesOneDay"; 
		/**
		 * KEY, 当前第几周
		 */
		public static final String CURRENT_WEEK = "currentWeek";
		/**
		 * KEY,上课时间的时单位数 。 上课的时间
		 */
		public static final String CLASS_TIME_HOUR = "classtimehour";
		/**
		 * KEY,上课时间 。 int
		 */
		public static final String CLASS_TIME = "classtime";

		/**
		 * KEY,手机静音模式。 string
		 */
		public static final String MUTEMODE = "mutemode";
		
		/**
		 * KEY,自动静音。 boolean
		 */
		public static final String AUTOMUTE = "automute";
		
		/**
		 * KEY,通知。 boolean
		 */
		public static final String NOTIFICATION = "notification";
		
		/**
		 * KEY,默认view。 
		 */
		public static final String DEFAULTVIEW = "defaultview";
		
		/**
		 * KEY,每节课时间 。 
		 */
		public static final String CLASS_LAST_MINUTE = "classlastmin";
		
		/**
		 * KEY,课间休息时间 。 
		 */
		public static final String CLASS_BREAK_MINUTE = "classbreakmin";
		
		/**
		 * KEY,上课时间的分单位数。
		 */
		public static final String CLASS_TIME_MINUTE = "classtimeminute"; 
		/**
		 * KEY, 下课时间的时单位数。
		 */
		public static final String BREAK_TIME_HOUR = "breaktimehour"; 
		/**
		 * KEY, 下课时间的分单位数。
		 */
		public static final String BREAK_TIME_MINUTE = "breaktimeminute";
		/**
		 * KEY, 开学第一天的年份。
		 */
		public static final String FIRST_DAY_YEAR = "firstdayyear"; 
		/**
		 * KEY, 开学第一天的月份。
		 */
		public static final String FIRST_DAY_MONTH = "firstdaymonth"; 
		/**
		 * KEY, 开学所在星期的第一天的日期。
		 */
		public static final String FIRST_DAY_DAY = "firstdayday";
		/**
		 * KEY, 提前的时间。
		 */
		public static final String IN_ADVANCE = "inAdvance"; 
		/**
		 * KEY, 提醒的时间（时）
		 */
		public static final String REMIND_TIME_HOUR = "remindTimeHour"; 
		/**
		 * KEY, 提醒的时间（分）
		 */
		public static final String REMIND_TIME_MINUTE = "remindTimeMinute";
	}

	/**
	 * 与上课周数相关的一些常量。
	 * 
	 * @author msdx
	 */
	public static class Weeks {
		/**
		 * 全部周标志
		 */
		public static final int FLAG_ALL = 0x80000000;
		/**
		 * 双周标志
		 */
		public static final int FLAG_EVEN = 0x40000000;
		/**
		 * 单周标志
		 */
		public static final int FLAG_ODD = 0x20000000;

		/**
		 * 用于计算上课的周数的掩码。 全部周 1111 1111 1111 1111 // 1111 1111 1111 1111
		 */
		public static final int ALL = 0xffffffff;
		/**
		 * 用于计算上课的周数的掩码。 双周 1010 1010 1010 1010 // 1010 1010 1010 1010
		 */
		public static final int EVEN = 0xaaaaaaaa;
		/**
		 * 用于计算上课的周数的掩码。 单周 0101 0101 0101 0101 0101 // 0101 0101 0101
		 */
		public static final int ODD = 0x55555555;
	}

	public static class UserExpPref{
		public static final String KEY_BG_PATH = "background_image_path";
	}
}
