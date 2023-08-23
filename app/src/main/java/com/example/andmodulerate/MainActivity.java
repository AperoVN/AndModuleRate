package com.example.andmodulerate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.rate.control.CallbackListener;
import com.rate.control.dialog.RateAppAnimeDialog;
import com.rate.control.dialog.RateFeedbackDialog;
import com.rate.control.dialog.rate_smile.RateCallBack;
import com.rate.control.dialog.rate_smile.RateSmileDialog;

import java.io.File;
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
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
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
        dialog = new RateSmileDialog(
                this,
                new RateCallBack() {
                    @Override
                    public void onFeedback(float rating, @NonNull String feedback, @NonNull List<String> options, @Nullable List<String> images) {
                        StringBuilder text = new StringBuilder();
                        for (String tag : options) {
                            text.append(" ").append(tag);
                        }
                        int size = 0;
                        if (images != null) {
                            size = images.size();
                        }
                        int finalSize = size;
                        runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "rate: " + rating + " tag: " + text + " image size: " + finalSize + " feedback: " + feedback,
                                Toast.LENGTH_SHORT).show()
                        );
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"test.pro.apero@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT,"App name + Feedback");
                        if (images != null) {
                            Uri uri = FileProvider.getUriForFile(
                                    MainActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    new File(images.get(0))
                            );
                            i.putExtra(Intent.EXTRA_STREAM, uri);
                        }
                        i.setType("image/*");
                        startActivity(Intent.createChooser(i,"Share via"));
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
                },
                null
        );
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
