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
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.R;

/**
 * 这是设置上课地点的界面及功能，包括对地点的CRUD。
 * 
 * @author ivanchou
 * 
 */
public class BuildingSetting extends Activity {

	private static final int EDIT = 6;
	private static final int DELETE = 7;
	private static final int ADD_BUILDING = 0;
	@AndroidView(id = R.id.set_building_listView, onCreateContextMenu = "listViewContextMenu", onItemClick = "listViewItemClick")
	private ListView listView;
	private ActionMode mActionMode;
	private int id;

	private IBuildingDAO mBuildingDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.building);
		UIBindUtil.bind(this, R.layout.set_building);
		
		mBuildingDAO = DAOFactory.getBuildingDAOInstance(this);
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
				id = (int) arg3;
				mActionMode = BuildingSetting.this.startActionMode(mActionModeCallback);
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
	protected void onResume() {
		setListAdapter();
		super.onResume();
	}

	/**
	 * 设置数据的来源。
	 */
	private final void setListAdapter() {
		Cursor cursor = mBuildingDAO.getAllBuildings();
		this.startManagingCursor(cursor);
		ListAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.building_list_item, cursor,
				new String[] { "building" }, new int[] { R.id.build_list_item });
		listView.setAdapter(adapter);
		startManagingCursor(cursor);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DBUtil.closeDB((DBCloseable) mBuildingDAO);
	}
	
	
	//using the contextual action mode
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.edit_build_menu, menu);
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
			case R.id.build_edit:
				GetCurriculumInfoUtil.initEditBuild(BuildingSetting.this, id);
				MyDialogFragment showDetailDialog = MyDialogFragment.newInstance("编辑教学楼");
				showDetailDialog.show(getFragmentManager(), "edit_build");
				mode.finish();
				return true;
			case R.id.build_del:
				// 如果教学楼被引用，则无法删除。
				if (mBuildingDAO.hasBuildingBeReferences(id)) {
					Toast.makeText(BuildingSetting.this, R.string.set_building_beRef,
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (mBuildingDAO.deleteBuilding(id)) {
					Toast.makeText(BuildingSetting.this, R.string.delete_success,
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
		MenuItem addBuild = menu.add(1, ADD_BUILDING, ADD_BUILDING, R.string.add_buliding)//.setIcon(android.R.drawable.ic_menu_add);
				.setIcon(R.drawable.add_new);
		addBuild.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	//ActionBar上的菜单，以及点按“添加教学楼”会自动生成一个Dialog
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case ADD_BUILDING:
			MyDialogFragment addCourseDialogFragment = MyDialogFragment.newInstance("添加教学楼");
			addCourseDialogFragment.show(getFragmentManager(), "add_build");
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	//用户添加教学楼的数据库操作
	public void doAddPositiveClick(String text) {
		final String building = text;
		if ("".equals(building)) {
			Toast.makeText(BuildingSetting.this, R.string.name_couldnot_null,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mBuildingDAO.insertBuilding(building)) {
			Toast.makeText(BuildingSetting.this, R.string.add_success,
					Toast.LENGTH_SHORT).show();
			setListAdapter();
		}
	}
	
	public void doEditPositiveClick(String text) {
		final String building = text;
		mBuildingDAO.updateBuilding(id, building);
		setListAdapter();
	}
	
}
