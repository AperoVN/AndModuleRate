package com.rate.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.rate.control.CallbackListener;
import com.rate.control.R;
import com.ymb.ratingbar_lib.RatingBar;


public class RateAppEmojiDialog extends Dialog {
    Context mContext;
    private Thread th;
    CallbackListener callbackListener;
    TextView txtRate;
    TextView txtTitle;
    TextView txtContent;
    ImageView imgSmile;
    ImageView btnClose;
    float ratting = 1f;

    public RateAppEmojiDialog(Context context, CallbackListener callbackListener) {
        super(context);
        mContext = context;
        this.callbackListener = callbackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_rate_emoji);
        initView();
    }

    private void initView() {
        setCancelable(false);
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        imgSmile = findViewById(R.id.imgSmile);
        txtRate = findViewById(R.id.txtRate);
        txtRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.onRating(ratting, "");
                dismiss();
            }
        });
        RatingBar rating = findViewById(R.id.rating);
        rating.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChange(final float v, float v1) {
                ratting = v1;
                setRatting((int) ratting);
            }
        });
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.onMaybeLater();
                dismiss();
            }
        });
    }

    private void setRatting(int stars) {
        txtTitle.setVisibility(View.VISIBLE);
        switch (stars) {
            case 1:
                imgSmile.setImageResource(R.drawable.ic_smile1);
                txtTitle.setText(R.string.title_oh_no);
                txtContent.setText(R.string.content_give_feedback);
                break;
            case 2:
                imgSmile.setImageResource(R.drawable.ic_smile2);
                txtTitle.setText(R.string.title_oh_no);
                txtContent.setText(R.string.content_give_feedback);
                break;
            case 3:
                imgSmile.setImageResource(R.drawable.ic_smile3);
                txtTitle.setText(R.string.title_oh_no);
                txtContent.setText(R.string.content_give_feedback);
                break;
            case 4:
                imgSmile.setImageResource(R.drawable.ic_smile4);
                txtTitle.setText(R.string.title_like_you);
                txtContent.setText(R.string.title_thank_feedback);
                break;
            case 5:
                imgSmile.setImageResource(R.drawable.ic_smile5);
                txtTitle.setText(R.string.title_like_you);
                txtContent.setText(R.string.title_thank_feedback);
                break;
        }
    }
}
