package com.rate.control;

public interface CallbackListener {
    void onMaybeLater();
    void onRating(float rating, String feedback);
}
