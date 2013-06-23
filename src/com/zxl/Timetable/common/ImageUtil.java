
package com.zxl.Timetable.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author ivanchou
 */
public class ImageUtil {
	public static Bitmap[] createBitmaps(Context context, int[] resIds) {
		Bitmap[] bitmaps = new Bitmap[resIds.length];
		for (int i = 0, length = bitmaps.length; i < length; i++) {
			bitmaps[i] = BitmapFactory.decodeResource(context.getResources(),
					resIds[i]);
		}
		return bitmaps;
	}
}
