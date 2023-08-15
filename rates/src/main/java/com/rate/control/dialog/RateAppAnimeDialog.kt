package com.rate.control.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.rate.control.CallbackListener

import com.rate.control.R
import com.rate.control.databinding.DialogRateAppAnimeBinding
import java.util.Locale

class RateAppAnimeDialog(
    private var mContext: Context,
    private var languageCode: String = "",
    private var callbackListener: CallbackListener
) : Dialog(
    mContext, R.style.DialogAnimeTheme
) {
    private var ratingCount = 0f
    private lateinit var binding: DialogRateAppAnimeBinding
    private var myLocale: Locale? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocale(language = languageCode)
        super.onCreate(savedInstanceState)
        binding = DialogRateAppAnimeBinding.inflate(LayoutInflater.from(mContext))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        window!!.setGravity(Gravity.CENTER);
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setCancelable(false)
        binding.rating.rating = ratingCount
        binding.btnRate.setOnClickListener {
            if (ratingCount > 0) {
                callbackListener.onRating(ratingCount, "")
                dismiss()
            }
        }
        updateEmojiStatus(binding.rating.rating.toInt())
        binding.rating.setOnRatingChangedListener { _, position ->
            ratingCount = position
            updateEmojiStatus(ratingCount.toInt())
        }
        binding.btnClose.setOnClickListener {
            callbackListener.onMaybeLater()
            dismiss()
        }
    }

    private fun updateEmojiStatus(stars: Int) {
        when (stars) {
            0 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_0s)
                binding.txtContent.text = context.getString(R.string.content_rate_0s)
            }

            1 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_1s)
                binding.txtContent.text = context.getString(R.string.content_rate_1s)
            }

            2 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_2s)
                binding.txtContent.text = context.getString(R.string.content_rate_2s)
            }

            3 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_3s)
                binding.txtContent.text = context.getString(R.string.content_rate_3s)
            }

            4 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_4s)
                binding.txtContent.text = context.getString(R.string.content_rate_4s)
            }

            5 -> {
                binding.imgSmile.setImageResource(R.drawable.img_rate_5s)
                binding.txtContent.text = context.getString(R.string.content_rate_5s)
            }
        }
    }

    private fun loadLocale(language: String = "") {
        if (language == "") {
            val config = Configuration()
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            config.locale = locale
            context.resources
                .updateConfiguration(config, context.resources.displayMetrics)
        } else {
            changeLang(language, context)
        }
    }

    private fun changeLang(lang: String, context: Context) {
        if (lang.equals("", ignoreCase = true)) return
        myLocale = Locale(lang)
        myLocale?.let { Locale.setDefault(it) }
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun setDefaultRateCount(rateCount: Float) {
        binding.rating.rating = rateCount
    }
}
