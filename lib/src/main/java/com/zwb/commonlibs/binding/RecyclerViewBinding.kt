package com.zwb.commonlibs.binding

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

internal class DataBindingAdapter<T>(
        private val context: Context,
        private val layoutId: Int,
        private val brId: Int,
        private val onBindingItemClickListener: OnBindingItemClickListener<T>?
) : RecyclerView.Adapter<DataBindingHolder<ViewDataBinding>>() {
    override fun onBindViewHolder(holder: DataBindingHolder<ViewDataBinding>, position: Int) {
        holder.binding.apply {
            setVariable(brId, data[position])
            executePendingBindings()
        }
        onBindingItemClickListener?.let { listener ->
            holder.binding.root.setOnClickListener {
                listener(it, data[position], position)
            }
        }
    }

    var data: List<T> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingHolder<ViewDataBinding> {
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return DataBindingHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

/**
 * Databinding RecycleView with view model
 *
 * @param recyclerView
 * @param layoutId
 * @param brId
 * @param items
 */
@BindingAdapter("layoutId", "brId", "items", "layoutManager", "onItemClick", requireAll = false)
internal fun <T> bindRecyclerViewWithViewModel(
        recyclerView: RecyclerView,
        layoutId: Int,
        brId: Int,
        items: List<T>?,
        tlm: RecyclerView.LayoutManager?,
        onBindingItemClickListener: OnBindingItemClickListener<T>?
) {
    var lm = tlm
    if (lm == null && recyclerView.layoutManager == null) {
        lm = LinearLayoutManager(recyclerView.context)
    }
    if (lm != null) {
        recyclerView.layoutManager = lm
    }
    if (items != null && layoutId != 0 && brId != 0) {
        var adapter = recyclerView.adapter
        if (adapter !is DataBindingAdapter<*>) {
            adapter = DataBindingAdapter<T>(recyclerView.context, layoutId, brId, onBindingItemClickListener)
            recyclerView.adapter = adapter
        }
        (adapter as DataBindingAdapter<T>).data = items
    }
}