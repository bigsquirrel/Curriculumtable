package com.zxl.Timetable.fragment;

import java.lang.reflect.Field;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoListener;
import com.zxl.Timetable.R;
import com.zxl.Timetable.base.dao.DBCloseable;
import com.zxl.Timetable.base.dao.IBuildingDAO;
import com.zxl.Timetable.base.dao.ICourseDAO;
import com.zxl.Timetable.base.dao.ICurriculumDAO;
import com.zxl.Timetable.base.dao.factory.DAOFactory;
import com.zxl.Timetable.base.entity.DeleteChoices;
import com.zxl.Timetable.base.entity.WeeksModel;
import com.zxl.Timetable.base.utils.DBUtil;
import com.zxl.Timetable.base.utils.DateUtil;
import com.zxl.Timetable.dto.CurriculumDTO;
import com.zxl.Timetable.entity.Curriculum;
import com.zxl.Timetable.ui.CurriculumAdd;
import com.zxl.Timetable.ui.CurriculumEdit;
import com.zxl.Timetable.ui.MyDialogFragment;
import com.zxl.Timetable.ui.SettingActivity;
import com.zxl.Timetable.util.GetCurriculumInfoUtil;
import com.zxl.Timetable.util.MyUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DayViewFragment extends Fragment {

	private SharedPreferences mSP;
	private String[] mWeekStrings;
	private ListView[] mListViews = new ListView[7];

	private View dayView;
	private ViewPager mViewPager;

	private DeleteChoices choices; // 是否同时删除科目或教学楼
	private int currentWeek = 0; // 当前周数。
	private int viewPagePosition;
	private int id; // 表示点击的listview项目在数据库的id

	private ActionMode mActionMode = null;

	private IBuildingDAO mBuildingDAO;
	private ICourseDAO mCourseDAO;
	private ICurriculumDAO mCurriculumDAO;
	private Activity context;
	private SpinnerAdapter adapter;
	private ActionBar actionBar;
	private UndoBarController.UndoListener undoListener;
	private Parcelable undoToken;
	private FixedSpeedScroller mScroller;
	private int mMyDuration = 400;          //持续时间
	private Field mField;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindRes();
		initFra();
		setHasOptionsMenu(true);

	}

	protected void bindView() {
		mViewPager = (ViewPager) dayView.findViewById(R.id.main_viewpager);

	}

	protected void bindRes() {
		mWeekStrings = getResources().getStringArray(R.array.array_week);
	}

	protected void initFra() {
		choices = new DeleteChoices();
		mBuildingDAO = DAOFactory.getBuildingDAOInstance(this.getActivity());
		mCourseDAO = DAOFactory.getCourseDAOInstance(this.getActivity());
		mCurriculumDAO = DAOFactory
				.getCurriculumDAOInstance(this.getActivity());
	}

	protected void initViewPager() {
		mViewPager.setOffscreenPageLimit(6);
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
		viewPagePosition = DateUtil.getCurrentDayOfWeek();
	}

	protected void initScroller() {
		try {
			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
			mField.set(mViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem currentWeekItem;

		inflater.inflate(R.menu.main_menu, menu);
		currentWeekItem = menu.findItem(R.id.action_current_week);

		LayerDrawable icon = (LayerDrawable) currentWeekItem.getIcon();
		MyUtils.setWeekIcon(icon, this.getActivity());

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_current_week: // 定位到today，显示提示信息
			refreshData();
			Toast.makeText(
					this.getActivity(),
					"现在是" + "第" + MyUtils.getCurrentWeek(this.getActivity())
							+ "周", Toast.LENGTH_SHORT).show();
			mViewPager.setCurrentItem(DateUtil.getCurrentDayOfWeek(), true);
			break;
		case R.id.action_add_curriculum: // 添加课程
			final Intent intent = new Intent(this.getActivity(),
					CurriculumAdd.class);
			intent.putExtra("dayOfWeek", mViewPager.getCurrentItem());
			startActivity(intent);
			break;

		case R.id.action_reset_timetable: // 清空课表
			MyDialogFragment addCourseDialogFragment = MyDialogFragment
					.newInstance("清空课表", this);
			addCourseDialogFragment
					.show(getFragmentManager(), "del_curriculum");
			break;

		case R.id.action_setting: // 课程表设置
			startActivity(new Intent(this.getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void changeChoise(int which, boolean isChecked) {
		choices.setChoise(which, isChecked);
	}

	/**
	 * 选择性的删除全部教学楼信息和课程信息
	 */
	public void doDeleteAllCurriculums() {
		if (mCurriculumDAO.deleteAllCurriculums()) {
			Toast.makeText(this.getActivity(),
					R.string.toast_delete_curriculums_success,
					Toast.LENGTH_SHORT).show();

			if (choices.deleteBuildings()) {
				mBuildingDAO.deleteAllBuildings();
				Toast.makeText(this.getActivity(),
						R.string.toast_delete_building_success,
						Toast.LENGTH_SHORT).show();
			}
			if (choices.deleteCourses()) {
				mCourseDAO.deleteAllCourses();
				Toast.makeText(this.getActivity(),
						R.string.toast_delete_course_success,
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(this.getActivity(),
					R.string.toast_delete_curriculums_failure,
					Toast.LENGTH_SHORT).show();
		}

		refreshData();
	}

	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 1500;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}

	}

	protected void refreshData() {
		mViewPager.setAdapter(new TimeTablePagerAdapter());
		mViewPager.setCurrentItem(viewPagePosition);
	}

	/*
	 * 
	 */
	protected class TimeTablePagerAdapter extends PagerAdapter {

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
			mListViews[position] = createListview(position);
			container.addView(mListViews[position], 0);
			return mListViews[position];
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mWeekStrings[position];
		};
	}

	private ListView createListview(int position) {
		final ListView listview = (ListView) this.getActivity()
				.getLayoutInflater().inflate(R.layout.main_list, null)
				.findViewById(R.id.main_listView);

		Cursor cursor = mCurriculumDAO.getCurriculumsByDay(position);
		CursorAdapter adapter = new CursorAdapter(this.getActivity(), cursor) {

			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				return ((Activity) context).getLayoutInflater().inflate(
						R.layout.main_list_item2, null);
			}

			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				CurriculumDTO dto = new CurriculumDTO(context, cursor,
						currentWeek);
				int resId = R.drawable.no_need_sign;

				setTextViewText(view, R.id.list_item_class2, dto.getOnClass());
				setTextViewText(view, R.id.list_item_time2,
						dto.getOnClassTime());
				setTextViewText(view, R.id.list_item_course2, dto.getCourse());
				setTextViewText(view, R.id.list_item_teacher2, dto.getTeacher());
				setTextViewText(view, R.id.list_item_room2, dto.getBuilding()
						+ dto.getRoom());

				if (!dto.isNeedAttendClass()) {
					setTextViewColor(view, R.id.list_item_class2);
					setTextViewColor(view, R.id.list_item_time2);
					setTextViewColor(view, R.id.list_item_course2);
					setTextViewColor(view, R.id.list_item_teacher2);
					setTextViewColor(view, R.id.list_item_room2);

				} else {
					if (dto.isClassSigned()) {
						resId = R.drawable.do_sign;
					} else {
						resId = R.drawable.not_do_sign;
					}
				}
				setImageView(view, R.id.list_item_sign2, resId);
				view.setTag(dto.getId());
			}

			/*
			 * 没课就将信息设置为灰色
			 */
			private void setTextViewColor(View view, int id) {
				int colorGray = getResources().getColor(R.color.darker_gray);
				((TextView) view.findViewById(id)).setTextColor(colorGray);
			}

			private void setTextViewText(View view, int id, String text) {
				((TextView) view.findViewById(id)).setText(text);
			}

			private void setImageView(View view, int id, int res) {
				((ImageView) view.findViewById(id)).setImageResource(res);
			}
		};
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				id = (int) arg3;
				showCurriculumDetailDialog();
			}
		});

		listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if (mActionMode != null) {
					mActionMode.finish();
					mActionMode = null;
				}
				id = (int) arg3;
				listview.setItemChecked(arg2, true);
				mActionMode = DayViewFragment.this.getActivity()
						.startActionMode(mActionModeCallback);
				return true;
			}

		});
		registerForContextMenu(listview);
		return listview;
	}

	// using the contextual action mode

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = DayViewFragment.this.getActivity()
					.getMenuInflater();
			inflater.inflate(R.menu.edit_context_menu, menu);
			System.out.println("onCreateActionMode");
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			System.out.println("onPrepareActionMode");
			undoListener = new UndoListener() {
				@Override
				public void onUndo(Parcelable token) {
					if (mCurriculumDAO.undoDelCurriculum(id)) {
						refreshData();
					}
					undoToken = token;
				}
			};
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			System.out.println("onDestroyActionMode");
			mActionMode = null;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			System.out.println("onActionItemClicked");
			switch (item.getItemId()) {
			case R.id.main_show:
				showCurriculumDetailDialog();
				mode.finish();
				return true;
			case R.id.main_edit:
				final Intent editIntent = new Intent(
						DayViewFragment.this.getActivity(),
						CurriculumEdit.class);
				editIntent.putExtra("_id", id);
				startActivity(editIntent);
				mode.finish();
				return true;
			case R.id.main_del:
				if (mCurriculumDAO.deleteCurriculum(id)) {
					refreshData();
				}
				mode.finish();
				UndoBarController.show(getActivity(), "已删除", undoListener,
						undoToken);
				return true;
			default:
				return false;
			}
		}
	};

	private void showCurriculumDetailDialog() {
		GetCurriculumInfoUtil.initGetInfo(DayViewFragment.this.getActivity(),
				id);

		String titleStr = GetCurriculumInfoUtil.getTitle();

		MyDialogFragment showDetailDialog = MyDialogFragment.newInstance(
				titleStr, this);
		showDetailDialog.show(getFragmentManager(), "detail_dialog");
	}

	public void doSignClassClick() {
		Curriculum curriculum = Curriculum.getCurriculum(this.getActivity(), id);
		int sign = curriculum.getSign();
		int weeks = curriculum.getWeeks();
		if (MyUtils.isCouldSign(getActivity(), curriculum.getDay(), curriculum.getOnClass())) {
			if (MyUtils.isClassSigned(weeks, currentWeek, sign)) {
				Toast.makeText(this.getActivity(), R.string.u_have_signed,
						Toast.LENGTH_SHORT).show();
			} else if (!WeeksModel.isNeedAttendClass(weeks, currentWeek)) {
				Toast.makeText(this.getActivity(), R.string.can_not_sign,
						Toast.LENGTH_SHORT).show();
			} else {
				curriculum.setSign(MyUtils.signClass(weeks, currentWeek, sign));
				if (mCurriculumDAO.updateCurriculum(curriculum)) {
					Toast.makeText(this.getActivity(), R.string.sign_success,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this.getActivity(), R.string.sign_failure,
							Toast.LENGTH_SHORT).show();
				}
				refreshData();
			}
		} else {
			Toast.makeText(getActivity(), R.string.the_time_can_not_do, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void doShareInfoClick() {
		Curriculum curriculum = Curriculum.getCurriculum(this.getActivity(), id);
		if (MyUtils.isCouldShare(getActivity(), curriculum.getDay(), curriculum.getOnClass())) {
			String info = "我在" + GetCurriculumInfoUtil.getPlace() + "上" + 
					GetCurriculumInfoUtil.getTeacher() + "的" + 
					GetCurriculumInfoUtil.getCourse() + "。（来自ivanchou课表）";
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, info);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		} else {
			Toast.makeText(getActivity(), R.string.the_time_can_not_do, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dayView = inflater.inflate(R.layout.activity_main, container, false);
		return dayView;
	}

	@Override
	public void onStart() {
		super.onStart();
		ActionBar actionBar = this.getActivity().getActionBar();
		actionBar.show();
		bindView();
		initViewPager();
//		initScroller();
	}

	@Override
	public void onResume() {
		super.onResume();
		currentWeek = MyUtils.getCurrentWeek(this.getActivity());
		refreshData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DBUtil.closeDB((DBCloseable) mBuildingDAO);
		DBUtil.closeDB((DBCloseable) mCourseDAO);
		DBUtil.closeDB((DBCloseable) mCurriculumDAO);
	}

}
