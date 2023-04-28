package com.rate.control;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
    private final static String PREF_NAME = "apero_rate_pref";
    private final static String COMPLETE_RATED = "COMPLETE_RATED";

    public static boolean getCompleteRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pre.getBoolean(COMPLETE_RATED, false);
    }

    public static void setCompleteRated(Context context, boolean isCompleteRated) {
        SharedPreferences pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pre.edit().putBoolean(COMPLETE_RATED, isCompleteRated).apply();
    }
}
