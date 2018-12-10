package com.cydeep.imageedit.imageEdit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cydeep.imageedit.R;
import com.cydeep.imageedit.util.ViewSizeUtil;

/**
 * Created by chenyu on 17/2/9.
 */

public class ImageSaturationSeekBar extends ViewGroup {
    private final int backgroundProgressColor;
    private final int progressColor;
    private final int seekBarSize;
    private final int seekBarResId;
    private final int progressSize;
    private final int textColor;
    private final float textSize;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private float downX;
    private float downY;
    private float lastX;
    private float lastY;
    private int touchSlop;
    private ImageView imageView;
    private boolean isTouchImageView;
    private Paint mPaint;
    private TextView textView;
    private View childView;
    private Bitmap bitmap;
    private int progress;

    public void setOnImageSaturationBarMoveListener(OnImageSaturationBarMoveListener onImageSaturationBarMoveListener) {
        this.onImageSaturationBarMoveListener = onImageSaturationBarMoveListener;
    }

    private OnImageSaturationBarMoveListener onImageSaturationBarMoveListener;

    public ImageSaturationSeekBar(Context context) {
        this(context, null);
    }

    public ImageSaturationSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageSaturationSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageSaturationSeekBar, defStyleAttr, 0);

        backgroundProgressColor = a.getColor(R.styleable.ImageSaturationSeekBar_backgroundProgressColor, 0xFFDBDCDE);
        progressColor = a.getColor(R.styleable.ImageSaturationSeekBar_saturationProgressColor, 0xFF7A1AE0);
        progressSize = a.getDimensionPixelSize(R.styleable.ImageSaturationSeekBar_progressSize, ViewSizeUtil.getCustomDimen(1f));
        seekBarSize = a.getDimensionPixelSize(R.styleable.ImageSaturationSeekBar_seekBarSize, ViewSizeUtil.getCustomDimen(29f));
        seekBarResId = a.getResourceId(R.styleable.ImageSaturationSeekBar_seekBarResId, R.drawable.icon_image_saturation_seek_bar);
        textColor = a.getColor(R.styleable.ImageSaturationSeekBar_saturationTextColor, 0xff645e66);
        textSize = a.getDimensionPixelSize(R.styleable.ImageSaturationSeekBar_saturationTextSize, 12);

        a.recycle();
        init();
    }

    private void init() {
        childView = View.inflate(getContext(), R.layout.image_saturation_seek, null);
        imageView = (ImageView) childView.findViewById(R.id.image_saturation_image);
        textView = (TextView) childView.findViewById(R.id.image_saturation_text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        imageView.setImageResource(seekBarResId);
        addView(childView);

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = configuration.getScaledTouchSlop();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(backgroundProgressColor);
        mPaint.setTextSize(progressSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMeasureSize = MeasureSpec.makeMeasureSpec(seekBarSize, MeasureSpec.EXACTLY);
        childView.measure(widthMeasureSize, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        childView.layout(0, 0, seekBarSize, getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float focusX = event.getX();
        float focusY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getY() >= 0 && event.getY() <= getMeasuredHeight()
                        && event.getX() >= -getScrollX() && event.getX() <= -getScrollX() + imageView.getMeasuredWidth()) {
                    isTouchImageView = true;
                }
                if (!isTouchImageView) {
                    move(focusX, focusY);
                }
                lastX = downX = focusX;
                lastY = downY = focusY;
                break;
            case MotionEvent.ACTION_MOVE:
                move(focusX, focusY);
                lastX = focusX;
                lastY = focusY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouchImageView = false;
                break;

        }
        return true;
    }

    private void move(float focusX, float focusY) {
        float distanceX = focusX - lastX;
        float scrollY = lastY - focusY;
        if (Math.abs(lastX - downX) > touchSlop && isTouchImageView) {
            if (distanceX > 0) {//向右滑动
                if (distanceX - getScrollX() >= getMeasuredWidth() - seekBarSize) {
                    distanceX = getMeasuredWidth() + getScrollX() - seekBarSize;
                } else {
                }
            } else {//向左滑动
                if (distanceX - getScrollX() <= 0) {
                    distanceX = getScrollX();
                } else {
                }
            }
            scrollBy(-(int) distanceX, 0);
            Bitmap backgroundBitmap = getBackgroundBitmap();
            Drawable drawable = new BitmapDrawable(backgroundBitmap);
            setBackground(drawable);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.savePic();


//        canvas.restore();
    }

    private Bitmap getBackgroundBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        tempCanvas.setDrawFilter(paintFlagsDrawFilter);
        drawBackgroundTemp(tempCanvas);
        drawProgressTemp(tempCanvas);
        return bitmap;
    }

    public void intScroll() {
        int maxProgress = getMeasuredWidth() - seekBarSize;
        float x = maxProgress / 2;
        scrollBy(-(int) x, 0);
        Bitmap backgroundBitmap = getBackgroundBitmap();
        Drawable drawable = new BitmapDrawable(backgroundBitmap);
        setBackground(drawable);
    }

    private void drawBackgroundTemp(Canvas canvas) {
        mPaint.setColor(backgroundProgressColor);
        canvas.drawRect(
                seekBarSize * 1.0f / 2 * 1.0f,
                getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f - progressSize * 1.0f / 2 * 1.0f,
                getMeasuredWidth() - seekBarSize * 1.0f / 2 * 1.0f,
                getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f + progressSize * 1.0f / 2 * 1.0f,
                mPaint
        );
    }

    private void drawProgressTemp(Canvas canvas) {
        mPaint.setColor(progressColor);
        float startX = 0;
        float endX = 0;
        float startY = getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f - progressSize * 1.0f / 2 * 1.0f;
        float endY = getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f + progressSize * 1.0f / 2 * 1.0f;
        int maxProgress = getMeasuredWidth() - seekBarSize;
        progress = 0;
        if (-getScrollX() == maxProgress / 2) {

        } else if (-getScrollX() < maxProgress / 2) {
            progress = -(getScrollX() + maxProgress / 2) * 100 / (maxProgress / 2);
            startX = seekBarSize * 1.0f / 2 * 1.0f - getScrollX();
            endX = seekBarSize * 1.0f / 2 * 1.0f + maxProgress / 2;
        } else {
            progress = ((-getScrollX() - maxProgress / 2) * 100 / (maxProgress / 2));
            startX = seekBarSize * 1.0f / 2 * 1.0f + maxProgress / 2;
            endX = startX + Math.abs(-getScrollX() - maxProgress / 2);
        }
        canvas.drawRect(
                startX,
                startY,
                endX,
                endY,
                mPaint
        );
        if (onImageSaturationBarMoveListener != null) {
            int saturation = (int) ((progress + 100) * 100f / 200f);
            onImageSaturationBarMoveListener.onProgress(saturation);
        }
        textView.setText(String.valueOf(progress));
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(backgroundProgressColor);
        canvas.drawRect(
                seekBarSize * 1.0f / 2 * 1.0f + getScrollX(),
                getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f - progressSize * 1.0f / 2 * 1.0f,
                getMeasuredWidth() - seekBarSize * 1.0f / 2 * 1.0f + getScrollX(),
                getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f + progressSize * 1.0f / 2 * 1.0f,
                mPaint
        );
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(progressColor);
        float startX = 0;
        float endX = 0;
        float startY = getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f - progressSize * 1.0f / 2 * 1.0f;
        float endY = getMeasuredHeight() - seekBarSize * 1.0f / 2 * 1.0f + progressSize * 1.0f / 2 * 1.0f;
        int maxProgress = getMeasuredWidth() - seekBarSize;
        int progress = 0;
        if (-getScrollX() == maxProgress / 2) {

        } else if (-getScrollX() < maxProgress / 2) {
            startX = seekBarSize * 1.0f / 2 * 1.0f;
            endX = seekBarSize * 1.0f / 2 * 1.0f + getScrollX() + maxProgress / 2;
            progress = (int) ((startX - endX) * 100 / (maxProgress / 2));
        } else {
            startX = seekBarSize * 1.0f / 2 * 1.0f + getScrollX() + maxProgress / 2;
            endX = startX - getScrollX() - maxProgress / 2;
            progress = (int) ((endX - startX) * 100 / (maxProgress / 2));
        }
        canvas.drawRect(
                startX,
                startY,
                endX,
                endY,
                mPaint
        );
        textView.setText(String.valueOf(progress));
    }

    public interface OnImageSaturationBarMoveListener {
        void onProgress(int progress);
    }
}
