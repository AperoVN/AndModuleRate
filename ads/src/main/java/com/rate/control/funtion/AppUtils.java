package com.rate.control.funtion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rate.control.R;
import com.rate.control.dialog.RateAppDialog;


public class AppUtils {
    public static void showRateDialog(final Context mContext) {
        RateAppDialog dialog = new RateAppDialog(mContext);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void rateApp(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
    }

}


