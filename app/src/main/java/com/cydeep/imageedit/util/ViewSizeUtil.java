package com.cydeep.imageedit.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cydeep.imageedit.ImageEditApplication;

/**
 * Created by chenyu on 15/10/18.
 */
public class ViewSizeUtil {

    /**
     * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
     * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
     */

    public static Rect getWindowRootViewRect(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame;
    }

    public static Rect getViewRectInParent(View view, ViewGroup parent) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        parent.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    public static Point getScreenSize() {
        WindowManager wmManager = (WindowManager) ImageEditApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wmManager.getDefaultDisplay();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     *
     * @param dimen dp值
     * @return
     */
    public static int getCustomDimen(float dimen){
        float density = ViewSizeUtil.getScreenSize().x / ViewSizeUtil.getDensity();
        int result = (int) (ViewSizeUtil.getDensity() * density * dimen / 360f);
        return result;
    }

    public static float getDensity() {
        WindowManager wmManager = (WindowManager) ImageEditApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wmManager.getDefaultDisplay();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        return metric.density;
    }

    public static int getDensityDpi() {
        WindowManager wmManager = (WindowManager) ImageEditApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wmManager.getDefaultDisplay();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        return metric.densityDpi;
    }

    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public static int getStatusBarHeight() {
        int height = 0;
        //获取status_bar_height资源的ID
        int resourceId = ImageEditApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            height = ImageEditApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }


}
