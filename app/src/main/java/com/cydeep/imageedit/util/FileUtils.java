package com.cydeep.imageedit.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.cydeep.imageedit.ImageEditApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenyu 15/4/25 下午1:47.
 * @description
 */
public class FileUtils {


    public static final String APP_DIR = "imageEdit";
    public static final String File_TEMP_SUFFIX = ".temp";
    public static final String File_APK_SUFFIX = ".apk";
    public static final String File_IMG_DIR = "img";
    public static final String File_IMG_SAVE = "savePic";
    public static final String File_VIDEO = "video";
    public static final String File_AUDIO = "audio";
    public static final String File_CLIP = "clip";
    public static final String File_TEMP_CLIP = "temp_clip";
    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
    public static final String PNG_FILE_SUFFIX = ".png";
    public static final String VIDEO_MP4_FILE_SUFFIX = ".mp4";
    public static final String VIDEO_FILE_SUFFIX = ".3gp";
    public static final String AUDIO_FILE_SUFFIX = ".amr";


    public static boolean isSdAailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }


    public static boolean isFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static File getDestinationPath(String versionName) {
        File file;
        file = setDestinationPath();
        File result = new File(file, (ImageEditApplication.getInstance().getPackageName() + versionName + ".apk"));
        if (!result.exists()) {
            result = new File(file, (ImageEditApplication.getInstance().getPackageName() + versionName + ".temp"));
        }

        return result;
    }

    public static File getDestinationPath() {
        return setDestinationPath();
    }


    private static File setDestinationPath() {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && Environment.getExternalStorageDirectory() != null) {
            file = new File(Environment.getExternalStorageDirectory(), APP_DIR);
        } else {
            String path = getInnerSDCard(ImageEditApplication.getInstance());
            if (getInnerSDCard(ImageEditApplication.getInstance()) != null) {
                file = new File(path, APP_DIR);
            } else {
                file = ImageEditApplication.getInstance().getFilesDir();
            }
        }
        if (file != null && !file.exists()) {
            file.mkdir();
        }
        return file;
    }


    public static boolean isFileDownloadComplete(String versionName) {
        File file = getDestinationPath(versionName);
        boolean result = false;
        if (file.getName().equals(ImageEditApplication.getInstance().getPackageName() + versionName + ".apk")) {
            result = true;
        }
        return result;
    }

    private static String getInnerSDCard(Context context) {
        String result = null;
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            for (int i = 0; i < ((String[]) invoke).length; i++) {
                result = ((String[]) invoke)[i];
                break;
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 获取存储空间
     */
    public static long getAvailableBytes(File root) {
        if (null != root) {
            StatFs stat = new StatFs(root.getPath());
            long availableBlocks = stat.getAvailableBlocks();
            return stat.getBlockSize() * availableBlocks;
        }
        return 0;
    }

    public static File getSourcePhotoFile() {
        return createImageFile();
    }

    public static String getCropPhotoFile() {
        File file = new File(setDestinationPath(), File_IMG_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + JPEG_FILE_SUFFIX;
        return path;
    }

    public static String getSaveFile(String dir, String suffix) {
        File file = new File(setDestinationPath(), dir);
        boolean mkdir = false;
        if (!file.exists()) {
            mkdir = file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis()) + suffix;
        return path;
    }

    public static String getSaveFile() {
        File file = new File(setDestinationPath(), File_IMG_SAVE);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis()) + JPEG_FILE_SUFFIX;
        return path;
    }

    public static String getSaveFile(String name) {
        File file = new File(setDestinationPath(), File_IMG_SAVE);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + name + PNG_FILE_SUFFIX;
        return path;
    }

    public static String getSaveFilePath(String name, String suffix) {
        File file = new File(setDestinationPath(), File_IMG_SAVE);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + name + suffix;
        return path;
    }

    public static String getVideoFile() {
        File file = new File(setDestinationPath(), File_VIDEO);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + VIDEO_FILE_SUFFIX;
        return path;
    }

    public static String getDir(String path) {
        File file = new File(setDestinationPath(), path);
        return file.getAbsolutePath();
    }

    public static String getAudioFile() {
        File file = new File(setDestinationPath(), File_AUDIO);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + AUDIO_FILE_SUFFIX;
        return path;
    }

    public static String getKey(String path, long size) {
        return path + "_" + size;
    }

    public static String getVideoImageFile() {
        File file = new File(setDestinationPath(), File_IMG_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + JPEG_FILE_SUFFIX;
        return path;
    }

    public static String getVideoThumbnailFile(String url) {
        File file = new File(setDestinationPath(), File_VIDEO);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + url + JPEG_FILE_SUFFIX;
        return path;
    }

    public static String getImageFile(String url) {
        File file = new File(setDestinationPath(), File_IMG_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + url + JPEG_FILE_SUFFIX;
        return path;
    }

    public static String getGifFile(String url) {
        File file = new File(setDestinationPath(), File_IMG_SAVE);
        if (!file.exists()) {
            file.mkdir();
        }
        if (url.lastIndexOf("/") != -1) {
            url = url.substring(url.lastIndexOf("/") + 1);
        }
        String path = file.getAbsolutePath() + "/" + url;
        return path;
    }

    public static String getMp4VideoImageFile(String url) {
        File file = new File(setDestinationPath(), File_VIDEO);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath() + "/" + url + VIDEO_MP4_FILE_SUFFIX;
        return path;
    }

    public static String saveFile(File sourcePath) throws FileNotFoundException {
        if (!sourcePath.exists()) {
            throw new FileNotFoundException("file can not found");
        }
        String saveFile = FileUtils.getSaveFile();
        FileInputStream fileInputStream = new FileInputStream(sourcePath);
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        byte[] bytes = new byte[4096];
        int length;
        try {
            while ((length = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return saveFile;
    }


    public static File getCopyFile(File sourcePath) throws FileNotFoundException {
        if (!sourcePath.exists()) {
            throw new FileNotFoundException("file can not found");
        }
        String saveFile = FileUtils.getGifFile(sourcePath.getAbsolutePath());
        FileInputStream fileInputStream = new FileInputStream(sourcePath);
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        byte[] bytes = new byte[4096];
        int length;
        try {
            while ((length = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new File(saveFile);
    }


    private static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = null;
        try {
            imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageF;
    }

    public static File getAlbumDir() {
        File storageDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getAlbumStorageDir(File_IMG_DIR);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            String path = getInnerSDCard(ImageEditApplication.getInstance());
            if (getInnerSDCard(ImageEditApplication.getInstance()) != null) {
                storageDir = new File(path, File_IMG_DIR);
            } else {
                storageDir = ImageEditApplication.getInstance().getFilesDir();
            }
        }
        return storageDir;
    }

    public static File getAlbumStorageDir(String albumName) {
        return new File(setDestinationPath(), albumName);
    }


    public static long getFolderSize(File var0) {
        long var1 = 0L;

        try {
            File[] var3 = var0.listFiles();

            for (int var4 = 0; var4 < var3.length; ++var4) {
                if (var3[var4].isDirectory()) {
                    var1 += getFolderSize(var3[var4]);
                } else {
                    var1 += var3[var4].length();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return var1;
    }

    public static void deleteFolderFile(String var1, boolean isDelete) {
        if (!TextUtils.isEmpty(var1)) {
            try {
                File file = new File(var1);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();

                    for (int i = 0; i < files.length; ++i) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }

                if (isDelete) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

    }


    public static String getFormatSize(long length) {
        String result = Formatter.formatFileSize(ImageEditApplication.getInstance(), length);
        if (result.contains(".00")) {
            result = result.replace(".00", "");
        } else if (result.endsWith("KB") && result.indexOf(".") != -1 && result.indexOf(" KB") - result.indexOf(".") > 1) {
            String kb = result.substring(0, result.indexOf(" KB"));
            result = new DecimalFormat("0.0").format(Double.valueOf(kb)) + " KB";
            if (result.endsWith(".0 KB")) {
                result = result.substring(0,result.indexOf(".0 KB")) + " KB";
            }
        } else if (result.endsWith("0 MB")) {
            result = result.substring(0,result.indexOf("0 MB")) + " MB";
        }
        return result;
    }


    public static enum FileType {

        /**
         * JEPG.
         */
        JPEG("FFD8FF"),

        /**
         * PNG.
         */
        PNG("89504E47"),

        /**
         * GIF.
         */
        GIF("47494638"),

        /**
         * MP4.
         */
        MP4("00000018667479706"),

        /**
         * TIFF.
         */
        TIFF("49492A00"),

        /**
         * Windows Bitmap.
         */
        BMP("424D"),

        /**
         * CAD.
         */
        DWG("41433130"),

        /**
         * Adobe Photoshop.
         */
        PSD("38425053"),

        /**
         * Rich Text Format.
         */
        RTF("7B5C727466"),

        /**
         * XML.
         */
        XML("3C3F786D6C"),

        /**
         * HTML.
         */
        HTML("68746D6C3E"),

        /**
         * Email [thorough only].
         */
        EML("44656C69766572792D646174653A"),

        /**
         * Outlook Express.
         */
        DBX("CFAD12FEC5FD746F"),

        /**
         * Outlook (pst).
         */
        PST("2142444E"),

        /**
         * MS Word/Excel.
         */
        XLS_DOC("D0CF11E0"),

        /**
         * MS Access.
         */
        MDB("5374616E64617264204A"),

        /**
         * WordPerfect.
         */
        WPD("FF575043"),

        /**
         * Postscript.
         */
        EPS("252150532D41646F6265"),

        /**
         * Adobe Acrobat.
         */
        PDF("255044462D312E"),

        /**
         * Quicken.
         */
        QDF("AC9EBD8F"),

        /**
         * Windows Password.
         */
        PWL("E3828596"),

        /**
         * ZIP Archive.
         */
        ZIP("504B0304"),

        /**
         * RAR Archive.
         */
        RAR("52617221"),

        /**
         * Wave.
         */
        WAV("57415645"),

        /**
         * AVI.
         */
        AVI("41564920"),

        /**
         * Real Audio.
         */
        RAM("2E7261FD"),

        /**
         * Real Media.
         */
        RM("2E524D46"),

        /**
         * MPEG (mpg).
         */
        MPG("000001BA"),

        /**
         * Quicktime.
         */
        MOV("6D6F6F76"),

        /**
         * Windows Media.
         */
        ASF("3026B2758E66CF11"),

        /**
         * MIDI.
         */
        MID("4D546864");

        private String value = "";

        /**
         * Constructor.
         *
         * @param value
         */
        private FileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    /**
     * Constructor
     */

    /**
     * 将文件头转换成16进制字符串
     *
     * @param src
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     *
     * @param filePath 文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(String filePath) throws IOException {

        byte[] b = new byte[28];

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static FileType getType(String filePath) throws IOException {

        String fileHead = getFileContent(filePath);

        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }

        fileHead = fileHead.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }

        return null;
    }

}
