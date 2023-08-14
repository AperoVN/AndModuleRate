package com.rate.control.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T : ViewBinding>(context: Context) : Dialog(context) {
    protected lateinit var binding: T
    abstract fun createBinding(): T
    abstract fun initViews()
    override fun onStart() {
        super.onStart()
        val width = (context.resources.displayMetrics.widthPixels)
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setLayout((width * 0.8).toInt(), height)
        window?.setDimAmount(0.3f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window!!.setGravity(Gravity.CENTER)
        binding = createBinding()
        setContentView(binding.root)
        initViews()
    }
}