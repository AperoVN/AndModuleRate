package com.rate.control.dialog.rate_smile

import com.rate.control.CallbackListener

interface RateCallBack : CallbackListener {
    fun onFeedback(rating: Float, feedback: String, tags: List<String>)
}
