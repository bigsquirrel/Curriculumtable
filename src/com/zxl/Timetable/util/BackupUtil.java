
package com.zxl.Timetable.util;

import com.lurencun.cfuture09.androidkit.util.apk.ApkInfo;
import com.lurencun.cfuture09.androidkit.util.apk.ResourceUtil;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author ivanchou
 * @Function
 */
public class BackupUtil {

	protected String DATA_BASE_PATH;
	protected String BACKUP_BASE_PATH;

	public BackupUtil(Context context) {
		ApkInfo apkInfo = new ResourceUtil(context).getApkInfo();
		DATA_BASE_PATH = new StringBuilder("/data/data/").append(
				apkInfo.packageName).toString();
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			BACKUP_BASE_PATH = Environment.getExternalStorageDirectory().getPath()
					+ "/com.zxl/backup/";
		} else {
			BACKUP_BASE_PATH = "/com.zxl/backup/";
			Toast.makeText(context, "没有检测到SD卡，可能无法备份成功", Toast.LENGTH_SHORT)
					.show();
		}
		BACKUP_BASE_PATH += apkInfo.packageName;
	}
}
