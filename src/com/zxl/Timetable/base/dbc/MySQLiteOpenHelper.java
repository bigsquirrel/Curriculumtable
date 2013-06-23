package com.zxl.Timetable.base.dbc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "unTimeTable.db"; // 数据库文件的名字。

	MySQLiteOpenHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建教学楼表
		Log.i("createTable", "CREATE TABLE[t_buildings]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [building] TEXT NOT NULL)");
		db.execSQL("CREATE TABLE[t_buildings]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [building] TEXT NOT NULL)");
		Log.i("createTableSuccess", "create table t_buildings successfully.");
		// 创建科目表
		Log.i("createTable", "CREATE TABLE[t_courses]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [course] TEXT NOT NULL, [nick] TEXT NOT NULL [teacher] TEXT NOT NULL)");
		db.execSQL("CREATE TABLE[t_courses]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [course] TEXT NOT NULL, [nick] TEXT NOT NULL, [teacher] TEXT NOT NULL)");
		Log.i("createTableSuccess", "create table t_courses successfully");
		// 创建课程表
		Log.i("createTable", "CREATE TABLE[t_curriculums]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [dayOfWeek] INTEGER, [onClass] INTEGER, [courseId] INTEGER, [weeks] INTEGER, [buildingId] INTEGER, [roomNum] TEXT, [remark] TEXT, [sign] INTEGER, [color] INTEGER, [deleted] TEXT, [remind] INTEGER, [json] TEXT)");
		db.execSQL("CREATE TABLE[t_curriculums]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [dayOfWeek] INTEGER, [onClass] INTEGER, [courseId] INTEGER, [weeks] INTEGER, [buildingId] INTEGER, [roomNum] TEXT, [remark] TEXT, [sign] INTEGER, [color] INTEGER, [deleted] TEXT, [remind] INTEGER, [json] TEXT)");
		
		Log.i("createTableSuccess", "create table t_curriculums successfully");
		
		//创建考试表   其中的courseId是从curriculumId中拿出来的
		Log.i("createTable", "CREATE TABLE[t_exams](_id) INTEGER PRIMARY KEY AUTOINCREMENT, [curriculumId] INTEGER, [data] INTEGER NOT NULL, [buildingId] INTEGER, [scores] INTEGER ");
		db.execSQL("CREATE TABLE[t_exams]([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [courseId] INTEGER, [date] INTEGER NOT NULL, [buildingId] INTEGER, [roomNum] TEXT, [scores] INTEGER, [totalScores] INTEGER, [sign] TEXT, [color] INTEGET, [deleted] TEXT, [remind] INTEGER, [json] TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
