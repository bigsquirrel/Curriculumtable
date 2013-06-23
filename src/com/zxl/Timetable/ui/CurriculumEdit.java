package com.zxl.Timetable.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.lurencun.cfuture09.androidkit.ui.UIBindUtil;
import com.lurencun.cfuture09.androidkit.ui.annotation.AndroidView;
import com.lurencun.cfuture09.androidkit.ui.annotation.OnItemSelect;
import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.R;

/**
 * 这是编辑课程的界面及功能实现。
 * 
 * @author ivanchou
 * 
 */
public class CurriculumEdit extends Activity {
	private static final int EDIT_CURRICULUM_CANCEL = 0;
	private static final int EDIT_CURRICULUM_OK = 1;
	private static final int AHEAD_TEN_MIN = 2;
	private static final int DEFAULT_SIGN_STATE = 0;
	private static final String DEFAULT_DELETE_STATE = "false";
	

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
	@AndroidView(id = R.id.add_curriculum_reminder_spinner2)
	private Spinner remindSpinner;
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

	private IBuildingDAO mBuildingDAO;
	private ICourseDAO mCourseDAO;
	private ICurriculumDAO mCurriculumDAO;

	private SharedPreferences settingsPreferences;
	
	private Toast toast;
	private Cursor buildingsCursor;
	private Cursor coursesCursor;
	private Cursor cursor; // 从数据库中查询返回的某个课程的信息。
	
