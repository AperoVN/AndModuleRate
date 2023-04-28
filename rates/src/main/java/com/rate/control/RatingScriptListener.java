package com.rate.control;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;

public interface RatingScriptListener {
    void onComplete(@NonNull Task<ReviewInfo> task);

    void onResultRated(int ratedLevel);

    void onExit();
}
