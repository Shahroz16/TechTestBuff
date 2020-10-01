package com.buffup.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.buffup.sdk.repository.BuffRepository
import com.buffup.sdk.ui.BuffLayout
import com.buffup.sdk.ui.BuffView
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class BuffInstrumentedTest {
    @get:Rule
    val activityRule = ActivityTestRule(FullscreenActivity::class.java)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val repository: BuffRepository = BuffRepository()

    @Test
    fun buffReceived_ShowOnView() {
        val buffLayout = activityRule.activity.findViewById<BuffLayout>(R.id.buff_view)
        repository.buffsLiveData.getOrAwaitValue {
            // Wait so that view is added to hierarchy
            Thread.sleep(5000)
            onView(instanceOf(BuffView::class.java)).check(matches(isDisplayed()))
        }
    }
}
