
package com.zxl.Timetable.base.dbc;

import com.zxl.Timetable.common.AppConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author IVANCHOU
 */
public class DBConnection {
	private MySQLiteOpenHelper mHelper;

	public DBConnection(Context context) {
		mHelper = new MySQLiteOpenHelper(context, AppConstant.DB_VERSION);
	}

	public SQLiteDatabase getWritableDatabase() {
		return mHelper.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDatabase() {
		return mHelper.getReadableDatabase();
	}
	
	public void close() {
		mHelper.close();
	}
}
