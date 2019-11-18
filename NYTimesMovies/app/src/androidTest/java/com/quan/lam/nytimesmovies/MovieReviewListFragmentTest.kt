package com.quan.lam.nytimesmovies

import android.os.Build
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
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.quan.lam.nytimesmovies.model.MPAARating
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * Custom assertion class to check Recycler view item count
 */
class RecyclerViewItemCountAssertion : ViewAssertion {

    private val matcher: Matcher<Int>

    constructor(expectedCount: Int) {
        this.matcher = Matchers.`is`(expectedCount)
    }

    constructor(matcher: Matcher<Int>) {
        this.matcher = matcher
    }

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, matcher)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scenario.moveToState(Lifecycle.State.STARTED)
        }
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun testListViewLoaded() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        Thread.sleep(5000)
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(20))
    }

    @Test
    fun testListViewLoadMore() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        Thread.sleep(5000)
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(20))
        onView(withId(R.id.reviewsList))
        onView(withId(R.id.reviewsList))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
            .perform(ViewActions.swipeUp())
        Thread.sleep(5000)
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(40))
    }

    @Test
    fun testListViewClicked() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.reviewsList)).
            perform(click())
    }

    @Test
    fun globalAgeLimitChangeTest() {
        val scenario = launchFragmentInContainer<MovieReviewListFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment {
            MPAARating.globalAgeLimit.value = MPAARating("G")
        }
        onView(withId(R.id.reviewsList)).check(RecyclerViewItemCountAssertion(Matchers.lessThan(20)))
    }
}