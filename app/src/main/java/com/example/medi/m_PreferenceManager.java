package com.example.medi;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 데이터 저장 및 로드 클래스
 */

public class m_PreferenceManager {
    public static final String PREFERENCES_NAME = "rebuild_preference";
    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * requestCode 저장
     * @param context
     * @param key
     * @param value
     */
    public static void setRequestCode(Context context, String key, Integer value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * requestCode 값 로드
     * @param context
     * @param key
     * @return
     */
    public static Integer getRequestCode(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Integer response = prefs.getInt(key, -1);
        if (response.equals(-1))
            return -1;
        return response;
    }


    /**
     * SimpleDateFormat 저장
     * @param context
     * @param key
     * @param value
     */
    public static void setSDF(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }


    /**
     * SimpleDateFormat 값 로드
     * @param context
     * @param key
     * @return
     */
    public static String getSDF(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Gson gson = new Gson();
        String response = prefs.getString(key, DEFAULT_VALUE_STRING);
        if (response.equals(DEFAULT_VALUE_STRING))
            return "";
        Type type = new TypeToken<String>(){}.getType();
        String value = gson.fromJson(response, type);
        return value;
    }


    /**
     * StringArrayList 저장
     * @param context
     * @param key
     * @param values
     */
    public static void setStringArrayList(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(values);
        editor.putString(key,json);
        editor.commit();
    }


    /**
     * StringArrayList 값 로드
     * @param context
     * @param key
     * @return
     */

    public static ArrayList<String> getStringArrayList(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Gson gson = new Gson();
        String response = prefs.getString(key, DEFAULT_VALUE_STRING);
        if (response.equals(DEFAULT_VALUE_STRING))
            return new ArrayList<String>();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> value = gson.fromJson(response, type);
        return value;
    }


    /**
     * BoolArrayList 값 저장
     * @param context
     * @param key
     * @param values
     */

    public static void setBoolArrayList(Context context, String key, ArrayList<Boolean> values) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(values);
        editor.putString(key,json);
        editor.commit();
    }


    /**
     * BoolArrayList 값 로드
     * @param context
     * @param key
     * @return
     */

    public static ArrayList<Boolean> getBoolArrayList(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Gson gson = new Gson();
        String response = prefs.getString(key, DEFAULT_VALUE_STRING);
        if (response.equals(DEFAULT_VALUE_STRING))
            return new ArrayList<Boolean>();
        Type type = new TypeToken<ArrayList<Boolean>>(){}.getType();
        ArrayList<Boolean> value = gson.fromJson(response, type);
        return value;
    }

    /**
     * ItemList 값 저장
     * @param context
     * @param key
     * @param value
     */

    public static void setItemList(Context context, String key, ArrayList<ListViewItem> value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }


    /**
     * ItemList 값 로드
     * @param context
     * @param key
     * @return
     */

    public static ArrayList<ListViewItem> getItemList(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Gson gson = new Gson();
        String response = prefs.getString(key, DEFAULT_VALUE_STRING);
        if (response.equals(DEFAULT_VALUE_STRING))
            return new ArrayList<ListViewItem>();
        Type type = new TypeToken<ArrayList<ListViewItem>>(){}.getType();
        ArrayList<ListViewItem> value = gson.fromJson(response, type);
        return value;
    }


    /**
     * 키 값 삭제
     * @param context
     * @param key
     */

    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }


    /**
     * 모든 저장 데이터 삭제
     * @param context
     */

    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

}
