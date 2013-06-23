package com.zxl.Timetable.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClassAlertReceiver extends BroadcastReceiver {

	private static final String TAG = "ClassAlertReceiver";
	private static final String NOTIFY_ACTION = "com.zxl.Timetable.NOTIFY";
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(TAG, "onReceive: a=" + intent.getAction() + " " + intent.toString());
		
		
		if (NOTIFY_ACTION.equals(intent.getAction())) {
			Intent i = new Intent();
			i.setClass(context, ClassAlertService.class);
			i.putExtras(intent);
//			i.putExtra("action", intent.getAction());
			context.startService(i);
		} 
	}

}
