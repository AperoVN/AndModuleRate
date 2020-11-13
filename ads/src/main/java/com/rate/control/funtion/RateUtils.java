package com.rate.control.funtion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rate.control.OnCallback;
import com.rate.control.R;
import com.rate.control.dialog.RateAppDialog;


public class RateUtils {
    public static void showRateDialog(final Context mContext, OnCallback callback) {
        RateAppDialog dialog = new RateAppDialog(mContext);
        dialog.setCallback(callback);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }
}


