
package com.zxl.Timetable.base.entity;

/**
 * 封装了清空课程的选择，是否同时删除科目或教学楼。
 * 
 * @author ivanchou
 * 
 */
public class DeleteChoices {

	private boolean[] choices = new boolean[2]; // 删除科目,删除教学楼

	public DeleteChoices() {
		choices[0] = false;
		choices[1] = false;
	}

	/**
	 * 设置是否删除科目或教学楼。
	 * 
	 * @param which
	 * @param isChecked
	 */
	public final void setChoise(int which, boolean isChecked) {
		if (which >= 0 && which <= 2) {
			choices[which] = isChecked;
		}
	}

	/**
	 * 是否删除科目。
	 * 
	 * @return
	 */
	public final boolean deleteCourses() {
		return choices[0];
	}

	/**
	 * 是否刪除教學樓。
	 * 
	 * @return
	 */
	public final boolean deleteBuildings() {
		return choices[1];
	}
}
