
package com.zxl.Timetable.base.utils;

import com.zxl.Timetable.base.dao.DBCloseable;

/**
 * @author ivanchou 
 */
public class DBUtil {

	/**
	 * 关闭数据库
	 * 
	 * @param closeable
	 */
	public static void closeDB(DBCloseable closeable) {
		if (closeable != null) {
			closeable.closeDB();
		}
	}
}
