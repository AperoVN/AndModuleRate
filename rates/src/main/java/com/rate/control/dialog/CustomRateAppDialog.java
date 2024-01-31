package com.rate.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.rate.control.CallbackListener;
import com.rate.control.R;
import com.ymb.ratingbar_lib.RatingBar;


public class CustomRateAppDialog extends Dialog {
    Context mContext;
    private Thread th;
    CallbackListener callbackListener;
    EditText edtFeedback;
    TextView txtTitle;
    TextView tv_submit;
    private TextView btnTooAds, btnNotWorking, btnOther;
    private LinearLayout layoutFeedback, layoutActions;
    float ratting = 1f;
    private String feedback = "";

    public CustomRateAppDialog(Context context, CallbackListener callbackListener) {
        super(context);
        mContext = context;
        this.callbackListener = callbackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_custom_rating);
        initView();
    }

    private void initView() {
        setCancelable(false);
        RatingBar rating = findViewById(R.id.rating);
        edtFeedback = findViewById(R.id.edtFeedback);
        txtTitle = findViewById(R.id.txtTitle);
        tv_submit = findViewById(R.id.tv_submit);
        btnTooAds = findViewById(R.id.btn_too_ads);
        btnNotWorking = findViewById(R.id.btn_not_working);
        btnOther = findViewById(R.id.btn_other);
        layoutFeedback = findViewById(R.id.layout_feedback);
        layoutActions = findViewById(R.id.layout_actions);

        txtTitle.setSelected(true);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, getContext().getString(R.string.thanks_feedback), Toast.LENGTH_SHORT).show();
                dismiss();
                if (feedback.trim().length() == 0) {
                    feedback = edtFeedback.getText().toString();
                }
                callbackListener.onRating(ratting, feedback);
            }
        });
        findViewById(R.id.ln_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callbackListener.onMaybeLater();
            }
        });
        rating.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChange(final float v, final float v1) {
                if (th != null) {
                    th.interrupt();
                }
                th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                            Log.e("Ratingbar", "v:" + v);
                            if (v1 == 0.0f)
                                return;
                            if (v1 <= 4.0) {
                                layoutFeedback.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.ln_later).setVisibility(View.GONE);
                                        layoutFeedback.setVisibility(View.VISIBLE);
                                    }
                                });
                                layoutActions.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutActions.setVisibility(v1 == 4 ? View.GONE : View.VISIBLE);
                                    }
                                });
                                if (v1 == 4) {
                                    edtFeedback.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtFeedback.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                                ratting = v1;
                                return;
                            }
                            dismiss();
                            callbackListener.onRating(v1, "");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
        btnTooAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption(1);
                edtFeedback.setVisibility(View.GONE);
                feedback = "Too many ads";
            }
        });
        btnNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption(2);
                edtFeedback.setVisibility(View.GONE);
                feedback = "Not working";
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption(3);
                edtFeedback.setVisibility(View.VISIBLE);
                feedback = "";
            }
        });
    }

    private void selectOption(int option) {
        btnTooAds.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 1 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));
        btnNotWorking.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 2 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));
        btnOther.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 3 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));

        btnTooAds.setTextColor(getContext().getResources().getColor(option == 1 ? R.color.colorWhite : R.color.colorText));
        btnNotWorking.setTextColor(getContext().getResources().getColor(option == 2 ? R.color.colorWhite : R.color.colorText));
        btnOther.setTextColor(getContext().getResources().getColor(option == 3 ? R.color.colorWhite : R.color.colorText));
    }
}
