package com.zxl.Timetable.fragment;


import com.zxl.Timetable.R;
import com.zxl.Timetable.base.utils.DateUtil;
import com.zxl.Timetable.base.utils.TimeUtil;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.nav.util.GridViewAdapter;
import com.zxl.Timetable.ui.CurriculumAdd;
import com.zxl.Timetable.ui.MyDialogFragment;
import com.zxl.Timetable.ui.SettingActivity;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.util.MyUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class TotalViewFragment extends Fragment {

	private View tolView;
	private GridView totalGridView;
	private SpinnerAdapter adapter;;
	private ActionBar actionBar;
	
	int screenWidth;
	int screenHeight;
	int columnWidth;
	int columnHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tolView = inflater.inflate(R.layout.total_curriculum_gridview, container, false);
		return tolView;
	}

	@Override
	public void onStart() {
		super.onStart();
		bindView();
		
		totalGridView.setColumnWidth(columnWidth);
		totalGridView.setAdapter(new GridViewAdapter(this.getActivity(), columnHeight));
		totalGridView.setOnItemClickListener(new ItemClickListener(getActivity()));
	}
	
	protected void bindView() {
		totalGridView = (GridView)tolView.findViewById(R.id.total_gridview);
	}

	private void init() {
		screenWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();
		columnWidth = screenWidth / 9;
		columnHeight = screenHeight / 7;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	class ItemClickListener implements OnItemClickListener {

		private Context context;
		public ItemClickListener(Context context) {
			this.context = context;
		}
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int dayOfWeek = arg2 % 8 - 1;
			int onClass = arg2 / 8 ;  //第几节
			if (arg2 % 8 != 0 && TimeUtil.hasSchool(context, dayOfWeek, onClass + 1)) {
				
				int id = Curriculum.getCurriculum(context, dayOfWeek, onClass + 1).get_id();
				GetCurriculumInfoUtil.initGetInfo(context, id);
				String titleStr = GetCurriculumInfoUtil.getTitle();
				MyDialogFragment showDetailDialog = MyDialogFragment.newInstance(
						titleStr, TotalViewFragment.this);
				showDetailDialog.show(getFragmentManager(), "detail_dialog");
			} else if (arg2 % 8 != 0 && !TimeUtil.hasSchool(context, dayOfWeek, onClass)) {
				Intent intent = new Intent(context, CurriculumAdd.class);
				intent.putExtra("dayOfWeek", dayOfWeek);
				intent.putExtra("onClass", onClass);
				startActivity(intent);
			}
			
		}
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem currentWeekItem;

		inflater.inflate(R.menu.oth_menu, menu);
		currentWeekItem = menu.findItem(R.id.action_current_week2);
		LayerDrawable icon = (LayerDrawable) currentWeekItem.getIcon();
		MyUtils.setWeekIcon(icon, this.getActivity());

		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_current_week2: // 定位到today，显示提示信息
			break;

		case R.id.action_setting2: // 课程表设置
			startActivity(new Intent(this.getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
