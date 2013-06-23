package com.zxl.Timetable.entity;

import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;

import android.content.Context;
import android.database.Cursor;

public class Building {
	private String buildStr;
	public static Building building;
	
	public static Building getBuilding(Context context, int id) {
		if (building == null) {
			building = new Building();
		}
		IBuildingDAO buildingDAO = DAOFactory.getBuildingDAOInstance(context);
		Cursor cursor = buildingDAO.getBuilding(id);
		
		String buildStr = cursor.getString(cursor.getColumnIndex("building"));
		building.setBuildStr(buildStr);
		
		cursor.close();
		return building;
	}
	
	
	
	
	public Building() {
		super();
	}

	public Building(String buildStr) {
		super();
		this.buildStr = buildStr;
	}


	public String getBuildStr() {
		return buildStr;
	}
	public void setBuildStr(String buildStr) {
		this.buildStr = buildStr;
	}
	
	
	
}
