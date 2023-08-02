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
import com.rate.control.databinding.DialogFeedbackRateBinding
import java.util.Locale

class RateFeedbackDialog(
    private var mContext: Context,
    private var languageCode: String = "",
    private var feedbackAction: () -> Unit
) : Dialog(
    mContext, com.rate.control.R.style.DialogAnimeTheme
) {
    private var myLocale: Locale? = null
    private lateinit var binding: DialogFeedbackRateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        loadLocale(languageCode)
        super.onCreate(savedInstanceState)
        binding = DialogFeedbackRateBinding.inflate(LayoutInflater.from(mContext))
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
        binding.btnOk.setOnClickListener {
            dismiss()
            feedbackAction()
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
}