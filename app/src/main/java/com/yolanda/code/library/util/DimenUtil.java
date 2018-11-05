package com.yolanda.code.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DimenUtil
{
    private static int getComplexUnit(int data)
    {
        return TypedValue.COMPLEX_UNIT_MASK & (data >> TypedValue.COMPLEX_UNIT_SHIFT);
    }

    public static boolean isPxVal(TypedValue val)
    {
        if (val != null && val.type == TypedValue.TYPE_DIMENSION &&
                getComplexUnit(val.data) == TypedValue.COMPLEX_UNIT_PX)
        {
            return true;
        }
        return false;
    }

    public static int getPercentWidthSizeBigger(int val)
    {
        int screenWidth = LayoutConfig.getInstance().getScreenWidth();
        int designWidth = LayoutConfig.getInstance().getDesignWidth();

        int res = val * screenWidth;
        if (res % designWidth == 0)
        {
            return res / designWidth;
        } else
        {
            return res / designWidth + 1;
        }

    }

    public static int getPercentHeightSizeBigger(int val)
    {
        int screenHeight = LayoutConfig.getInstance().getScreenHeight();
        int designHeight = LayoutConfig.getInstance().getDesignHeight();

        int res = val * screenHeight;
        if (res % designHeight == 0)
        {
            return res / designHeight;
        } else
        {
            return res / designHeight + 1;
        }
    }

    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        try
        {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static int[] getScreenSize(Context context, boolean useDeviceSize)
    {
        int[] size = new int[2];
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (!useDeviceSize)
        {
            size[0] = widthPixels;
            size[1] = heightPixels - getStatusBarHeight(context);

            return size;
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try
            {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored)
            {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored)
            {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }
}
