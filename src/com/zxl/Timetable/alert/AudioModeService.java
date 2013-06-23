package com.zxl.Timetable.alert;

import com.zxl.Timetable.util.MyUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author ivanchou
 *
 */
public class AudioModeService extends Service {
	
	private AudioManager am;
	private int ringerMode;
	private String mode; //判断是上课还是下课
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		ringerMode = am.getRingerMode();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (mode.equals("class") && MyUtils.getMuteValue(this)) {
			changeAudioMode();
			setAudioModeChangeReminder();
		} else if (mode.equals("break")) {
			resetAudioMode();
		}
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle i = intent.getExtras();
		mode = i.getString("mode");
		return super.onStartCommand(intent, flags, startId);
	}

	//judge it is need for auto mute the phone
	protected void changeAudioMode() {
		String mode = MyUtils.getMuteMode(this);
		if (mode.equals("mute")) {
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else if (mode.equals("vibrate")) {
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		}
	}
	
	protected void resetAudioMode() {
		am.setRingerMode(ringerMode);
	}
	
	protected void setAudioModeChangeReminder() {
		long classLastTime = ((long)(MyUtils.getClassLastMinute(this))) * 60 * 1000;
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent audioIntent = new Intent(this, AudioModeService.class);
		
		audioIntent.putExtra("mode", "break");
		PendingIntent pi = PendingIntent.getService(this, 0, audioIntent, PendingIntent.FLAG_ONE_SHOT);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + classLastTime, pi);
	}
}
