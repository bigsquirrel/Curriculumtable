
package com.zxl.Timetable.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.widget.Toast;

import com.lurencun.cfuture09.androidkit.io.FileUtils;

/**
 * 备份与恢复的工具类。封装了备份与恢复的相关操作。
 * 
 * @author ivanchou
 */
public class BackupLocalUtil extends BackupUtil{

	private String SHARED_PREFS;
	private String DATABASES;
	private Context mContext;

	private String BACKUP_DATABASES;
	private String BACKUP_SHARED_PREFS;

	public BackupLocalUtil(Context context) {
		super(context);
		mContext = context;
		SHARED_PREFS = DATA_BASE_PATH + "/shared_prefs";
		DATABASES = DATA_BASE_PATH + "/databases";
		BACKUP_DATABASES = BACKUP_BASE_PATH + "/database";
		BACKUP_SHARED_PREFS = BACKUP_BASE_PATH + "/shared_prefs";
	}

	/**
	 * 备份文件
	 * 
	 * @return 当且仅当数据库及配置文件都备份成功时返回true。
	 */
	public boolean doBackup() {
		return backupDB() && backupSharePrefs();
	}

	private boolean backupDB() {
		return copyDir(DATABASES, BACKUP_DATABASES, "备份数据库文件成功:"
				+ BACKUP_DATABASES, "备份数据库文件失败");
	}

	private boolean backupSharePrefs() {
		return copyDir(SHARED_PREFS, BACKUP_SHARED_PREFS, "备份配置文件成功:"
				+ BACKUP_SHARED_PREFS, "备份配置文件失败");
	}

	/**
	 * 恢复
	 * 
	 * @return 当且仅当数据库及配置文件都恢复成功时返回true。
	 */
	public boolean doRestore() {
		if (new File(BACKUP_BASE_PATH).exists()) {
			return restoreDB() && restoreSharePrefs();
		} else {
			showToast("没有备份文件。");
			return false;
		}
	}

	private boolean restoreDB() {
		return copyDir(BACKUP_DATABASES, DATABASES, "恢复数据库文件成功", "恢复数据库文件失败");
	}

	private boolean restoreSharePrefs() {
		return copyDir(BACKUP_SHARED_PREFS, SHARED_PREFS, "恢复配置文件成功",
				"恢复配置文件失败");
	}

	private final void showToast(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 复制目录
	 * 
	 * @param srcDir
	 *            源目录
	 * @param destDir
	 *            目标目录
	 * @param successMsg
	 *            复制成功的提示语
	 * @param failedMsg
	 *            复制失败的提示语
	 * @return 当复制成功时返回true, 否则返回false。
	 */
	private final boolean copyDir(String srcDir, String destDir,
			String successMsg, String failedMsg) {
		try {
			FileUtils.copyDirectory(new File(srcDir), new File(destDir));
		} catch (IOException e) {
			e.printStackTrace();
			showToast(failedMsg);
			return false;
		}
		showToast(successMsg);
		return true;
	}
}
