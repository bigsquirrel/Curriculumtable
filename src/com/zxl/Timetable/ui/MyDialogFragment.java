package com.zxl.Timetable.ui;

import com.zxl.Timetable.fragment.DayViewFragment;
import com.zxl.Timetable.fragment.StatViewFragment;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.util.MyUtils;
import com.zxl.Timetable.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyDialogFragment extends DialogFragment {

	//显示课程信息的dialog view
	private TextView timeText;
	private TextView placeText;
	private TextView weeksText;
	private TextView markText;
	private TextView signedText;
	
	//添加科目的dialog view
	private EditText courseText;
	private EditText teacherText;
	private EditText nickText;
	
	//添加教学楼的dialog view
	private EditText buildingText;
	
	//设置学期周数的dialog view
	private EditText weekText;
	
	//设置当前周的dialog
	private EditText curWeekText;
	
	//设置一天几节课的dialog
	private EditText dayClsText;
	
	//
	private TimePicker timePicker;
	
	private EditText scoresText;
	private EditText tolScoresText;
	
	private View showDetailView;	
	private View addCourseView;
	private View editCourseView;
	private View addBuildingView;
	private View editBulidingView;
	private View setWeeksView; 
	private View setCurWeekView;
	private View setDayClsView;
	private View setClassMinView;
	private View setBreakMinView;
	private View setTimeView;
	private View setScoresView;
	
	private static int hour;
	private static int minute;
	private static Fragment fra;
	
	public static MyDialogFragment newInstance(String title) {
		MyDialogFragment fragment = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}
	
	public static MyDialogFragment newInstance(String title, Fragment f) {
		MyDialogFragment fragment = new MyDialogFragment();
		fra = f;
		Bundle args = new Bundle();
		args.putString("title", title);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String dialogTag = getTag();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = null;
		String titleStr = getArguments().getString("title");
		
		//显示课程详细信息的dialog
		if(dialogTag == "detail_dialog") {
			
			showDetailView = inflater.inflate(R.layout.show_curriculum_detail, null);
			timeText = (TextView) showDetailView.findViewById(R.id.show_curriculum_time);
			placeText = (TextView) showDetailView.findViewById(R.id.show_curriculum_place);
			weeksText = (TextView) showDetailView.findViewById(R.id.show_curriculum_weeks);
			markText = (TextView) showDetailView.findViewById(R.id.show_curriculum_remark);
			signedText = (TextView) showDetailView.findViewById(R.id.show_curriculum_signed);
			
			builder = new AlertDialog.Builder(getActivity());
			
			//标题栏设置科目+老师
			builder.setTitle(titleStr)
			//时间为 星期+具体时间
			//地点
			//上课跨度
			//备注
				.setView(showDetailView)
				.setPositiveButton(R.string.mark_curriculum, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//在数据库添加去上课标记字段
						((DayViewFragment)fra).doSignClassClick();
					}
				})
				.setNegativeButton(R.string.share_curriculum, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((DayViewFragment)fra).doShareInfoClick();
					}
				});
			showDetail();
		
		}
		//添加科目的dialog
		else if (dialogTag == "add_course") {
			addCourseView = inflater.inflate(R.layout.add_course, null);
			courseText = (EditText) addCourseView.findViewById(R.id.set_course_edit1);
			teacherText = (EditText) addCourseView.findViewById(R.id.set_course_edit2);
			nickText = (EditText) addCourseView.findViewById(R.id.set_course_edit3);
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(addCourseView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text1 = courseText.getText().toString();
						String text2 = nickText.getText().toString();
						String text3 = teacherText.getText().toString();
						
						((CourseSetting)getActivity()).doAddPositiveClick(text1, text2, text3);
						courseText.setText("");
						nickText.setText("");
						teacherText.setText("");
						
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		
		}
		//添加教学楼的dialog
		else if (dialogTag == "add_build") {
			addBuildingView = inflater.inflate(R.layout.add_buliding, null);
			buildingText = (EditText) addBuildingView.findViewById(R.id.set_building_edit);
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(addBuildingView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = buildingText.getText().toString();
						((BuildingSetting)getActivity()).doAddPositiveClick(text);
						buildingText.setText("");
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			return builder.create();
		} 
		//删除数据的Dialog
		else if (dialogTag == "del_curriculum") {
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setMultiChoiceItems(new String[] { "同时删除所有科目", "同时删除所有教学楼" }, new boolean[] { false, false }, new OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//						((MainActivity)getActivity()).changeChoise(which, isChecked);
						((DayViewFragment)fra).changeChoise(which, isChecked);
					}
				})
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						((MainActivity)getActivity()).doDeleteAllCurriculums();
						((DayViewFragment)fra).doDeleteAllCurriculums();
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			return builder.create();
		} 
		//编辑教学楼的dialog
		else if (dialogTag == "edit_build") {
			editBulidingView = inflater.inflate(R.layout.add_buliding, null);
			buildingText = (EditText) editBulidingView.findViewById(R.id.set_building_edit);
			
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(editBulidingView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = buildingText.getText().toString();
						((BuildingSetting)getActivity()).doEditPositiveClick(text);
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GetCurriculumInfoUtil.endEditWork();
					}
				});
			getBulid();
			return builder.create();
			
		}
		//编辑科目信息的dialog
		else if (dialogTag == "edit_course") {
			editCourseView = inflater.inflate(R.layout.add_course, null);
			courseText = (EditText)editCourseView.findViewById(R.id.set_course_edit1);
			teacherText = (EditText)editCourseView.findViewById(R.id.set_course_edit2);
			nickText = (EditText)editCourseView.findViewById(R.id.set_course_edit3);
			
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(editCourseView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text1 = courseText.getText().toString();
						String text2 = nickText.getText().toString();
						String text3 = teacherText.getText().toString();
						((CourseSetting)getActivity()).doEditPositiveClick(text1, text2, text3);
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GetCurriculumInfoUtil.endEditCourse();
					}
				});
			getCourse();
			return builder.create();
		}
		//设置学期周数的dialog
		else if (dialogTag == "set_weeks") {
			setWeeksView = inflater.inflate(R.layout.set_weeks, null);
			weekText = (EditText) setWeeksView.findViewById(R.id.set_weeks);
			
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setWeeksView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = weekText.getText().toString();
						if (MyUtils.isEmpty(text)) {
							Toast.makeText(getActivity(), R.string.empty_error, Toast.LENGTH_SHORT).show();
						} else {
							int i = Integer.valueOf(text).intValue();
							if (i>=1 && i<=25) {
								((OthSettingActivity)getActivity()).saveAllWeek(i);
							} else {
								Toast.makeText(getActivity(), R.string.set_weeks_failure, Toast.LENGTH_SHORT).show();
							}
						}
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			return builder.create();
		}
		//设置当前周
		else if (dialogTag == "set_current_week") {
			setCurWeekView = inflater.inflate(R.layout.set_current_week, null);
			curWeekText = (EditText) setCurWeekView.findViewById(R.id.set_current_week);
			int allWeeks = MyUtils.getAllWeeks(getActivity());
			curWeekText.setHint("设置当前周" + "(小于" + allWeeks + ")");
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setCurWeekView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int allWeeks = MyUtils.getAllWeeks(getActivity());
						String text = curWeekText.getText().toString();
						if (MyUtils.isEmpty(text)) {
							Toast.makeText(getActivity(), R.string.empty_error, Toast.LENGTH_SHORT).show();
						} else {
							int i = Integer.valueOf(text).intValue();
							if (i <= allWeeks) {
								((OthSettingActivity)getActivity()).saveCurWeek(i);
							} else {
								Toast.makeText(getActivity(), R.string.set_curweek_failure, Toast.LENGTH_SHORT).show();
							}
						}
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		}
		//设置一天几节课的dialog
		else if (dialogTag == "set_day_class") {
			setDayClsView = inflater.inflate(R.layout.set_day_cls, null);
			dayClsText = (EditText) setDayClsView.findViewById(R.id.set_day_class);
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setDayClsView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = dayClsText.getText().toString();
						if (MyUtils.isEmpty(text)) {
							Toast.makeText(getActivity(), R.string.empty_error, Toast.LENGTH_SHORT).show();
						} else {
							int i = Integer.valueOf(text).intValue();
							((OthSettingActivity)getActivity()).saveDayClass(i);
						}
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			return builder.create();
		}
		//设置一节课多少分钟的dialog
		else if (dialogTag == "set_class_min") {
			setClassMinView = inflater.inflate(R.layout.set_day_cls, null);
			dayClsText = (EditText) setClassMinView.findViewById(R.id.set_day_class);
			dayClsText.setHint("请输入每节课的时间（分钟）");
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setClassMinView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = dayClsText.getText().toString();
						if (MyUtils.isEmpty(text)) {
							Toast.makeText(getActivity(), R.string.empty_error, Toast.LENGTH_SHORT).show();
						} else {
							int i = Integer.valueOf(text).intValue();
							((OthSettingActivity)getActivity()).saveClassMin(i);
						}
					}
				});
			return builder.create();
		}
		//设置课间休息
		else if (dialogTag == "set_break_min") {
			setBreakMinView = inflater.inflate(R.layout.set_day_cls, null);
			dayClsText = (EditText) setBreakMinView.findViewById(R.id.set_day_class);
			dayClsText.setHint("请输入课件休息时间（分钟）");
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setBreakMinView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = dayClsText.getText().toString();
						if (MyUtils.isEmpty(text)) {
							Toast.makeText(getActivity(), R.string.empty_error, Toast.LENGTH_SHORT).show();
						} else {
							int i = Integer.valueOf(text).intValue();
							((OthSettingActivity)getActivity()).saveBreakMin(i);
						}
					}
				});
			return builder.create();
		}
		//设置上课时间的dialog
		else if (dialogTag == "set_class_time") {
			setTimeView = inflater.inflate(R.layout.set_time, null);
			timePicker = (TimePicker) setTimeView.findViewById(R.id.set_time_picker);
			timePicker.setIs24HourView(true);
			timePicker.setCurrentHour(8);
			timePicker.setCurrentMinute(0);
			
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setTimeView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hour = timePicker.getCurrentHour();
						minute = timePicker.getCurrentMinute();
						((ClassTimeSetting)getActivity()).doSetClassTimeClick(hour, minute);
					}
				});
			return builder.create();
		}
		//添加考试
		else if (dialogTag == "confirm_add_exam") {
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setMessage(R.string.comfirm_exam)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((StatViewFragment)fra).doAddExamClick();
						
					}
				})
				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		}
		//给考试设置成绩信息
		else if (dialogTag == "confirm_add_scores") {
			setScoresView = inflater.inflate(R.layout.set_exam_scores, null);
			scoresText = (EditText) setScoresView.findViewById(R.id.set_exam_scores);
			tolScoresText = (EditText) setScoresView.findViewById(R.id.set_exam_tolscores);
			builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(titleStr)
				.setView(setScoresView)
				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text1 = scoresText.getText().toString();
						String text2 = tolScoresText.getText().toString();
						if (!MyUtils.isEmpty(text1) || !MyUtils.isEmpty(text2)) {
							int s = Integer.valueOf(text1).intValue();
							int ts = Integer.valueOf(text2).intValue();
							((StatViewFragment)fra).doAddScoresClick(s, ts);
						}
					}
				});
		}
