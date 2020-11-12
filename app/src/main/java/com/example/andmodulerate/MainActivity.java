package com.example.andmodulerate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rate.control.funtion.AppUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        AppUtils.showRateDialog(this);
    }
}