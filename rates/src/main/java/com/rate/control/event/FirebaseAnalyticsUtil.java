package com.rate.control.event;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsUtil {
    public static void logClickAdsEvent(Context context, String name) {
        FirebaseAnalytics.getInstance(context).logEvent(name, null);
    }
}