//		//设置下课时间的dialog
//		else if (dialogTag == "set_break_time") {
//			builder = new AlertDialog.Builder(getActivity());
//			builder.setTitle(titleStr)
//				.setView(setTimeView)
//				.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//						int hour = timePicker.getCurrentHour();
//						int minute = timePicker.getCurrentMinute();
//						((ClassTimeSetting)getActivity()).doSetBreakTimeClick(hour, minute);
//					}
//				})
//				.setNegativeButton(R.string.alert_dialog_cancel, new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//					}
//				});
//			autoSetBreakTime();
//			return builder.create();
//			
//		}
		return builder.create();
		
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showDetail() {
		String timeStr = GetCurriculumInfoUtil.getTime();
		String placeStr = GetCurriculumInfoUtil.getPlace();
		String weekStr = GetCurriculumInfoUtil.getWeeks();
		String markStr = GetCurriculumInfoUtil.getMark();
		String signedStr = GetCurriculumInfoUtil.getClassSignedWeek();
		
		timeText.setText(timeStr);
		placeText.setText(placeStr);
		weeksText.setText(weekStr);
		markText.setText(markStr);
		signedText.setText(signedStr);
		
	}
	
	public void getBulid(){
		String buildStr = GetCurriculumInfoUtil.getBulid();
		buildingText.setText(buildStr);
	}
	
	public void getCourse(){
		String courseStr = GetCurriculumInfoUtil.getCourse();
		String teacherStr = GetCurriculumInfoUtil.getTeacher();
		String nickStr = GetCurriculumInfoUtil.getNick();
		
		courseText.setText(courseStr);
		teacherText.setText(teacherStr);
		nickText.setText(nickStr);
	}

//	public void autoSetBreakTime() {
//		int tmp = (minute + 45)/60;
//		minute = (minute + 45)%60;
//		
//		System.out.println("auto set time" + "tmp:" + tmp + "hour:" + hour);
//		hour = tmp + hour;
//		timePicker.setCurrentHour(hour);
//		timePicker.setCurrentMinute(minute);
//		System.out.println("auto set time" + hour + ":" + minute);
//	}

}
