package com.zxl.Timetable.fragment;

import com.zxl.Timetable.R;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.IExamDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.common.AppConstant;
import com.zxl.Timetable.dto.CurriculumDTO;
import com.zxl.Timetable.dto.ExamDTO;
import com.zxl.Timetable.ui.CurriculumAllInfo;
import com.zxl.Timetable.ui.MyDialogFragment;
import com.zxl.Timetable.ui.SettingActivity;
import com.zxl.Timetable.ui.MainActivity.DropDownListener;
import com.zxl.Timetable.util.MyUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class StatViewFragment extends Fragment {
	private static final int ALL_STATU = 2;
	
	private View statView;
	private ViewPager mViewPager; //这个viewpager是用来显示“未完结”、“正在考试”、“已完结”
	private String [] statusStrs; 
	private int viewPagePosition;
	private ListView[] mListViews = new ListView[3];
	private ICurriculumDAO mCurriculumDAO;
	private IExamDAO mExamDAO;
	private int currentWeek = 0; // 当前周数。
	private int curriculumId; // it is the click id
	private int examId;
	private ActionBar actionBar;
	private SpinnerAdapter adapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		statView = inflater.inflate(R.layout.stat_fragment, container, false);
		return statView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initAll();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		currentWeek = MyUtils.getCurrentWeek(this.getActivity());
		actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		refreshData();
	}

	protected void initAll() {
		statusStrs = getResources().getStringArray(R.array.array_statu);
		mCurriculumDAO = DAOFactory.getCurriculumDAOInstance(this.getActivity());
		mExamDAO = DAOFactory.getExamDaoInstance(this.getActivity());
		mViewPager = (ViewPager)statView.findViewById(R.id.statu_viewpager);
		mViewPager.setOffscreenPageLimit(ALL_STATU);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				viewPagePosition = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		viewPagePosition = 0;
	}
	
	protected void refreshData() {
		mViewPager.setAdapter(new StatuTimePagerAdapter());
		mViewPager.setCurrentItem(viewPagePosition);
	}
	
	protected class StatuTimePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mListViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (mListViews[position] != null) {
				unregisterForContextMenu(mListViews[position]);
			}
			mListViews[position] = createListView(position);
			container.addView(mListViews[position], 0);
			return mListViews[position];
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return statusStrs[position];
		}
		
	}
	
	
	
	protected ListView createListView(int position) {
		final ListView listView = (ListView)this.getActivity().getLayoutInflater().inflate(R.layout.main_list, null)
									.findViewById(R.id.main_listView);
		if (position == 0) {
			Cursor cursor = mCurriculumDAO.getAllCurriculums();
			CursorAdapter adapter = new CursorAdapter(this.getActivity(), cursor) {
				
				@Override
				public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
					return ((Activity)arg0).getLayoutInflater().inflate(R.layout.stat_list_item, null);
				}
				
				@Override
				public void bindView(View arg0, Context arg1, Cursor arg2) {
					CurriculumDTO dto = null;
					int resId;
					
					for (int i = 1; i <= currentWeek; i++) {
						dto = new CurriculumDTO(arg2, i);
						if (!dto.isNeedAttendClass()) {
							resId = R.drawable.no_need_sign ;
						} else if (dto.isClassSigned()) {
							resId = R.drawable.do_sign;
						} else {
							resId = R.drawable.not_do_sign;
						}
						setImageView(arg0, i, resId);
					}
					setTextViewText(arg0, R.id.stat_course_name, dto.getCourse());
					setTextViewText(arg0, R.id.stat_othinfo, getOthInfo(dto));
				}
				
				private void setTextViewText(View view, int id, String text) {
					((TextView) view.findViewById(id)).setText(text);
				}
				
				private void setImageView(View view, int id, int res) {
					int myRes = MyUtils.convertIntToRes(id);
					if (myRes != 0) {
						((ImageView) view.findViewById(myRes)).setImageResource(res);
					}
				}
				
				private String getOthInfo(CurriculumDTO dto) {
					return AppConstant.week[dto.getDay()] + " " + dto.getOnClass();
				}
			};
			
			listView.setAdapter(adapter);
			
			//点击条目弹出dialog询问用户是否添加考试？ 
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//show dialog that notifi the user confirm add a exam
					curriculumId = (int)arg3;
					MyDialogFragment dialogFragment = MyDialogFragment.newInstance("添加考试", StatViewFragment.this);
					dialogFragment.show(getFragmentManager(), "confirm_add_exam");
				}
			});
			
			
		} else if (position == 1) {
			Cursor cursor = mExamDAO.getFutureExams();
			CursorAdapter adapter = new CursorAdapter(this.getActivity(), cursor) {

				@Override
				public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
					return ((Activity)arg0).getLayoutInflater().inflate(R.layout.exam_list_item, null);
				}
				
				@Override
				public void bindView(View arg0, Context arg1, Cursor arg2) {
					ExamDTO dto = new ExamDTO(arg2);
					
					setTextViewText(arg0, R.id.exam_course_name, dto.getCourse());
					setTextViewText(arg0, R.id.exam_date_time, MyUtils.convertMillisToTime(dto.getDate()));
					setTextViewText(arg0, R.id.exam_place, dto.getBuilding() + " " + dto.getRoomNum());
					setTextViewText(arg0, R.id.exam_remind, dto.getRemind() + "");
				}
				
				private void setTextViewText(View view, int id, String text) {
					((TextView) view.findViewById(id)).setText(text);
				}

			};
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					examId = (int) arg3;
					MyDialogFragment dialogFragment = MyDialogFragment.newInstance("添加成绩", StatViewFragment.this);
					dialogFragment.show(getFragmentManager(), "confirm_add_scores");
				}
			});
		} else if (position == 2) {
			Cursor cursor = mExamDAO.getCompleteExams();
			CursorAdapter adapter = new CursorAdapter(this.getActivity(), cursor) {

				@Override
				public void bindView(View arg0, Context arg1, Cursor arg2) {
					ExamDTO dto = new ExamDTO(arg2, 0);
					setTextViewText(arg0, R.id.over_course_name, dto.getCourse());
					setTextViewText(arg0, R.id.over_course_scores, dto.getScores() + "/" + dto.getTolScores());
				}

				@Override
				public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
					return ((Activity)arg0).getLayoutInflater().inflate(R.layout.over_list_item, null);
				}
				
				private void setTextViewText(View view, int id, String text) {
					((TextView) view.findViewById(id)).setText(text);
				}
				
			};
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(getActivity(), CurriculumAllInfo.class);
					intent.putExtra("id", (int) arg3 );
					startActivity(intent);
				}
			});
			
		}
		
		return listView;
	}
	
	public void doAddExamClick(){
		FragmentManager fm = getActivity().getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		AddExamFragment addExamFragment = new AddExamFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("id", curriculumId);
		addExamFragment.setArguments(bundle);
		ft.addToBackStack(null);
		ft.replace(R.id.main_test, addExamFragment, "add_exam");
		ft.commit();
		
	}
	
	public void doAddScoresClick(int scores, int totalScores) {
		mExamDAO.updateScores(examId, scores, totalScores);
		refreshData();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem currentWeekItem;

		inflater.inflate(R.menu.oth_menu, menu);
		currentWeekItem = menu.findItem(R.id.action_current_week2);
		LayerDrawable icon = (LayerDrawable) currentWeekItem.getIcon();
		MyUtils.setWeekIcon(icon, this.getActivity());

		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_current_week2: // 定位到today，显示提示信息
			break;

		case R.id.action_setting2: // 课程表设置
			startActivity(new Intent(this.getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
