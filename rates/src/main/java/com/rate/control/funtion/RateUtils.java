package com.rate.control.funtion;

import android.content.Context;
import android.view.ViewGroup;

import com.rate.control.CallbackListener;
import com.rate.control.OnCallback;
import com.rate.control.R;
import com.rate.control.RatingScriptListener;
import com.rate.control.dialog.CustomRateAppDialog;
import com.rate.control.dialog.RateApp2Dialog;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateAppBlueDialog;
import com.rate.control.dialog.RateAppDialog;
import com.rate.control.dialog.RateAppEmojiDialog;
import com.rate.control.dialog.RateAppWithReason;
import com.rate.control.dialog.RatingScriptDialog;


public class RateUtils {
    public static void showRateDialog(final Context mContext, OnCallback callback) {
        RateAppDialog dialog = new RateAppDialog(mContext);
        dialog.setCallback(callback);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showRateBlueDialog(final Context mContext, OnCallback callback) {
        RateAppBlueDialog dialog = new RateAppBlueDialog(mContext);
        dialog.setCallback(callback);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showRate2Dialog(final Context mContext, OnCallback callback) {
        RateApp2Dialog dialog = new RateApp2Dialog(mContext);
        dialog.setCallback(callback);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showCustomRateDialog(Context context, CallbackListener callbackListener) {
        CustomRateAppDialog dialog = new CustomRateAppDialog(context, callbackListener);
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(w, h);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showRateDialogWithReason(Context context, CallbackListener callbackListener) {
        RateAppWithReason dialog = new RateAppWithReason(context, callbackListener);
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(w, h);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showRateEmojiDialog(Context context, CallbackListener callbackListener) {
        RateAppEmojiDialog dialog = new RateAppEmojiDialog(context, callbackListener);
        dialog.show();
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(w, h);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
    }

    public static void showRatingScript(Context context, RatingScriptListener listener, String appName) {
        RatingScriptDialog dialog = new RatingScriptDialog(context, appName);
        dialog.addRatingScriptListener(listener);
        dialog.show();
    }

    public static void showRatingAnime(Context context, CallbackListener callbackListener) {
        RateAppAnimeDialog dialog = new RateAppAnimeDialog(context, "en", callbackListener);
        dialog.show();
    }
}


