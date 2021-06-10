package com.example.andmodulerate;

import android.os.Bundle;
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
    }

    @Override
    public void onBackPressed() {
//        RateUtils.showRate2Dialog(this, new OnCallback() {
//            @Override
//            public void onMaybeLater() {
//                finish();
//            }
//
//            @Override
//            public void onSubmit(String review) {
//                Toast.makeText(MainActivity.this, review, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//            @Override
//            public void onRate() {
//                Toast.makeText(MainActivity.this, "rated", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
        RateUtils.showRateDialogWithReason(this, new CallbackListener() {
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