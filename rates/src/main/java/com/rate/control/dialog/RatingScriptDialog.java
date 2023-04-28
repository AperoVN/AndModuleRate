package com.rate.control.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.rate.control.R;
import com.rate.control.RatingScriptListener;
import com.rate.control.SharePreferenceUtils;
import com.rate.control.databinding.DialogRatingScriptBinding;
import com.rate.control.event.FirebaseAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

public class RatingScriptDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private int ratedLevel = 0;
    private List<ImageView> rateListView;
    private RatingScriptListener ratingListener;
    private DialogRatingScriptBinding binding;
    private final String appName;

    public RatingScriptDialog(@NonNull Context context, String appName) {
        super(context);
        this.context = context;
        this.appName = appName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflate = LayoutInflater.from(getContext());
        binding = DialogRatingScriptBinding.inflate(inflate);
        setContentView(binding.getRoot());
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.75);
        getWindow().setLayout(width, height);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        rateListView = new ArrayList<>();
        rateListView.add(binding.imgStarOne);
        rateListView.add(binding.imgStarTwo);
        rateListView.add(binding.imgStarThree);
        rateListView.add(binding.imgStarFour);
        rateListView.add(binding.imgStarFive);

        binding.imgStarOne.setOnClickListener(this);
        binding.imgStarTwo.setOnClickListener(this);
        binding.imgStarThree.setOnClickListener(this);
        binding.imgStarFour.setOnClickListener(this);
        binding.imgStarFive.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.btnRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgStarOne) {
            onClickStar(1);
        } else if (id == R.id.imgStarTwo) {
            onClickStar(2);
        } else if (id == R.id.imgStarThree) {
            onClickStar(3);
        } else if (id == R.id.imgStarFour) {
            onClickStar(4);
        } else if (id == R.id.imgStarFive) {
            onClickStar(5);
        } else if (id == R.id.btnCancel) {
            dismiss();
            ratingListener.onExit();
        } else if (id == R.id.btnRate) {
            reviewApp();
        }
    }

    private void onClickStar(int index) {
        if (ratedLevel == index && index > 0) --index;
        for (int i = 0; i <= rateListView.size() - 1; i++) {
            if (i <= index - 1) {
                rateListView.get(i).setImageResource(R.drawable.ic_star_blue);
            } else {
                rateListView.get(i).setImageResource(R.drawable.ic_star_unselect);
            }
        }
        ratedLevel = index;
        switch (ratedLevel) {
            case 0:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_zero);
                break;
            case 1:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_one);
                break;
            case 2:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_two);
                break;
            case 3:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_three);
                break;
            case 4:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_four);
                break;
            case 5:
                binding.imgEmotion.setImageResource(R.drawable.ic_emotion_level_five);
                break;
        }

        switch (ratedLevel) {
            case 0: {
                binding.txtTitle.setVisibility(View.GONE);
                binding.txtMessage.setText(context.getString(R.string.rating_invitation));
                binding.btnRate.setEnabled(false);
                binding.btnRate.setAlpha(0.2f);
                break;
            }
            case 1:
            case 2:
            case 3: {
                binding.txtTitle.setVisibility(View.VISIBLE);
                binding.txtTitle.setText(context.getString(R.string.oh_no));
                binding.txtMessage.setText(context.getString(R.string.please_feedback));
                binding.btnRate.setEnabled(true);
                binding.btnRate.setAlpha(1f);
                break;
            }
            case 4:
            case 5: {
                binding.txtTitle.setVisibility(View.VISIBLE);
                binding.txtTitle.setText(context.getString(R.string.we_like_you_too));
                binding.txtMessage.setText(context.getString(R.string.rate_thanks_feedback));
                binding.btnRate.setEnabled(true);
                binding.btnRate.setAlpha(1f);
                break;
            }
        }
    }

    public void addRatingScriptListener(RatingScriptListener ratingScriptListener) {
        this.ratingListener = ratingScriptListener;
    }

    private void reviewApp() {
        if (ratedLevel > 0) {
            dismiss();
            ratingListener.onResultRated(ratedLevel);
            String rateStarEvent = String.format(getContext().getString(R.string.rate_start_event), String.valueOf(ratedLevel));
            FirebaseAnalyticsUtil.logClickAdsEvent(context, rateStarEvent);
            if (ratedLevel == 5) {
                ReviewManager manager = ReviewManagerFactory.create(context);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(@NonNull Task<ReviewInfo> task) {
                        dismiss();
                        ratingListener.onComplete(task);
                    }
                });
            } else {
                startEmailApp(context);
            }
            SharePreferenceUtils.setCompleteRated(context, true);
        }
    }

    private boolean appInstalledOrNot(Context context, String package_name) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(package_name, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void startEmailApp(Context context) {
        String gmailPackage = getContext().getString(R.string.gmail_package);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subjectFixback = String.format(getContext().getString(R.string.subject_mail_fixback_rate), appName);
        String contentFixback = String.format(getContext().getString(R.string.content_mail_fixback_rate), appName, String.valueOf(ratedLevel));

        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getContext().getString(R.string.mail_fixback_rated)});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subjectFixback);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contentFixback);
        sendIntent.setType("message/rfc822");
        if (appInstalledOrNot(context, gmailPackage)) {
            sendIntent.setPackage(gmailPackage);
            context.startActivity(sendIntent);
        } else {
            try {
                context.startActivity(Intent.createChooser(sendIntent, null));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void show() {
        boolean isCompleteRated = SharePreferenceUtils.getCompleteRated(context);
        if (!isCompleteRated) {
            super.show();
        } else {
            ratingListener.onExit();
        }
    }
}
