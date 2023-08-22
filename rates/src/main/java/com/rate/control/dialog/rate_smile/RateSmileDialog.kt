package com.rate.control.dialog.rate_smile

import android.content.Context
import com.rate.control.R
import com.rate.control.databinding.DialogWithSmileBinding
import com.rate.control.dialog.BaseDialog

class RateSmileDialog(
    context: Context,
    private val rateCallBack: RateCallBack,
    private val goodRating: Float = 4f,
    private val showFeedback: Boolean,
    private val onUploadMedia: () -> Unit,
) : BaseDialog<DialogWithSmileBinding>(context) {

    lateinit var feedbackSmileDialog: FeedbackSmileDialog
    override fun createBinding(): DialogWithSmileBinding {
        return DialogWithSmileBinding.inflate(layoutInflater)
    }

    fun addMedia(uris: MutableList<String>) {
        feedbackSmileDialog.addMedia(uris)
    }

    fun addOptions(options: MutableList<String>) {
        feedbackSmileDialog.setOptions(options)
    }

    override fun initViews() {
        setCancelable(false)
        binding.btnRate.setOnClickListener {
            if (binding.rating.rating != 0f) {
                if (binding.rating.rating >= goodRating) {
                    rateCallBack.onRating(binding.rating.rating, "")
                    dismiss()
                } else {
                    dismiss()
                    if (showFeedback) {
                        feedbackSmileDialog = FeedbackSmileDialog(
                            context = context,
                            onClose = { rateCallBack.onRating(binding.rating.rating, "") },
                            onSubmit = { tags, feedback ->
                                rateCallBack.onFeedback(binding.rating.rating, feedback, tags)
                            },
                            onUpload = { onUploadMedia() }
                        )
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
}
