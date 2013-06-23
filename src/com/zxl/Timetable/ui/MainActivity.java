package com.zxl.Timetable.ui;

import com.zxl.Timetable.R;
import com.zxl.Timetable.alert.CheckService;
import com.zxl.Timetable.fragment.DayViewFragment;
import com.zxl.Timetable.fragment.StatViewFragment;
import com.zxl.Timetable.fragment.TotalViewFragment;
import com.zxl.Timetable.util.ItemLoadAsyncTask;
import com.zxl.Timetable.util.MyUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity {

	private ActionBar actionBar;
	private SpinnerAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		
	}

	public class DropDownListener implements OnNavigationListener {

		private DayViewFragment dayViewFragment = null;
		private TotalViewFragment totalViewFragment = null;
		private StatViewFragment statViewFragment = null;
		
		private FragmentManager manager;
		private FragmentTransaction transaction;
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// TODO Auto-generated method stub
			manager = getFragmentManager();
			transaction = manager.beginTransaction();
			if (itemPosition == 0) {
				dayViewFragment = new DayViewFragment();
				transaction.replace(R.id.main_test, dayViewFragment, "dayView");
				
			} else if (itemPosition == 1){
				totalViewFragment = new TotalViewFragment();
				transaction.replace(R.id.main_test, totalViewFragment, "totalView");
			} else if (itemPosition == 2) {
				statViewFragment = new StatViewFragment();
				transaction.replace(R.id.main_test, statViewFragment, "statView");
			}				
			transaction.commit();
			return false;
		}
	}
	
	
	public void loadAllItem() {
		int count = MyUtils.getDayClass(this) * 8;
		new ItemLoadAsyncTask(this, count) {}.execute(count);		
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		adapter = ArrayAdapter.createFromResource(this, R.array.dropdownlist, R.layout.dropdownlist_style);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setListNavigationCallbacks(adapter, new DropDownListener());
		loadAllItem();
		CheckService.startServie(this);
	}



//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		finish();
//	}
	
	
}
