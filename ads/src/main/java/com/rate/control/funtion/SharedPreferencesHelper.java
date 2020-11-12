package com.rate.control.funtion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesHelper {
    private static final String FILE_SETTING = "setting.pref";
    private static final String IS_PURCHASE = "IS_PURCHASE";
    private static final String IS_RATE = "IS_RATE";
    private static final String kEY_COUNT = "COUNT";

    public static void setPurchased(Activity activity, boolean isPurcharsed) {
        SharedPreferences pref = activity.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_PURCHASE, isPurcharsed);
        editor.apply();
    }

    public static boolean isPurchased(Activity activity) {
        return activity.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).getBoolean(IS_PURCHASE, false);
    }

    public static void setRated(Context context, boolean isRate) {
        SharedPreferences pref = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_RATE, isRate);
        editor.apply();
    }

    private static int getCountBack(Context context) {
        SharedPreferences pref = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        return pref.getInt(kEY_COUNT, 1);
    }

    private static void setCountBack(Context context, int count) {
        SharedPreferences pref = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(kEY_COUNT, count);
        editor.apply();
    }

    public static void increaseCount(Context context) {
        int count = getCountBack(context);
        count++;
        setCountBack(context, count);
    }

    public static boolean isRated(Context context) {
        int count = getCountBack(context);
        if (count == 3 || count == 5 || count == 7) {
            return context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).getBoolean(IS_RATE, false);
        }
        return true;
    }

}
