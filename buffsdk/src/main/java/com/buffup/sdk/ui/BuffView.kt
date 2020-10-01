package com.buffup.sdk.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.selection.SelectionTracker
import com.buffup.sdk.R
import com.buffup.sdk.model.Answer
import com.buffup.sdk.model.Author
import com.buffup.sdk.model.BuffResult
import com.buffup.sdk.model.Question
import kotlinx.android.synthetic.main.widget_buff_view.view.*

/**
 * BuffView that has three sections in it. The first one contains info about the author. The
 * second contains question details. And the last part has answers list with only selectable
 * in one lifetime. The view can be reused for multiple buffs.
 *
 * @see stateManager [BuffStateManager] that manages its lifecycle
 * @see buffListener [BuffLayout.BuffListener] Buff listener that gets callback for buff updates
 */
class BuffView(context: Context) : ConstraintLayout(context), BuffStateManager.ViewHolder {
    private val buffAnswerAdapter = BuffAnswerAdapter()
    private var stateManager: BuffStateManager? = null

    var buffListener: BuffLayout.BuffListener? = null

    init {
        inflate(context, R.layout.widget_buff_view, this)
        val buffMargin = resources.getDimensionPixelSize(R.dimen.buff_margin)
        setPadding(buffMargin, buffMargin, buffMargin, buffMargin)
        buff_answers_list.apply {
            adapter = buffAnswerAdapter
            buffAnswerAdapter.setupTracker(this)
        }
        buff_close.setOnClickListener {
            stateManager?.onClear()
            buffListener?.onBuffCleared()
            removeSelf()
        }
        // Setting question max width to 50% of screen
        val maxWidth = resources.displayMetrics.widthPixels.times(0.5F).toInt() - buffMargin
        if (maxWidth > 0) {
            question_text.updateLayoutParams<LayoutParams> {
                matchConstraintMaxWidth = maxWidth
            }
        }
        buffAnswerAdapter.tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (buffAnswerAdapter.tracker?.selection?.isEmpty?.not() == true) {
                    stateManager?.onAnswerSelected()
                    buffListener?.onBuffAnswerSelected()
                }
            }
        })
    }

    private fun removeSelf() {
        (parent as? ViewGroup)?.removeView(this)
        stateManager = null
    }

    fun setupWithBuff(parent: ViewGroup, buff: BuffResult, manager: BuffStateManager? = null) {
        if (this.parent != null) {
            stateManager?.onClear()
            removeSelf()
        }
        stateManager = manager ?: BuffStateManagerImpl(this, buff.timeToShow)
        buffAnswerAdapter.clearSelection()
        setupSender(buff.author)
        setupQuestions(buff.question)
        setupAnswers(buff.answers)
        stateManager?.onAttach()
        parent.addView(this, layoutParams)
        stateManager?.onStart()
    }

    override fun setMaxProgress(secondsUntilFinished: Int) {
        question_time_progress.max = secondsUntilFinished
        question_time_progress.progress = secondsUntilFinished
        question_time_progress.isIndeterminate = false
        question_time.text = secondsUntilFinished.toString()
    }

    override fun updateProgress(secondsUntilFinished: Int) {
        question_time_progress.progress = question_time_progress.max - secondsUntilFinished
        question_time.text = secondsUntilFinished.toString()
    }

    override fun onBuffExpired() {
        buffListener?.onBuffExpired()
        removeSelf()
    }

    private fun setupSender(author: Author) {
        sender_image.visibility = if (author.image.isNullOrBlank()) View.GONE
        else View.VISIBLE
        sender_name.text = context.getString(
            R.string.display_name_format,
            author.firstName, author.lastName
        ).trim()
    }

    private fun setupQuestions(question: Question) {
        question_text.text = question.title
    }

    private fun setupAnswers(answers: List<Answer>) {
        buffAnswerAdapter.submitList(answers)
    }
}