	private int id = -1;
	private int sign;
	

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.edit_curriculum);
		
		UIBindUtil.bind(this, R.layout.add_curriculum2);
		// 先获取课程的ID。
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("_id");
		if (id == -1) {
			Toast.makeText(this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
			finish();
		}

		mBuildingDAO = DAOFactory.getBuildingDAOInstance(this);
		mCourseDAO = DAOFactory.getCourseDAOInstance(this);
		mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(this);

		settingsPreferences = getSharedPreferences(
				AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

		toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		initSpinner();
		if (weekGroup.getCheckedRadioButtonId() == -1) {
			allRadioButton.setChecked(true);
		}
		initEditText();
	}


	/**
	 * 初始化所有的spinner。
	 */
	private final void initSpinner() {
		cursor = mCurriculumDAO.getCurriculum(id);

		// 设置表示星期几的spinner
		setSpinnerArrayAdapter(daySpinner, AppConstant.week);
		daySpinner.setSelection(cursor.getInt(cursor.getColumnIndex("dayOfWeek")), true);
		

		// 设置表示第几节的spinner
		final int classCount = settingsPreferences.getInt(
				AppConstant.Preferences.CLASSES_ONE_DAY, 12);
		final String[] tmp = new String[classCount];
		for (int i = 0; i < classCount; i++) {
			tmp[i] = AppConstant.CLASSES_ONE_DAY[i];
		}
		setSpinnerArrayAdapter(classSpinner, tmp);
		classSpinner.setSelection(
				cursor.getInt(cursor.getColumnIndex("onClass")) - 1, true);

		// 设置提醒时间的spinner 
		ArrayAdapter<CharSequence> aheadTimeAdapter = ArrayAdapter
				.createFromResource(this, R.array.class_remind_time, android.R.layout.simple_spinner_item);
		aheadTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		remindSpinner.setAdapter(aheadTimeAdapter);
		remindSpinner.setSelection(getRemindSelect(cursor.getInt(cursor.getColumnIndex("remind"))));
		
		// 设置科目的spinner
		coursesCursor = mCourseDAO.getAllCourses();
		setSpinnerCursorAdapter(coursesSpinner, "course", coursesCursor);
		// 设置科目当前位置。
		coursesCursor.moveToFirst();
		do {
			if (coursesCursor.getInt(coursesCursor.getColumnIndex("_id")) == cursor
					.getInt(cursor.getColumnIndex("courseId"))) {
				break;
			}
		} while (coursesCursor.moveToNext());
		coursesSpinner.setSelection(coursesCursor.getPosition(), true);

		// 设置教学楼的spinner。
		buildingsCursor = mBuildingDAO.getAllBuildings();
		setSpinnerCursorAdapter(buildingsSpinner, "building", buildingsCursor);
		// 设置科目默认位置。
		buildingsCursor.moveToFirst();
		do {
			if (buildingsCursor.getInt(buildingsCursor.getColumnIndex("_id")) == cursor
					.getInt(cursor.getColumnIndex("buildingId"))) {
				break;
			}
		} while (buildingsCursor.moveToNext());
		buildingsSpinner.setSelection(buildingsCursor.getPosition(), true);

		// 开始周数
		final int endWeek = settingsPreferences.getInt(
				AppConstant.Preferences.ALL_WEEK, 22);
		final String[] startWeeks = new String[endWeek];
		for (int i = 0; i < startWeeks.length; i++) {
			startWeeks[i] = AppConstant.WEEKS_ON_TERM[i];
		}
		setSpinnerArrayAdapter(weekStartSpinner, startWeeks);
		
		
		// 结束周数
		setWeekEndSpinner(1, endWeek);
		// 设置单、双周。
		WeeksModel model = WeeksModel.getWeeksModel(cursor.getInt(cursor
				.getColumnIndex("weeks")));
		weekStartSpinner.setSelection(model.getStart() - 1, true);
		weekEndSpinner.setSelection(model.getEnd() - model.getStart(), true);
		
		sign = cursor.getInt(cursor.getColumnIndex("sign"));
		switch (model.getFlag()) {
		case AppConstant.Weeks.FLAG_ALL:
			allRadioButton.setChecked(true);
			break;
		case AppConstant.Weeks.FLAG_EVEN:
			evenRadioButton.setChecked(true);
			break;
		case AppConstant.Weeks.FLAG_ODD:
			oddRadioButton.setChecked(true);
			break;
		default:
			System.out
					.println("EditCurriculumActivity --#200--weekModel flag error--"
							+ model.getFlag());
		}
		this.startManagingCursor(cursor);
		this.startManagingCursor(coursesCursor);
		this.startManagingCursor(buildingsCursor);
	}

	/**
	 * 开始周数被选择后的回调方法。
	 */
	public void onWeekStartItemSelected(AdapterView<?> view, View v,
			int position, long id) {
		final int endWeek = settingsPreferences.getInt(
				AppConstant.Preferences.ALL_WEEK, 22);
		setWeekEndSpinner(position + 1, endWeek);
	}

	/**
	 * 为Spinner设置ArrayAdapter。
	 * 
	 * @param spinner
	 * @param strings
	 *            数据来源。
	 */
	private final void setSpinnerArrayAdapter(Spinner spinner, String[] strings) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				CurriculumEdit.this, android.R.layout.simple_spinner_item,
				strings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/**
	 * 为spinner设置SimpleCursorAdapter。
	 * 
	 * @param spinner
	 * @param cursor
	 */
	private final void setSpinnerCursorAdapter(Spinner spinner, String from,
			Cursor cursor) {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor,
				new String[] { from }, new int[] { android.R.id.text1 });
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/**
	 * 初始化EditText。
	 */
	private final void initEditText() {
		roomNumEdit.setText(cursor.getString(cursor.getColumnIndex("roomNum")));
		remarkEdit.setText(cursor.getString(cursor.getColumnIndex("remark")));
	}

	/**
	 * 设置结束周数。
	 * 
	 * @param start
	 *            上限。
	 * @param end
	 *            下限。
	 */
	private final void setWeekEndSpinner(int start, int end) {
		final String[] endWeeks = new String[end - start + 1];
		for (int i = 0; i < endWeeks.length; i++) {
			endWeeks[i] = AppConstant.WEEKS_ON_TERM[start + i - 1];
		}
		setSpinnerArrayAdapter(weekEndSpinner, endWeeks);
//		weekEndSpinner.setSelection(0, true);
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
				curriculum.getOnClass(), id);
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


	/**
	 * 保存课程信息。
	 */
	private final void saveCurriculum(Curriculum curriculum) {
		coursesCursor.moveToPosition(coursesSpinner.getSelectedItemPosition());
		curriculum.set_id(id);
		curriculum.setCourseId(coursesCursor.getInt(coursesCursor
				.getColumnIndex("_id")));
		buildingsCursor.moveToPosition(buildingsSpinner
				.getSelectedItemPosition());
		curriculum.setBuildingId(buildingsCursor.getInt(buildingsCursor
				.getColumnIndex("_id")));
		
		curriculum.setRemind(getRemindTime());
		curriculum.setRoomNum(roomNumEdit.getText().toString());
		curriculum.setRemark(remarkEdit.getText().toString());
		curriculum.setSign(DEFAULT_SIGN_STATE);
		curriculum.setDeleted(DEFAULT_DELETE_STATE);
		//颜色后来再设置
		curriculum.setColor(0);
		
		if (mCurriculumDAO.updateCurriculum(curriculum)) {
			toast.setText(R.string.edit_success);
			toast.show();
		} else {
			toast.setText(R.string.edit_failure);
			toast.show();
		}
	}
	
	private int getRemindSelect(int i) {
		switch (i) {
		case 0:
			return 0;
		case 5:
			return 1;
		case 10:
			return 2;
		case 15:
			return 3;
		case 20:
			return 4;
		case 25:
			return 5;
		case 30:
			return 6;
		case 45:
			return 7;
		default:
			break;
		}
		return 0;
	}
	
	private int getRemindTime() {
		int i = remindSpinner.getSelectedItemPosition();
		switch (i) {
		case 0:
			return 0;
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			return 15;
		case 4:
			return 20;
		case 5:
			return 25;
		case 6:
			return 30;
		case 7:
			return 45;

		default:
			break;
		}
		return 0;
	}

	@Override
	protected void onDestroy() {
		cursor.close();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case EDIT_CURRICULUM_OK:
				// 保存并退出
				// 科目或上课地点是否未填。
				Curriculum c = new Curriculum();
				c.setWeeks(getCurriculumWeeks());
				// 上课周数实际上是否为空。
				if (isWeeksNull(c)) {
					toast.setText(R.string.add_curriculum_weeksIsNull);
					toast.show();
					return false;
				}
				// 当上课周数不为空时，判断是否与其它课程的上课时间冲突。
				if (!checkWeeksConflict(c)) {
					saveCurriculum(c);
					CurriculumEdit.this.finish();
				} else {
					toast.setText(R.string.curriculums_weeks_conflict);
					toast.show();
				}
				return true;
				
			case EDIT_CURRICULUM_CANCEL:
				finish();
				return true;
			
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem cancelMenuItem = menu.add(0, EDIT_CURRICULUM_CANCEL, EDIT_CURRICULUM_CANCEL, R.string.edit_curriculum_cancel).setIcon(android.R.drawable.ic_menu_delete);
		MenuItem okMenuItem = menu.add(0, EDIT_CURRICULUM_OK, EDIT_CURRICULUM_OK ,R.string.edit_curriculum_ok).setIcon(android.R.drawable.ic_menu_save);
		
		cancelMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		okMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
}
