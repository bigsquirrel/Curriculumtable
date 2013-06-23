package com.zxl.Timetable.ui;

import com.lurencun.cfuture09.androidkit.db.KV;
import com.zxl.Timetable.base.entity.SimpleDate;
import com.zxl.Timetable.base.utils.DateUtil;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.util.MyUtils;
import com.zxl.Timetable.R;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.MenuItem;

public class OthSettingActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener{

	String allWeek;
	String currentWeek;
	String allClass;
	String classLastMin;
	String breakMin;
	
	Preference allWeekPre;
	Preference curWeekPre;
	Preference allClasPre;
	Preference classLastPre;
	Preference breakMinPre;
	
	String class1Str;
	String class2Str;
	String class3Str;
	String class4Str;
	String class5Str;
	String class6Str;
	String class7Str;
	String class8Str;
	String class9Str;
	String class10Str;
	String class11Str;
	String class12Str;
	String class13Str;
	
	Preference class1Pre;
	Preference class2Pre;
	Preference class3Pre;
	Preference class4Pre;
	Preference class5Pre;
	Preference class6Pre;
	Preference class7Pre;
	Preference class8Pre;
	Preference class9Pre;
	Preference class10Pre;
	Preference class11Pre;
	Preference class12Pre;
	Preference class13Pre;
	
