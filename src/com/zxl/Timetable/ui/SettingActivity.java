package com.zxl.Timetable.ui;

import com.zxl.Timetable.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener{

	String buliding;
	String curriculum;
	String otherSetting;
	String notifi;
	String autoMute;
	String muteMode;
	String time;
	String about;
	
	Preference bulidingPre;
	Preference curriculumPre;
	Preference otherSetPre;
	Preference timePre;
	CheckBoxPreference notifiPre;
	CheckBoxPreference autoMutePre;
	ListPreference muteModePre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
		init();
		setClickListener();
	}
	
	private void init() {
		buliding = getResources().getString(R.string.building);
		curriculum = getResources().getString(R.string.course);
		otherSetting = getResources().getString(R.string.othset);
		notifi = getResources().getString(R.string.notification);
		autoMute = getResources().getString(R.string.auto_mute);
		muteMode = getResources().getString(R.string.mute_mode);
		time = getResources().getString(R.string.cls_brk_time);
		
		
		bulidingPre = findPreference(buliding);
		curriculumPre = findPreference(curriculum);
		otherSetPre = findPreference(otherSetting);
		notifiPre = (CheckBoxPreference)findPreference(notifi);
		autoMutePre = (CheckBoxPreference)findPreference(autoMute);
		muteModePre = (ListPreference)findPreference(muteMode);
		timePre = findPreference(time);
	}

	private void setClickListener() {
		bulidingPre.setOnPreferenceChangeListener(this);
		bulidingPre.setOnPreferenceClickListener(this);
		
		curriculumPre.setOnPreferenceChangeListener(this);
		curriculumPre.setOnPreferenceClickListener(this);
		
		otherSetPre.setOnPreferenceChangeListener(this);
		otherSetPre.setOnPreferenceClickListener(this);
		
		timePre.setOnPreferenceChangeListener(this);
		timePre.setOnPreferenceClickListener(this);
		
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
				Intent intent = new Intent(this, MainActivity.class);
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
		if(preference.getKey().equals(buliding)) {  
            Log.v("SystemSetting", "buliding preference is clicked");  
            startActivity(new Intent(this, BuildingSetting.class));
        } else if(preference.getKey().equals(curriculum)) {  
            Log.v("SystemSetting", "curriculum preference is clicked");  
            startActivity(new Intent(this, CourseSetting.class));
        } else if (preference.getKey().equals(otherSetting)) {
			Log.v("SystemSetting", "otherset preference is clicked");  
            startActivity(new Intent(this, OthSettingActivity.class));
		} else if (preference.getKey().equals(time)) {
			Log.v("SystemSetting", "set time prefence is clicked");
			startActivity(new Intent(this, ClassTimeSetting.class));
		} else {  
            return false;  
        }  
        return true; 
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		//判断是哪个Preference改变了  
        if(preference.getKey().equals(buliding))  
        {  
            Log.v("SystemSetting", "buliding preference is changed");  
        }  
        else if(preference.getKey().equals(curriculum))  
        {  
            Log.v("SystemSetting", "curriculum preference is changed");  
        }  
        else if(preference.getKey().equals(otherSetting))  
        {  
            Log.v("SystemSetting", "otherset preference is changed");  
        }  
        else  
        {  
            //如果返回false表示不允许被改变  
            return false;  
        }  
        //返回true表示允许改变  
        return true;  
	}

	
}
