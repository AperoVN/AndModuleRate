package com.rate.control.dialog.rate_smile

import android.content.Context
import com.rate.control.R
import com.rate.control.databinding.DialogWithSmileBinding
import com.rate.control.dialog.BaseDialog

class RateSmileDialog(
    context: Context,
    private val rateCallBack: RateCallBack,
    private val goodRating: Float = 4f,
    private val isShowFeedback: Boolean,
    private val onUploadMedia: () -> Unit,
    private val options: MutableList<String>? = null,
) : BaseDialog<DialogWithSmileBinding>(context) {

    lateinit var feedbackSmileDialog: FeedbackSmileDialog
    override fun createBinding(): DialogWithSmileBinding {
        return DialogWithSmileBinding.inflate(layoutInflater)
    }

    fun addMedia(uris: MutableList<String>) {
        feedbackSmileDialog.addMedia(uris)
    }

    override fun initViews() {
        setCancelable(false)
        initFeedbackDialog()
        binding.btnRate.setOnClickListener {
            if (binding.rating.rating != 0f) {
                if (binding.rating.rating >= goodRating) {
                    rateCallBack.onRating(binding.rating.rating, "")
                    dismiss()
                } else {
                    dismiss()
                    if (isShowFeedback && this::feedbackSmileDialog.isInitialized) {
                        feedbackSmileDialog.show()
                    } else {
                        rateCallBack.onRating(binding.rating.rating, "")
                    }
                }
            }
        }
        binding.btnDismiss.setOnClickListener {
            rateCallBack.onMaybeLater()
            dismiss()
        }
        binding.rating.setOnRatingChangedListener { _, newRating ->
            if (newRating == 0f) {
                binding.ivSmile.setImageResource(R.drawable.rate_ic_rate_good)
                binding.tvMessage.setText(R.string.rate_message)
                binding.btnRate.setText(R.string.rate_us)
            } else if (newRating >= goodRating) {
                binding.ivSmile.setImageResource(R.drawable.rate_ic_rate_good)
                binding.tvMessage.setText(R.string.rate_message_good)
                binding.btnRate.setText(R.string.rate_on_google_play)
            } else {
                binding.ivSmile.setImageResource(R.drawable.rate_ic_rate_bad)
                binding.tvMessage.setText(R.string.rate_message_bad)
                binding.btnRate.setText(R.string.rate_us)
            }
        }
    }

    private fun initFeedbackDialog() {
        if (isShowFeedback) {
            val tags = options ?: mutableListOf(
                context.getString(R.string.rate_feedback_default_option1),
                context.getString(R.string.rate_feedback_default_option2),
                context.getString(R.string.rate_feedback_default_option3),
            )
            feedbackSmileDialog = FeedbackSmileDialog(
                context = context,
                options = tags,
                onClose = { rateCallBack.onRating(binding.rating.rating, "") },
                onSubmit = { options, listImage, feedback ->
                    rateCallBack.onFeedback(binding.rating.rating, feedback, options, listImage)
                },
                onUpload = { onUploadMedia() }
            )
        }
    }
}
