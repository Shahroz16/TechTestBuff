package com.buffup.sdk.ui

import androidx.recyclerview.selection.SelectionTracker
import java.util.concurrent.atomic.AtomicBoolean

internal class BuffAnswerSelectionPredicate : SelectionTracker.SelectionPredicate<Long>() {
    private val isSelectionEnabled: AtomicBoolean = AtomicBoolean(true)

    override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
        if (canSelect()) {
            isSelectionEnabled.set(false)
            return true
        }
        return false
    }

    override fun canSetStateAtPosition(
        position: Int, nextState: Boolean
    ): Boolean {
        if (canSelect()) {
            isSelectionEnabled.set(false)
            return true
        }
        return false
    }

    override fun canSelectMultiple(): Boolean {
        return false
    }

    private fun canSelect(): Boolean {
        return isSelectionEnabled.get()
    }

    fun reset() {
        isSelectionEnabled.set(true)
    }
}