	MyDialogFragment dialogFragment;
	private KV mKV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.oth_preference);
		init();
		setListener();
		getSettings();
	}
	
	private void init() {
		mKV = new KV(this, AppConstant.Preferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		allWeek = getResources().getString(R.string.weeks);
		currentWeek = getResources().getString(R.string.weekOfTerm);
		allClass = getResources().getString(R.string.classOneday);
		classLastMin = getResources().getString(R.string.classLastMin);
		breakMin = getResources().getString(R.string.breakMin);
		
		allWeekPre = findPreference(allWeek);
		curWeekPre = findPreference(currentWeek);
		allClasPre = findPreference(allClass);
		classLastPre = findPreference(classLastMin);
		breakMinPre = findPreference(breakMin);
		
		class1Str = getResources().getString(R.string.class1);
		class2Str = getResources().getString(R.string.class2);
		class3Str = getResources().getString(R.string.class3);
		class4Str = getResources().getString(R.string.class4);
		class5Str = getResources().getString(R.string.class5);
		class6Str = getResources().getString(R.string.class6);
		class7Str = getResources().getString(R.string.class7);
		class8Str = getResources().getString(R.string.class8);
		class9Str = getResources().getString(R.string.class9);
		class10Str = getResources().getString(R.string.class10);
		class11Str = getResources().getString(R.string.class11);
		class12Str = getResources().getString(R.string.class12);
		class13Str = getResources().getString(R.string.class13);
		
		
		class1Pre = findPreference(class1Str);
		class2Pre = findPreference(class2Str);
		class3Pre = findPreference(class3Str);
		class4Pre = findPreference(class4Str);
		class5Pre = findPreference(class5Str);
		class6Pre = findPreference(class6Str);
		class7Pre = findPreference(class7Str);
		class8Pre = findPreference(class8Str);
		class9Pre = findPreference(class9Str);
		class10Pre = findPreference(class10Str);
		class11Pre = findPreference(class11Str);
		class12Pre = findPreference(class12Str);
		class13Pre = findPreference(class13Str);
		
	}
	
	private void setListener() {
		allWeekPre.setOnPreferenceChangeListener(this);
		allWeekPre.setOnPreferenceClickListener(this);
		
		curWeekPre.setOnPreferenceChangeListener(this);
		curWeekPre.setOnPreferenceClickListener(this);
		
		allClasPre.setOnPreferenceChangeListener(this);
		allClasPre.setOnPreferenceClickListener(this);
		
		classLastPre.setOnPreferenceChangeListener(this);
		classLastPre.setOnPreferenceClickListener(this);
		
		breakMinPre.setOnPreferenceChangeListener(this);
		breakMinPre.setOnPreferenceClickListener(this);
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
	public boolean onPreferenceClick(Preference preference) {
		//判断哪个preference点击了
		if(preference.getKey().equals(allWeek)) {  
			dialogFragment = MyDialogFragment.newInstance("设置学期周数");
        	dialogFragment.show(getFragmentManager(), "set_weeks");
        } else if(preference.getKey().equals(currentWeek)) {  
            dialogFragment = MyDialogFragment.newInstance("当前第几周");
            dialogFragment.show(getFragmentManager(), "set_current_week");
        } else if (preference.getKey().equals(allClass)) {
            dialogFragment = MyDialogFragment.newInstance("每天几节课");
            dialogFragment.show(getFragmentManager(), "set_day_class");
		} else if (preference.getKey().equals(classLastMin)) {  
            dialogFragment = MyDialogFragment.newInstance("每节课多少分钟");
            dialogFragment.show(getFragmentManager(), "set_class_min");
        } else if (preference.getKey().equals(breakMin)) {
        	dialogFragment = MyDialogFragment.newInstance("课件休息多少分钟");
            dialogFragment.show(getFragmentManager(), "set_break_min");
		} else {
			return false;
		}
		
        return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		//判断是哪个Preference改变了  
        if(preference.getKey().equals(allWeek)) {
        	
        } else if(preference.getKey().equals(currentWeek)) {
        	
        } else if(preference.getKey().equals(allClass)) {
        	
        } else {  
            //如果返回false表示不允许被改变  
            return false;  
        }  
        //返回true表示允许改变  
        return true;  	
	}
	
	
	private void getSettings() {
		final int allWeeks = MyUtils.getAllWeeks(this);
		allWeekPre.setSummary("本学期一共有" + allWeeks + "周");
		final int curWeek = MyUtils.getCurrentWeek(this);
		curWeekPre.setSummary("当前是第" + curWeek + "周");
		final int cls = MyUtils.getDayClass(this);
		allClasPre.setSummary("一天有" + cls + "节课");
		final int clsMin = MyUtils.getClassLastMinute(this);
		classLastPre.setSummary("一节课：" + clsMin + "分钟");
		final int brkMin = MyUtils.getBreakMinute(this);
		breakMinPre.setSummary("课件休息：" + brkMin + "分钟");
	}
	
	/*
	 * 将 cls 之后的设置为不可点击
	 */
	private void setPreEnable(int no) {
		
	}
	/*
	 * 保存当前学期一共有多少周
	 */
	public void saveAllWeek(int weeks) {
		mKV.put(AppConstant.Preferences.ALL_WEEK, weeks);
		mKV.commit();
		getSettings();
	}

	/*
	 * 由当前是第几周推算出开学的日期
	 */
	public void saveCurWeek(int cur) {
		final SimpleDate simpleDate = new SimpleDate(DateUtil.getFirstDayOfTerm(cur));
		mKV.put(AppConstant.Preferences.FIRST_DAY_YEAR, simpleDate.getYear());
		mKV.put(AppConstant.Preferences.FIRST_DAY_MONTH, simpleDate.getMonth());
		mKV.put(AppConstant.Preferences.FIRST_DAY_DAY,simpleDate.getDayOfMonth());
		mKV.commit();
		getSettings();
	}
	
	/*
	 * 设置每天几节课
	 */
	public void saveDayClass(int cla) {
		mKV.put(AppConstant.Preferences.CLASSES_ONE_DAY, cla);
		mKV.commit();
		getSettings();
	}
	
	/*
	 * 设置一节课多少分钟
	 */
	public void saveClassMin(int min) {
		mKV.put(AppConstant.Preferences.CLASS_LAST_MINUTE, min);
		mKV.commit();
		getSettings();
	}
	
	/*
	 * 设置课间时间
	 */
	public void saveBreakMin(int min) {
		mKV.put(AppConstant.Preferences.CLASS_BREAK_MINUTE, min);
		mKV.commit();
		getSettings();
	}
}
