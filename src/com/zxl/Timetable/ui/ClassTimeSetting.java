package com.zxl.Timetable.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.util.ClassTimeSetUtil;
import com.zxl.Timetable.util.MyUtils;
import com.zxl.Timetable.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ClassTimeSetting extends Activity {

//	private SharedPreferences preferences;
	private int dayClass;
	private ListView listView;
	String[] tmp;
	private int id;
	private static boolean flag = false;
//	private int hour;
//	private int minute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_class_time);
		setTitle(R.string.time_set);
		dayClass = MyUtils.getDayClass(this);
		listView = (ListView) findViewById(R.id.set_class_time_listview);
//		preferences = this.getSharedPreferences(AppConstant.Preferences.PREFERENCES_NAME, Activity.MODE_PRIVATE);
		
		ClassTimeSetUtil.init(this, 1);
//		ClassTimeSetUtil.autoSetAllClassTime(dayClass);
		
		setValue();
		setListItemClick();
	}
	
	private void setListItemClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				id = (int)arg3 + 1;
				MyDialogFragment dialogFragment = MyDialogFragment.newInstance("设置：上课时间");
				dialogFragment.show(getFragmentManager(), "set_class_time");
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
	
	/*
	 * 设置一个临时的数组存放“第一节”……
	 */
	private void setClassCountTmp() {
		tmp = new String[dayClass + 1];
		for (int i = 1; i <= dayClass; i++) {
			tmp[i] = AppConstant.CLASSES_ONE_DAY[i-1];
		}
	}
	/*
	 * 设置每一个list的值
	 */
	private void setValue() {
		setClassCountTmp(); //可以拿到一个包含“第一节，第二节……”的数组
		List<Map<String, String>> timeList = new ArrayList<Map<String,String>>();
		for (int i = 1; i <= dayClass; i++) {
			ClassTimeSetUtil.setId(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", tmp[i]);
			map.put("classtime", "上课时间:" + ClassTimeSetUtil.getClassTimeString());
			map.put("breaktime", "下课时间:" + ClassTimeSetUtil.getBreakTimeString());
			timeList.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, timeList, R.layout.class_time_list_item, 
				new String[] {"title", "classtime", "breaktime"}, new int[] {R.id.class_item_name, R.id.class_item_classtime, R.id.class_item_breaktime});
		
		listView.setAdapter(adapter);
	}
	
	protected void doSetClassTimeClick(int h, int m) {
		ClassTimeSetUtil.setClassTime(h, m, id, dayClass);
		setValue();
		flag = true;
	}

}
