
package com.zxl.Timetable.base.dao;

import android.database.Cursor;

/**
 * @author ivanchou
 */
public interface IBuildingDAO {
	/**
	 * 某一教学楼是否被引用。
	 * 
	 * @param buildingId
	 *            教学楼的ID.
	 */
	public boolean hasBuildingBeReferences(int buildingId);

	/**
	 * 清空所有教学楼。
	 */
	public void deleteAllBuildings();

	/**
	 * 添加教学楼。
	 * 
	 * @param building
	 *            教学楼名字。
	 * @return 当且仅当插入成功时返回true, 否则返回false。
	 */
	public boolean insertBuilding(String building);

	/**
	 * 获取所有的教学楼。
	 * 
	 * @return Cursor对象，包含所有的教学楼科目。注意它不是同步的。
	 */
	public Cursor getAllBuildings();
	
	
	/*
	 * 获取某个教学楼
	 */
	public Cursor getBuilding(int id) ;
	

	/**
	 * 删除某个教学楼。
	 * 
	 * @param id
	 *            在数据库中教学楼的ID。
	 * @return 当且仅当删除成功时返回true, 否则返回false。
	 */
	public boolean deleteBuilding(int id);

	/**
	 * 修改教学楼的名字。
	 * 
	 * @param id
	 *            要修改的教学楼的ID。
	 * @param building
	 *            教学楼修改之后的名字。
	 * @return 当且仅当修改成功时返回true, 否则返回false.
	 */
	public boolean updateBuilding(int id, String building);
}
