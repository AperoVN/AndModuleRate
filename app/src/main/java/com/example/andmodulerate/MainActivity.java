package com.example.andmodulerate;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rate.control.CallbackListener;
import com.rate.control.OnCallback;
import com.rate.control.RatingScriptListener;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateFeedbackDialog;
import com.rate.control.dialog.RatingScriptDialog;
import com.rate.control.funtion.RateUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.forceShowRate).setOnClickListener(v -> new RateAppAnimeDialog(MainActivity.this, "en", new CallbackListener() {
            @Override
            public void onMaybeLater() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Maybe later", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onRating(float rating, String feedback) {
                new RateFeedbackDialog(MainActivity.this, " es", () -> {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
                    return null;
                }).show();
            }
        }).show());
    }

    @Override
    public void onBackPressed() {
//        RateUtils.showRateEmojiDialog(this, new CallbackListener() {
//            @Override
//            public void onMaybeLater() {
//                finish();
//            }
//
//            @Override
//            public void onRating(float rating, String feedback) {
//                runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
//            }
//        });

        RateUtils.showRatingScript(
                this,
                new RatingScriptListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<com.google.android.play.core.review.ReviewInfo> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), getString(R.string.thanks_feedback), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Rate error", Toast.LENGTH_LONG).show();
                        }
                        MainActivity.super.onBackPressed();
                    }

                    @Override
                    public void onResultRated(int ratedLevel) {
                        Toast.makeText(getBaseContext(), "Rated " + ratedLevel + " star", Toast.LENGTH_LONG).show();
                        MainActivity.super.onBackPressed();
                    }

                    @Override
                    public void onExit() {
                        MainActivity.super.onBackPressed();
                    }
                },
                getString(R.string.app_name)
        );
    }
}