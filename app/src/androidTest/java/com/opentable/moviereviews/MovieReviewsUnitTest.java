package com.opentable.moviereviews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.opentable.moviereviews.viewmodel.MoviesViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MovieReviewsUnitTest {
    MoviesViewModel viewModel;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.opentable.moviereviews", appContext.getPackageName());
    }

    @Test
    public void updateUI() {
        viewModel = new MoviesViewModel();
        viewModel.getItemList();

        // In the interest of time the below sleep is a non-preferred way of waiting for the
        // REST call to complete
        // More involved test would involve utilizing Test observables to detect changes in LiveData
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //validation
        assertNotNull(viewModel.getItemList());
        assertTrue(viewModel.getItemList().getValue().size()>0);
        assertFalse(viewModel.getItemList().getValue().isEmpty());

    }
}
