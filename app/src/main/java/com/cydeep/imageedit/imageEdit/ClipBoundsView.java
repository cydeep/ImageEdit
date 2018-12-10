package com.cydeep.imageedit.imageEdit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.cydeep.imageedit.util.ViewSizeUtil;


/**
 * Created by chenyu on 17/2/13.
 */

public class ClipBoundsView extends View {
    private int dp_1;
    private int dp_124;
    private int dp_250;
    private int dp_360;
    private int dp_119;
    private int dp_240;
    private Paint mPaint;

    public ClipBoundsView(Context context) {
        this(context,null);
    }

    public ClipBoundsView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipBoundsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(ViewSizeUtil.getCustomDimen(360f),ViewSizeUtil.getCustomDimen(360f));
    }

    private void init() {
        dp_1 = ViewSizeUtil.getCustomDimen(1f);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffffffff);
        float textSize = dp_1 / ViewSizeUtil.getDensity();
        dp_124 = ViewSizeUtil.getCustomDimen(124f);
        dp_240 = ViewSizeUtil.getCustomDimen(240f);
        dp_250 = ViewSizeUtil.getCustomDimen(250f);
        dp_360 = ViewSizeUtil.getCustomDimen(360f);
        dp_119 = ViewSizeUtil.getCustomDimen(119f);
        mPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawRect(0,0,getMeasuredWidth(),dp_1,mPaint);
        canvas.drawRect(0,dp_119,getMeasuredWidth(),dp_119 + dp_1,mPaint);
        canvas.drawRect(0,dp_240 - dp_1,getMeasuredWidth(),dp_240,mPaint);
        canvas.drawRect(0,dp_360 - dp_1,getMeasuredWidth(),dp_360,mPaint);
        canvas.drawRect(dp_119,dp_1,dp_119 + dp_1,dp_360 - 2 * dp_1,mPaint);
        canvas.drawRect(dp_240 - dp_1,dp_1,dp_240,dp_360 - 2 * dp_1,mPaint);
        canvas.restore();
    }

}
