package com.cydeep.imageedit.util;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.cydeep.imageedit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static String saveClip(Bitmap photo, String path) {
        String fileName = FileUtils.getSaveFile(path, FileUtils.PNG_FILE_SUFFIX);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName, false));
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getSuitBitmap(String filePath) {
        Bitmap bitmap;
        int degree = ImageUtil.readPictureDegree(filePath);
        if (filePath.startsWith("file://")) {
            filePath = filePath.substring(7);
        }
        bitmap = BitmapDecodeUtil.decodeBitmap(filePath);
        if (degree != 0) {
            bitmap = ImageUtil.rotateBitmap(bitmap, degree);
        }
        return bitmap;
    }

    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .showImageOnLoading(R.drawable.pic_normal)
                .showImageForEmptyUri(R.drawable.pic_normal).showImageOnFail(R.drawable.pic_normal)
                .imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true).build();// 构建完成
        return options;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return degree;
        }
    }

    public static ExifInterface getExif(String imgPath) {
        ExifInterface exif = null;
        if (imgPath != null) {
            if (imgPath.startsWith("http://")) {
                File file = ImageLoader.getInstance().getDiskCache().get(imgPath);
                if (file != null) {
                    String path = file.getAbsolutePath();
                    if (!path.isEmpty()) {
                        imgPath = path;
                    }
                }
            }
            if (imgPath.startsWith("file://")) {
                imgPath = imgPath.substring(6);
            }
            try {
                exif = new ExifInterface(imgPath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }

        }

        return exif;
    }

}
