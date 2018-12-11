package com.cydeep.imageedit.imageEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.R;
import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.util.FileUtils;
import com.cydeep.imageedit.util.ImageUtil;
import com.cydeep.imageedit.util.ViewSizeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by chenyu on 17/2/9.
 */

public class ImageSaturationActivity extends BaseActivity {

    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageView mGPUImageView;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void initTitle(TitleViews titleViews) {
        titleViews.custom_title.setBackgroundResource(R.color.white);
        setStatusBarColorRes(R.color.white);
        titleViews.center_container_title_text.setText(R.string.post_image_saturation);
        titleViews.center_container_title_text.setTextColor(0xff645e66);
        titleViews.title_divider.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_image_saturation);
        ViewGroup.LayoutParams layoutParams = getView(R.id.image_container).getLayoutParams();
        layoutParams.height = layoutParams.width = ViewSizeUtil.getCustomDimen(360f);
        RelativeLayout.LayoutParams seekBarLayoutParams = (RelativeLayout.LayoutParams) getView(R.id.imageSaturationSeekBar).getLayoutParams();
        seekBarLayoutParams.width = ViewSizeUtil.getCustomDimen(314f);
        seekBarLayoutParams.height = ViewSizeUtil.getCustomDimen(51f);
        seekBarLayoutParams.setMargins(ViewSizeUtil.getCustomDimen(23f),0,0,0);
        GPUImageFilter filter = GPUImageFilterTools.createFilterForType(ImageEditApplication.getInstance(), GPUImageFilterTools.FilterType.SATURATION);
        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
        final ImageSaturationSeekBar imageSaturationSeekBar = (ImageSaturationSeekBar) getView(R.id.imageSaturationSeekBar);
        imageSaturationSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageSaturationSeekBar.intScroll();
                imageSaturationSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        mGPUImageView = getView(R.id.post_image);
//        final Bitmap bitmap = ImageEditActivity.currentBitmap;
        final Bitmap bitmap = ImageUtil.getSuitBitmap(url);
        mGPUImageView.setImage(bitmap);
        mGPUImageView.setFilter(filter);
        setImageVieSize(mGPUImageView, bitmap.getWidth(), bitmap.getHeight());

        imageSaturationSeekBar.setOnImageSaturationBarMoveListener(new ImageSaturationSeekBar.OnImageSaturationBarMoveListener() {
            @Override
            public void onProgress(final int progress) {
                mFilterAdjuster.adjust(progress);
                mGPUImageView.requestRender();
            }
        });
        setOnClickListener(R.id.activity_image_saturation_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setOnClickListener(R.id.activity_image_saturation_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitDialog();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String saveClip = ImageUtil.saveClip(mGPUImageView.capture(bitmap.getWidth(), bitmap.getHeight()), FileUtils.File_TEMP_CLIP);
//                            ImageEditActivity.currentBitmap = ImageEditActivity.originalBitmap = mGPUImageView.capture(ImageEditActivity.currentBitmap.getWidth(), ImageEditActivity.currentBitmap.getHeight());
                            ImageEditApplication.getInstance().handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("url",saveClip);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    hideWaitDialog();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public static void startImageSaturationActivity(BaseActivity context,String url, int requestCode) {
        Intent intent = new Intent(context, ImageSaturationActivity.class);
        intent.putExtra("url",url);
        context.startActivityForResult(intent, requestCode);
    }

    private void setImageVieSize(View view, int width, int height) {
        int fixSize = ViewSizeUtil.getCustomDimen(360f);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width == height) {
            width = height = fixSize;
        } else {
            if (width > height) {
                height = height * fixSize / width;
                width = fixSize;
            } else {
                width = width * fixSize / height;
                height = fixSize;
            }
        }
        layoutParams.height = height;
        layoutParams.width = width;
    }
}
