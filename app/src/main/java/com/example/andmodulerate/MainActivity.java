package com.example.andmodulerate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rate.control.CallbackListener;
import com.rate.control.dialog.CustomRateAppDialog;
import com.rate.control.dialog.RateApp2Dialog;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateAppBlueDialog;
import com.rate.control.dialog.RateAppDialog;
import com.rate.control.dialog.RateAppEmojiDialog;
import com.rate.control.dialog.RateAppWithReason;
import com.rate.control.dialog.RateFeedbackDialog;
import com.rate.control.dialog.RatingScriptDialog;
import com.rate.control.dialog.rate_smile.FeedbackActivity;
import com.rate.control.dialog.rate_smile.RateCallBack;
import com.rate.control.dialog.rate_smile.RateSmileDialog;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class MainActivity extends AppCompatActivity {
    private RateSmileDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnRateAppAnimeDialog).setOnClickListener(v -> new RateAppAnimeDialog(MainActivity.this, "en", 3, new CallbackListener() {
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

        findViewById(R.id.btnCustomRateAppDialog).setOnClickListener(v -> new CustomRateAppDialog(MainActivity.this,  new CallbackListener() {
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

        findViewById(R.id.btnRateApp2Dialog).setOnClickListener(v -> new RateApp2Dialog(MainActivity.this).show());

        findViewById(R.id.btnRateAppBlueDialog).setOnClickListener(v -> new RateAppBlueDialog(MainActivity.this).show());

        findViewById(R.id.btnRateAppDialog).setOnClickListener(v -> new RateAppDialog(MainActivity.this).show());

        findViewById(R.id.btnRateAppEmojiDialog).setOnClickListener(v -> new RateAppEmojiDialog(MainActivity.this, new CallbackListener() {
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

        findViewById(R.id.btnRateAppWithReason).setOnClickListener(v -> new RateAppWithReason(MainActivity.this, new CallbackListener() {
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

        findViewById(R.id.btnRateFeedbackDialog).setOnClickListener(v -> new RateFeedbackDialog(
                MainActivity.this,
                "en",
                (Function0) () -> Unit.INSTANCE).show());

        findViewById(R.id.btnRatingScriptDialog).setOnClickListener(v -> new RatingScriptDialog(MainActivity.this, getString(R.string.app_name)).show());

        checkPermission();
    }

    ActivityResultLauncher<Intent> feedbackLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && intent != null) {
                    ArrayList<String> listOption = intent.getStringArrayListExtra(FeedbackActivity.LIST_OPTION);
                    ArrayList<String> listImage = intent.getStringArrayListExtra(FeedbackActivity.LIST_IMAGE);
                    String textFeedback = intent.getStringExtra(FeedbackActivity.TEXT_FEEDBACK);
                    sendEmail(listOption, listImage, textFeedback);
                } else {
                    Toast.makeText(this, "Close Feedback", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void sendEmail(ArrayList<String> options, ArrayList<String> images, String feedback) {
        StringBuilder text = new StringBuilder();
        for (String tag : options) {
            text.append(" ").append(tag);
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"test.pro.apero@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "App name + Feedback");
        if (images != null && !images.isEmpty()) {
            ArrayList<Uri> list = new ArrayList<>();
            for (String path: images) {
                list.add(Uri.parse(path));
            }
            i.putExtra(Intent.EXTRA_STREAM, list);
        }
        i.putExtra(Intent.EXTRA_TEXT, feedback);
        i.setType("*/*");
        i.setAction(Intent.ACTION_SEND_MULTIPLE);
        startActivity(Intent.createChooser(i, "Share via"));
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new RateSmileDialog(
                this,
                new RateCallBack() {
                    @Override
                    public void onClose() {
                        finish();
                    }

                    @Override
                    public void onRating(float rating) {
                        if (rating >= 4) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating, Toast.LENGTH_SHORT).show());
                        } else {
                            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                            feedbackLauncher.launch(intent);
                        }
                    }
                },
                4f
        ).show();
    }
}
