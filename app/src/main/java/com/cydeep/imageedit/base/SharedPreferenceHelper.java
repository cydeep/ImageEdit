package com.cydeep.imageedit.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.cydeep.imageedit.ImageEditApplication;

public class SharedPreferenceHelper {

    private static final String SP_FILE_NAME = "blemobi";
    private static SharedPreferenceHelper sharedPrefHelper = null;
    private static SharedPreferences sharedPreferences;


    public static synchronized SharedPreferenceHelper getInstance() {
        if (null == sharedPrefHelper) {
            sharedPrefHelper = new SharedPreferenceHelper();
        }
        return sharedPrefHelper;
    }

    private SharedPreferenceHelper() {
        sharedPreferences = ImageEditApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putImageFilters(String uuid, String imageFilters) {
        sharedPreferences.edit().putString(uuid + "imageFilters", imageFilters).commit();
    }

    public String getImageFilters(String uuid) {
        return sharedPreferences.getString(uuid + "imageFilters", "");
    }

}
