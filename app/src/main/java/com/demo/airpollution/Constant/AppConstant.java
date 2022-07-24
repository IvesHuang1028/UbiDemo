package com.demo.airpollution.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


import com.demo.airpollution.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Project : dir demo
 * Class : AppConstant
 * Create by Ives 2022
 */

// 此處為全域變數宣告地方
public class AppConstant {
    // 解析度參數
    // Layout 一律單位用PX
    // 新解析度算法 ex 純 code 1280 * heightScale * density
    //                     xml getWidth() * heightScale
    public static boolean isAutoScale = false;   //自動縮放開關
    public static float ori_Width = 800;
    public static float ort_Height = 1280;
    public static float widthScale;
    public static float heightScale;
    public static float density;
    public static float textScale; // 字體放大倍率


    //使用者、設定資料使用
    public static void setProperty(Context context, String key) {
        try {
            Field f = AppConstant.class.getField(key);
            Class<?> t = f.getType();
            SharedPreferences settings = context.getSharedPreferences("air.AppProperty", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            if (t == String.class) {
                editor.putString(key, (String) f.get(null));
            } else if (t == boolean.class) {
                editor.putBoolean(key, f.getBoolean(null));
            } else if (t == double.class) {
                editor = putDouble(editor, key, f.getDouble(null));
            } else if (t == float.class) {
                editor.putFloat(key, f.getFloat(null));
            } else if (t == int.class) {
                editor.putInt(key, f.getInt(null));
            } else if (t == long.class) {
                editor.putLong(key, f.getLong(null));
            } else {
                throw new Exception("no such type of setProperty overloading");
            }
            editor.apply();
        } catch (Exception e) {
            MyLog.e("Property", e.getMessage());
            Toast.makeText(context, "setProperty fatal error", Toast.LENGTH_SHORT);
        }
    }

    private static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
