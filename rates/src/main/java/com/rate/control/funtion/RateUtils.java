package com.rate.control.funtion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rate.control.OnCallback;
import com.rate.control.R;
import com.rate.control.dialog.RateApp2Dialog;
import com.rate.control.dialog.RateAppBlueDialog;
import com.rate.control.dialog.RateAppDialog;


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
}


