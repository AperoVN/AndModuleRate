package com.example.andmodulerate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rate.control.CallbackListener;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateFeedbackDialog;
import com.rate.control.dialog.rate_smile.RateCallBack;
import com.rate.control.dialog.rate_smile.RateSmileDialog;

import java.util.ArrayList;
import java.util.List;

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
        initRateSmile();
        checkPermission();
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

    private void initRateSmile() {
        dialog = new RateSmileDialog(
                this,
                new RateCallBack() {
                    @Override
                    public void onFeedback(float rating, @NonNull String feedback, @NonNull List<String> tags) {
                        StringBuilder text = new StringBuilder();
                        for (String tag : tags) {
                            text.append(" ").append(tag);
                        }
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " tag: " + text + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onMaybeLater() {
                        finish();
                    }

                    @Override
                    public void onRating(float rating, String feedback) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
                    }
                },
                4f,
                true,
                () -> {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent, 100);
                    return null;
                }
        );
    }

    @Override
    public void onBackPressed() {
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                ArrayList<String> list = new ArrayList<>();
                list.add(getRealPathFromURI(uri));
                dialog.addMedia(list);
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri contentURI) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            // cursor.close();
            return cursor.getString(column_index);
        }
        // cursor.close();
        return null;
    }
}
