package com.cydeep.imageedit.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.TypedValue;

import com.cydeep.imageedit.ImageEditApplication;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by chenyu on 17/2/16.
 */

public class BitmapDecodeUtil {
    private static final int DEFAULT_DENSITY = 240;
    private static final float SCALE_FACTOR = 0.75f;
    private static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.RGB_565;

    public static BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = DEFAULT_BITMAP_CONFIG;
        options.inScaled = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            Field field = null;
            try {
                field = BitmapFactory.Options.class.getDeclaredField("inNativeAlloc");
                field.setAccessible(true);
                field.setBoolean(options, true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return options;
    }

    public static Bitmap decodeBitmap(int resId) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = getBitmapOptions();
            BitmapFactory.decodeResource(ImageEditApplication.getInstance().getResources(), resId, options);
            getAfterBitmap(options);
            String key = getKey(String.valueOf(options.outWidth), String.valueOf(options.outHeight), "file://" + resId);
            bitmap = getMemoryCacheBitmap(key);
            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = BitmapFactory.decodeResource(ImageEditApplication.getInstance().getResources(), resId, options);
                putInMemoryCache(key, bitmap);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeBitmap(String pathName) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = getBitmapOptions();
            BitmapFactory.decodeFile(pathName, options);
            getAfterBitmap(options);
            String key = getKey(String.valueOf(options.outWidth), String.valueOf(options.outHeight), pathName);
            bitmap = getMemoryCacheBitmap(key);
            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                putInMemoryCache(key, bitmap);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static String getKey(String width, String height, String path) {
        return path + "_" + "width" + width + "_" + "height" + height;
    }

    private static void putInMemoryCache(String key, Bitmap bitmap) {
        if (bitmap != null) {
            MemoryCache memoryCache = ImageLoader.getInstance().getMemoryCache();
            memoryCache.put(key, bitmap);
        }
    }

    private static Bitmap getMemoryCacheBitmap(String key) {
        MemoryCache memoryCache = ImageLoader.getInstance().getMemoryCache();
        return memoryCache.get(key);
    }

    public static void getAfterBitmap(BitmapFactory.Options options) {
        float widthRate = options.outWidth * 1.0f / 2048 * 1.0f;
        float heightRate = options.outHeight * 1.0f / 2048 * 1.0f;
//        int displayDensityDpi = ViewSizeUtil.getDensityDpi();
//        float displayDensity = ViewSizeUtil.getDensity();
        options.inSampleSize = 1;
//        if (displayDensityDpi > DEFAULT_DENSITY && displayDensity > 1.5f) {
//            int density = (int) (displayDensityDpi * 0.75f);
//            options.inDensity = density;
//            options.inTargetDensity = density;
//        }
        if (widthRate * heightRate > 1) {
            options.inSampleSize = (int) (widthRate * heightRate) + 1;
        }
        options.inJustDecodeBounds = false;
    }

    public static Bitmap decodeBitmap(Context context, InputStream is) {
        Bitmap bitmap = null;
        try {
            checkParam(context);
            checkParam(is);
            BitmapFactory.Options options = getBitmapOptions();
            BitmapFactory.decodeStream(is, null, options);
            getAfterBitmap(options);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            putInMemoryCache("is://" + bitmap.hashCode(), bitmap);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap compressBitmap(int resId, int maxWidth, int maxHeight) {
        final TypedValue value = new TypedValue();
        InputStream is = null;
        try {
            is = ImageEditApplication.getInstance().getResources().openRawResource(resId, value);
            return compressBitmap(is, maxWidth, maxHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap compressBitmap(Context context, String pathName, int maxWidth, int maxHeight) {
        checkParam(context);
        InputStream is = null;
        try {
            is = new FileInputStream(pathName);
            return compressBitmap(is, maxWidth, maxHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap compressBitmap(InputStream is, int maxWidth, int maxHeight) {
        checkParam(is);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opt);
        int height = opt.outHeight;
        int width = opt.outWidth;
        int sampleSize = computeSampleSize(width, height, maxWidth, maxHeight);
        BitmapFactory.Options options = getBitmapOptions();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeStream(is, null, options);
    }

    private static int computeSampleSize(int width, int height, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            final int heightRate = Math.round((float) height / (float) maxHeight);
            final int widthRate = Math.round((float) width / (float) maxWidth);
            inSampleSize = heightRate < widthRate ? heightRate : widthRate;
        }
        if (inSampleSize % 2 != 0) {
            inSampleSize -= 1;
        }
        return inSampleSize <= 1 ? 1 : inSampleSize;
    }

    private static <T> void checkParam(T param) {
        if (param == null)
            throw new NullPointerException();
    }
}

