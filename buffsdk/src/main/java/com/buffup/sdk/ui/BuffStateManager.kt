package com.buffup.sdk.ui

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit

abstract class BuffStateManager {
    abstract fun onAttach()
    abstract fun onStart()
    abstract fun onClear()
    abstract fun onAnswerSelected()

    interface ViewHolder {
        fun setMaxProgress(secondsUntilFinished: Int)
        fun updateProgress(secondsUntilFinished: Int)
        fun onBuffExpired()
    }

    companion object {
        internal const val DEFAULT_TIME_TO_LIVE = 30
        internal const val DEFAULT_TIME_TO_CONFIRM = 2
        internal const val DEFAULT_COUNT_DOWN_INTERVAL = 500L
    }
}

internal class BuffStateManagerImpl(
    private val view: ViewHolder,
    private val timeToLive: Int = DEFAULT_TIME_TO_LIVE,
    private val timeToConfirm: Int = DEFAULT_TIME_TO_CONFIRM,
    countDownInterval: Long = DEFAULT_COUNT_DOWN_INTERVAL
) : BuffStateManager() {
    private val timeToLiveCountDown = object : CountDownTimer(
        TimeUnit.SECONDS.toMillis(timeToLive.toLong()), countDownInterval
    ) {
        override fun onTick(millisUntilFinished: Long) {
            updateViewProgress(millisUntilFinished)
        }

        override fun onFinish() {
            onExpired()
        }
    }
    private val confirmationCountDown = object : CountDownTimer(
        TimeUnit.SECONDS.toMillis(timeToConfirm.toLong()), countDownInterval
    ) {
        override fun onTick(millisUntilFinished: Long) {
            updateViewProgress(millisUntilFinished)
        }

        override fun onFinish() {
            onExpired()
        }
    }

    private fun updateViewProgress(millisUntilFinished: Long) {
        view.updateProgress(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toInt() + 1)
    }

    private fun onExpired() {
        view.onBuffExpired()
    }

    override fun onAttach() {
        view.setMaxProgress(timeToLive)
    }

    override fun onStart() {
        timeToLiveCountDown.start()
    }

    override fun onClear() {
        timeToLiveCountDown.cancel()
        confirmationCountDown.cancel()
    }

    override fun onAnswerSelected() {
        timeToLiveCountDown.cancel()
        view.setMaxProgress(timeToConfirm)
        confirmationCountDown.start()
    }
}
