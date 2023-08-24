package com.rate.control.dialog.rate_smile

import android.content.Context
import com.rate.control.R
import com.rate.control.databinding.DialogWithSmileBinding
import com.rate.control.dialog.BaseDialog

class RateSmileDialog(
    context: Context,
    private val rateCallBack: RateCallBack,
    private val goodRating: Float = 4f,
) : BaseDialog<DialogWithSmileBinding>(context) {

    override fun createBinding(): DialogWithSmileBinding {
        return DialogWithSmileBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setCancelable(false)
        binding.btnRate.setOnClickListener {
            if (binding.rating.rating != 0f) {
                rateCallBack.onRating(binding.rating.rating)
                dismiss()
            }
        }
        binding.btnDismiss.setOnClickListener {
            rateCallBack.onClose()
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
