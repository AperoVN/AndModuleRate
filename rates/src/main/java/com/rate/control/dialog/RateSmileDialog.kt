package com.rate.control.dialog

import android.content.Context
import com.rate.control.CallbackListener
import com.rate.control.R
import com.rate.control.databinding.DialogWithSmileBinding

class RateSmileDialog(
    context: Context,
    private val callbackListener: CallbackListener,
    private val goodRating: Float = 4f,
) :
    BaseDialog<DialogWithSmileBinding>(context) {
    override fun createBinding(): DialogWithSmileBinding {
        return DialogWithSmileBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setCancelable(false)
        binding.btnRate.setOnClickListener {
            if (binding.rating.rating != 0f) {
                if (binding.rating.rating >= goodRating) {
                    callbackListener.onRating(binding.rating.rating, "")
                    dismiss()
                } else {
                    dismiss()
                    val dialogFeedback =
                        FeedbackDialog(
                            context,
                            binding.rating.rating,
                            callbackListener = callbackListener
                        )
                    dialogFeedback.show()
                }
            }
        }
        binding.btnDismiss.setOnClickListener {
            callbackListener.onMaybeLater()
            dismiss()
        }
        binding.rating.setOnRatingChangedListener { oldRating, newRating ->
            if (newRating == 0f) {
                binding.ivSmile.setImageResource(R.drawable.rate_ic_rate_good)
                binding.tvMessage.setText(R.string.rate_message)
                binding.btnRate.setText(R.string.rate_on_google_play)
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