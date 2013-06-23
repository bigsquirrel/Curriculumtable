package com.zxl.Timetable.ui;


//import com.jjoe64.graphview.BarGraphView;
//import com.jjoe64.graphview.GraphViewSeries;
//import com.jjoe64.graphview.GraphView.GraphViewData;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kusand.graphview.BarGraphView;
import com.kusand.graphview.GraphView.GraphViewData;
import com.kusand.graphview.GraphViewSeries;
import com.zxl.Timetable.R;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.dto.CurriculumDTO;
import com.zxl.Timetable.entity.AllOfOneCourse;
import com.zxl.Timetable.entity.Exam;
import com.zxl.Timetable.entity.ShareEntity;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.util.MyUtils;

import android.R.interpolator;
import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author ivanchou
 *
 */
public class CurriculumAllInfo extends Activity {

	private static final int SHARE = 1;
	
	private int id; //exam id
	private int courseId;
	private int allWeeks;
	private int tolClass;
	private int globalTolClass;
	private int globalSignClass;
	private int score;
	private int tolScore;
	
	private GraphViewData viewData;
	private GraphViewData [] viewDatas;
	private BarGraphView barGraphView; 
	private ListView detailList;
	private TextView describeInfo;
	private Exam exam;
	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stat_chart);
		findView();
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		//use the exam id to get the course id
		exam = Exam.getExam(this, id);
		courseId = exam.getCourseId();
		score = exam.getScores();
		tolScore = exam.getTotalScores();
		GetCurriculumInfoUtil.initEditCourse(this, courseId);
		setTitle(GetCurriculumInfoUtil.getCourse());
		statSignInfo(courseId);
		drawGraph();
//		showDetailList();
		detailList.setAdapter(new MyDetailAdapter(this));
	}
	
	
	public void statSignInfo(int id) {
		int tolSignClass;
		globalTolClass = 0;
		globalSignClass = 0;
		AllOfOneCourse.getAllOfOneCourse(this, id);
		allWeeks = MyUtils.getAllWeeks(this);
		viewDatas = new GraphViewData [allWeeks];
		for (int i = 1; i <= allWeeks; i++) {
			int j = AllOfOneCourse.getTolClass(i);
			if (j > tolClass) {
				tolClass = j;    //总共的课时数
			}
			tolSignClass = AllOfOneCourse.getTolSignClass(i); //总共签到的次数
			viewData = new GraphViewData(i, tolSignClass); //
			viewDatas[i - 1] = viewData;
			globalTolClass = globalTolClass + j;
			globalSignClass = globalSignClass + tolSignClass;
		}
	}
	
	public void drawGraph() {
			
		String horizonLab [] = new String[allWeeks];
		String verticalLab [] = new String[tolClass + 1];
		for (int i = 0; i < horizonLab.length; i++) {
			horizonLab[i] = AppConstant.HORIZONLAB[i];
		}
		for (int i = 0; i < verticalLab.length; i++) {
			verticalLab[i] = AppConstant.VERTICALLAB[i];
		}
		
		barGraphView.addSeries(new GraphViewSeries(viewDatas));
		barGraphView.setHorizontalLabels(horizonLab);
		barGraphView.setVerticalLabels(verticalLab);
		describeInfo.setText(genDescribeInfo());
	}
	
	protected String genDescribeInfo() {
		String str = "本课程一周" + tolClass + "个课时,整个学期一共有" + globalTolClass + "个课时.您一共签到" 
					+ globalSignClass + "次,占总共的" + toPercent(globalSignClass, globalTolClass) + ".您在本课程的考试中得分:"
					+ score + ",总分:"  + tolScore + ".";
		return str;
	}
	
	protected String toPercent(int i, int j) {
		double per = (i*1.0) / j;
		System.out.println("percent : " + per);
		DecimalFormat df = new DecimalFormat("0.0%");
		String str = df.format(per);
		return str;
	}
	
	private final class ViewHolder {
		TextView course;
		TextView dayOfWeek;
	}
	
	public class MyDetailAdapter extends BaseAdapter {
		
		int resId;
		boolean isNeedAttendClass;
		boolean isClassSigned;
		
		private LayoutInflater mInflater;
		private Context c;
		public MyDetailAdapter (Context context) {
			c = context;
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return tolClass;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.stat_list_item, null);
				holder.course = (TextView)convertView.findViewById(R.id.stat_course_name);
				holder.dayOfWeek = (TextView)convertView.findViewById(R.id.stat_othinfo);
				convertView.setTag(holder);
			}
			holder = (ViewHolder)convertView.getTag();
			AllOfOneCourse course = AllOfOneCourse.allCurArray.get(position);
			GetCurriculumInfoUtil.initGetInfo(c, course.getCurriculumid());
			holder.course.setText(GetCurriculumInfoUtil.getCourse());
			holder.dayOfWeek.setText(getOthInfo(course));
			
			for (int i = 0; i < allWeeks; i++) {
				isNeedAttendClass = WeeksModel.isNeedAttendClass(course.getWeeks(), i);
				isClassSigned = MyUtils.isClassSigned(course.getWeeks(), i, course.getSign());
				if (!isNeedAttendClass) {
					resId = R.drawable.no_need_sign ;
				} else if (isClassSigned) {
					resId = R.drawable.do_sign;
				} else {
					resId = R.drawable.not_do_sign;
				}
				setImageView(convertView, i, resId);
			}
			return convertView;
		}
		
		private void setImageView(View view, int id, int res) {
			int myRes = MyUtils.convertIntToRes(id);
			if (myRes != 0) {
				((ImageView) view.findViewById(myRes)).setImageResource(res);
			}
		}
		
		private String getOthInfo(AllOfOneCourse course) {
			return AppConstant.week[course.getDayOfWeek()] + " " + AppConstant.CLASSES_ONE_DAY[course.getOnClass()];
		}
		
	}
	
	private void findView() {
		barGraphView = (BarGraphView)findViewById(R.id.stat_chart_view);
		detailList = (ListView)findViewById(R.id.stat_item_detail);
		describeInfo = (TextView)findViewById(R.id.stat_detail_info);
//		detailList.addHeaderView((TextView)findViewById(R.id.stat_title));
//		detailList.addHeaderView((View)findViewById(R.id.stat_title_line));
//		detailList.addHeaderView(barGraphView);
//		detailList.addHeaderView(describeInfo);
//		detailList.addHeaderView((View)findViewById(R.id.stat_info_line));
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
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.share_menu, menu);
		MenuItem shareItem = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
		mShareActionProvider.setShareIntent(getShareIntent());
		return true;
	}

	protected Intent getShareIntent() {
		WindowManager windowManager = getWindowManager();
		Window window = getWindow();
		View decorview = this.getWindow().getDecorView();
		ShareEntity share = new ShareEntity();
		return share.getIntentSharePhotoAndText(share.GetandSaveCurrentImage(window, windowManager, decorview), genDescribeInfo());
	}
	
}
