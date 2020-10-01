package com.buffup.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.buffup.sdk.model.Buff
import com.buffup.sdk.model.BuffResult
import com.buffup.sdk.model.succeeded
import com.buffup.sdk.network.BuffError
import com.buffup.sdk.repository.BuffRepository

/**
 * Buff container layout that adds buff dynamically. It automatically adds buff on top of other
 * views. Buffs are received by observing live data from [BuffRepository]. It requires owner class
 * to implement [LifecycleOwner] to optimize observing on LiveData.
 */
class BuffLayout(
    context: Context, attrs: AttributeSet
) : FrameLayout(context, attrs), LifecycleObserver {
    private val buffView by lazy {
        BuffView(context).apply {
            layoutParams = generateDefaultLayoutParams().apply {
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
                gravity = Gravity.START or Gravity.BOTTOM
            }
        }
    }
    private val buffRepository: BuffRepository by lazy { BuffRepository() }

    @VisibleForTesting
    private val buffObserver: Observer<Buff> = object : Observer<Buff> {
        override fun onChanged(response: Buff) {
            if (response.succeeded) {
                buffListener?.onBuffLoaded(response.buffResult ?: return)
                addBuff(response.buffResult ?: return)
                Log.v("buff", response.buffResult.toString())
            } else {
                buffListener?.onError(response.buffError ?: return)
                Log.v("error", response.buffError.toString())
            }
        }
    }

    fun setListener(buffListener: BuffListener) {
        this.buffListener = buffListener
    }

    private var buffListener: BuffListener?
        get() = buffView.buffListener
        set(value) {
            buffView.buffListener = value
        }

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this) ?: throw InstantiationException(
            "View owner class must implement LifecycleOwner"
        )
    }

    private fun addBuff(buff: BuffResult) {
        buffView.setupWithBuff(this, buff)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun viewStarted() {
        buffRepository.buffsLiveData.observeForever(buffObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun viewStopped() {
        buffRepository.buffsLiveData.removeObserver(buffObserver)
    }

    interface BuffListener {
        fun onBuffLoaded(buff: BuffResult)
        fun onBuffAnswerSelected()
        fun onBuffCleared()
        fun onBuffExpired()
        fun onError(error: BuffError)
    }
}
