package com.cydeep.imageedit;


import android.app.Application;
import android.os.Environment;
import android.os.Handler;

import com.cydeep.imageedit.util.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;


public class ImageEditApplication extends Application {

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

        }
    };

    private static ImageEditApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCache(new UnlimitedDiskCache(new File(Environment.getExternalStorageDirectory(), FileUtils.APP_DIR + "/" + FileUtils.File_IMG_DIR))) // 自定义缓存路径,自定义图片路径,不能现在disk存储大小
                .diskCacheSize(50 * 1024 * 1024)
//                        .threadPoolSize(2)
                .memoryCache(new FIFOLimitedMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);

    }
    public static ImageEditApplication getInstance() {
        return sInstance;
    }
}
