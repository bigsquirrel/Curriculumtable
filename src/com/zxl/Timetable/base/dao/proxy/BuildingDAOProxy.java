
package com.zxl.Timetable.base.dao.proxy;

import android.content.Context;
import android.database.Cursor;

import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.impl.BuildingDAOImpl;
import com.zxl.Timetable.base.dbc.DBConnection;

/**
 * @author ivanchou
 */
public class BuildingDAOProxy implements IBuildingDAO, DBCloseable {
	private IBuildingDAO mBuildingDAO;
	private DBConnection mConnection;

	public BuildingDAOProxy(Context context) {
		mConnection = new DBConnection(context);
		mBuildingDAO = new BuildingDAOImpl(mConnection.getReadableDatabase(),
				mConnection.getWritableDatabase());
	}

	@Override
	public boolean hasBuildingBeReferences(int buildingId) {
		return mBuildingDAO.hasBuildingBeReferences(buildingId);
	}

	@Override
	public void deleteAllBuildings() {
		mBuildingDAO.deleteAllBuildings();
	}

	@Override
	public boolean insertBuilding(String building) {
		return mBuildingDAO.insertBuilding(building);
	}

	@Override
	public Cursor getAllBuildings() {
		return mBuildingDAO.getAllBuildings();
	}

	@Override
	public boolean deleteBuilding(int id) {
		return mBuildingDAO.deleteBuilding(id);
	}

	@Override
	public boolean updateBuilding(int id, String building) {
		return mBuildingDAO.updateBuilding(id, building);
	}

	@Override
	public void closeDB() {
		if (mConnection != null) {
			mConnection.close();
		}
	}

	@Override
	public Cursor getBuilding(int id) {
		return mBuildingDAO.getBuilding(id);
	}

}
