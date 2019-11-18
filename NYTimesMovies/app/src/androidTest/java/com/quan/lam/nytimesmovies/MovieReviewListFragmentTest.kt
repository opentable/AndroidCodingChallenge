package com.quan.lam.nytimesmovies

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.quan.lam.nytimesmovies.ui.main.MovieReviewListFragment
import org.junit.Test
import org.junit.runner.RunWith
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.contrib.RecyclerViewActions

/**
 * Custom assertion class to check Recycler view item count
 */
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assert(adapter!!.itemCount == expectedCount)
    }
}

/**
 * Movie Review List Fragment UI Automation test
 */
@RunWith(AndroidJUnit4::class)
class MovieReviewListFragmentTest {
    @Test
    fun testOnFragmentLifeCycle() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun testListViewLoaded() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(20))
    }

    @Test
    fun testListViewLoadMore() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(20))
        onView(withId(R.id.reviewsList))
        onView(withId(R.id.reviewsList))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(21))
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(40))
    }
}