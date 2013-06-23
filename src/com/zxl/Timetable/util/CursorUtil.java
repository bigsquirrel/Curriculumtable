
package com.zxl.Timetable.util;

import android.database.Cursor;

/**
 * @author ivanchou
 */
public class CursorUtil {

	public static String getString(Cursor c, String column) {
		return c.getString(c.getColumnIndex(column));
	}

	public static int getInt(Cursor c, String column) {
		return c.getInt(c.getColumnIndex(column));
	}

	public static short getShort(Cursor c, String column) {
		return c.getShort(c.getColumnIndex(column));
	}

	public static long getLong(Cursor c, String column) {
		return c.getLong(c.getColumnIndex(column));
	}

	public static float getFloat(Cursor c, String column) {
		return c.getFloat(c.getColumnIndex(column));
	}

	public static double getDouble(Cursor c, String column) {
		return c.getDouble(c.getColumnIndex(column));
	}

	public static byte[] getBlob(Cursor c, String column) {
		return c.getBlob(c.getColumnIndex(column));
	}
}
