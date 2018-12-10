package com.cydeep.imageedit.imageEdit;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.activity.BaseActivity;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.concurrent.ExecutorService;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by chenyu on 17/2/8.
 */

public class ImageFilterHandler {

    public static void setFilterBitmap(ExecutorService executorService, GPUImageFilter filter, final String key, Bitmap bitmap, final ImageView imageView) {
        GPUImage.getBitmapForFilter(executorService, bitmap,filter, new GPUImage.ResponseListener<Bitmap>() {
            @Override
            public void response(final Bitmap item) {
                MemoryCache mMCache = ImageLoader.getInstance().getMemoryCache();
                mMCache.put(key,item);
                String tag = (String) imageView.getTag();
                if (tag.equals(key)) {
                    ImageEditApplication.getInstance().handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(item);
                        }
                    });
                }
            }
        });
    }

    public static void setFilterBitmap(ExecutorService executorService, final BaseActivity baseActivity, GPUImageFilter filter, Bitmap bitmap, final ImageView imageView) {
        baseActivity.showWaitDialog();
        GPUImage.getBitmapForFilter(executorService, bitmap, filter, new GPUImage.ResponseListener<Bitmap>() {
            @Override
            public void response(final Bitmap item) {
                ImageEditApplication.getInstance().handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseActivity.hideWaitDialog();
                        imageView.setImageBitmap(item);
                    }
                });
            }
        });
    }
}
