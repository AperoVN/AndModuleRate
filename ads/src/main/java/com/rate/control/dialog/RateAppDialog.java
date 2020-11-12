package com.rate.control.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.rate.control.R;
import com.rate.control.funtion.AppUtils;
import com.rate.control.funtion.SharedPreferencesHelper;
import com.ymb.ratingbar_lib.RatingBar;


public class RateAppDialog extends Dialog {
    Context mContext;
    private Thread th;

    public RateAppDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_rate);
        initView();
    }

    private void initView() {
        setCancelable(false);
        RatingBar rating = findViewById(R.id.rating);
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Thank for your feedback!", Toast.LENGTH_SHORT).show();
                dismiss();
                ((Activity) mContext).finish();
            }
        });
        findViewById(R.id.ln_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((Activity) mContext).finish();
            }
        });
        rating.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChange(final float v, float v1) {
                if (th != null) {
                    th.interrupt();
                }
                th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            SharedPreferencesHelper.setRated(mContext, true);
                            if (v < 4.0) {
                                findViewById(R.id.ln_feedback).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ln_feedback).setVisibility(View.VISIBLE);
                                        findViewById(R.id.ln_later).setVisibility(View.GONE);
                                    }
                                });
                                return;
                            }
                            AppUtils.rateApp(mContext);
                            dismiss();
                            ((Activity) mContext).finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });

    }
}
