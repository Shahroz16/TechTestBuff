package com.buffup.sdk.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buffup.sdk.R
import com.buffup.sdk.model.Answer

internal class BuffAnswerAdapter : ListAdapter<Answer, BuffAnswerViewHolder>(DiffCallback) {
    private val buffAnswerSelectionPredicate = BuffAnswerSelectionPredicate()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    fun setupTracker(recyclerView: RecyclerView) {
        tracker = SelectionTracker.Builder(
            "buff-answer-id",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            BuffAnswerLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(buffAnswerSelectionPredicate).build()
    }

    fun clearSelection() {
        buffAnswerSelectionPredicate.reset()
        tracker?.clearSelection()
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffAnswerViewHolder {
        return BuffAnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_buff_answer, parent, false
            )
        ).apply {
            itemView.setOnClickListener { tracker?.select(getItemId(bindingAdapterPosition)) }
        }
    }

    override fun onBindViewHolder(holder: BuffAnswerViewHolder, position: Int) {
        holder.bindView(getItem(position), tracker?.isSelected(getItemId(position)) == true)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Answer>() {
        override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
            return oldItem.title == newItem.title
        }
    }
}
