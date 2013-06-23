package com.zxl.Timetable.util;

import com.zxl.Timetable.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class WeekOfTermDrawable extends Drawable {
	
	private String mWeekOfTerm = "1";
    private final Paint mPaint;
    private final Rect mTextBounds = new Rect();
    private static float mTextSize = 8;
    
    public WeekOfTermDrawable(Context c) {
    	 mTextSize = c.getResources().getDimension(R.dimen.currentweek_icon_text_size);
    	 mPaint = new Paint();
         mPaint.setAlpha(255);
         mPaint.setColor(0xFFFFFFFF);
         mPaint.setTypeface(Typeface.DEFAULT_BOLD);
         mPaint.setTextSize(mTextSize);
         mPaint.setTextAlign(Paint.Align.CENTER);
    }

	@Override
	public void draw(Canvas canvas) {
		 mPaint.getTextBounds(mWeekOfTerm, 0, mWeekOfTerm.length(), mTextBounds);
	     int textHeight = mTextBounds.bottom - mTextBounds.top;
	     Rect bounds = getBounds();
	     canvas.drawText(mWeekOfTerm, bounds.right / 2, ((float) bounds.bottom + textHeight + 1) / 2, mPaint);
		
	}

	@Override
	public int getOpacity() {
		return PixelFormat.UNKNOWN;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	public void setWeekOfTerm(int currentWeek) {
        mWeekOfTerm = Integer.toString(currentWeek);
        invalidateSelf();
    }
}
