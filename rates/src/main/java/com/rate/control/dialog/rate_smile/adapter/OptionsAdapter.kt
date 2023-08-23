package com.rate.control.dialog.rate_smile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rate.control.R
import com.rate.control.databinding.ItemOptionsBinding
import com.rate.control.dialog.rate_smile.model.OptionItem

class OptionsAdapter(
    private val context: Context,
    private val onItemClick: (List<String>) -> Unit,
) : RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {
    private var options = mutableListOf<OptionItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(
            ItemOptionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: MutableList<String>) {
        options.clear()
        options = data.map { OptionItem(it) }.toMutableList()
        notifyDataSetChanged()
    }

    fun getListOptionSelected(): List<String> {
        return options.filter { it.isSelected }.map { it.text }
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindView(options[position])
    }

    inner class OptionsViewHolder(private val binding: ItemOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: OptionItem) {
            binding.txtOptions.text = item.text
            binding.txtOptions.setOnClickListener {
                item.isSelected = !item.isSelected
                options[adapterPosition] = item
                notifyItemChanged(adapterPosition)
                onItemClick(getListOptionSelected())
            }
            if (item.isSelected) {
                binding.txtOptions.setTextColor(ContextCompat.getColorStateList(context, R.color.rate_feedback_option_selected_color))
                binding.txtOptions.backgroundTintList = ContextCompat.getColorStateList(context, R.color.rate_feedback_option_bg_selected_color)
            } else {
                binding.txtOptions.setTextColor(ContextCompat.getColorStateList(context, R.color.rate_feedback_option_color))
                binding.txtOptions.backgroundTintList = ContextCompat.getColorStateList(context, R.color.rate_feedback_option_bg_color)
            }
        }
    }
}
