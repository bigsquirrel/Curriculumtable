
package com.zxl.Timetable.base.dao.factory;

import android.content.Context;

import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.base.dao.proxy.BuildingDAOProxy;
import com.zxl.Timetable.base.dao.proxy.CourseDAOProxy;
import com.zxl.Timetable.base.dao.proxy.CurriculumDAOProxy;
import com.zxl.Timetable.base.dao.proxy.ExamDaoProxy;

/**
 * @author ivanchou
 */
public class DAOFactory {

	/**
	 * 返回课程DAO的实例。
	 * 
	 * @param context
	 * @return
	 */
	public static ICurriculumDAO getCurriculumDAOInstance(Context context) {
		return new CurriculumDAOProxy(context);
	}

	/**
	 * 返回教学楼DAO的实例。
	 * 
	 * @param context
	 * @return
	 */
	public static IBuildingDAO getBuildingDAOInstance(Context context) {
		return new BuildingDAOProxy(context);
	}

	/**
	 * 返回科目DAO的实例。
	 * 
	 * @param context
	 * @return
	 */
	public static ICourseDAO getCourseDAOInstance(Context context) {
		return new CourseDAOProxy(context);
	}
	
	/**
	 * 返回考试DAO的实例。
	 * 
	 * @param context
	 * @return
	 */
	
	public static IExamDAO getExamDaoInstance(Context context) {
		return new ExamDaoProxy(context);
		
	}
}
