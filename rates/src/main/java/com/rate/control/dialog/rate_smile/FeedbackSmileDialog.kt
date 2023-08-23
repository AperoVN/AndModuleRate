package com.rate.control.dialog.rate_smile

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.rate.control.R
import com.rate.control.databinding.DialogFeedbackSmileBinding
import com.rate.control.dialog.rate_smile.adapter.ImageAdapter
import com.rate.control.dialog.rate_smile.adapter.OptionsAdapter

class FeedbackSmileDialog(
    private val context: Context,
    private val options: List<String>,
    private val onClose: () -> Unit,
    private val onSubmit: (List<String>, List<String>?, String) -> Unit,
    private val onUpload: () -> Unit,
) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen) {
    private lateinit var binding: DialogFeedbackSmileBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var optionsAdapter: OptionsAdapter
    private var listOptionSelected = mutableListOf<String>()
    private var textFeedback = ""
    private var minTextFeedback = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
        binding = DialogFeedbackSmileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setCancelable(false)
        initListener()
        initOptions()
        initMedia()
        checkEnableSubmit()
    }

    private fun initOptions() {
        optionsAdapter = OptionsAdapter(context) {
            listOptionSelected = it.toMutableList()
            checkEnableSubmit()
        }
        optionsAdapter.updateData(options.toMutableList())
        binding.rvOptions.apply {
            adapter = optionsAdapter
        }
    }

    private fun initMedia() {
        imageAdapter = ImageAdapter(
            onAddClick = {
                onUpload()
            },
            onRemoveLast = {
                Handler(context.mainLooper).postDelayed({
                    binding.rvMedia.visibility = View.GONE
                    binding.txtUpload.visibility = View.VISIBLE
                }, 50)

            }
        )
        binding.rvMedia.apply {
            adapter = imageAdapter
        }
    }

    private fun checkEnableSubmit() {
        if (listOptionSelected.size > 0 && textFeedback.length >= minTextFeedback) {
            binding.txtSubmit.isEnabled = true
            binding.txtSubmit.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.rate_feedback_submit_bg_color)
        } else {
            binding.txtSubmit.isEnabled = false
            binding.txtSubmit.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.rate_feedback_submit_bg_disable_color)
        }
    }

    private fun initListener() {
        binding.txtUpload.setOnClickListener {
            onUpload()
            binding.txtUpload.visibility = View.GONE
            binding.rvMedia.visibility = View.VISIBLE
        }

        binding.edtFeedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /*noop*/
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /*noop*/
            }

            override fun afterTextChanged(text: Editable?) {
                textFeedback = text.toString()
                Handler(context.mainLooper).postDelayed(
                    { checkEnableSubmit() },
                    300
                )
            }
        })

        binding.imgBack.setOnClickListener {
            onClose()
            dismiss()
        }

        binding.txtSubmit.setOnClickListener {
            val result = imageAdapter.getData()
            onSubmit(listOptionSelected, result, textFeedback)
            dismiss()
        }
    }

    fun addMedia(uris: MutableList<String>) {
        imageAdapter.updateData(uris)
        binding.rvMedia.smoothScrollToPosition(binding.rvMedia.adapter?.itemCount?.minus(1) ?: 0)
    }

    fun setMinTextFeedback(min: Int) {
        minTextFeedback = min
    }
}
