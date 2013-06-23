
package com.zxl.Timetable.nav.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;

import com.zxl.Timetable.common.ImageUtil;

/**
 * @author ivanchou
 */
public class ImgPagerAdapter extends PagerAdapter {
	private Context mContext;
	private int[] mResIds;
	private Bitmap[] mBitmaps;
	private ImageView[] mImageViews;

	public ImgPagerAdapter(Context context, int[] resIds) {
		mContext = context;
		mResIds = resIds;
		createBitmaps();
		createImageViews();
	}

	private void createImageViews() {
		mImageViews = new ImageView[mBitmaps.length];
		for (int i = 0, length = mImageViews.length; i < length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(mBitmaps[i]);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}
	}

	private void createBitmaps() {
		mBitmaps = ImageUtil.createBitmaps(mContext, mResIds);
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {
		// ((ViewPager) view).removeView(mImageViews[position]);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return mBitmaps.length;
	}

	@Override
	public Object instantiateItem(View view, int position) {
//		View v1 = ((Activity) mContext).findViewById(R.id.nav_nexttime);
//		View v2 = ((Activity) mContext).findViewById(R.id.nav_startuse);
//		v1.setVisibility(View.GONE);
//		v2.setVisibility(View.GONE);
//		if (position == 0) {
//			v1.setVisibility(View.VISIBLE);
//		}
//		if (position == mResIds.length - 1) {
//			v2.setVisibility(View.VISIBLE);
//		}
		// ((ViewPager) view).addView(mImageViews[position]);
		return mImageViews[position];
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
