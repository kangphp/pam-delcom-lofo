package com.ifs21031.lostfound.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifs21031.lostfound.data.remote.response.LostFoundsItem
import com.ifs21031.lostfound.databinding.ItemRowLofoBinding

class LofosAdapter :
    ListAdapter<LostFoundsItem,
            LofosAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var originalData = mutableListOf<LostFoundsItem>()
    private var filteredData = mutableListOf<LostFoundsItem>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowLofoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = originalData[originalData.indexOf(getItem(position))]
        holder.binding.cbItemLofoIsFinished.setOnCheckedChangeListener(null)
        holder.binding.cbItemLofoIsFinished.setOnLongClickListener(null)
        holder.bind(data)
        holder.binding.cbItemLofoIsFinished.setOnCheckedChangeListener { _, isChecked ->
            data.isCompleted = if (isChecked) 1 else 0
            holder.bind(data)
            onItemClickCallback.onCheckedChangeListener(data, isChecked)
        }
        holder.binding.ivItemLofoDetail.setOnClickListener {
            onItemClickCallback.onClickDetailListener(data.id)
        }
    }

    class MyViewHolder(val binding: ItemRowLofoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LostFoundsItem) {
            binding.apply {
                tvItemLofoTitle.text = data.title
                cbItemLofoIsFinished.isChecked = data.isCompleted == 1
            }
        }
    }

    fun submitOriginalList(list: List<LostFoundsItem>) {
        originalData = list.toMutableList()
        filteredData = list.toMutableList()
        submitList(originalData)
    }

    fun filter(query: String) {
        filteredData = if (query.isEmpty()) {
            originalData
        } else {
            originalData.filter {
                (it.title.contains(query, ignoreCase = true))
            }.toMutableList()
        }
        submitList(filteredData)
    }

    interface OnItemClickCallback {
        fun onCheckedChangeListener(todo: LostFoundsItem, isChecked: Boolean)
        fun onClickDetailListener(todoId: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LostFoundsItem>() {
            override fun areItemsTheSame(
                oldItem: LostFoundsItem,
                newItem: LostFoundsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LostFoundsItem,
                newItem: LostFoundsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}