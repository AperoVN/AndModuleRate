package com.rate.control.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.rate.control.CallbackListener
import com.rate.control.databinding.DialogFeedbackBinding

class FeedbackDialog(
    context: Context,
    private val rate: Float,
    private val callbackListener: CallbackListener
) : BaseDialog<DialogFeedbackBinding>(context) {
    override fun createBinding(): DialogFeedbackBinding {
        return DialogFeedbackBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setCancelable(false)
        binding.edtFeedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnSubmit.isEnabled =
                    !(p0.isNullOrEmpty() || p0.toString().trim().isNullOrEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.btnDismiss.setOnClickListener {
            callbackListener.onRating(rate, "")
            dismiss()
        }
        binding.btnSubmit.setOnClickListener {
            try {
                FirebaseDatabase.getInstance().getReference("feedbacks").push()
                    .setValue(binding.edtFeedback.text.toString().trim())
            } catch (e: IllegalStateException) {
                Log.d("Feedback", "Firebase not init")
            }
            callbackListener.onRating(rate, binding.edtFeedback.text.toString())
            dismiss()
        }
    }
}