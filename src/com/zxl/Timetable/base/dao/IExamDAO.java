package com.zxl.Timetable.base.dao;

import com.zxl.Timetable.entity.Exam;

import android.database.Cursor;

/**
 * @author IVANCHOU
 */
public interface IExamDAO {

	/**
	 * 根据课程id查询考试
	 * @param
	 * @return 返回 
	 */
	public Cursor getExam(int id);
	/**
	 * 查询所有需要将要考试的课程，无需参数，因为exams表只有在有考试的时候才会创建，要让courseId唯一
	 * @param
	 * @return 返回 
	 */
	public Cursor getFutureExams();
	
	/**
	 * 查询所有考完试的课程
	 * @return 
	 */
	public Cursor getCompleteExams();
	
	/**
	 * 添加考试
	 * @return 
	 */
	public boolean insertExam(Exam exam);
	
	/**
	 * 修改考试
	 * @return 
	 */
	public boolean updateExam(Exam exam);
	
	public boolean updateScores(int id, int s, int ts);
	/**
	 * 删除考试
	 * @param id是指生成考试的id
	 * @return 
	 */
	public boolean deletedExam(int id);
	
	/**
	 * 删除所有考试（学期结束）
	 * @return 
	 */
	public boolean deletedAllExams();
	
	public boolean isExamsEmpty();
}
