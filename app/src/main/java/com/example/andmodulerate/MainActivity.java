package com.example.andmodulerate;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rate.control.CallbackListener;
import com.rate.control.OnCallback;
import com.rate.control.funtion.RateUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.forceShowRate).setOnClickListener(v -> RateUtils.showRateEmojiDialog(MainActivity.this, new CallbackListener() {
            @Override
            public void onMaybeLater() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Maybe later", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onRating(float rating, String feedback) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
            }
        }));
    }

    @Override
    public void onBackPressed() {
        RateUtils.showRateEmojiDialog(this, new CallbackListener() {
            @Override
            public void onMaybeLater() {
                finish();
            }

            @Override
            public void onRating(float rating, String feedback) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "rate: " + rating + " feedback: " + feedback, Toast.LENGTH_SHORT).show());
            }
        });
    }
}