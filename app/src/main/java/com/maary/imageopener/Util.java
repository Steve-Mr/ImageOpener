package com.maary.imageopener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowMetrics;

import java.io.IOException;

public class Util {
    public static Bitmap getBitmap(Intent intent, Context context) throws IOException {
        Uri imageUri = intent.getData();
        if (imageUri != null) {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } else return null;
    }

    public static Point getDeviceBounds(Context context) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity)context).getWindowManager().getMaximumWindowMetrics();
            point = new Point(windowMetrics.getBounds().width(), windowMetrics.getBounds().height());
        } else {
            Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
            display.getRealSize(point);
        }
        return point;
    }

    public static Boolean isVertical(int dheight, int dwidth, Bitmap bitmap) {
        int bitmap_full_width = bitmap.getWidth();
        int bitmap_full_height = bitmap.getHeight();

        double device_scale = (double) dheight / dwidth;
        double bitmap_scale = (double) bitmap_full_height / bitmap_full_width;

        return device_scale < bitmap_scale;
    }
}
