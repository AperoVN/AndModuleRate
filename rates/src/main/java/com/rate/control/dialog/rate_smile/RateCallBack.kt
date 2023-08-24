package com.rate.control.dialog.rate_smile

interface RateCallBack {
    fun onClose()

    fun onRating(rating: Float)
}
