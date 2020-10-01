package com.buffup.sdk.ui

import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.buffup.sdk.model.Answer
import kotlinx.android.synthetic.main.list_item_buff_answer.view.*

internal class BuffAnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(answer: Answer, isActivated: Boolean = false) {
        itemView.answer_text.text = answer.title
        itemView.isActivated = isActivated
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = bindingAdapterPosition
            override fun getSelectionKey(): Long? = itemId
        }
}
