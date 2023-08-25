package com.example.andmodulerate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.rate.control.CallbackListener;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateFeedbackDialog;
import com.rate.control.dialog.rate_smile.FeedbackActivity;
import com.rate.control.dialog.rate_smile.RateCallBack;
import com.rate.control.dialog.rate_smile.RateSmileDialog;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RateSmileDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.forceShowRate).setOnClickListener(v -> new RateAppAnimeDialog(MainActivity.this, "en", 3, new CallbackListener() {
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
