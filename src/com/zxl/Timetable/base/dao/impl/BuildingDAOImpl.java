
package com.zxl.Timetable.base.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxl.Timetable.base.dao.IBuildingDAO;

/**
 * @author IVANCHOU
 */
public class BuildingDAOImpl implements IBuildingDAO {
	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;

	public BuildingDAOImpl(SQLiteDatabase dbRead, SQLiteDatabase dbWrite) {
		this.dbRead = dbRead;
		this.dbWrite = dbWrite;
	}

	@Override
	public boolean hasBuildingBeReferences(int buildingId) {
		Cursor c = dbRead.rawQuery(
				"select count(*) as sum from t_curriculums where buildingId="
						+ buildingId, null);
		c.moveToFirst();
		final int sum = c.getInt(c.getColumnIndex("sum"));
		c.close();
		return sum > 0;
	}

	@Override
	public void deleteAllBuildings() {
		dbWrite.delete("t_buildings", "1", null);
	}

	@Override
	public boolean insertBuilding(String building) {
		ContentValues v = new ContentValues();
		v.put("building", building);
		return dbWrite.insert("t_buildings", null, v) != -1;
	}

	@Override
	public Cursor getAllBuildings() {
		return dbRead.rawQuery("select * from t_buildings", null);
	}

	@Override
	// TODO 方法名重构
	public boolean deleteBuilding(int id) {
		return dbWrite.delete("t_buildings", "_id=" + id, null) >= 1;
	}

	@Override
	public boolean updateBuilding(int id, String building) {
		ContentValues v = new ContentValues();
		v.put("building", building);
		return dbWrite.update("t_buildings", v, "_id=" + id, null) >= 1;
	}

	@Override
	public Cursor getBuilding(int id) {
		Cursor cursor = dbRead.rawQuery("select building from t_buildings where _id=" + id, null);
		cursor.moveToFirst();
		return cursor;
	}

}
