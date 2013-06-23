package com.zxl.Timetable.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.lurencun.cfuture09.androidkit.ui.UIBindUtil;
import com.lurencun.cfuture09.androidkit.ui.annotation.AndroidView;
import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.entity.Course;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.R;

/**
 * 设置科目的界面及功能，包括对科目的CRUD.
 * 
 * @author ivanchou
 * 
 */
public class CourseSetting extends Activity {
	private static final int ADD_COURSE = 0;
	@AndroidView(id = R.id.set_course_listView, onCreateContextMenu = "onListViewCreateContextMenu", onItemClick = "onListViewItemClick")
	private ListView listView;

	private ICourseDAO mCourseDAO;
	private Cursor cursor;
	private ActionMode mActionMode;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.course);
		UIBindUtil.bind(this, R.layout.set_course);
		mCourseDAO = DAOFactory.getCourseDAOInstance(this);
		setOnLongClickListener(listView);
	}

	private void setOnLongClickListener(ListView lv) {

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (mActionMode != null) {
					return false;
				}
				id = (int)arg3;
				mActionMode = CourseSetting.this.startActionMode(mActionModeCallback);
				arg1.setSelected(true);
				return true;
			}
		});
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
			case android.R.id.home:
				Intent intent = new Intent(this, SettingActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DBUtil.closeDB((DBCloseable) mCourseDAO);
	}

	/**
	 * 设置数据的来源。
	 */
	private final void setListAdapter() {
		cursor = mCourseDAO.getAllCourses();
		this.startManagingCursor(cursor);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.my_simple_list_item, cursor, new String[] {
				"course", "teacher" }, new int[] { android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		setListAdapter();
		super.onResume();
	}


	//using the contextual action mode
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.edit_course_menu, menu);
			return true;
		}
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
		
		
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			
			switch (item.getItemId()) {
			case R.id.course_edit:
				GetCurriculumInfoUtil.initEditCourse(CourseSetting.this, id);
				MyDialogFragment addCourseDialogFragment = MyDialogFragment.newInstance("编辑科目");
				addCourseDialogFragment.show(getFragmentManager(), "edit_course");
				mode.finish();
				return true;
			case R.id.course_del:
				// 如果科目引用，则无法删除。
				if (mCourseDAO.hasCourseBeReferences(id)) {
					Toast.makeText(CourseSetting.this, R.string.set_course_beRef,
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (mCourseDAO.deleteCourse(id)) {
					Toast.makeText(CourseSetting.this, R.string.delete_success,
							Toast.LENGTH_SHORT).show();
					setListAdapter();
				}
				mode.finish();
				return true;
			default:
				return false;
			}
			return false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem addCourse = menu.add(1, ADD_COURSE, ADD_COURSE, R.string.add_course)
				.setIcon(R.drawable.add_new);
		addCourse.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case ADD_COURSE:
			MyDialogFragment addCourseDialogFragment = MyDialogFragment.newInstance("添加科目");
			addCourseDialogFragment.show(getFragmentManager(), "add_course");
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void doAddPositiveClick(String text1, String text2, String text3) {
		if ("".equals(text1) || "".equals(text2) || "".equals(text3) ) {
			Toast.makeText(CourseSetting.this, R.string.course_couldnot_null,
					Toast.LENGTH_SHORT).show();
			return;
		}
		final Course course = new Course(text1, text2, text3);
		if (mCourseDAO.insertCourse(course)) {
			Toast.makeText(CourseSetting.this, R.string.add_success,
					Toast.LENGTH_SHORT).show();
			setListAdapter();
		}
	}

	public void doEditPositiveClick(String text1, String text2, String text3) {
		final Course course = new Course(text1, text2, text3);
		mCourseDAO.updateCourse(id, course);
		setListAdapter();
	}
}
