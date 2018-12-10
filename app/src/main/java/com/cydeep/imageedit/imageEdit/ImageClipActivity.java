package com.cydeep.imageedit.imageEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.R;
import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.photoview.PhotoViewAttacher;
import com.cydeep.imageedit.util.ViewSizeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageClipActivity extends BaseActivity {
    private PhotoViewAttacher mAttacher;
    private RelativeLayout image_container;
    private View clip_bounds_view;
    private int dp_360;
    private RectF baseRectF;
    private float baseScaleRate = 1;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void initTitle(TitleViews titleViews) {
        hideTitleBar();
    }

    public static void startImageClipActivity(BaseActivity baseActivity, int requestCode) {
        Intent intent = new Intent(baseActivity, ImageClipActivity.class);
        baseActivity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter_clip);
        getView(R.id.bottom).getLayoutParams().height = ViewSizeUtil.getCustomDimen(44f);
        imageView = (ImageView) getView(R.id.clip_image_view);
        bitmap = ImageEditActivity.currentBitmap;
        imageView.setImageBitmap(bitmap);
        mAttacher = new PhotoViewAttacher(imageView);
        mAttacher.setMediumScale(2.0f);
        mAttacher.setMaximumScale(3.0f);
        image_container = (RelativeLayout) getView(R.id.image_container);
        clip_bounds_view = getView(R.id.clip_bounds_view);
        dp_360 = ViewSizeUtil.getCustomDimen(360f);
        image_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int deltHeight = setCustomBounds();
                setMask(deltHeight);
                image_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        imageView.setTag(true);
        mAttacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {

            @Override
            public void onMatrixChanged(RectF rect) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    boolean flag = (boolean) tag;
                    baseRectF = new RectF(rect);
                    if (bitmap.getHeight() >= bitmap.getWidth()) {
                        if (flag) {
                            float bitmapRate = bitmap.getHeight() * 1.0f / bitmap.getWidth() * 1.0f;
                            float viewRate = image_container.getHeight() * 1.0f / image_container.getWidth() * 1.0f;
                            if (bitmapRate > viewRate) {//图片是否超过imageview的控件区域
                                imageView.setTag(false);
                                baseScaleRate = dp_360 * 1.0f / rect.width() * 1.0f;
                                if (baseScaleRate > 3.0f) {
                                    mAttacher.setMaximumScale(baseScaleRate + 2);
                                }
                                mAttacher.setScale(baseScaleRate);
                            } else {
                                imageView.setTag(null);
                            }
                        } else {
                            imageView.setTag(null);
                        }
                        float height = image_container.getHeight() * 1.0f > baseRectF.height() ? baseRectF.height() : image_container.getHeight() * 1.0f;
//                        mAttacher.setCustomMinScale(dp_360 * 1.0f / height);
                        mAttacher.setMinimumScale(dp_360 * 1.0f / height);
                    } else {
                        if (flag) {
                            imageView.setTag(false);
                            baseScaleRate = dp_360 * 1.0f / rect.height() * 1.0f;
                            if (baseScaleRate > 3.0f) {
                                mAttacher.setMaximumScale(baseScaleRate + 2);
                            }
                            mAttacher.setScale(baseScaleRate);
                            mAttacher.setMinimumScale(baseRectF.height() * 1.0f / dp_360 * 1.0f);
                        } else {
                            imageView.setTag(null);
                        }
                    }
                }
            }
        });
        setOnClickListener(R.id.photo_full_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap.getHeight() >= bitmap.getWidth()) {
                    if (mAttacher.getScale() < baseScaleRate) {
                        mAttacher.setScale(baseScaleRate);
                    } else {
                        float height = image_container.getHeight() * 1.0f > baseRectF.height() ? baseRectF.height() : image_container.getHeight() * 1.0f;
                        mAttacher.setScale(dp_360 * 1.0f / height);
                    }
                } else {
                    if (mAttacher.getScale() < baseScaleRate) {
                        mAttacher.setScale(baseScaleRate);
                    } else {
                        mAttacher.setScale(baseRectF.height() * 1.0f / dp_360 * 1.0f);
                    }
                }
            }
        });
        setOnClickListener(R.id.comm_reset, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAttacher.update();
//                if (bitmap.getHeight() < bitmap.getWidth()) {
//                    mAttacher.setScale(baseScaleRate);
//                }
                mAttacher.setScale(baseScaleRate);
            }
        });

        setOnClickListener(R.id.comm_done, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitDialog();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        ImageEditActivity.currentBitmap = ImageEditActivity.originalBitmap = clip();
                        ImageEditApplication.getInstance().handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hideWaitDialog();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
        setOnClickListener(R.id.comm_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setMask(int deltHeight) {
        View bottom = getView(R.id.clip_bounds_view_below);
        bottom.setBackgroundResource(R.color.black_overlay);
        View above = getView(R.id.clip_bounds_view_above);
        above.setBackgroundResource(R.color.black_overlay);
        above.getLayoutParams().height = bottom.getLayoutParams().height = deltHeight;
    }

    private int setCustomBounds() {
        int deltHeight = (image_container.getHeight() - dp_360) / 2;
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = dp_360;
        rectF.top = deltHeight;
        rectF.bottom = rectF.top + dp_360;
        mAttacher.setCustomBounds(rectF);
        return deltHeight;
    }

    private Bitmap clip() {
        RectF mClipBorderRectF = getClipBorder();
        final Drawable drawable = imageView.getDrawable();
        final float[] matrixValues = new float[9];
        Matrix displayMatrix = mAttacher.getDrawMatrix();
        displayMatrix.getValues(matrixValues);
        final float scale = matrixValues[Matrix.MSCALE_X] * drawable.getIntrinsicWidth() / bitmap.getWidth();
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        final float cropX = (-transX + mClipBorderRectF.left) / scale;
        final float cropY = (-transY + mClipBorderRectF.top) / scale;
        final float cropWidth = mClipBorderRectF.width() / scale;
        final float cropHeight = mClipBorderRectF.height() / scale;
        return  Bitmap.createBitmap(bitmap, (int) cropX, (int) cropY, (int) cropWidth, (int) cropHeight, null, false);
    }

    private RectF getClipBorder() {
        RectF mClipBorderViewRectF = new RectF(ViewSizeUtil.getViewRectInParent(clip_bounds_view, image_container));
        RectF displayRect = mAttacher.getDisplayRect();
        if (bitmap.getHeight() >= bitmap.getWidth()) {
            if (mAttacher.getScale() < baseScaleRate) {
                mClipBorderViewRectF.left = displayRect.left;
                mClipBorderViewRectF.right = displayRect.right;
            }
        } else {
            if (mAttacher.getScale() < baseScaleRate) {
                mClipBorderViewRectF.top = displayRect.top;
                mClipBorderViewRectF.bottom = displayRect.bottom;
            }
        }
        return mClipBorderViewRectF;
    }

}
