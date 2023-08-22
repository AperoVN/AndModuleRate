package com.rate.control.dialog.rate_smile.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rate.control.databinding.ItemAddBinding
import com.rate.control.databinding.ItemImageBinding

class ImageAdapter(
    private val onAddClick: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_IMAGE = 0
    private val TYPE_ADD = 1
    private val ADD = "add"
    private val list = mutableListOf(ADD)

    fun updateData(data: MutableList<String>) {
        val index = list.size - 1
        list.addAll(list.size - 1, data)
        notifyItemRangeInserted(index, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE) {
            ImageViewHolder(
                ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            AddViewHolder(
                ItemAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bindView(list[position], position)
        } else if (holder is AddViewHolder) {
            holder.bindView()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1) {
            TYPE_ADD
        } else {
            TYPE_IMAGE
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: String, position: Int) {
            binding.imgMedia.setImageURI(Uri.parse(item))

            binding.imgRemoveMedia.setOnClickListener {
                list.remove(item)
                notifyItemRemoved(position)
            }
        }
    }

    inner class AddViewHolder(private val binding: ItemAddBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView() {
            binding.imgAdd.setOnClickListener {
                onAddClick()
            }
        }
    }
}
