package com.zxl.Timetable.ui;

import com.lurencun.cfuture09.androidkit.ui.annotation.AndroidView;
import com.lurencun.cfuture09.androidkit.ui.annotation.OnItemSelect;
import com.zxl.Timetable.base.dao.*;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CurriculumBase extends Activity {

	private IBuildingDAO mBuildingDAO;
	private ICourseDAO mCourseDAO;
	private ICurriculumDAO mCurriculumDAO;
	
	private SharedPreferences settingsPreferences;
	
	@AndroidView(id = R.id.add_curriculum_day_spinner2)
	private Spinner daySpinner;
	@AndroidView(id = R.id.add_curriculum_classStart_spinner2)
	private Spinner classSpinner;
	@AndroidView(id = R.id.add_curriculum_course_spinner2)
	private Spinner coursesSpinner;
	@AndroidView(id = R.id.add_curriculum_building_spinner2)
	private Spinner buildingsSpinner;
	@AndroidView(id = R.id.add_curriculum_weekStart_spinner2, onItemSelect = @OnItemSelect(onItemSelected = "onWeekStartItemSelected"))
	private Spinner weekStartSpinner;
	@AndroidView(id = R.id.add_curriculum_weekEnd_spinner2)
	private Spinner weekEndSpinner;
	@AndroidView(id = R.id.add_curriculum_roomnum_editText2)
	private EditText roomNumEdit;
	@AndroidView(id = R.id.add_curriculum_week_group2)
	private RadioGroup weekGroup;
	@AndroidView(id = R.id.add_curriculum_odd_radio2)
	private RadioButton oddRadioButton;
	@AndroidView(id = R.id.add_curriculum_even_radio2)
	private RadioButton evenRadioButton;
	@AndroidView(id = R.id.add_curriculum_all_radio2)
	private RadioButton allRadioButton;
	@AndroidView(id = R.id.add_curriculum_remark_edit2)
	private EditText remarkEdit;
	
	private Toast mToast;
	private Cursor buildingsCursor;
	private Cursor coursesCursor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 初始化所有的spinner。
	 */
	private final void initSpinner() {
		// 设置表示星期几的spinner
		ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_weeks,android.R.layout.simple_spinner_item);
		dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpinner.setAdapter(dayAdapter);
		
		Bundle bundle = getIntent().getExtras();

		System.out.println(bundle.getInt("dayOfWeek"));
		daySpinner.setSelection(bundle.getInt("dayOfWeek") % 7, true);

		// 设置表示第几节的spinner
		final int classCount = settingsPreferences.getInt(AppConstant.Preferences.CLASSES_ONE_DAY, 12);
		final String[] tmp = new String[classCount];
		for (int i = 0; i < classCount; i++) {
			tmp[i] = AppConstant.CLASSES_ONE_DAY[i];
		}
		ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tmp);
		classAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classSpinner.setAdapter(classAdapter);

		coursesCursor = mCourseDAO.getAllCourses();
		SimpleCursorAdapter coursesCursorAdapter = new SimpleCursorAdapter(
				this, android.R.layout.simple_spinner_item, coursesCursor,
				new String[] { "course" }, new int[] { android.R.id.text1 });
		coursesCursorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		coursesSpinner.setAdapter(coursesCursorAdapter);

		buildingsCursor = mBuildingDAO.getAllBuildings();
		SimpleCursorAdapter buildingsCursorAdapter = new SimpleCursorAdapter(
				this, android.R.layout.simple_spinner_item, buildingsCursor,
				new String[] { "building" }, new int[] { android.R.id.text1 });
		buildingsCursorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingsSpinner.setAdapter(buildingsCursorAdapter);

		// 开始周数
		final int endWeek = settingsPreferences.getInt(
				AppConstant.Preferences.ALL_WEEK, 22);
		final String[] startWeeks = new String[endWeek];
		for (int i = 0; i < startWeeks.length; i++) {
			startWeeks[i] = AppConstant.WEEKS_ON_TERM[i];
		}
		ArrayAdapter<String> startWeeksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, startWeeks);
		startWeeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weekStartSpinner.setAdapter(startWeeksAdapter);
		// 结束周数
		setWeekEndSpinner(1, endWeek);

		this.startManagingCursor(coursesCursor);
		this.startManagingCursor(buildingsCursor);
	}
	
	
	/**
	 * 设置结束周数。
	 * 
	 * @param start
	 *            上限。
	 * @param end
	 *            下限。
	 * @param context 
	 */
	private final void setWeekEndSpinner(int start, int end) {
		final String[] endWeeks = new String[end - start + 1];
		for (int i = 0; i < endWeeks.length; i++) {
			endWeeks[i] = AppConstant.WEEKS_ON_TERM[start + i - 1];
		}
		ArrayAdapter<String> endWeeksAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				endWeeks);
		endWeeksAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weekEndSpinner.setAdapter(endWeeksAdapter);
		weekEndSpinner.setSelection(0);
	}
	
	/**
	 * 开始周数被选择后的回调方法。
	 */
	public void onWeekStartItemSelected(AdapterView<?> view, View v, int position, long id) {
		final int endWeek = settingsPreferences.getInt(AppConstant.Preferences.ALL_WEEK, 22);
		setWeekEndSpinner(position + 1, endWeek);
	}
	
	/**
	 * 检查上课时间是否冲突。
	 * 
	 * @return 当且仅当冲突时返回true，否则返回false。
	 */
	private final boolean checkWeeksConflict(Curriculum curriculum) {
		curriculum.setDay(daySpinner.getSelectedItemPosition());
		curriculum.setOnClass(classSpinner.getSelectedItemPosition() + 1);
		Cursor c = mCurriculumDAO.getCurriculumsWeeks(curriculum.getDay(),
				curriculum.getOnClass());
		while (c.moveToNext()) {
			if (curriculum.isConflictWith(c.getInt(c.getColumnIndex("weeks")))) {
				c.close();
				return true;
			}
		}
		c.close();
		return false;
	}
	
	/**
	 * 清除输入文本。
	 */
	private final void clearEditText() {
		remarkEdit.setText("");
		roomNumEdit.setText("");
	}
	
	/**
	 * 上课周数实际上是否为空。
	 * 
	 * @param curriculum
	 *            课程信息
	 * @return 当且仅当为空时返回true, 否则返回false。
	 */
	private final boolean isWeeksNull(Curriculum curriculum) {
		if ((curriculum.getWeeks() & 0xffffff) == 0) {
			return true;
		}
		return false;
	}
	

	/**
	 * 得到表示上课周数的int型数据。
	 * 
	 * @return
	 */
	private final int getCurriculumWeeks() {
		int weeks = 0;
		// 设置上课周数
		final int start = weekStartSpinner.getSelectedItemPosition() + 1;
		final int end = weekEndSpinner.getSelectedItemPosition() + start;
		final int radioId = weekGroup.getCheckedRadioButtonId();
		if (radioId == oddRadioButton.getId()) {
			weeks = Curriculum.weekModelParseToInt(start, end,
					AppConstant.Weeks.ODD, AppConstant.Weeks.FLAG_ODD);
		} else if (radioId == evenRadioButton.getId()) {
			weeks = Curriculum.weekModelParseToInt(start, end,
					AppConstant.Weeks.EVEN, AppConstant.Weeks.FLAG_EVEN);
		} else {
			weeks = Curriculum.weekModelParseToInt(start, end,
					AppConstant.Weeks.ALL, AppConstant.Weeks.FLAG_ALL);
		}
		return weeks;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DBUtil.closeDB((DBCloseable) mBuildingDAO);
		DBUtil.closeDB((DBCloseable) mCourseDAO);
		DBUtil.closeDB((DBCloseable) mCurriculumDAO);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
}
