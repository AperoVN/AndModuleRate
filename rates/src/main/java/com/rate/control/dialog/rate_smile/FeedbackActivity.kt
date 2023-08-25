package com.rate.control.dialog.rate_smile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.rate.control.R
import com.rate.control.databinding.ActivityFeedbackBinding
import com.rate.control.dialog.rate_smile.adapter.ImageAdapter
import com.rate.control.dialog.rate_smile.adapter.OptionsAdapter


class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var optionsAdapter: OptionsAdapter
    private var listOptionSelected = arrayListOf<String>()
    private var textFeedback = ""

    private val defaultOptions by lazy {
        arrayListOf(
            getString(R.string.rate_feedback_default_option1),
            getString(R.string.rate_feedback_default_option2),
            getString(R.string.rate_feedback_default_option3),
        )
    }

    companion object {
        const val OPTIONS = "OPTIONS"
        const val MIN_TEXT = "MIN_TEXT"
        const val LIST_OPTION = "LIST_OPTION"
        const val TEXT_FEEDBACK = "TEXT_FEEDBACK"
        const val LIST_IMAGE = "LIST_IMAGE"
    }

    private val options by lazy {
        intent.getStringArrayListExtra(OPTIONS) ?: defaultOptions
    }

    private val minTextFeedback by lazy {
        intent.getIntExtra(MIN_TEXT, 6)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        initListener()
        initOptions()
        initMedia()
        checkEnableSubmit()
    }

    private fun initOptions() {
        optionsAdapter = OptionsAdapter(this) {
            listOptionSelected.clear()
            listOptionSelected.addAll(it)
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
                openGallery()
            },
            onRemoveLast = {
                Handler(mainLooper).postDelayed({
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
                ContextCompat.getColorStateList(this, R.color.rate_feedback_submit_bg_color)
        } else {
            binding.txtSubmit.isEnabled = false
            binding.txtSubmit.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.rate_feedback_submit_bg_disable_color)
        }
    }

    private fun initListener() {
        binding.txtUpload.setOnClickListener {
            openGallery()
            showMedia(true)
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
                Handler(mainLooper).postDelayed(
                    { checkEnableSubmit() },
                    300
                )
            }
        })

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtSubmit.setOnClickListener {
            val resultImage = arrayListOf<String>()
            imageAdapter.getData()?.let {
                resultImage.addAll(it)
            }
            val intent = Intent()
            intent.putStringArrayListExtra(LIST_OPTION, listOptionSelected)
            intent.putExtra(TEXT_FEEDBACK, textFeedback)
            intent.putExtra(LIST_IMAGE, resultImage)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun addMedia(uris: MutableList<String>) {
        imageAdapter.updateData(uris)
        binding.rvMedia.smoothScrollToPosition(binding.rvMedia.adapter?.itemCount?.minus(1) ?: 0)
    }

    private fun showMedia(isShow: Boolean) {
        binding.txtUpload.isVisible = !isShow
        binding.rvMedia.isVisible = isShow
    }

    private val photoPickerLauncher =
        registerForActivityResult(PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                addMedia(uris.map { it.toString() }.toMutableList())
            } else {
                showMedia(imageAdapter.getSize() != 1)
            }
        }

    private fun openGallery() {
        photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}
