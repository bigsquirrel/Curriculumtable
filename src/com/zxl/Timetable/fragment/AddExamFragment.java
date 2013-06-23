package com.zxl.Timetable.fragment;

import java.util.Calendar;

import com.zxl.Timetable.R;
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.entity.Course;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.entity.Exam;
import com.zxl.Timetable.util.MyUtils;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
/**
 * @author ivanchou
 *
 */
public class AddExamFragment extends Fragment {
	
	private static final int AHEAD_THIRTY_MIN = 5; //设置提起的时间为30分钟
	private static final int ADD_EXAM_CANCEL = 0;
	private static final int ADD_EXAM_OK = 1;
	
	private int id; // from statviewfragment 
	private Exam exam;
	private int courseId;
	
	private View addView;
	private TextView courseName;
	private TextView moreInfo;
	
	private Spinner buildSpinner;
	private EditText roomText;
	private Spinner remindSpinner;
	
	//set the exam time date? and hour
	private Button dateBtn;
	private Button timeBtn;
	
	private IBuildingDAO mBuildingDAO;
	private IExamDAO mExamDAO;
	private Cursor buildingsCursor;
	
	private String roomNum;
	
	
	private Course mCourse;
	private Curriculum mCurriculum;
	
	private Calendar c;
	private int mYear;
	private int mMonth;
	private int mDayOfMonth;
	private int mHour;
	private int mMinute;
	
	private ActionBar actionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//get the id and then put the id's curriculum to the t_exam
		id = getArguments().getInt("id");
		setHasOptionsMenu(true);
	}

	
	@Override
	public void onStart() {
		super.onStart();
		initAll();
	}

	@Override
	public void onResume() {
		super.onResume();
		actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.add_exam);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addView = inflater.inflate(R.layout.add_exam, null);
		return addView;
	}
	
	public void initAll() {
		exam = new Exam();
		mExamDAO = DAOFactory.getExamDaoInstance(getActivity());
		
		courseName = (TextView)addView.findViewById(R.id.exam_course_name);
		moreInfo = (TextView)addView.findViewById(R.id.exam_course_info);
		buildSpinner = (Spinner)addView.findViewById(R.id.exam_building_name);
		roomText = (EditText)addView.findViewById(R.id.exam_room);
		dateBtn = (Button)addView.findViewById(R.id.exam_date_set_btn);
		timeBtn = (Button)addView.findViewById(R.id.exam_time_set_btn);
		remindSpinner = (Spinner)addView.findViewById(R.id.exam_remind);
		
		mBuildingDAO = DAOFactory.getBuildingDAOInstance(getActivity());
		buildingsCursor = mBuildingDAO.getAllBuildings();
		SimpleCursorAdapter buildingsCursorAdapter = new SimpleCursorAdapter(
				getActivity(), android.R.layout.simple_spinner_item, buildingsCursor,
				new String[] { "building" }, new int[] { android.R.id.text1 });
		buildingsCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildSpinner.setAdapter(buildingsCursorAdapter);
		
		mCurriculum = Curriculum.getCurriculum(getActivity(), id);
		courseId = mCurriculum.getCourseId();   //这个courseId是要写到t_exam里的
		mCourse = Course.getCourse(getActivity(), courseId);
		courseName.setText(mCourse.getCourse());
		moreInfo.setText(mCourse.getTeacher() + "   " + WeeksModel.getWeeksModel(mCurriculum.getWeeks()).toString());
		
		ArrayAdapter<CharSequence> aheadTimeAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.class_remind_time, android.R.layout.simple_spinner_item);
		aheadTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		remindSpinner.setAdapter(aheadTimeAdapter);
		remindSpinner.setSelection(AHEAD_THIRTY_MIN);
		
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		
		setDate();
		setTime();
		
		setBtnClickListener();
	}
	
	
	public void setDate() {
		String date = MyUtils.formatCalenderDate(c);
		dateBtn.setText(date);
	}
	
	public void setTime() {
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		String time = MyUtils.formatCalenderTime(c);
		timeBtn.setText(time);
	}
	
	public void saveInfo() {
		buildingsCursor.moveToPosition(buildSpinner.getSelectedItemPosition());
		exam.setBuildingId(buildingsCursor.getInt(buildingsCursor.getColumnIndex("_id")));
		
		roomNum = roomText.getText().toString();
		exam.setRoomNum(roomNum);
		
		exam.setDate(c.getTimeInMillis());
		exam.setCourseId(courseId);
		exam.setColor(0);
		exam.setDeleted("false");
		exam.setRemind(MyUtils.getRemindTime(remindSpinner.getSelectedItemPosition()));
		exam.setSign("false");
		
		mExamDAO.insertExam(exam);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem cancelMenuItem = menu.add(0, ADD_EXAM_CANCEL, ADD_EXAM_CANCEL, R.string.add_exam_cancel).setIcon(android.R.drawable.ic_menu_delete);
		MenuItem okMenuItem = menu.add(0, ADD_EXAM_OK, ADD_EXAM_OK, R.string.add_exam_ok).setIcon(android.R.drawable.ic_menu_save);
		
		cancelMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		okMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADD_EXAM_CANCEL:
			break;
		case ADD_EXAM_OK:
			saveInfo();
			break;
		case android.R.id.home:
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}


	//all below is about set date and time , no need to read more
	public void setBtnClickListener() {
		dateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddExamFragment.this.getActivity(), mDateSetListener, mYear, mMonth, mDayOfMonth).show();
			}
		});
		
		timeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TimePickerDialog(AddExamFragment.this.getActivity(), mTimeSetListener, mHour, mMinute, true).show();
			}
		});
	}
	
	
	public DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDayOfMonth = dayOfMonth;
			refreshDate();
		}
	};
	
	
	public TimePickerDialog.OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			refreshTime();
		}
	};
	
	public void refreshDate() {
		c.set(Calendar.YEAR, mYear);
		c.set(Calendar.MONTH, mMonth);
		c.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
		setDate();
	}
	
	public void refreshTime() {
		c.set(Calendar.HOUR_OF_DAY, mHour);
		c.set(Calendar.MINUTE, mMinute);
		setTime();
	}
}
